package OrindaAcademy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Open a csv infile of reorganized and merged Orinda Academy SIS yearly student output files
 * 
 * Parse each line (2005, 2006 through 2008, and 2008 on have different data layouts and need to be treated separately)
 * 
 * Manual treatment of the SIS files, includes adding a first column with the school year
 * 	for 2006 through 2008 
 * 		parent names for married parents were entered as "Daniel & Lisa Stefani", those need to be pulled apart
 * 		a 5th column "Man First" contains a manually generated boolean that tells if the first name is a man "Daniel & Lisa Smith" (true), or "Rebecca & Scott Jones" (false)
 * 		manually make sure that names like "Daniel & Lisa Smith" have ampersands, and "Susan Rosenthal/Howie Perlin" is used to indicate marrieds with different last names
 * 		convert "James and Terry Holmes" to "James & Terry Holmes" 
 *  
 * Output a csv file that is in the format of the NPSP import format, which should be ready to read into Salesforce
 * 
 * @author stevepodell
 * @version 1.0
 * January 2017
 */
public class OaImportToSF {
	public static void main(String[] args) throws IOException {
		String inFile = "oaIn.csv";
		String outFile = "reduced.csv";
		if(args.length > 0)
			inFile = args[0];
		if(args.length > 1)
			outFile = args[1];
		
		// Clean up old intermediary and output files, open the new input stream
		FileInputStream fstream = cleanUpOldFiles(inFile, outFile);
		
		// Read in the processed Excel file, that has been written out from Excel as a csv
		List<SFImportObject> list =  parseInputFile(fstream);
		
		// Replace later student names with the first student name in a family so that they share the first ACCOUNT1 NAME (foreign key)
		doReplacements(list);
		
		 // Sort the list by ACCOUNT1_NAME		
		Collections.sort(list, new Comparator<SFImportObject>(){
		     public int compare(SFImportObject o1, SFImportObject o2){
		         return o1.getACCOUNT1_NAME().compareToIgnoreCase(o2.getACCOUNT1_NAME());
		     }
		});

		// Build the Sibling consolidation list that takes multiple siblings, and associates them under the SIS student name field  that includes  of the first sibling to attend the school
		buildSiblingSubstitutionList( writeFullCsvIntermediate(list) );
		
		// For a given family, reduce the set of SFImportObjects to the least possible number while retaining the key data	
		List<SFImportObject> reducedList = reduceSFImports(list);
		
		// Write out the output file, reduced.csv, ready to be imported to Salesforce  
		FileWriter fwRed = new FileWriter(outFile);
		fwRed.write(SFImportObject.NPSP_Data_ImportTemplate+"\n");
		for(SFImportObject o: reducedList) {
			fwRed.write( o.toString() +"\n");
		}
		fwRed.close();		
		System.out.println("Wrote reduced.csv");
	}

	private static void doReplacements(List<SFImportObject> list) {
		for(SFImportObject o: list) {
			String replacement = Siblings.map.get(o.getACCOUNT1_NAME());

			if(replacement != null && replacement.length() > 0 ) {
				o.setACCOUNT1_NAME(o.setOA_SIS_Unique_ID(replacement));
			}
			replacement = Siblings.map.get(o.getACCOUNT2_NAME());
			if(replacement != null && replacement.length() > 0 ) {
				o.setACCOUNT2_NAME(replacement);
			}
		}
	}
	
	private static List<SFImportObject> reduceSFImports( List<SFImportObject> list) {
		List<SFImportObject> reducedlist = new ArrayList<SFImportObject>();
		List<SFImportObject> foreignKeyList = new ArrayList<SFImportObject>();
		String fkey = "";
		for(SFImportObject o: list) {
			if(fkey.isEmpty()) {
				fkey = o.getACCOUNT1_NAME();
			} else if( fkey.equals(o.getACCOUNT1_NAME())) {
				foreignKeyList.add(o);
			} else {
				reduceSet(foreignKeyList, reducedlist);
				foreignKeyList.clear();
				fkey = o.getACCOUNT1_NAME();
				foreignKeyList.add(o);
			}
		}
		return reducedlist;
	}
	
	private static Set<String> writeFullCsvIntermediate(List<SFImportObject> list) {
		FileWriter fullfw;
		Set<String> linkedHashSet = new LinkedHashSet<String>();
		try {
			fullfw = new FileWriter("full.csv");
				for(SFImportObject o: list) {
				linkedHashSet.add(o.getACCOUNT1_NAME());
				fullfw.write(o.toString()+"\n");
			}
			fullfw.close();
			System.out.println("Wrote full.csv");  
		} catch (IOException e) {
			System.out.println("ERROR: Writing full.csv");
		}
		return linkedHashSet;
	}
	
