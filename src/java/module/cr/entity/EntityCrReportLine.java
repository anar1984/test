package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrReportLine extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_USER_ID = "fkUserId";
    public static String REPORT_HTML = "reportHtml";
    public static String ACCESS_TYPE = "accessType";
    public static String REPORT_TYPE = "reportType";
    public static String REPORT_DESC = "reportDesc";
    public static String LANG = "lang";
    public static String REPORT_NAME = "reportName";
    public static String FK_MODULE_ID = "fkModuleId";
    private String fkModuleId = "";
    private String reportName = "";
    private String fkUserId = "";
    private String reportHtml = "";
    private String accessType = "";
    private String reportType = "";
    private String reportDesc = "";
    private String lang = "";

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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

}
