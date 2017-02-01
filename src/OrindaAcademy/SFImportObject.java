package OrindaAcademy;

/**
 * An object to contain a the data for a NPSP_Data_ImportTemplate line, and a "toString()" to generate that output line
 * Salesforces's "Non Profit Success Package"
 * @author stevepodell
 *
 */
public class SFImportObject {
	public static String NPSP_Data_ImportTemplate = 
			"Household OA SIS Unique ID,"
			+ "Contact1 Salutation,Contact1 First Name,Contact1 Last Name,Contact1 Birthdate,Contact1 Title,Contact1 Personal Email,Contact1 Work Email,Contact1 Alternate Email,Contact1 Preferred Email,Contact1 Home Phone,Contact1 Work Phone,Contact1 Mobile Phone,Contact1 Other Phone,Contact1 Preferred Phone,"
			+ "Contact2 Salutation,Contact2 First Name,Contact2 Last Name,Contact2 Birthdate,Contact2 Title,Contact2 Personal Email,Contact2 Work Email,Contact2 Alternate Email,Contact2 Preferred Email,Contact2 Home Phone,Contact2 Work Phone,Contact2 Mobile Phone,Contact2 Other Phone,Contact2 Preferred Phone,"
			+ "Account1 Name,"
			+ "Account2 Name,"
			+ "Home Street,Home City,Home State/Province,Home Zip/Postal Code,Home Country,"
			+ "Household OA Account Type,"
			+ "Household First Year at School,Household First Year Grade,Household Last Year at School,Household Last Year Grade,Household Class of,"
			+ "Contact1 Relationship1,Contact2 Relationship2,Contact Primary,OA Household Phone";
	/*
	OA SIS Unique ID         OA_SIS_Unique_ID__c       Text(50)																Household
    OA Account Type          OA_Account_Type__c        Picklist																Household
	First Year at School     First_Year_at_School__c   Text(16)			OA custom field,									Household
	First Year Grade         First_Year_Grade__c       Number(2, 0)		OA custom field, 									Household
	Last Year at School      Last_Year_at_School__c    Text(16)			OA custom field, 									Household
	Last Year Grade          Last_Year_Grade__c        Number(2, 0)		OA custom field, 									Household
	Class of                 Class_of__c               Number(4, 0)		OA custom field,                                    Household
	Relationship1            Relationship__c           Picklist			Shares the OA "Contact Types" Picklist Value Sets	Contact1
	Relationship2            Relationship__c           Picklist			Shares the OA "Contact Types" Picklist Value Sets	Contact2
	Contact Type             Contact_Type__c           Picklist			Shares the OA "Contact Types" Picklist Value Sets	Household
	Contact Primary          Contact_Primary__c        Checkbox			OA custom field
	OA Household Phone       OA_Household_Phone__c     Phone            OA custom field,                                    Household
	*/
	

