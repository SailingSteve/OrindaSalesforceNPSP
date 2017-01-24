package OrindaAcademy;

import java.util.ArrayList;
import java.util.List;

/**
 * parse a line into a SFImportObject
 * @author stevepodell
 */
public class NPSPData {
	
	public NPSPData() {
	}
	
	/**
	 * Early days (2005 through 2011), the only choices are two parents together, one parent, two parents apart.   
	 * 
	 * If PARENT/GUARDIAN contains two names, and there is NO PARENT/GUAR 2 
	 * 		then take the first two CONTACTs, make them into a single SF row
	 * 		then make a student row
	 * 
	 * If PARENT/GUARDIAN contains two names, and there IS A PARENT/GUAR 2
	 * 		then two SF rows
	 * 		then make a student row
	 * 
	 * If PARENT/GUARDIAN contains one name
	 * 		then make one SF row
	 * 		then make a student row
 	 */
	
	public List<SFImportObject> parseLine(String[] sa, int year) {
		SFImportObject sfio2Par = new SFImportObject();
		SFImportObject sfioPar1 = new SFImportObject();
		SFImportObject sfioPar2 = new SFImportObject();
		SFImportObject sfioPar3 = new SFImportObject();
		SFImportObject sfioPar4 = new SFImportObject();
		SFImportObject sfioKido = new SFImportObject();

		try {
			sfio2Par.SchoolYear = sfioPar1.SchoolYear = sfioPar2.SchoolYear = sfioPar3.SchoolYear = sfioPar4.SchoolYear = sfioKido.SchoolYear = sa[0].trim();
			String foreignKey = sa[2].replaceAll("!", "").trim();
			sfio2Par.ACCOUNT1_NAME = sfioPar1.ACCOUNT1_NAME = sfioPar2.ACCOUNT1_NAME = sfioPar3.ACCOUNT1_NAME = sfioPar4.ACCOUNT1_NAME = 
						sfioKido.ACCOUNT1_NAME = foreignKey;
					
			if( year == 2005 ) {
				parse2005ParentsAndAddresses(sfio2Par, sfioPar1, sfioPar2, sfioKido, sa);
			} else if( year < 2011 ){
				parseEarlyParentsAndAddresses(sfio2Par, sfioPar1, sfioPar2, sfioKido, sa);				
			} else {
				parseLateParentsAndAddresses(sfio2Par, sfioPar1, sfioPar2, sfioPar3, sfioPar4, sfioKido, sa);
			}

			sfioKido.FirstYearatSchool = sa[0];

		} catch( Exception e ) {
			System.out.println("parseEarlyLine, Error parsing: " + sa);
		}

		List<SFImportObject> objs = new ArrayList<SFImportObject>();
		if( sfio2Par.CONTACT_TYPE != "Null" ) 
			objs.add(sfio2Par);
		if( sfioPar1.CONTACT_TYPE != "Null" ) 
			objs.add(sfioPar1);
		if( sfioPar2.CONTACT_TYPE != "Null" ) 
			objs.add(sfioPar2);
		if( sfioPar3.CONTACT_TYPE != "Null" ) 
			objs.add(sfioPar3);
		if( sfioPar4.CONTACT_TYPE != "Null" ) 
			objs.add(sfioPar4);
		
		objs.add(sfioKido);
		
		return objs;
	}

	private String[] buildSFName(String name) {
		String first = "";
		String last = "";
		
		name = name.trim();
		String[] sap = name.split(" ");
		if(name.length() < 1) {
		} else if( sap.length == 1 ) {
			first = name;
		} else if( sap.length == 2) {
			first = sap[0];
			last = sap[1];
		} else {
			for( int i = 0; i < sap.length - 1; i++ ) {
				first += sap[i] + " ";
			}
			first = first.trim();
			last = sap[sap.length-1];
		}
		String[] temp = {first,last};
		return temp;
	}

