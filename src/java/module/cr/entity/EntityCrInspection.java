package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrInspection extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_USER_ID = "fkUserId";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String FK_SUBMODULE_ATTRIBUTE_ID = "fkSubmoduleAttributeId";
    public static String FK_PRIVATE_SUBMODULE_ATTRIBUTE_ID = "fkPrivateSubmoduleAttributeId";
    public static String INSPECTION_VALUE = "inspectionValue";
    public static String INSPECTION_DATE = "inspectionDate";
    public static String INSPECTION_TIME = "inspectionTime";
    public static String LANG = "lang";
    public static String INSPECTION_CODE = "inspectionCode";
    public static String HA_INSPECTION_VALUE = "haInspectionValue";
    private String haInspectionValue = "";

    private String inspectionCode = "";
    private String fkUserId = "";
    private String fkPatientId = "";
    private String fkSubmoduleAttributeId = "";
    private String fkPrivateSubmoduleAttributeId = "";
    private String inspectionValue = "";
    private String inspectionDate = "";
    private String inspectionTime = "";
    private String lang = "";

    public String getHaInspectionValue() {
        return haInspectionValue;
    }

    public void setHaInspectionValue(String haInspectionValue) {
        this.haInspectionValue = haInspectionValue;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
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

}
