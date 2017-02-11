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
			+ "Contact1 Relationship1,Contact2 Relationship2,Contact Primary,OA Household Phone,Student Names";

	/*
	These fields are actually in "Account", but the importer needs them to be labeled (in note field) Household.First_Year_Grade__c for example
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
	Student Names            Student_Names__c          Text(50)         OA custom field,                                    Household
	*/
	

	private String CONTACT1_SALUTATION = "";
    private String CONTACT1_FIRSTNAME = "";
    private String CONTACT1_LASTNAME = "";
    private String CONTACT1_BIRTHDATE = "";
    private String CONTACT1_TITLE = "";
    private String CONTACT1_PERSONAL_EMAIL = "";
    private String CONTACT1_WORK_EMAIL = "";
    private String CONTACT1_ALTERNATE_EMAIL = "";
    private String CONTACT1_PREFERRED_EMAIL = "";
    private String CONTACT1_HOME_PHONE = "";
    private String CONTACT1_WORK_PHONE = "";
    private String CONTACT1_MOBILE_PHONE = "";
    private String CONTACT1_OTHER_PHONE = "";
    private String CONTACT1_PREFERRED_PHONE = "";
    private String CONTACT2_SALUTATION = "";
    private String CONTACT2_FIRSTNAME = "";
    private String CONTACT2_LASTNAME = "";
    private String CONTACT2_BIRTHDATE = "";
    private String CONTACT2_TITLE = "";
    private String CONTACT2_PERSONAL_EMAIL = "";
    private String CONTACT2_WORK_EMAIL = "";
    private String CONTACT2_ALTERNATE_EMAIL = "";
    private String CONTACT2_PREFERRED_EMAIL = "";
    private String CONTACT2_HOME_PHONE = "";
    private String CONTACT2_WORK_PHONE = "";
    private String CONTACT2_MOBILE_PHONE = "";
    private String CONTACT2_OTHER_PHONE = "";
    private String CONTACT2_PREFERRED_PHONE = "";
    private String ACCOUNT1_NAME = "";
    private String ACCOUNT2_NAME = "";
    private String HOME_STREET = "";
    private String HOME_CITY = "";
    private String HOME_STATE_PROVINCE = "";
    private String HOME_ZIP_POSTAL_CODE = "";
    private String HOME_COUNTRY = "";
    // Custom fields, all OA specific 
    private String OA_SIS_Unique_ID = "";
    private String OA_Account_Type = "";
    private String First_Year_at_School = ""; 			
    private String First_Year_Grade = ""; 					
    private String Last_Year_at_School = ""; 				
    private String Last_Year_Grade = ""; 					
    private String Class_of = ""; 							
    private String Relationship1 = "";
    private String Relationship2 = "";
    private String Contact_Primary = "false";   			
    private String SchoolYear = "";				
    private String OA_Household_Phone  = "";            // The NPSP version is broken, write instead into a OA Household Phone custom field
    private String Student_Names = "";
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getOA_SIS_Unique_ID().trim()).append(",");
		sb.append(getCONTACT1_SALUTATION().trim()).append(",");
		sb.append(getCONTACT1_FIRSTNAME().trim()).append(",");
		sb.append(getCONTACT1_LASTNAME().trim()).append(",");
		sb.append(getCONTACT1_BIRTHDATE().trim()).append(",");
		sb.append(getCONTACT1_TITLE().trim()).append(",");
		sb.append(getCONTACT1_PERSONAL_EMAIL().trim()).append(",");
		sb.append(getCONTACT1_WORK_EMAIL().trim()).append(",");
		sb.append(getCONTACT1_ALTERNATE_EMAIL().trim()).append(",");
		sb.append(getCONTACT1_PREFERRED_EMAIL().trim()).append(",");
		sb.append(getCONTACT1_HOME_PHONE().trim()).append(",");
		sb.append(getCONTACT1_WORK_PHONE().trim()).append(",");
		sb.append(getCONTACT1_MOBILE_PHONE().trim()).append(",");
		sb.append(getCONTACT1_OTHER_PHONE().trim()).append(",");
		sb.append(getCONTACT1_PREFERRED_PHONE().trim()).append(",");
		sb.append(getCONTACT2_SALUTATION().trim()).append(",");
		sb.append(getCONTACT2_FIRSTNAME().trim()).append(",");
		sb.append(getCONTACT2_LASTNAME().trim()).append(",");
		sb.append(getCONTACT2_BIRTHDATE().trim()).append(",");
		sb.append(getCONTACT2_TITLE().trim()).append(",");
		sb.append(getCONTACT2_PERSONAL_EMAIL().trim()).append(",");
		sb.append(getCONTACT2_WORK_EMAIL().trim()).append(",");
		sb.append(getCONTACT2_ALTERNATE_EMAIL().trim()).append(",");
		sb.append(getCONTACT2_PREFERRED_EMAIL().trim()).append(",");
		sb.append(getCONTACT2_HOME_PHONE().trim()).append(",");
		sb.append(getCONTACT2_WORK_PHONE().trim()).append(",");
		sb.append(getCONTACT2_MOBILE_PHONE().trim()).append(",");
		sb.append(getCONTACT2_OTHER_PHONE().trim()).append(",");
		sb.append(getCONTACT2_PREFERRED_PHONE().trim()).append(",");
		sb.append(getACCOUNT1_NAME().trim()).append(",");
		sb.append(getACCOUNT2_NAME().trim()).append(",");
		sb.append(getHOME_STREET().trim()).append(",");
		sb.append(getHOME_CITY().trim()).append(",");
		sb.append(getHOME_STATE_PROVINCE().trim()).append(",");
		sb.append(getHOME_ZIP_POSTAL_CODE().trim()).append(",");
		sb.append(getHOME_COUNTRY().trim()).append(",");
		sb.append(getOA_Account_Type().trim()).append(",");
		sb.append(getFirst_Year_at_School().trim()).append(",");
		sb.append(getFirst_Year_Grade().trim()).append(",");
		sb.append(getLast_Year_at_School().trim()).append(",");
		sb.append(getLast_Year_Grade().trim()).append(",");
		sb.append(getClass_of().trim()).append(",");
		sb.append(getRelationship1().trim()).append(",");
		sb.append(getRelationship2().trim()).append(",");
		sb.append(getContact_Primary().trim()).append(",");
		sb.append(getOA_Household_Phone().trim()).append(",");           // The NPSP version of "Household Phone" is broken, write instead into a OA Household Phone custom field
		sb.append(getStudent_Names().trim());


		return sb.toString();
	}
	
	
	// Auto-generated getters and setters

	public String getCONTACT1_SALUTATION() {
		return CONTACT1_SALUTATION;
	}

	public void setCONTACT1_SALUTATION(String cONTACT1_SALUTATION) {
		CONTACT1_SALUTATION = cONTACT1_SALUTATION.trim();
	}

	public String getCONTACT1_FIRSTNAME() {
		return CONTACT1_FIRSTNAME;
	}

	public void setCONTACT1_FIRSTNAME(String cONTACT1_FIRSTNAME) {
		CONTACT1_FIRSTNAME = cONTACT1_FIRSTNAME.trim();
	}

	public String getCONTACT1_LASTNAME() {
		return CONTACT1_LASTNAME;
	}

	public void setCONTACT1_LASTNAME(String cONTACT1_LASTNAME) {
		CONTACT1_LASTNAME = cONTACT1_LASTNAME.trim();
	}

	public String getCONTACT1_BIRTHDATE() {
		return CONTACT1_BIRTHDATE;
	}

	public void setCONTACT1_BIRTHDATE(String cONTACT1_BIRTHDATE) {
		CONTACT1_BIRTHDATE = cONTACT1_BIRTHDATE.trim();
	}

	public String getCONTACT1_TITLE() {
		return CONTACT1_TITLE;
	}

	public void setCONTACT1_TITLE(String cONTACT1_TITLE) {
		CONTACT1_TITLE = cONTACT1_TITLE.trim();
	}

	public String getCONTACT1_PERSONAL_EMAIL() {
		return CONTACT1_PERSONAL_EMAIL;
	}

	public void setCONTACT1_PERSONAL_EMAIL(String cONTACT1_PERSONAL_EMAIL) {
		CONTACT1_PERSONAL_EMAIL = cONTACT1_PERSONAL_EMAIL.trim();
	}

	public String getCONTACT1_WORK_EMAIL() {
		return CONTACT1_WORK_EMAIL;
	}

	public void setCONTACT1_WORK_EMAIL(String cONTACT1_WORK_EMAIL) {
		CONTACT1_WORK_EMAIL = cONTACT1_WORK_EMAIL.trim();
	}

	public String getCONTACT1_ALTERNATE_EMAIL() {
		return CONTACT1_ALTERNATE_EMAIL;
	}

	public void setCONTACT1_ALTERNATE_EMAIL(String cONTACT1_ALTERNATE_EMAIL) {
		CONTACT1_ALTERNATE_EMAIL = cONTACT1_ALTERNATE_EMAIL.trim();
	}

	public String getCONTACT1_PREFERRED_EMAIL() {
		return CONTACT1_PREFERRED_EMAIL;
	}

	public void setCONTACT1_PREFERRED_EMAIL(String cONTACT1_PREFERRED_EMAIL) {
		CONTACT1_PREFERRED_EMAIL = cONTACT1_PREFERRED_EMAIL.trim();
	}

	public String getCONTACT1_HOME_PHONE() {
		return CONTACT1_HOME_PHONE;
	}

	public void setCONTACT1_HOME_PHONE(String cONTACT1_HOME_PHONE) {
		CONTACT1_HOME_PHONE = cONTACT1_HOME_PHONE.trim();
	}

	public String getCONTACT1_WORK_PHONE() {
		return CONTACT1_WORK_PHONE;
	}

	public void setCONTACT1_WORK_PHONE(String cONTACT1_WORK_PHONE) {
		CONTACT1_WORK_PHONE = cONTACT1_WORK_PHONE.trim();
	}

	public String getCONTACT1_MOBILE_PHONE() {
		return CONTACT1_MOBILE_PHONE;
	}

	public void setCONTACT1_MOBILE_PHONE(String cONTACT1_MOBILE_PHONE) {
		CONTACT1_MOBILE_PHONE = cONTACT1_MOBILE_PHONE.trim();
	}

	public String getCONTACT1_OTHER_PHONE() {
		return CONTACT1_OTHER_PHONE;
	}

	public void setCONTACT1_OTHER_PHONE(String cONTACT1_OTHER_PHONE) {
		CONTACT1_OTHER_PHONE = cONTACT1_OTHER_PHONE.trim();
	}

	public String getCONTACT1_PREFERRED_PHONE() {
		return CONTACT1_PREFERRED_PHONE;
	}

	public void setCONTACT1_PREFERRED_PHONE(String cONTACT1_PREFERRED_PHONE) {
		CONTACT1_PREFERRED_PHONE = cONTACT1_PREFERRED_PHONE.trim();
	}

	public String getCONTACT2_SALUTATION() {
		return CONTACT2_SALUTATION;
	}

	public void setCONTACT2_SALUTATION(String cONTACT2_SALUTATION) {
		CONTACT2_SALUTATION = cONTACT2_SALUTATION.trim();
	}

	public String getCONTACT2_FIRSTNAME() {
		return CONTACT2_FIRSTNAME;
	}

	public void setCONTACT2_FIRSTNAME(String cONTACT2_FIRSTNAME) {
		CONTACT2_FIRSTNAME = cONTACT2_FIRSTNAME.trim();
	}

	public String getCONTACT2_LASTNAME() {
		return CONTACT2_LASTNAME;
	}

	public void setCONTACT2_LASTNAME(String cONTACT2_LASTNAME) {
		CONTACT2_LASTNAME = cONTACT2_LASTNAME.trim();
	}

	public String getCONTACT2_BIRTHDATE() {
		return CONTACT2_BIRTHDATE;
	}

	public void setCONTACT2_BIRTHDATE(String cONTACT2_BIRTHDATE) {
		CONTACT2_BIRTHDATE = cONTACT2_BIRTHDATE.trim();
	}

	public String getCONTACT2_TITLE() {
		return CONTACT2_TITLE;
	}

	public void setCONTACT2_TITLE(String cONTACT2_TITLE) {
		CONTACT2_TITLE = cONTACT2_TITLE.trim();
	}

	public String getCONTACT2_PERSONAL_EMAIL() {
		return CONTACT2_PERSONAL_EMAIL;
	}

	public void setCONTACT2_PERSONAL_EMAIL(String cONTACT2_PERSONAL_EMAIL) {
		CONTACT2_PERSONAL_EMAIL = cONTACT2_PERSONAL_EMAIL.trim();
	}

	public String getCONTACT2_WORK_EMAIL() {
		return CONTACT2_WORK_EMAIL;
	}

	public void setCONTACT2_WORK_EMAIL(String cONTACT2_WORK_EMAIL) {
		CONTACT2_WORK_EMAIL = cONTACT2_WORK_EMAIL.trim();
	}

	public String getCONTACT2_ALTERNATE_EMAIL() {
		return CONTACT2_ALTERNATE_EMAIL;
	}

	public void setCONTACT2_ALTERNATE_EMAIL(String cONTACT2_ALTERNATE_EMAIL) {
		CONTACT2_ALTERNATE_EMAIL = cONTACT2_ALTERNATE_EMAIL.trim();
	}

	public String getCONTACT2_PREFERRED_EMAIL() {
		return CONTACT2_PREFERRED_EMAIL;
	}

	public void setCONTACT2_PREFERRED_EMAIL(String cONTACT2_PREFERRED_EMAIL) {
		CONTACT2_PREFERRED_EMAIL = cONTACT2_PREFERRED_EMAIL.trim();
	}

	public String getCONTACT2_HOME_PHONE() {
		return CONTACT2_HOME_PHONE;
	}

	public void setCONTACT2_HOME_PHONE(String cONTACT2_HOME_PHONE) {
		CONTACT2_HOME_PHONE = cONTACT2_HOME_PHONE.trim();
	}

	public String getCONTACT2_WORK_PHONE() {
		return CONTACT2_WORK_PHONE;
	}

	public void setCONTACT2_WORK_PHONE(String cONTACT2_WORK_PHONE) {
		CONTACT2_WORK_PHONE = cONTACT2_WORK_PHONE.trim();
	}

	public String getCONTACT2_MOBILE_PHONE() {
		return CONTACT2_MOBILE_PHONE;
	}

	public void setCONTACT2_MOBILE_PHONE(String cONTACT2_MOBILE_PHONE) {
		CONTACT2_MOBILE_PHONE = cONTACT2_MOBILE_PHONE.trim();
	}

	public String getCONTACT2_OTHER_PHONE() {
		return CONTACT2_OTHER_PHONE;
	}

	public void setCONTACT2_OTHER_PHONE(String cONTACT2_OTHER_PHONE) {
		CONTACT2_OTHER_PHONE = cONTACT2_OTHER_PHONE.trim();
	}

	public String getCONTACT2_PREFERRED_PHONE() {
		return CONTACT2_PREFERRED_PHONE;
	}

	public void setCONTACT2_PREFERRED_PHONE(String cONTACT2_PREFERRED_PHONE) {
		CONTACT2_PREFERRED_PHONE = cONTACT2_PREFERRED_PHONE.trim();
	}

	public String getACCOUNT1_NAME() {
		return ACCOUNT1_NAME;
	}

	public String setACCOUNT1_NAME(String aCCOUNT1_NAME) {
		ACCOUNT1_NAME = aCCOUNT1_NAME.trim();
		return aCCOUNT1_NAME;
	}

	public String getACCOUNT2_NAME() {
		return ACCOUNT2_NAME;
	}

	public void setACCOUNT2_NAME(String aCCOUNT2_NAME) {
		ACCOUNT2_NAME = aCCOUNT2_NAME.trim();
	}

	public String getHOME_STREET() {
		return HOME_STREET;
	}

	public void setHOME_STREET(String hOME_STREET) {
		HOME_STREET = hOME_STREET.trim();
	}

	public String getHOME_CITY() {
		return HOME_CITY;
	}

	public void setHOME_CITY(String hOME_CITY) {
		HOME_CITY = hOME_CITY.trim();
	}

	public String getHOME_STATE_PROVINCE() {
		return HOME_STATE_PROVINCE;
	}

	public void setHOME_STATE_PROVINCE(String hOME_STATE_PROVINCE) {
		HOME_STATE_PROVINCE = hOME_STATE_PROVINCE.trim();
	}

	public String getHOME_ZIP_POSTAL_CODE() {
		return HOME_ZIP_POSTAL_CODE;
	}

	public void setHOME_ZIP_POSTAL_CODE(String hOME_ZIP_POSTAL_CODE) {
		HOME_ZIP_POSTAL_CODE = hOME_ZIP_POSTAL_CODE.trim();
	}

	public String getHOME_COUNTRY() {
		return HOME_COUNTRY;
	}

	public void setHOME_COUNTRY(String hOME_COUNTRY) {
		HOME_COUNTRY = hOME_COUNTRY.trim();
	}

	public String getOA_SIS_Unique_ID() {
		return OA_SIS_Unique_ID;
	}

	public String setOA_SIS_Unique_ID(String oA_SIS_Unique_ID) {
		OA_SIS_Unique_ID = oA_SIS_Unique_ID.trim();
		return oA_SIS_Unique_ID;
	}

	public String getOA_Account_Type() {
		return OA_Account_Type;
	}

	public void setOA_Account_Type(String oA_Account_Type) {
		OA_Account_Type = oA_Account_Type.trim();
	}

	public String getFirst_Year_at_School() {
		return First_Year_at_School;
	}

	public void setFirst_Year_at_School(String first_Year_at_School) {
		First_Year_at_School = first_Year_at_School.trim();
	}

	public String getFirst_Year_Grade() {
		return First_Year_Grade;
	}

	public void setFirst_Year_Grade(String first_Year_Grade) {
		First_Year_Grade = first_Year_Grade.trim();
	}

	public String getLast_Year_at_School() {
		return Last_Year_at_School;
	}

	public void setLast_Year_at_School(String last_Year_at_School) {
		Last_Year_at_School = last_Year_at_School.trim();
	}

	public String getLast_Year_Grade() {
		return Last_Year_Grade;
	}

	public void setLast_Year_Grade(String last_Year_Grade) {
		Last_Year_Grade = last_Year_Grade.trim();
	}

	public String getClass_of() {
		return Class_of;
	}

	public void setClass_of(String class_of) {
		Class_of = class_of.trim();
	}

	public String getRelationship1() {
		return Relationship1;
	}

	public void setRelationship1(String relationship1) {
		Relationship1 = relationship1.trim();
	}

	public String getRelationship2() {
		return Relationship2;
	}

	public void setRelationship2(String relationship2) {
		Relationship2 = relationship2.trim();
	}

	public String getContact_Primary() {
		return Contact_Primary;
	}

	public void setContact_Primary(String contact_Primary) {
		Contact_Primary = contact_Primary.trim();
	}

	public String getSchoolYear() {
		return SchoolYear;
	}

	public String setSchoolYear(String schoolYear) {
		SchoolYear = schoolYear.trim();
		return schoolYear;
	}

	public String getOA_Household_Phone() {
		return OA_Household_Phone;
	}

	public void setOA_Household_Phone(String oA_Household_Phone) {
		OA_Household_Phone = oA_Household_Phone.trim();
	}

	public String getStudent_Names() {
		return Student_Names;
	}

	public void setStudent_Names(String student_Names) {
		Student_Names = student_Names.trim();
	}
}