	// Parents together same name, different name, and single parent
	private void parseEarlyParentsNonSecondaryPhonesAddress(SFImportObject sfio, boolean bManFirst, String[] sa) {
		int year = Integer.valueOf(sa[0].substring(0, 4));
		
		sfio.HOME_STREET              = sa[13];
		sfio.HOME_CITY                = sa[14];
		sfio.HOME_STATE_PROVINCE      = sa[15];
		sfio.HOME_ZIP_POSTAL_CODE     = sa[16];
		sfio.HOUSEHOLD_PHONE          = sa[17];
		sfio.CONTACT1_WORK_PHONE      = (bManFirst) ? sa[20] : sa[21];
		sfio.CONTACT2_WORK_PHONE      = (bManFirst) ? sa[21] : sa[20];
		sfio.CONTACT1_MOBILE_PHONE    = (bManFirst) ? sa[22] : sa[23];
		sfio.CONTACT2_MOBILE_PHONE    = (bManFirst) ? sa[23] : sa[22];
		sfio.CONTACT1_SALUTATION      = sa[24];
		
		if( year == 2008 || year == 2009 || year == 2010 ) {
			sfio.CONTACT1_PREFERRED_EMAIL = sa[33];
			sfio.CONTACT1_PERSONAL_EMAIL  = (bManFirst) ? sa[33] : sa[34];
			sfio.CONTACT2_PERSONAL_EMAIL  = (bManFirst) ? sa[34] : sa[33];
			sfio.CONTACT2_PREFERRED_EMAIL = (bManFirst) ? sa[34] : sa[33];
		} else {
			sfio.CONTACT1_PREFERRED_EMAIL = sa[33];
			sfio.CONTACT1_PERSONAL_EMAIL  = (bManFirst) ? sa[34] : sa[36];
			sfio.CONTACT2_PERSONAL_EMAIL  = (bManFirst) ? sa[36] : sa[34];
			sfio.CONTACT1_WORK_EMAIL      = (bManFirst) ? sa[35] : sa[37];
			sfio.CONTACT2_WORK_EMAIL      = (bManFirst) ? sa[37] : sa[35];			
		}
		
		sfio.Relationship1            = (bManFirst) ? "Father" : "Mother";
		if(sfio.CONTACT_TYPE.equals("Parents Together")) {
			sfio.Relationship2        = (bManFirst) ? "Mother" : "Father";
		}
	}
	
	
	private void parseEarlyParentsAndAddresses(SFImportObject sfio2Par, SFImportObject sfioPar1, SFImportObject sfioPar2, SFImportObject sfioKido, String[] sa) {
		try { 
			boolean bManFirst = sa[4].contains("1");
			if( sa[3].contains("/") ) {								// Two parents living together, different last names
				String[] pars = sa[3].split("/");					// Jacob Rosenberg / Patricia Reed	
				String[] names = buildSFName(pars[0]);
				sfio2Par.CONTACT1_FIRSTNAME = names[0].trim();
				sfio2Par.CONTACT1_LASTNAME = names[1].trim();
				names = buildSFName(pars[1]);
				sfio2Par.CONTACT2_FIRSTNAME = names[0].trim();
				sfio2Par.CONTACT2_LASTNAME  = names[1].trim();
				sfio2Par.CONTACT_TYPE = "Parents Together";
				sfio2Par.CONTACT_PRIMARY = "true";
				parseEarlyParentsNonSecondaryPhonesAddress( sfio2Par,  bManFirst, sa);
			} else if ( sa[3].contains("&") ) {						// Two parents living together, SAME last names
				String[] pars = sa[3].split("&");                   // Monika & Alan Rosenfeld	
				sfio2Par.CONTACT1_FIRSTNAME = pars[0].trim();
				String[] names = buildSFName(pars[1]);
				sfio2Par.CONTACT2_FIRSTNAME = names[0].trim();
				sfio2Par.CONTACT1_LASTNAME  = names[1].trim();
				sfio2Par.CONTACT2_LASTNAME  = names[1].trim();
				sfio2Par.CONTACT_TYPE = "Parents Together";
				sfio2Par.CONTACT_PRIMARY = "true";
				parseEarlyParentsNonSecondaryPhonesAddress( sfio2Par,  bManFirst, sa);
			} else {												// One parent living with child
				String[] names = buildSFName(sa[3]);
				sfioPar1.CONTACT1_FIRSTNAME = names[0].trim();
				sfioPar1.CONTACT1_LASTNAME  = names[1].trim();
				sfioPar1.CONTACT_TYPE = "Parent";
				sfioPar1.CONTACT_PRIMARY = "true";
				parseEarlyParentsNonSecondaryPhonesAddress( sfioPar1,  bManFirst, sa);
				if( sa[5].trim().length() > 0 ) {					// Second parent at another address
					names = buildSFName(sa[5]);
					sfioPar2.CONTACT1_FIRSTNAME = names[0].trim();
					sfioPar2.CONTACT1_LASTNAME  = names[1].trim();
					sfioPar2.CONTACT_TYPE = "Parent";
					sfioPar2.HOME_STREET           = sa[26];
					sfioPar2.HOME_CITY             = sa[27];
					sfioPar2.HOME_STATE_PROVINCE   = sa[28];
					sfioPar2.HOME_ZIP_POSTAL_CODE  = sa[29];
					sfioPar2.HOUSEHOLD_PHONE       = sa[30];
					sfioPar2.CONTACT1_WORK_PHONE   = sa[31];
					sfioPar2.CONTACT1_SALUTATION   = sa[32];
					sfioPar2.CONTACT1_WORK_EMAIL   = sa[21];
					sfioPar2.CONTACT2_WORK_EMAIL   = sa[20];
					sfioPar2.CONTACT1_ALTERNATE_EMAIL = sa[38];
					sfioPar2.CONTACT2_ALTERNATE_EMAIL = sa[40];
				}
			}

			parseEarlyKidRecord(sfioKido, sa);
			
		} catch( Exception e ) {
			System.out.println("parseEarlyParentsAndAddresses, Error parsing " + sa[3] + " in year " + sa[0]);
		}
	}

