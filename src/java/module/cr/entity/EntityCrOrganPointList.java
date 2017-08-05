package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrOrganPointList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String ORGAN_POINT_NAME = "organPointName";
    public static String ORGAN_POINT_DESCRIPTION = "organPointDescription";
    public static String ORGAN_POINT_STATUS = "organPointStatus";
    public static String FK_PARENT_ORGAN_POINT_ID = "fkParentOrganPointId";
    public static String LANG = "lang";
    public static String PARENT_ORGAN_POINT_NAME = "parentOrganPointName";
    public static String ORGAN_POINT_STATUS_NAME = "organPointStatusName";

    private String organPointStatusName = "";
    private String parentOrganPointName = "";
    private String organPointName = "";
    private String organPointDescription = "";
    private String organPointStatus = "";
    private String fkParentOrganPointId = "";
    private String lang = "";

    public String getOrganPointStatusName() {
        return organPointStatusName;
    }

    public void setOrganPointStatusName(String organPointStatusName) {
        this.organPointStatusName = organPointStatusName;
    }

    public String getParentOrganPointName() {
        return parentOrganPointName;
    }

    public void setParentOrganPointName(String parentOrganPointName) {
        this.parentOrganPointName = parentOrganPointName;
    }

    public String getOrganPointName() {
        return organPointName;
    }

    public void setOrganPointName(String organPointName) {
        this.organPointName = organPointName;
    }

    public String getOrganPointDescription() {
        return organPointDescription;
    }

    public void setOrganPointDescription(String organPointDescription) {
        this.organPointDescription = organPointDescription;
    }

    public String getOrganPointStatus() {
        return organPointStatus;
    }

    public void setOrganPointStatus(String organPointStatus) {
        this.organPointStatus = organPointStatus;
    }

    public String getFkParentOrganPointId() {
        return fkParentOrganPointId;
    }

    public void setFkParentOrganPointId(String fkParentOrganPointId) {
        this.fkParentOrganPointId = fkParentOrganPointId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
