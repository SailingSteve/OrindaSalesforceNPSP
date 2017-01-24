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
		
		// Delete the output files
		FileInputStream fstream = null;
		try {
			File f = new File(outFile);
	        f.delete();
	        f = new File("sibs.txt");
	        f.delete();
	        System.out.println("Deleted sibs.txt");
	        f = new File("familyShareForeignKey.csv");
	        f.delete();
	        System.out.println("Deleted familyShareForeignKey.csv");
	        f = new File("reduced.csv");
	        f.delete();
	        System.out.println("Deleted reduced.csv");
			fstream = new FileInputStream(inFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file ... " + inFile);
			return;
		}		
		
		// Read in the processed Excel file, that has been written out as csv
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
	
		// Sort by foreign key (ACCOUNT1_NAME)
		Collections.sort(list, new Comparator<SFImportObject>(){
		     public int compare(SFImportObject o1, SFImportObject o2){
		         return o1.ACCOUNT1_NAME.compareToIgnoreCase(o2.ACCOUNT1_NAME);
		     }
		});

		Set<String> linkedHashSet = new LinkedHashSet<String>();
		for(SFImportObject o: list) {
			linkedHashSet.add(o.ACCOUNT1_NAME.trim());
		}
		
		// Start the deduping, each student and parent has a line for each year they attended at this point.  Also need to combine families under one key value.
		/*
		Aardvark; Bill
		Agramont; Elizabeth
		Agramont; Gabriela
		Agramont; Sebastian 
		Agramont; Sergio
		Podell; Steve
		Frank; Joey
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
			linkedHashSet.remove(s);
		}
		
		/*
		Agramont; Elizabeth
		Agramont; Gabriela
		Agramont; Sebastian 
		Agramont; Sergio
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
		// This is the list that goes into Siblings.java, I didn't make this automated since there is so much inconsistent data.  
		
		// Families share the first ACCOUNT1 NAME (foreign key)
		for(SFImportObject o: list) {
			String replacement = Siblings.map.get(o.ACCOUNT1_NAME.trim());
			if(replacement != null && replacement.length() > 0 ) {
				o.ACCOUNT1_NAME = replacement.trim();
			}
		}
		
		FileWriter fwRaw = new FileWriter("familyShareForeignKey.csv");
		fwRaw.write(SFImportObject.NPSP_Data_ImportTemplate+"\n");
		for(SFImportObject o: list) {
			fwRaw.write( o.toString() +"\n");
		}
		fwRaw.close();
		System.out.println("Wrote familyShareForeignKey.csv");


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
		
		FileWriter fwRed = new FileWriter("reduced.csv");
		fwRed.write(SFImportObject.NPSP_Data_ImportTemplate+"\n");
		for(SFImportObject o: reducedlist) {
			fwRed.write( o.toString() +"\n");
		}
		fwRed.close();		
		System.out.println("Wrote reduced.csv");

	}

	private static void reduceSet(List<SFImportObject> foreignKeyList, List<SFImportObject> reducedlist) {
		List<SFImportObject> list = new ArrayList<SFImportObject>();
		Set<String> names = new HashSet<String>();

		for(SFImportObject o: foreignKeyList) {
			if(o.CONTACT_TYPE.equals("Student") || o.CONTACT_TYPE.equals("Summer Student") ) {
				list.add(o);
				names.add(o.CONTACT1_FIRSTNAME);
			}
		}

		// First reduce the students and summer students
		SFImportObject oNewest = null;
		String FirstYearatSchool = ""; 		//     Account1 Street
		String FirstYearGrade = ""; 			//     Account1 City
		String LastYearatSchool = ""; 		//     Account1 State/Province
		String LastYearGrade = ""; 			//     Account1 Country
		String[] studentTypes = new String[]{ "Student","Summer Student" };
		for(String type : studentTypes ) {			
			for(String name : names) {
				for(SFImportObject o2 : list) {
					if( o2.CONTACT_TYPE.equals(type) && o2.CONTACT1_FIRSTNAME.equals(name) ) {
						if(oNewest == null) {
							oNewest = o2;
							FirstYearatSchool 	= o2.SchoolYear;
							LastYearatSchool 	= o2.SchoolYear;
							FirstYearGrade 		= o2.FirstYearGrade;
							LastYearGrade 		= o2.FirstYearGrade;
						} else {
							if (Integer.parseInt(o2.SchoolYear.substring(0, 4)) < Integer.parseInt(FirstYearatSchool.substring(0, 4))) {
								FirstYearatSchool = o2.SchoolYear;
								FirstYearGrade    = o2.FirstYearGrade;
							} else if(Integer.parseInt(o2.SchoolYear.substring(0, 4)) > Integer.parseInt(LastYearatSchool.substring(0, 4))) {
								oNewest = o2;
								LastYearatSchool  = o2.SchoolYear;
								LastYearGrade     = o2.FirstYearGrade;
							}
						}
					}
				} 
			if(oNewest != null) {
				oNewest.FirstYearatSchool   = FirstYearatSchool;
				oNewest.LastYearatSchool 	= LastYearatSchool;
				oNewest.FirstYearGrade      = FirstYearGrade;
				oNewest.LastYearGrade       = LastYearGrade;
				reducedlist.add(oNewest);
				oNewest = null;
				}
			} // this student			
		} // this studentType

		// Now reduce the adult record types, use the latest one as a basis for consolidation (has the latest address info)
		list = new ArrayList<SFImportObject>();
		String[] adultTypes = new String[]{ "Parent","Parents Together", "Mother","Father","Grandparent","Other",
											"Brother","Sister","Guardian","Host Family","Uncle","Aunt"};
		Set<String> adults = new HashSet<String>(Arrays.asList(adultTypes));
		for(SFImportObject o: foreignKeyList) {
			if(adults.contains(o.CONTACT_TYPE)) {
				list.add(o);
			}
		}

		oNewest = null;
		for(String type : adultTypes ) {			
			for(SFImportObject o2 : list) {
				if( o2.CONTACT_TYPE.equals(type) ){
					if(oNewest == null) {
						oNewest = o2;
						FirstYearatSchool 	= o2.SchoolYear;
						LastYearatSchool 	= o2.SchoolYear;
					} else {
						if (Integer.parseInt(o2.SchoolYear.substring(0, 4)) < Integer.parseInt(FirstYearatSchool.substring(0, 4))) {
							FirstYearatSchool = o2.SchoolYear;
							FirstYearGrade    = o2.FirstYearGrade;
						} else if(Integer.parseInt(o2.SchoolYear.substring(0, 4)) > Integer.parseInt(LastYearatSchool.substring(0, 4))) {
							oNewest = o2;
							LastYearatSchool  = o2.SchoolYear;
							LastYearGrade     = o2.FirstYearGrade;
						}
					}
				}
			} // this adult
			
			if(oNewest != null) {
				oNewest.FirstYearatSchool   = FirstYearatSchool;
				oNewest.LastYearatSchool 	= LastYearatSchool;
				reducedlist.add(oNewest);
				oNewest = null;
			}

		} // all adult types
	}
}
