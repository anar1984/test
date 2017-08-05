package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrSubmodule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_MODULE_ID = "fkModuleId";
    public static String SUBMODULE_NAME = "submoduleName";
    public static String LI_SUBMODULE_STATUS = "liSubmoduleStatus";
    public static String SUBMODULE_DESCRIPTION = "submoduleDescription";
    public static String SORT_BY = "sortBy";
    public static String LANG = "lang";
    public static String SUBMODULE_TYPE = "submoduleType";
    private String submoduleType = "";

    private String fkModuleId = "";
    private String submoduleName = "";
    private String liSubmoduleStatus = "";
    private String submoduleDescription = "";
    private String sortBy = "";
    private String lang = "";

    public String getSubmoduleType() {
        return submoduleType;
    }

    public void setSubmoduleType(String submoduleType) {
        this.submoduleType = submoduleType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
    }

    public String getSubmoduleName() {
        return submoduleName;
    }

    public void setSubmoduleName(String submoduleName) {
        this.submoduleName = submoduleName;
    }

    public String getLiSubmoduleStatus() {
        return liSubmoduleStatus;
    }

    public void setLiSubmoduleStatus(String liSubmoduleStatus) {
        this.liSubmoduleStatus = liSubmoduleStatus;
    }

    public String getSubmoduleDescription() {
        return submoduleDescription;
    }

    public void setSubmoduleDescription(String submoduleDescription) {
        this.submoduleDescription = submoduleDescription;
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
