package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrReportLineList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_USER_ID = "fkUserId";
    public static String REPORT_HTML = "reportHtml";
    public static String ACCESS_TYPE = "accessType";
    public static String ACCESS_TYPE_NAME = "accessTypeName";
    public static String REPORT_TYPE = "reportType";
    public static String REPORT_TYPE_NAME = "reportTypeName";
    public static String REPORT_DESC = "reportDesc";
    public static String LANG = "lang";
    public static String REPORT_NAME = "reportName";
    private String reportName = "";
    public static String FK_MODULE_ID = "fkModuleId";
    private String fkModuleId = "";
    public static String MODULE_NAME = "moduleName";
    private String moduleName = "";

    private String fkUserId = "";
    private String reportHtml = "";
    private String accessType = "";
    private String accessTypeName = "";
    private String reportType = "";
    private String reportTypeName = "";
    private String reportDesc = "";
    private String lang = "";

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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getReportHtml() {
        return reportHtml;
    }

    public void setReportHtml(String reportHtml) {
        this.reportHtml = reportHtml;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getAccessTypeName() {
        return accessTypeName;
    }

    public void setAccessTypeName(String accessTypeName) {
        this.accessTypeName = accessTypeName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getReportDesc() {
        return reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
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
