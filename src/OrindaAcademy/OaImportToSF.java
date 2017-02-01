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
		String outFile = "oaOut.csv";
		if(args.length > 0)
			inFile = args[0];
		if(args.length > 1)
			outFile = args[1];
		
		/*
		 * Step:  Delete the previous output files
		 */
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
			return;
		}		
		
		/*
		 * Step:  Read in the processed Excel file, that has been written out from Excel as a csv
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		List<SFImportObject> list = new ArrayList<SFImportObject>();
		String line = null;  
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
		
		/*
		 * Step:  Replace later student names with the first student name in a family so that they share the first ACCOUNT1 NAME (foreign key)
		 */		
		for(SFImportObject o: list) {
			String replacement = Siblings.map.get(o.ACCOUNT1_NAME.trim());

			if(replacement != null && replacement.length() > 0 ) {
				o.ACCOUNT1_NAME = o.OA_SIS_Unique_ID = replacement.trim();
			}
			replacement = Siblings.map.get(o.ACCOUNT2_NAME.trim());
			if(replacement != null && replacement.length() > 0 ) {
				o.ACCOUNT2_NAME = replacement.trim();
			}
		}
	
		/*
		 * Step:  Sort the list by ACCOUNT1_NAME
		 */		
		Collections.sort(list, new Comparator<SFImportObject>(){
		     public int compare(SFImportObject o1, SFImportObject o2){
		         return o1.ACCOUNT1_NAME.compareToIgnoreCase(o2.ACCOUNT1_NAME);
		     }
		});

		FileWriter fullfw = new FileWriter("full.csv");
		Set<String> linkedHashSet = new LinkedHashSet<String>();
		for(SFImportObject o: list) {
			linkedHashSet.add(o.ACCOUNT1_NAME.trim());
			fullfw.write(o.toString()+"\n");
		}
		fullfw.close();
		System.out.println("Wrote full.csv");  
		
		
		/*
		 * Build the Sibling consolidation list that takes multiple siblings, and associates them under the SIS student name field 
		 * that includes  of the first sibling to attend the school 
		 */
		buildSiblingSubstitutionList(linkedHashSet);
		
		/*
		 * Step:  For a given family, reduce the set of SFImportObjects to the least possible number while retaining the key data
		 */		
		List<SFImportObject> reducedlist = new ArrayList<SFImportObject>();
		List<SFImportObject> foreignKeyList = new ArrayList<SFImportObject>();
		String fkey = "";
		for(SFImportObject o: list) {
			if(fkey.isEmpty()) {
				fkey = o.ACCOUNT1_NAME;
			} else if( fkey.equals(o.ACCOUNT1_NAME)) {
				foreignKeyList.add(o);
			} else {
				reduceSet(foreignKeyList, reducedlist);
				foreignKeyList.clear();
				fkey = o.ACCOUNT1_NAME;
				foreignKeyList.add(o);
			}
		}
		
		/*
		 * Final Step: Write out the output file, reduced.csv, ready to be imported to Salesforce  
		 */		
		FileWriter fwRed = new FileWriter("reduced.csv");
		fwRed.write(SFImportObject.NPSP_Data_ImportTemplate+"\n");
		for(SFImportObject o: reducedlist) {
			fwRed.write( o.toString() +"\n");
		}
		fwRed.close();		
		System.out.println("Wrote reduced.csv");

	}

	/*
	 * Reduce the set of parent and student names to one of each type for each participant 
	 * (with the exception of two parents, married and living together, who share one import object)
	 */
	private static void reduceSet(List<SFImportObject> foreignKeyList, List<SFImportObject> reducedlist) {
		List<SFImportObject> list = new ArrayList<SFImportObject>();
		Set<String> names = new HashSet<String>();
		
		for(SFImportObject o: foreignKeyList) {
			if(o.OA_Account_Type.equals("Student") || o.OA_Account_Type.equals("Summer Student") ) {
				list.add(o);
				names.add(o.CONTACT1_FIRSTNAME);				
			}
		}
	
		// First reduce the students and summer students
		SFImportObject oNewest = null;
		String First_Year_at_School = ""; 		//     Account1 Street
		String First_Year_Grade = ""; 			//     Account1 City
		String Last_Year_at_School = ""; 		//     Account1 State/Province
		String Last_Year_Grade = ""; 			//     Account1 Country
		String[] studentTypes = new String[]{ "Student","Summer Student" };
		for(String type : studentTypes ) {			
			for(String name : names) {
				for(SFImportObject o2 : list) {
					if( o2.OA_Account_Type.equals(type) && o2.CONTACT1_FIRSTNAME.equals(name) ) {
						if(oNewest == null) {
							oNewest = o2;
							First_Year_at_School    = o2.SchoolYear;
							Last_Year_at_School     = o2.SchoolYear;
							First_Year_Grade        = o2.First_Year_Grade;
							Last_Year_Grade         = o2.First_Year_Grade;
						} else {
							if (Integer.parseInt(o2.SchoolYear.substring(0, 4)) < Integer.parseInt(First_Year_at_School.substring(0, 4))) {
								if(o2.SchoolYear.length() > 0)
									First_Year_at_School = o2.SchoolYear;
								if( o2.First_Year_Grade.length() > 0)
									First_Year_Grade     = o2.First_Year_Grade;
							} else if(Integer.parseInt(o2.SchoolYear.substring(0, 4)) > Integer.parseInt(Last_Year_at_School.substring(0, 4))) {
								oNewest = o2;
								if(o2.SchoolYear.length() > 0)
									Last_Year_at_School  =  o2.SchoolYear;
								if(o2.First_Year_Grade.length() > 0)
									Last_Year_Grade      =  o2.First_Year_Grade;
							}
						}
					}
				} 
			if(oNewest != null) {
				oNewest.First_Year_at_School  = First_Year_at_School;
				oNewest.Last_Year_at_School   = Last_Year_at_School;
				oNewest.First_Year_Grade      = First_Year_Grade;
				oNewest.Last_Year_Grade       = Last_Year_Grade;
				if(type.equals("Student") && oNewest.Last_Year_Grade.equals("12")) {
					oNewest.Class_of = Last_Year_at_School.substring(5);
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
			if(adults.contains(o.OA_Account_Type)) {
				list.add(o);
			}
		}

		oNewest = null;
		for(String type : adultTypes ) {			
			for(SFImportObject o2 : list) {
				if( o2.OA_Account_Type.equals(type) ){
					if(oNewest == null) {
						oNewest = o2;
						First_Year_at_School 	= o2.SchoolYear;
						Last_Year_at_School 	= o2.SchoolYear;
					} else {
						if (Integer.parseInt(o2.SchoolYear.substring(0, 4)) < Integer.parseInt(First_Year_at_School.substring(0, 4))) {
							if(o2.SchoolYear.length() > 0)
								First_Year_at_School = o2.SchoolYear;
							if( o2.First_Year_Grade.length() > 0)
								First_Year_Grade     = o2.First_Year_Grade;
						} else if(Integer.parseInt(o2.SchoolYear.substring(0, 4)) > Integer.parseInt(Last_Year_at_School.substring(0, 4))) {
							oNewest = o2;
							if(o2.SchoolYear.length() > 0)
								Last_Year_at_School  =  o2.SchoolYear;
							if(o2.First_Year_Grade.length() > 0)
								Last_Year_Grade      =  o2.First_Year_Grade;
						}
					}
				}
			} // this adult
			
			if(oNewest != null) {
				oNewest.First_Year_at_School   = First_Year_at_School;
				oNewest.Last_Year_at_School 	= Last_Year_at_School;
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
	private static void buildSiblingSubstitutionList(Set<String> linkedHashSet) throws IOException {
		/* 
		 * Step:  Make a map of counters of unique Last Names
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
		 * Step: Write out the list of possible sibling matches, this could be commented out if execution speed became a problem
		 * because it is only needed once, and the output file sibs.txt is read into Siblings.java after being manually corrected.
		 * The data was so messy, it was easier to do a manual step here. 
		 */		
		String uniqueKey = "";
		FileWriter fw = new FileWriter("sibs.txt");

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
		System.out.println("Wrote sibs.txt");  
	}
	
}