	private static List<SFImportObject>  parseInputFile(FileInputStream fstream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		List<SFImportObject> list = new ArrayList<SFImportObject>();
		String line = null;  
		try {
			while ((line = br.readLine()) != null)  {  
				String[] sa = null;
				int year = 0;
				try {
					year = new Integer(line.substring(0, 4));
					line += ",-";
					sa = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					if( sa.length != 139) {
						System.out.println("main: line skipped since it contained " + sa.length + " elements: " + line);
						continue;
					}
				} catch (Exception e ) {
					System.out.println("main, line : " + line + "\n threw " + e);
					continue;
				}
									
				List<SFImportObject> sfios = null;
				sfios = new NPSPData().parseLine(sa, year);		
				list.addAll(sfios);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("ERROR: Reading input file");
		}
		return list;
	}
	
	/*
	 * Reduce the set of parent and student names to one of each type for each participant 
	 * (with the exception of two parents, married and living together, who share one import object)
	 */
	private static void reduceSet(List<SFImportObject> foreignKeyList, List<SFImportObject> reducedlist) {
		List<SFImportObject> list = new ArrayList<SFImportObject>();
		Set<String> names = new HashSet<String>();
		
		for(SFImportObject o: foreignKeyList) {
			if(o.getOA_Account_Type().equals("Student") || o.getOA_Account_Type().equals("Summer Student") ) {
				list.add(o);
				names.add(o.getCONTACT1_FIRSTNAME());				
			}
		}
	
		// First reduce the students and summer students
		SFImportObject oNewest = null;
		String students = "";
		String First_Year_at_School = ""; 		//     Account1 Street
		String First_Year_Grade = ""; 			//     Account1 City
		String Last_Year_at_School = ""; 		//     Account1 State/Province
		String Last_Year_Grade = ""; 			//     Account1 Country
		String[] studentTypes = new String[]{ "Summer Student","Student"};
		for(String type : studentTypes ) {			
			for(String name : names) {
				for(SFImportObject o2 : list) {
					if( o2.getOA_Account_Type().equals(type) && o2.getCONTACT1_FIRSTNAME().equals(name) ) {
						if( ! students.contains(o2.getCONTACT1_FIRSTNAME())) {
							if(students.length() > 0 )
								students += ", ";
							students += (o2.getCONTACT1_FIRSTNAME());
						}

						if(oNewest == null) {
							oNewest = o2;
							First_Year_at_School    = o2.getSchoolYear();
							Last_Year_at_School     = o2.getSchoolYear();
							First_Year_Grade        = o2.getFirst_Year_Grade();
							Last_Year_Grade         = o2.getFirst_Year_Grade();
						} else {
							if (Integer.parseInt(o2.getSchoolYear().substring(0, 4)) < Integer.parseInt(First_Year_at_School.substring(0, 4))) {
								if(o2.getSchoolYear().length() > 0)
									First_Year_at_School = o2.getSchoolYear();
								if( o2.getFirst_Year_Grade().length() > 0)
									First_Year_Grade     = o2.getFirst_Year_Grade();
							} else if(Integer.parseInt(o2.getSchoolYear().substring(0, 4)) > Integer.parseInt(Last_Year_at_School.substring(0, 4))) {
								oNewest = o2;
								if(o2.getSchoolYear().length() > 0)
									Last_Year_at_School  =  o2.getSchoolYear();
								if(o2.getFirst_Year_Grade().length() > 0)
									Last_Year_Grade      =  o2.getFirst_Year_Grade();
							}
						}
					}
				} 
			if(oNewest != null) {
				oNewest.setFirst_Year_at_School(First_Year_at_School);
				oNewest.setLast_Year_at_School(Last_Year_at_School);
				oNewest.setFirst_Year_Grade(First_Year_Grade);
				oNewest.setLast_Year_Grade(Last_Year_Grade);
				if(type.equals("Student") && oNewest.getLast_Year_Grade().equals("12")) {
					oNewest.setClass_of(Last_Year_at_School.substring(5));
				}
					
				reducedlist.add(oNewest);
				oNewest = null;
				}
			} // this student			
		} // this studentType


		// Now reduce the adult record types, use the latest one as a basis for consolidation (it has the latest address info)
		list = new ArrayList<SFImportObject>();
		String[] adultTypes = new String[]{ "Parents Together","Parent","Uncle","Aunt","Father","Mother",
											"Brother","Sister","Guardian","Host Family","Grandparent","Friend","Agent","Other"};		
		
		Set<String> adults = new HashSet<String>(Arrays.asList(adultTypes));
		for(SFImportObject o: foreignKeyList) {
			if(adults.contains(o.getOA_Account_Type())) {
				list.add(o);
			}
		}

		oNewest = null;
		for(String type : adultTypes ) {			
			for(SFImportObject o2 : list) {
				if( o2.getOA_Account_Type().equals(type) ){
					if(oNewest == null) {
						oNewest = o2;
					} else {
						if (Integer.parseInt(o2.getSchoolYear().substring(0, 4)) < Integer.parseInt(First_Year_at_School.substring(0, 4))) {
						} else if(Integer.parseInt(o2.getSchoolYear().substring(0, 4)) > Integer.parseInt(Last_Year_at_School.substring(0, 4))) {
							oNewest = o2;
						}
					}
				}
			} // this adult
			
			if(oNewest != null) {
				oNewest.setFirst_Year_at_School(First_Year_at_School);  // This will be for the last Student processed, in the case of multiple enrollees, so it is imperfect
				oNewest.setLast_Year_at_School(Last_Year_at_School);
				oNewest.setFirst_Year_Grade(First_Year_Grade); 			
				oNewest.setLast_Year_Grade(Last_Year_Grade); 			
				oNewest.setStudent_Names("\"" + students + "\"");
				reducedlist.add(oNewest);
				oNewest = null;
			}
		} // all adult types
	}
	
	 /* each student and parent has a line for each year they attended at this point.  
	 * Also need to combine families under one key value.
	 * 
	 * Aardvark; Bill
	 * Aragon; Elizabeth
	 * Aragon; Gabriela
	 * Aragon; Sebastian 
	 * Aragon; Sergio
	 * Delancy; Steve
	 * Frank; Joey
	 */		
	private static void buildSiblingSubstitutionList(Set<String> linkedHashSet) {
		/* 
		 * Make a map of counters of unique Last Names
		 */		
		Map<String, Integer> counters = new HashMap<String, Integer>();
		for(String s : linkedHashSet) {
			String[] names = s.split(";");
			if( counters.containsKey(names[0].trim())) {
				Integer i = counters.get(names[0].trim());
				counters.put(names[0].trim(), ++i);
			} else {
				counters.put(names[0].trim(), new Integer(1));
			}
		}
		 
		List<String> remove = new ArrayList<String>();
		for(String s : linkedHashSet) {
			String[] names = s.split(";");
			if( counters.get(names[0].trim()) == 1 ) {
				remove.add(s);
			}
		}
		for(String s: remove) {
			linkedHashSet.remove(s);		// Remove the Names that don't need consolidation by family
		}
		
		/*
		Aragon; Elizabeth
		Aragon; Gabriela
		Aragon; Sebastian 
		Aragon; Sergio
		*/

		/*
		 * Write out the list of possible sibling matches, this could be commented out if execution speed became a problem
		 * because it is only needed once, and the output file sibs.txt is read into Siblings.java after being manually corrected.
		 * The data was so messy, it was easier to do a manual step here. 
		 */		
		String uniqueKey = "";
		FileWriter fw;
		try {
			fw = new FileWriter("sibs.txt");

			for(String s : linkedHashSet) {
				if(uniqueKey.equals("")) {
					uniqueKey = s.trim();
				} else {
					String[] keys  = uniqueKey.split(";");
					String[] names = s.split(";");
					if(keys[0].trim().equals(names[0].trim()) ) {
						fw.write("	  m.put(\"" + s.trim() + "\",\"" + uniqueKey + "\");\n" );
					} else {
						uniqueKey = s.trim();
					}
				}
			}
		fw.close();	
		} catch (IOException e) {
			System.out.println("ERROR: Writing sibs.txt");
		}
		System.out.println("Wrote sibs.txt");  
	}
	
	private static FileInputStream cleanUpOldFiles(String inFile, String outFile) {
		FileInputStream fstream = null;
		try {
			File f = new File(outFile);
	        f.delete();
	        f = new File("full.csv");
	        f.delete();
	        System.out.println("Deleted full.csv");
	        f = new File("sibs.txt");
	        f.delete();
	        System.out.println("Deleted sibs.txt");
	        f = new File("reduced.csv");
	        f.delete();
	        System.out.println("Deleted reduced.csv");
			fstream = new FileInputStream(inFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file ... " + inFile);
			return null;
		}		
		return fstream;
	}
}
