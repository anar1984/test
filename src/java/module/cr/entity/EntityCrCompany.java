package module.cr.entity;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
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
    
    public static final String COMPANY_NAME = "companyName";
    public static final String COMPANY_DOMAIN = "companyDomain";
    public static final String COMPANY_COUNTRY = "companyCountry";
    public static final String COMPANY_TIME_ZONE = "companyTimeZone";
    public static final String COMPANY_ADDRESS = "companyAddress";
    public static final String COMPANY_CURRENCY = "companyCurrency";
    public static final String ACTIVATION_ID = "activationId";
    public static final String COMPANY_DB = "companyDb";
    public static final String COMPANY_TYPE = "companyType";
    

    private String companyName = "";
    private String companyDomain = "";
    private String companyCountry = "";
    private String companyTimeZone = "";
    private String companyAddress = "";
    private String companyCurrency = "";
    private String activationId = "";
    private String companyDb = "";
    private String companyType = "";
    
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
    

    

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ID, this.getId());
            obj.put(COMPANY_NAME, this.getCompanyName());
            obj.put(COMPANY_DOMAIN, this.getCompanyDomain());
            obj.put(COMPANY_COUNTRY, this.getCompanyCountry());
            obj.put(COMPANY_TIME_ZONE, this.getCompanyTimeZone());
            obj.put(COMPANY_ADDRESS, this.getCompanyAddress());
            obj.put(COMPANY_CURRENCY, this.getCompanyCurrency());
            obj.put(COMPANY_DB, this.getCompanyDb());
            obj.put(COMPANY_TYPE, this.getCompanyType());
        } catch (JSONException ex) {
//            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            
            this.setId(obj.getString("id"));
            this.setCompanyName(obj.getString(COMPANY_NAME));
            this.setCompanyDomain(obj.getString(COMPANY_DOMAIN));
            this.setCompanyCountry(obj.getString(COMPANY_COUNTRY));
            this.setCompanyTimeZone(obj.getString(COMPANY_TIME_ZONE));
            this.setCompanyAddress(obj.getString(COMPANY_ADDRESS));
            this.setCompanyCurrency(obj.getString(COMPANY_CURRENCY));
            this.setCompanyDb(obj.getString(COMPANY_DB));
            this.setCompanyType(obj.getString(COMPANY_TYPE));
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrCompany.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyDomain
     */
    public String getCompanyDomain() {
        return companyDomain;
    }

    /**
     * @param companyDomain the companyDomain to set
     */
    public void setCompanyDomain(String companyDomain) {
        this.companyDomain = companyDomain;
    }

    /**
     * @return the companyCountry
     */
    public String getCompanyCountry() {
        return companyCountry;
    }

    /**
     * @param companyCountry the companyCountry to set
     */
    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    /**
     * @return the companyTimeZone
     */
    public String getCompanyTimeZone() {
        return companyTimeZone;
    }

    /**
     * @param companyTimeZone the companyTimeZone to set
     */
    public void setCompanyTimeZone(String companyTimeZone) {
        this.companyTimeZone = companyTimeZone;
    }

    /**
     * @return the companyAddress
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * @param companyAddress the companyAddress to set
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * @return the companyCurrency
     */
    public String getCompanyCurrency() {
        return companyCurrency;
    }

    /**
     * @param companyCurrency the companyCurrency to set
     */
    public void setCompanyCurrency(String companyCurrency) {
        this.companyCurrency = companyCurrency;
    }
    
    /**
     * @return the activationId
     */
    public String getActivationId() {
        return activationId;
    }

    /**
     * @param activationId the activationId to set
     */
    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }
    
    /**
     * @return the companyDb
     */
    public String getCompanyDb() {
        return companyDb;
    }

    /**
     * @param companyDb the companyDb to set
     */
    public void setCompanyDb(String companyDb) {
        this.companyDb = companyDb;
    }

    /**
     * @return the companyType
     */
    public String getCompanyType() {
        return companyType;
    }

    /**
     * @param companyType the companyType to set
     */
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
