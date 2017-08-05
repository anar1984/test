package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrPatientList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String PATIENT_NAME = "patientName";
    public static String PATIENT_SURNAME = "patientSurname";
    public static String PATIENT_MIDDLE_NAME = "patientMiddleName";
    public static String PATIENT_BIRTH_DATE = "patientBirthDate";
    public static String PATIENT_BIRTH_PLACE = "patientBirthPlace";
    public static String SEX = "sex";
    public static String SEX_NAME = "sexName";
    public static String OCCUPATION = "occupation";
    public static String OCCUPATION_NAME = "occupationName";
    public static String MARITUAL_STATUS = "maritualStatus";
    public static String MARITUAL_STATUS_NAME = "maritualStatusName";
    public static String EDUCATION = "education";
    public static String EDUCATION_NAME = "educationName";
    public static String BLOOD_GROUP = "bloodGroup";
    public static String BLOOD_GROUP_NAME = "bloodGroupName";
    public static String RH_FACTOR = "rhFactor";
    public static String RH_FACTOR_NAME = "rhFactorName";
    public static String MOBILE_1 = "mobile1";
    public static String MOBILE_2 = "mobile2";
    public static String TELEPHONE_1 = "telephone1";
    public static String TELEPHONE_2 = "telephone2";
    public static String EMAIL_1 = "email1";
    public static String EMAIL_2 = "email2";
    public static String COUNTRY = "country";
    public static String CITY = "city";
    public static String POST_INDEX = "postIndex";
    public static String IS_ACTIVE = "isActive";
    public static String IS_ACTIVE_NAME = "isActiveName";
    public static String LANG = "lang";
    public static String DESCRIPTION = "description";
    public static String FK_OWNER_USER_ID = "fkOwnerUserId";
    public static String PATIENT_ID = "patientId";
    private String patientId = "";
    public static String PATIENT_IMAGE = "patientImage";
    private String patientImage = "";

    private String fkOwnerUserId = "";
    private String patientName = "";
    private String patientSurname = "";
    private String patientMiddleName = "";
    private String patientBirthDate = "";
    private String patientBirthPlace = "";
    private String sex = "";
    private String sexName = "";
    private String occupation = "";
    private String occupationName = "";
    private String maritualStatus = "";
    private String maritualStatusName = "";
    private String education = "";
    private String educationName = "";
    private String bloodGroup = "";
    private String bloodGroupName = "";
    private String rhFactor = "";
    private String rhFactorName = "";
    private String mobile1 = "";
    private String mobile2 = "";
    private String telephone1 = "";
    private String telephone2 = "";
    private String email1 = "";
    private String email2 = "";
    private String country = "";
    private String city = "";
    private String postIndex = "";
    private String isActive = "";
    private String isActiveName = "";
    private String lang = "";
    private String description = "";

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientImage() {
        return patientImage;
    }

    public void setPatientImage(String patientImage) {
        this.patientImage = patientImage;
    }

    public String getFkOwnerUserId() {
        return fkOwnerUserId;
    }

    public void setFkOwnerUserId(String fkOwnerUserId) {
        this.fkOwnerUserId = fkOwnerUserId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }

    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public String getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(String patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getPatientBirthPlace() {
        return patientBirthPlace;
    }

    public void setPatientBirthPlace(String patientBirthPlace) {
        this.patientBirthPlace = patientBirthPlace;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getMaritualStatus() {
        return maritualStatus;
    }

    public void setMaritualStatus(String maritualStatus) {
        this.maritualStatus = maritualStatus;
    }

    public String getMaritualStatusName() {
        return maritualStatusName;
    }

    public void setMaritualStatusName(String maritualStatusName) {
        this.maritualStatusName = maritualStatusName;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroupName() {
        return bloodGroupName;
    }

    public void setBloodGroupName(String bloodGroupName) {
        this.bloodGroupName = bloodGroupName;
    }

    public String getRhFactor() {
        return rhFactor;
    }

    public void setRhFactor(String rhFactor) {
        this.rhFactor = rhFactor;
    }

    public String getRhFactorName() {
        return rhFactorName;
    }

    public void setRhFactorName(String rhFactorName) {
        this.rhFactorName = rhFactorName;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActiveName() {
        return isActiveName;
    }

    public void setIsActiveName(String isActiveName) {
        this.isActiveName = isActiveName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