	private void parse2005ParentsAndAddresses(SFImportObject sfio2Par, SFImportObject sfioPar1, SFImportObject sfioPar2, SFImportObject sfioKido, String[] sa) {
		try { 
			String[] names = null;
			boolean bManFirst = sa[4].contains("1");
			if(sa[5].trim().isEmpty()) {		// PARENT/GUAR 2, One parent living with child (No second parent name)
				names = buildSFName(sa[3]);
				sfioPar1.CONTACT1_FIRSTNAME = names[0].trim();
				sfioPar1.CONTACT1_LASTNAME  = names[1].trim();
				sfioPar1.CONTACT_TYPE = "Parent";
				sfioPar1.CONTACT_PRIMARY = "true";
				parseEarlyParentsNonSecondaryPhonesAddress( sfioPar1,  bManFirst, sa);
			} else if( ! sa[26].trim().isEmpty() ) { // PAR 2 STREET has content, then two parents with two addresses
				names = buildSFName(sa[5]);
				sfioPar2.CONTACT1_FIRSTNAME = names[0].trim();
				sfioPar2.CONTACT1_LASTNAME  = names[1].trim();
				sfioPar2.CONTACT_TYPE = "Parent";
				sfioPar2.HOME_STREET           = sa[26];
				sfioPar2.HOME_CITY             = sa[27];
				sfioPar2.HOME_STATE_PROVINCE   = sa[28];
				sfioPar2.HOME_ZIP_POSTAL_CODE  = sa[29];
				sfioPar2.HOUSEHOLD_PHONE       = sa[30];
				sfioPar2.CONTACT1_HOME_PHONE   = sa[30];
				sfioPar2.CONTACT1_WORK_PHONE   = sa[31];
				sfioPar2.CONTACT1_SALUTATION   = sa[32];
				sfioPar2.CONTACT1_WORK_EMAIL   = sa[21];
				sfioPar2.CONTACT2_WORK_EMAIL   = sa[20];
				sfioPar2.CONTACT1_ALTERNATE_EMAIL = sa[38];
				sfioPar2.CONTACT2_ALTERNATE_EMAIL = sa[40];
				parseEarlyParentsNonSecondaryPhonesAddress( sfioPar2,  bManFirst, sa);
			} else {								// Two parents living together,
				names = buildSFName(sa[3]);
				sfio2Par.CONTACT1_FIRSTNAME = names[0].trim();
				sfio2Par.CONTACT1_LASTNAME = names[1].trim();
				names = buildSFName(sa[5]);
				sfio2Par.CONTACT2_FIRSTNAME = names[0].trim();
				sfio2Par.CONTACT2_LASTNAME  = names[1].trim();
				sfio2Par.CONTACT_TYPE = "Parents Together";
				sfio2Par.CONTACT_PRIMARY = "true";
				parseEarlyParentsNonSecondaryPhonesAddress( sfio2Par,  bManFirst, sa);
			} 
			parseEarlyKidRecord(sfioKido, sa);
			
		} catch( Exception e ) {
			System.out.println("parse2005ParentsAndAddresses, Error parsing " + sa[3] + " in year " + sa[0]);
		}
	}
	
