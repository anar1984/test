package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrInspectionList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_USER_ID = "fkUserId";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String FK_SUBMODULE_ATTRIBUTE_ID = "fkSubmoduleAttributeId";
    public static String SA_SUBMODULE_ATTRIBUTE_ID = "saSubmoduleAttributeId";
    public static String INSPECTION_VALUE = "inspectionValue";
    public static String INSPECTION_DATE = "inspectionDate";
    public static String INSPECTION_TIME = "inspectionTime";
    public static String INSPECTION_CODE = "inspectionCode";
    public static String FINAL_VALUE = "finalValue";
    public static String HA_INSPECTION_VALUE = "haInspectionValue";
    public static String FK_VALUE_TYPE_ID = "fkValueTypeId";
    public static String FK_MODULE_ID = "fkModuleId";
    public static String FK_SUBMODULE_ID = "fkSubmoduleId";
    public static String PATIENT_NAME = "patientName";
    public static String PATIENT_SURNAME = "patientSurname";
    public static String PATIENT_MIDDLE_NAME = "patientMiddleName";
    public static String ATTRIBUTE_CODE = "attributeCode";
    public static String DOCTOR_FULLNAME = "doctorFullname";
    public static String FK_ATTRIBUTE_ID = "fkAttributeId";
    public static String PATIENT_BIRTH_DATE = "patientBirthDate";
    public static String PATIENT_BIRTH_PLACE = "patientBirthPlace";
    public static String SEX = "sex";
    public static String SUBMODULE_VALU = "submoduleValue";
    
    private String submoduleValue = "";

    private String patientBirthDate = "";
    private String patientBirthPlace = "";
    private String sex = "";
    private String fkAttributeId = "";
    private String doctorFullname = "";
    private String fkUserId = "";
    private String fkPatientId = "";
    private String fkSubmoduleAttributeId = "";
    private String saSubmoduleAttributeId = "";
    private String inspectionValue = "";
    private String inspectionDate = "";
    private String inspectionTime = "";
    private String inspectionCode = "";
    private String finalValue = "";
    private String haInspectionValue = "";
    private String fkValueTypeId = "";
    private String fkModuleId = "";
    private String fkSubmoduleId = "";
    private String patientName = "";
    private String patientSurname = "";
    private String patientMiddleName = "";
    private String attributeCode = "";

    public String getSubmoduleValue() {
        return submoduleValue;
    }

    public void setSubmoduleValue(String submoduleValue) {
        this.submoduleValue = submoduleValue;
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

    public String getFkAttributeId() {
        return fkAttributeId;
    }

    public void setFkAttributeId(String fkAttributeId) {
        this.fkAttributeId = fkAttributeId;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getFkPatientId() {
        return fkPatientId;
    }

    public void setFkPatientId(String fkPatientId) {
        this.fkPatientId = fkPatientId;
    }

    public String getFkSubmoduleAttributeId() {
        return fkSubmoduleAttributeId;
    }

    public void setFkSubmoduleAttributeId(String fkSubmoduleAttributeId) {
        this.fkSubmoduleAttributeId = fkSubmoduleAttributeId;
    }

    public String getSaSubmoduleAttributeId() {
        return saSubmoduleAttributeId;
    }

    public void setSaSubmoduleAttributeId(String saSubmoduleAttributeId) {
        this.saSubmoduleAttributeId = saSubmoduleAttributeId;
    }

    public String getInspectionValue() {
        return inspectionValue;
    }

    public void setInspectionValue(String inspectionValue) {
        this.inspectionValue = inspectionValue;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getHaInspectionValue() {
        return haInspectionValue;
    }

    public void setHaInspectionValue(String haInspectionValue) {
        this.haInspectionValue = haInspectionValue;
    }

    public String getFkValueTypeId() {
        return fkValueTypeId;
    }

    public void setFkValueTypeId(String fkValueTypeId) {
        this.fkValueTypeId = fkValueTypeId;
    }

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
    }

    public String getFkSubmoduleId() {
        return fkSubmoduleId;
    }

    public void setFkSubmoduleId(String fkSubmoduleId) {
        this.fkSubmoduleId = fkSubmoduleId;
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

    public String getDoctorFullname() {
        return doctorFullname;
    }

    public void setDoctorFullname(String doctorFullname) {
        this.doctorFullname = doctorFullname;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

}
