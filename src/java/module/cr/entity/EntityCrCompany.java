package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrCompany extends CoreEntity {

    //////////////////////=mandatory
    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String COMPANY_NAME = "companyName";
    public static String COMPANY_DOMAIN = "companyDomain";
    public static String COMPANY_COUNTRY = "companyCountry";
    public static String COMPANY_TIME_ZONE = "companyTimeZone";
    public static String COMPANY_ADDRESS = "companyAddress";
    public static String COMPANY_CURRENCY = "companyCurrency";
    public static String ACTIVATION_ID = "activationId";
    public static String COMPANY_DB = "companyDb";
    public static String COMPANY_TYPE = "companyType";
    public static String FK_USER_ID = "fkUserId";
    public static String COMPANY_STATUS = "companyStatus";
    public static String USER_BIRTH_PLACE = "userBirthPlace";
    public static String EXPIRE_DATE = "expireDate";
    public static String COMPANY_LANG = "companyLang";
    public static String ACTIVE_USER_COUNT = "activeUserCount";
    public static String PERSON_USERNAME = "personUsername";

    private String personUsername = "";
    private String activeUserCount = "";
    private String companyLang = "";
    private String companyName = "";
    private String companyDomain = "";
    private String companyCountry = "";
    private String companyTimeZone = "";
    private String companyAddress = "";
    private String companyCurrency = "";
    private String activationId = "";
    private String companyDb = "";
    private String companyType = "";
    private String fkUserId = "";
    private String companyStatus = "";
    private String userBirthPlace = "";
    private String expireDate = "";

    public enum CompanyStatus {
        VERIFY("V"),
        PENDING("P"),
        ACTIVE("A"),
        CREATE("C"),
        DELETED("D");
        private final String status;
 
        CompanyStatus(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }

    public enum CompanyType {
        PERSONAL("P"),
        COMPANY("C");
        private final String type;

        CompanyType(String type) {
            this.type = type;
        }

        public String toString() {
            return this.type;
        }
    }

    public String getPersonUsername() {
        return personUsername;
    }

    public void setPersonUsername(String personUsername) {
        this.personUsername = personUsername;
    }

    public String getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(String activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public String getCompanyLang() {
        return companyLang;
    }

    public void setCompanyLang(String companyLang) {
        this.companyLang = companyLang;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(String companyDomainCode) {
        this.companyDomain = companyDomainCode;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyTimeZone() {
        return companyTimeZone;
    }

    public void setCompanyTimeZone(String companyTimeZone) {
        this.companyTimeZone = companyTimeZone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyCurrency() {
        return companyCurrency;
    }

    public void setCompanyCurrency(String companyCurrency) {
        this.companyCurrency = companyCurrency;
    }

    public String getActivationId() {
        return activationId;
    }

    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }

    public String getCompanyDb() {
        return companyDb;
    }

    public void setCompanyDb(String companyDb) {
        this.companyDb = companyDb;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getUserBirthPlace() {
        return userBirthPlace;
    }

    public void setUserBirthPlace(String userBirthPlace) {
        this.userBirthPlace = userBirthPlace;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
