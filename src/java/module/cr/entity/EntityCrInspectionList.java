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
    public static String USER_FULLNAME = "userFullname";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String PATIENT_FULLNAME = "patientFullname";
    public static String FK_MODULE_ID = "fkModuleId";
    public static String MODULE_NAME = "moduleName";
    public static String FK_SUBMODULE_ATTRIBUTE_ID = "fkSubmoduleAttributeId";
    public static String ATTRIBUTE_NAME = "attributeName";
    public static String FK_SUBMODULE_ID = "fkSubmoduleId";
    public static String SUBMODULE_NAME = "submoduleName";
    public static String FK_PRIVATE_SUBMODULE_ATTRIBUTE_ID = "fkPrivateSubmoduleAttributeId";
    public static String PRIVATE_SUBMODULE_ATTRIBUTE_NAME = "privateSubmoduleAttributeName";
    public static String INSPECTION_VALUE = "inspectionValue";
    public static String INSPECTION_DATE = "inspectionDate";
    public static String INSPECTION_TIME = "inspectionTime";
    public static String LANG = "lang";
    public static String INSPECTION_CODE = "inspectionCode";
    public static String SA_SUBMODULE_ATTRIBUTE_ID = "saSubmoduleAttributeId";
    public static String HA_INSPECTION_VALUE = "haInspectionValue";
    public static String FINAL_VALUE = "finalValue";
    public static String CORE_DATE = "coreDate";
    public static String CORE_TIME = "coreTime";
    public static String FK_VALUE_TYPE_ID = "fkValueTypeId";
    public static String FK_COMPANY_ID = "fkCompanyId";

    private String fkCompanyId = "";
    private String fkValueTypeId = "";
    private String coreDate = "";
    private String coreTime = "";

    private String finalValue = "";
    private String haInspectionValue = "";
    private String saSubmoduleAttributeId = "";
    private String inspectionCode = "";
    private String fkUserId = "";
    private String userFullname = "";
    private String fkPatientId = "";
    private String patientFullname = "";
    private String fkModuleId = "";
    private String moduleName = "";
    private String fkSubmoduleAttributeId = "";
    private String attributeName = "";
    private String fkSubmoduleId = "";
    private String submoduleName = "";
    private String fkPrivateSubmoduleAttributeId = "";
    private String privateSubmoduleAttributeName = "";
    private String inspectionValue = "";
    private String inspectionDate = "";
    private String inspectionTime = "";
    private String lang = "";

    public String getFkCompanyId() {
        return fkCompanyId;
    }

    public void setFkCompanyId(String fkCompanyId) {
        this.fkCompanyId = fkCompanyId;
    }

    public String getFkValueTypeId() {
        return fkValueTypeId;
    }

    public void setFkValueTypeId(String fkValueTypeId) {
        this.fkValueTypeId = fkValueTypeId;
    }

    public String getCoreDate() {
        return coreDate;
    }

    public void setCoreDate(String coreDate) {
        this.coreDate = coreDate;
    }

    public String getCoreTime() {
        return coreTime;
    }

    public void setCoreTime(String coreTime) {
        this.coreTime = coreTime;
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

    public String getSaSubmoduleAttributeId() {
        return saSubmoduleAttributeId;
    }

    public void setSaSubmoduleAttributeId(String saSubmoduleAttributeId) {
        this.saSubmoduleAttributeId = saSubmoduleAttributeId;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getPatientFullname() {
        return patientFullname;
    }

    public void setPatientFullname(String patientFullname) {
        this.patientFullname = patientFullname;
    }

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getFkSubmoduleId() {
        return fkSubmoduleId;
    }

    public void setFkSubmoduleId(String fkSubmoduleId) {
        this.fkSubmoduleId = fkSubmoduleId;
    }

    public String getSubmoduleName() {
        return submoduleName;
    }

    public void setSubmoduleName(String submoduleName) {
        this.submoduleName = submoduleName;
    }

    public String getPrivateSubmoduleAttributeName() {
        return privateSubmoduleAttributeName;
    }

    public void setPrivateSubmoduleAttributeName(String privateSubmoduleAttributeName) {
        this.privateSubmoduleAttributeName = privateSubmoduleAttributeName;
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

    public String getFkPrivateSubmoduleAttributeId() {
        return fkPrivateSubmoduleAttributeId;
    }

    public void setFkPrivateSubmoduleAttributeId(String fkPrivateSubmoduleAttributeId) {
        this.fkPrivateSubmoduleAttributeId = fkPrivateSubmoduleAttributeId;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
