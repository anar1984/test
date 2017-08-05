package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrPerson extends CoreEntity {

   
    public EntityCrPerson() {
        super();
    }

    ////////////////////////// manual
    public static  String ID = "id";
    public static  String STATUS = "status";
    public static  String INSERT_DATE = "insertDate";
    public static  String MODIFICATION_DATE = "modificationDate";
    public static  String NAME = "name";
    public static  String SURNAME = "surname";
    public static  String MIDDLENAME = "middlename";
    public static  String BDATE = "bdate";
    public static  String BPLACE = "bplace";
    public static  String ID_CARD = "idCard";
    public static  String EMAIL_1 = "email1";
    public static  String EMAIL_2 = "email2";
    public static  String MOBILE_1 = "mobile1";
    public static  String MOBILE_2 = "mobile2";
    public static  String FAX_1 = "fax1";
    public static  String FAX_2 = "fax2";
    public static  String TELEPHONE_1 = "telephone1";
    public static  String TELEPHONE_2 = "telephone2";

    private String name = "";
    private String surname = "";
    private String middlename = "";
    private String bdate = "";
    private String bplace = "";
    private String idCard = "";
    private String email1 = "";
    private String email2 = "";
    private String mobile1 = "";
    private String mobile2 = "";
    private String fax1 = "";
    private String fax2 = "";
    private String telephone1 = "";
    private String telephone2 = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getBplace() {
        return bplace;
    }

    public void setBplace(String bplace) {
        this.bplace = bplace;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getFax2() {
        return fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
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

}
