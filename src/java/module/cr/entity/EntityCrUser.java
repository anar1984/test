package module.cr.entity;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMError;
import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrUser extends CoreEntity {

    //////////////////////=mandatory
    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_EMPLOYEE_ID = "fkEmployeeId";
    public static String LI_USER_PERMISSION_CODE = "liUserPermissionCode";
    public static String TG_USER_ID = "tgUserId";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String USER_SHORT_ID = "userShortId";
    public static String USER_IMAGE = "userImage";
    public static String USER_PERSON_NAME = "userPersonName";
    public static String USER_PERSON_SURNAME = "userPersonSurname";
    public static String USER_PERSON_MIDDLENAME = "userPersonMiddlename";
    public static String USER_BIRTH_DATE = "userBirthDate";
    public static String USER_BIRTH_PLACE = "userBirthPlace";
    public static String FK_COMPANY_ID = "fkCompanyId";
    public static String SEX = "sex";
    public static String OCCUPATION = "occupation";
    public static String MOBILE_1 = "mobile1";
    public static String MOBILE_2 = "mobile2";
    public static String TELEPHONE_1 = "telephone1";
    public static String TELEPHONE_2 = "telephone2";
    public static String EMAIL_1 = "email1";
    public static String EMAIL_2 = "email2";
    public static String USER_STATUS = "userStatus";
    private String userStatus = "";

    private String fkEmployeeId = "";
    private String liUserPermissionCode = "";
    private String tgUserId = "";
    private String username = "";
    private String password = "";
    private String userShortId = "";
    private String userImage = "";
    private String userPersonName = "";
    private String userPersonSurname = "";
    private String userPersonMiddlename = "";
    private String userBirthDate = "";
    private String userBirthPlace = "";
    private String fkCompanyId = "";
    private String sex = "";
    private String occupation = "";
    private String mobile1 = "";
    private String mobile2 = "";
    private String telephone1 = "";
    private String telephone2 = "";
    private String email1 = "";
    private String email2 = "";
    private String expireDate = "";
    private String lang = "";
    private String domain = "";
    private String companyId="";

    public String selectCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    

    public String getUserShortId() {
        return userShortId;
    }

    public void setUserShortId(String userShortId) {
        this.userShortId = userShortId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserPersonName() {
        return userPersonName;
    }

    public void setUserPersonName(String userPersonName) {
        this.userPersonName = userPersonName;
    }

    public String getUserPersonSurname() {
        return userPersonSurname;
    }

    public void setUserPersonSurname(String userPersonSurname) {
        this.userPersonSurname = userPersonSurname;
    }

    public String getUserPersonMiddlename() {
        return userPersonMiddlename;
    }

    public void setUserPersonMiddlename(String userPersonMiddlename) {
        this.userPersonMiddlename = userPersonMiddlename;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getUserBirthPlace() {
        return userBirthPlace;
    }

    public void setUserBirthPlace(String userBirthPlace) {
        this.userBirthPlace = userBirthPlace;
    }

    public String getFkCompanyId() {
        return fkCompanyId;
    }

    public void setFkCompanyId(String fkCompanyId) {
        this.fkCompanyId = fkCompanyId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getLiUserPermissionCode() {
        return liUserPermissionCode;
    }

    public void setLiUserPermissionCode(String liUserPermissionCode) {
        this.liUserPermissionCode = liUserPermissionCode;
    }

    public String getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(String tgUserId) {
        this.tgUserId = tgUserId;
    }

    public String getFkEmployeeId() {
        return fkEmployeeId;
    }

    public void setFkEmployeeId(String fkEmployeeID) {
        this.fkEmployeeId = fkEmployeeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String selectLang() {
        return this.lang;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String selectDomain() {
        return domain;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", this.getId());
            obj.put("username", this.getUsername());
//            obj.put("password", this.getPassword());
            obj.put("lang", this.selectLang());
            obj.put("domain", this.selectDomain());
            obj.put("companyId", this.selectCompanyId());
        } catch (JSONException ex) {
//            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setUsername(obj.getString("username"));
            this.setId(obj.getString("id"));
//            this.setPassword(obj.getString("password"));
            this.setLang(obj.getString("lang"));
            this.setDomain(obj.getString("domain"));
            this.setCompanyId(obj.getString("companyId"));

        } catch (JSONException ex) {
            Logger.getLogger(EntityCrUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