	private void parseEarlyKidRecord(SFImportObject sfioKido, String[] sa) {
		try { 
			sfioKido.CONTACT1_FIRSTNAME = sa[7].trim();				// The student,  living with the primary parent, or both parents
			if( sa[8].trim().length() > 0 ) {
				sfioKido.CONTACT1_FIRSTNAME += " " + sa[8].trim();
			}
			sfioKido.CONTACT1_LASTNAME    = sa[6].trim();
			sfioKido.CONTACT_TYPE 		  = "Student";
			sfioKido.Relationship1        = "Student";
			sfioKido.FirstYearGrade       = sa[11];
			sfioKido.HOME_STREET          = sa[13];
			sfioKido.HOME_CITY            = sa[14];
			sfioKido.HOME_STATE_PROVINCE  = sa[15];
			sfioKido.HOME_ZIP_POSTAL_CODE = sa[16];
			sfioKido.HOUSEHOLD_PHONE      = sa[17];
			if( ! sa[19].equals("1/0/00") )
				sfioKido.CONTACT1_BIRTHDATE   = sa[19];

		} catch( Exception e ) {
			System.out.println("parseKidRecord, Error parsing " + sa[3] + " in year " + sa[0]);
		}
	}

	private void parseLateKidRecord(SFImportObject sfioKido, String[] sa) {
		try { 
			// sa[2] = Kornguth; Gregory!
			String name = sa[2].replaceAll("[! ]", "");
			String[] sap = name.split(";");

			sfioKido.CONTACT1_FIRSTNAME = sap[1];				// The student,  living with the primary parent, or both parents
			sfioKido.CONTACT1_LASTNAME  = sap[0];
			if(sa[137].equals("ACTIVE")) {
				sfioKido.CONTACT_TYPE  = "Student";		
				sfioKido.Relationship1 = "Student";
			} else {
				sfioKido.CONTACT_TYPE  = "Summer Student";
				sfioKido.Relationship1 = "Summer Student";
			}

			sfioKido.HOME_STREET            = sa[12];
			sfioKido.HOME_CITY              = sa[13];
			sfioKido.HOME_STATE_PROVINCE    = sa[14];
			sfioKido.HOME_COUNTRY           = sa[15];
			sfioKido.HOME_ZIP_POSTAL_CODE   = sa[16];
			sfioKido.HOUSEHOLD_PHONE        = sa[17];

			if( ! sa[136].equals("1/0/00") )
				sfioKido.CONTACT1_BIRTHDATE   	= sa[136];
			sfioKido.FirstYearGrade    		= sa[135];

		} catch( Exception e ) {
			System.out.println("parseKidRecord, Error parsing " + sa[3] + " in year " + sa[0]);
		}
	}

