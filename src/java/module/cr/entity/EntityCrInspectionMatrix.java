package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrInspectionMatrix extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_PARENT_ID = "fkParentId";
    public static String FK_SUBMODULE_ATTRIBUTE_ID = "fkSubmoduleAttributeId";
    public static String SHORT_NAME = "shortName";

    private String fkParentId = "";
    private String fkSubmoduleAttributeId = "";
    private String shortName = "";

    public String getFkParentId() {
        return fkParentId;
    }

    public void setFkParentId(String fkParentId) {
        this.fkParentId = fkParentId;
    }

    public String getFkSubmoduleAttributeId() {
        return fkSubmoduleAttributeId;
    }

    public void setFkSubmoduleAttributeId(String fkSubmoduleAttributeId) {
        this.fkSubmoduleAttributeId = fkSubmoduleAttributeId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    
}
