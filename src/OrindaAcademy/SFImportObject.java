package OrindaAcademy;

/**
 * An object to contain a the data for a NPSP_Data_ImportTemplate line, and a "toString()" to generate that output line
 * @author stevepodell
 *
 */
public class SFImportObject {
	public static String NPSP_Data_ImportTemplate = 
			"Contact1 Salutation,Contact1 First Name,Contact1 Last Name,Contact1 Birthdate,Contact1 Title,Contact1 Personal Email,Contact1 Work Email,Contact1 Alternate Email,Contact1 Preferred Email,Contact1 Home Phone,Contact1 Work Phone,Contact1 Mobile Phone,Contact1 Other Phone,Contact1 Preferred Phone,"
			+ "Contact2 Salutation,Contact2 First Name,Contact2 Last Name,Contact2 Birthdate,Contact2 Title,Contact2 Personal Email,Contact2 Work Email,Contact2 Alternate Email,Contact2 Preferred Email,Contact2 Home Phone,Contact2 Work Phone,Contact2 Mobile Phone,Contact2 Other Phone,Contact2 Preferred Phone,"
			+ "Household Phone,Account1 Name,Account1 Street,Account1 City,Account1 State/Province,Account1 Zip/Postal Code,Account1 Country,Account1 Phone,Account1 Website,"
			+ "Account2 Name,Account2 Street,Account2 City,Account2 State/Province,Account2 Zip/Postal Code,Account2 Country,Account2 Phone,Account2 Website,"
			+ "Home Street,Home City,Home State/Province,Home Zip/Postal Code,Home Country,"
			+ "Donation Donor,Donation Amount,Donation Date,Donation Name,Donation Record Type Name,Donation Stage,Donation Type,Donation Description,Donation Member Level,Donation Membership Start Date,Donation Membership End Date,Donation Membership Origin,"
			+ "Campaign Name,Campaign Member Status,Payment Check/Reference Number,Payment Method";

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
    public String HOUSEHOLD_PHONE = "";
    public String ACCOUNT1_NAME = "";
    public String ACCOUNT1_STREET = "";
    public String ACCOUNT1_CITY = "";
    public String ACCOUNT1_STATE_PROVINCE = "";
    public String ACCOUNT1_ZIP_POSTAL_CODE = "";
    public String ACCOUNT1_COUNTRY = "";
    public String ACCOUNT1_PHONE = "";
    public String ACCOUNT1_WEBSITE = "";
    public String ACCOUNT2_NAME = "";
    public String ACCOUNT2_STREET = "";
    public String ACCOUNT2_CITY = "";
    public String ACCOUNT2_STATE_PROVINCE = "";
    public String ACCOUNT2_ZIP_POSTAL_CODE = "";
    public String ACCOUNT2_COUNTRY = "";
    public String ACCOUNT2_PHONE = "";
    public String ACCOUNT2_WEBSITE = "";
    public String HOME_STREET = "";
    public String HOME_CITY = "";
    public String HOME_STATE_PROVINCE = "";
    public String HOME_ZIP_POSTAL_CODE = "";
    public String HOME_COUNTRY = "";
    public String DONATION_DONOR = "";
    public String DONATION_AMOUNT = "";
    public String DONATION_DATE = "";
    public String DONATION_NAME = "";
    public String DONATION_RECORD_TYPE_NAME = "";
    public String DONATION_STAGE = "";
    public String DONATION_TYPE = "";
    public String DONATION_DESCRIPTION = "";
    public String DONATION_MEMBER_LEVEL  = "";
    public String DONATION_MEMBERSHIP_START_DATE = "";
    public String DONATION_MEMBERSHIP_END_DATE = "";
    public String DONATION_MEMBERSHIP_ORIGIN = "";
    public String DONATION_CAMPAIGN_NAME = "";
    public String CAMPAIGN_MEMBER_STATUS = "";
    public String PAYMENT_CHECK_REFERENCE_NUMBER = "";
    public String PAYMENT_METHOD = "";
    public String FirstYearatSchool = ""; 				// Account1 Street
    public String FirstYearGrade = ""; 					// Account1 City
    public String LastYearatSchool = ""; 				// Account1 State/Province
    public String LastYearGrade = ""; 					// Account1 Country
    public String Classof = ""; 							// Account1 Zip/Postal Code
    public String Relationship1 = ""; 					// Account1 Country
    public String Relationship2 = ""; 					// Account1 Phone   
    public String CONTACT_TYPE = "Null"; 				// Account1 Website
    public String CONTACT_PRIMARY = "false";   			// Account2 Street
    public String SchoolYear = "";						//    Don't store, just for sorting
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
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
		sb.append(HOUSEHOLD_PHONE.trim()).append(",");
		sb.append(ACCOUNT1_NAME.trim()).append(",");
		sb.append(FirstYearatSchool.trim()).append(",");			// ACCOUNT1_STREET
		sb.append(FirstYearGrade.trim()).append(",");				// ACCOUNT1_CITY
		sb.append(LastYearatSchool.trim()).append(",");				// ACCOUNT1_STATE_PROVINCE
		sb.append(LastYearGrade.trim()).append(",");				// ACCOUNT1_ZIP_POSTAL_CODE
		sb.append(Classof.trim()).append(",");						// ACCOUNT1_COUNTRY
		sb.append(Relationship1.trim()).append(",");				// ACCOUNT1_PHONE
		sb.append(Relationship2.trim()).append(",");				// ACCOUNT1_WEBSITE
		sb.append(ACCOUNT2_NAME.trim()).append(",");
		sb.append(CONTACT_TYPE.trim()).append(",");					// ACCOUNT2_STREET
		sb.append(CONTACT_PRIMARY.trim()).append(",");				// ACCOUNT2_CITY
		sb.append(ACCOUNT2_STATE_PROVINCE.trim()).append(",");
		sb.append(ACCOUNT2_ZIP_POSTAL_CODE.trim()).append(",");
		sb.append(ACCOUNT2_COUNTRY.trim()).append(",");
		sb.append(ACCOUNT2_PHONE.trim()).append(",");
		sb.append(ACCOUNT2_WEBSITE.trim()).append(",");
		sb.append(HOME_STREET.trim()).append(",");
		sb.append(HOME_CITY.trim()).append(",");
		sb.append(HOME_STATE_PROVINCE.trim()).append(",");
		sb.append(HOME_ZIP_POSTAL_CODE.trim()).append(",");
		sb.append(HOME_COUNTRY.trim()).append(",");
		sb.append(DONATION_DONOR.trim()).append(",");
		sb.append(DONATION_AMOUNT.trim()).append(",");
		sb.append(DONATION_DATE.trim()).append(",");
		sb.append(DONATION_NAME.trim()).append(",");
		sb.append(DONATION_RECORD_TYPE_NAME.trim()).append(",");
		sb.append(DONATION_STAGE.trim()).append(",");
		sb.append(DONATION_TYPE.trim()).append(",");
		sb.append(DONATION_DESCRIPTION.trim()).append(",");
		sb.append(DONATION_MEMBER_LEVEL.trim()).append(",");			// text 
		sb.append(DONATION_MEMBERSHIP_START_DATE.trim()).append(",");
		sb.append(DONATION_MEMBERSHIP_END_DATE.trim()).append(",");
		sb.append(DONATION_MEMBERSHIP_ORIGIN.trim()).append(",");
		sb.append(DONATION_CAMPAIGN_NAME.trim()).append(",");
		sb.append(CAMPAIGN_MEMBER_STATUS.trim()).append(",");
		sb.append(PAYMENT_CHECK_REFERENCE_NUMBER.trim()).append(",");
		sb.append(PAYMENT_METHOD.trim());

		return sb.toString();
	}
}