	/**
	 * For APID create a "Yang; Mulun (Jill)" "Account1 Name" foreign key for each family record.
	 * If CONTACT1.ADDRESS.STREET matches CONTACT2.ADDRESS.STREET
	 * 		then take the first two CONTACTs, make them into a single SF row
	 * 		then make a student row
	 * 		if there are CONTACT3 and CONTACT4, make a record for each
	 * If CONTACT1.ADDRESS.STREET DOES NOT match CONTACT2.ADDRESS.STREET
	 * 		Make SF Rows for CONTACT1, CONTACT2, CONTACT3, CONTACT4 (if they exist)
	 * 		then make a student row
	 */
	
	private void parseLateParentsAndAddresses(SFImportObject sfio2Par, SFImportObject sfioPar1, SFImportObject sfioPar2, 
			SFImportObject sfioPar3, SFImportObject sfioPar4, SFImportObject sfioKido, String[] sa) {
		try { 
			if( compareAddresses(sa[12], sa[45]) ) {                    // Two parents living together, same address
				sfio2Par.CONTACT1_FIRSTNAME = sa[5].trim();
				if( ! sa[6].trim().isEmpty() )
					sfio2Par.CONTACT1_FIRSTNAME += " " + sa[6].trim();
				sfio2Par.CONTACT1_LASTNAME = sa[7].trim();
				if( ! sa[8].trim().isEmpty() )
					sfio2Par.CONTACT1_LASTNAME += " " + sa[8].trim();

				sfio2Par.CONTACT2_FIRSTNAME = sa[38].trim();
				if( ! sa[41].trim().isEmpty() )
					sfio2Par.CONTACT2_FIRSTNAME += " " + sa[39].trim();
				sfio2Par.CONTACT2_LASTNAME = sa[40].trim();
				if( ! sa[41].trim().isEmpty() )
					sfio2Par.CONTACT2_LASTNAME += " " + sa[41].trim();
				sfio2Par.CONTACT_TYPE = "Parents Together";
				sfio2Par.CONTACT_PRIMARY = "true";
				parseLateParentsMarriedPhonesAddress( sfio2Par, sa);
				if( ! sa[7+(2*33)].trim().isEmpty() ) 					// If there is a contact 3 last name ...   7 40 73
					parseLateContactsNamePhonesAddress(sfioPar3, 3, sa);
				if( ! sa[7+(3*33)].trim().isEmpty() ) 					// If there is a contact 4 last name ...
					parseLateContactsNamePhonesAddress(sfioPar4, 4, sa);
			} else {						// Multiple parent records... 1, 2, 3, and 4
				parseLateContactsNamePhonesAddress(sfioPar1, 1, sa);	// Should always be a contact1
				if( ! sa[7+(1*33)].trim().isEmpty() ) 					// If there is a contact 2 last name ...
					parseLateContactsNamePhonesAddress(sfioPar2, 2, sa);
				if( ! sa[7+(2*33)].trim().isEmpty() ) 					// If there is a contact 3 last name ...
					parseLateContactsNamePhonesAddress(sfioPar3, 3, sa);
				if( ! sa[7+(3*33)].trim().isEmpty() ) 					// If there is a contact 4 last name ...
					parseLateContactsNamePhonesAddress(sfioPar4, 4, sa);
			}

			parseLateKidRecord(sfioKido, sa);
			
		} catch( Exception e ) {
			System.out.println("parseLateParentsAndAddresses, Error parsing " + sa[3] + " in year " + sa[0]);
		}

	}
	