	public String CONTACT1_SALUTATION = "";
    public String CONTACT1_FIRSTNAME = "";
    public String CONTACT1_LASTNAME = "";
    public String CONTACT1_BIRTHDATE = "";
    public String CONTACT1_TITLE = "";
    public String CONTACT1_PERSONAL_EMAIL = "";
    public String CONTACT1_WORK_EMAIL = "";
    public String CONTACT1_ALTERNATE_EMAIL = "";
    public String CONTACT1_PREFERRED_EMAIL = "";
    public String CONTACT1_HOME_PHONE = "";
    public String CONTACT1_WORK_PHONE = "";
    public String CONTACT1_MOBILE_PHONE = "";
    public String CONTACT1_OTHER_PHONE = "";
    public String CONTACT1_PREFERRED_PHONE = "";
    public String CONTACT2_SALUTATION = "";
    public String CONTACT2_FIRSTNAME = "";
    public String CONTACT2_LASTNAME = "";
    public String CONTACT2_BIRTHDATE = "";
    public String CONTACT2_TITLE = "";
    public String CONTACT2_PERSONAL_EMAIL = "";
    public String CONTACT2_WORK_EMAIL = "";
    public String CONTACT2_ALTERNATE_EMAIL = "";
    public String CONTACT2_PREFERRED_EMAIL = "";
    public String CONTACT2_HOME_PHONE = "";
    public String CONTACT2_WORK_PHONE = "";
    public String CONTACT2_MOBILE_PHONE = "";
    public String CONTACT2_OTHER_PHONE = "";
    public String CONTACT2_PREFERRED_PHONE = "";
    public String ACCOUNT1_NAME = "";
    public String ACCOUNT2_NAME = "";
    public String HOME_STREET = "";
    public String HOME_CITY = "";
    public String HOME_STATE_PROVINCE = "";
    public String HOME_ZIP_POSTAL_CODE = "";
    public String HOME_COUNTRY = "";
    // Custom fields, all OA specific 
    public String OA_SIS_Unique_ID = "";
    public String OA_Account_Type = "";
    public String First_Year_at_School = ""; 			
    public String First_Year_Grade = ""; 					
    public String Last_Year_at_School = ""; 				
    public String Last_Year_Grade = ""; 					
    public String Class_of = ""; 							
    public String Relationship1 = "";
    public String Relationship2 = "";
    public String Contact_Primary = "false";   			
    public String SchoolYear = "";				
    public String OA_Household_Phone  = "";            // The NPSP version is broken, write instead into a OA Household Phone custom field
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(OA_SIS_Unique_ID.trim()).append(",");
		sb.append(CONTACT1_SALUTATION.trim()).append(",");
		sb.append(CONTACT1_FIRSTNAME.trim()).append(",");
		sb.append(CONTACT1_LASTNAME.trim()).append(",");
		sb.append(CONTACT1_BIRTHDATE.trim()).append(",");
		sb.append(CONTACT1_TITLE.trim()).append(",");
		sb.append(CONTACT1_PERSONAL_EMAIL.trim()).append(",");
		sb.append(CONTACT1_WORK_EMAIL.trim()).append(",");
		sb.append(CONTACT1_ALTERNATE_EMAIL.trim()).append(",");
		sb.append(CONTACT1_PREFERRED_EMAIL.trim()).append(",");
		sb.append(CONTACT1_HOME_PHONE.trim()).append(",");
		sb.append(CONTACT1_WORK_PHONE.trim()).append(",");
		sb.append(CONTACT1_MOBILE_PHONE.trim()).append(",");
		sb.append(CONTACT1_OTHER_PHONE.trim()).append(",");
		sb.append(CONTACT1_PREFERRED_PHONE.trim()).append(",");
		sb.append(CONTACT2_SALUTATION.trim()).append(",");
		sb.append(CONTACT2_FIRSTNAME.trim()).append(",");
		sb.append(CONTACT2_LASTNAME.trim()).append(",");
		sb.append(CONTACT2_BIRTHDATE.trim()).append(",");
		sb.append(CONTACT2_TITLE.trim()).append(",");
		sb.append(CONTACT2_PERSONAL_EMAIL.trim()).append(",");
		sb.append(CONTACT2_WORK_EMAIL.trim()).append(",");
		sb.append(CONTACT2_ALTERNATE_EMAIL.trim()).append(",");
		sb.append(CONTACT2_PREFERRED_EMAIL.trim()).append(",");
		sb.append(CONTACT2_HOME_PHONE.trim()).append(",");
		sb.append(CONTACT2_WORK_PHONE.trim()).append(",");
		sb.append(CONTACT2_MOBILE_PHONE.trim()).append(",");
		sb.append(CONTACT2_OTHER_PHONE.trim()).append(",");
		sb.append(CONTACT2_PREFERRED_PHONE.trim()).append(",");
		sb.append(ACCOUNT1_NAME.trim()).append(",");
		sb.append(ACCOUNT2_NAME.trim()).append(",");
		sb.append(HOME_STREET.trim()).append(",");
		sb.append(HOME_CITY.trim()).append(",");
		sb.append(HOME_STATE_PROVINCE.trim()).append(",");
		sb.append(HOME_ZIP_POSTAL_CODE.trim()).append(",");
		sb.append(HOME_COUNTRY.trim()).append(",");
		sb.append(OA_Account_Type.trim()).append(",");
		sb.append(First_Year_at_School.trim()).append(",");
		sb.append(First_Year_Grade.trim()).append(",");
		sb.append(Last_Year_at_School.trim()).append(",");
		sb.append(Last_Year_Grade.trim()).append(",");
		sb.append(Class_of.trim()).append(",");
		sb.append(Relationship1.trim()).append(",");
		sb.append(Relationship2.trim()).append(",");
		sb.append(Contact_Primary.trim()).append(",");
		sb.append(OA_Household_Phone.trim());           // The NPSP version is broken, write instead into a OA Household Phone custom field


		return sb.toString();
	}
}