	// "75 Ardmore" and "75 Ardmore Dr" and "75 Ardmore Dr." and "75 Ardmore Drive"
	private boolean compareAddresses(String add1, String add2) {
		String[] sa1 = add1.split(" ");
		String[] sa2 = add2.split(" ");
		
		return (sa1.length > 1) && (sa2.length > 1) && sa1[0].equals(sa2[0]) && sa1[1].equals(sa2[1]); 
	}
	
	
	// Parents together same name or different name
	private void parseLateParentsMarriedPhonesAddress(SFImportObject sfio, String[] sa) {
		sfio.HOME_STREET              = sa[12];
		sfio.HOME_CITY                = sa[13];
		sfio.HOME_STATE_PROVINCE      = sa[14];
		sfio.HOME_COUNTRY     		  = sa[15];
		sfio.HOME_ZIP_POSTAL_CODE     = sa[16];
		sfio.HOUSEHOLD_PHONE          = sa[17];
		sfio.CONTACT1_HOME_PHONE   	  = sa[17];
		sfio.CONTACT1_MOBILE_PHONE    = sa[19];
		sfio.CONTACT1_WORK_PHONE      = sa[20];
		
		sfio.CONTACT1_SALUTATION      = sa[4];
		sfio.CONTACT1_PREFERRED_EMAIL = sa[22];
		sfio.CONTACT1_PERSONAL_EMAIL  = sa[22];
		sfio.CONTACT1_WORK_EMAIL      = sa[23];

		sfio.CONTACT2_MOBILE_PHONE    = sa[52];
		sfio.CONTACT2_WORK_PHONE      = sa[53];
		
		sfio.CONTACT2_SALUTATION      = sa[37];
		sfio.CONTACT2_PREFERRED_EMAIL = sa[55];
		sfio.CONTACT2_PERSONAL_EMAIL  = sa[55];
		sfio.CONTACT2_WORK_EMAIL      = sa[56];
		
		sfio.Relationship1            = sa[26];
		sfio.Relationship2            = sa[59];
	}
	
	// Single Parents, anyone in a SIS contact1, and also handles contact2,3,&4
	private void parseLateContactsNamePhonesAddress(SFImportObject sfio, int contact, String[] sa) {		
		int offset = 0;
		switch(contact) {
			case 1:
				offset = 0;
				sfio.CONTACT_PRIMARY = "true";
				break;
			case 2:
				offset = 33; 
				break;
			case 3:
				offset = 33*2; 
				break;
			case 4:
				offset = 33*3;
				break;
		}
		
		sfio.CONTACT1_FIRSTNAME = sa[5+offset].trim();
		if( ! sa[6+offset].trim().isEmpty() )
			sfio.CONTACT1_FIRSTNAME += " " + sa[6+offset].trim();
		sfio.CONTACT1_LASTNAME = sa[7+offset].trim();
		if( ! sa[8+offset].trim().isEmpty() )
			sfio.CONTACT1_LASTNAME += " " + sa[8+offset].trim();

		sfio.HOME_STREET              = sa[12+offset];
		sfio.HOME_CITY                = sa[13+offset];
		sfio.HOME_STATE_PROVINCE      = sa[14+offset];
		sfio.HOME_COUNTRY     		  = sa[15+offset];
		sfio.HOME_ZIP_POSTAL_CODE     = sa[16+offset];

		sfio.HOUSEHOLD_PHONE          = sa[17+offset];
		sfio.CONTACT1_HOME_PHONE      = sa[17+offset];
		sfio.CONTACT1_MOBILE_PHONE    = sa[19+offset];
		sfio.CONTACT1_WORK_PHONE      = sa[20+offset];
		
		sfio.CONTACT1_SALUTATION      = sa[4+offset];
		sfio.CONTACT1_PREFERRED_EMAIL = sa[22+offset];
		sfio.CONTACT1_PERSONAL_EMAIL  = sa[22+offset];
		sfio.CONTACT1_WORK_EMAIL      = sa[23+offset];
		
		sfio.Relationship1            = sa[26+offset];
		sfio.CONTACT_TYPE 			  = sa[26+offset];
	}
}
