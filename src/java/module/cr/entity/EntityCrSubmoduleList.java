package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrSubmoduleList extends CoreEntity {

    public static String ID = "ID";
    public static String STATUS = "STATUS";
    public static String INSERT_DATE = "INSERT";
    public static String MODIFICATION_DATE = "MODIFICATION";
    public static String FK_MODULE_ID = "FK";
    public static String MODULE_NAME = "MODULE";
    public static String subMODULE_NAME = "subMODULE";
    public static String LI_SUBMODULE_STATUS = "LI";
    public static String SUBMODULE_STATUS_NAME = "subMODULE";
    public static String SUBMODULE_DESCRIPTION = "SUBMODULE";
    public static String SORT_BY = "SORT";
    public static String LANG = "LANG";
    public static String SUBMODULE_TYPE = "submoduleType";
    private String submoduleType = "";
    public static String SUBMODULE_TYPE_NAME = "submoduleTypeName";
    private String submoduleTypeName = "";

    private String fkModuleId = "";
    private String moduleName = "";
    private String submoduleName = "";
    private String liSubmoduleStatus = "";
    private String submoduleStatusName = "";
    private String submoduleDescription = "";
    private String sortBy = "";
    private String lang = "";

    public String getSubmoduleType() {
        return submoduleType;
    }

    public void setSubmoduleType(String submoduleType) {
        this.submoduleType = submoduleType;
    }

    public String getSubmoduleTypeName() {
        return submoduleTypeName;
    }

    public void setSubmoduleTypeName(String submoduleTypeName) {
        this.submoduleTypeName = submoduleTypeName;
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

    public String getSubmoduleStatusName() {
        return submoduleStatusName;
    }

    public void setSubmoduleStatusName(String submoduleStatusName) {
        this.submoduleStatusName = submoduleStatusName;
    }

    public String getSubmoduleDescription() {
        return submoduleDescription;
    }

    public void setSubmoduleDescription(String submoduleDescription) {
        this.submoduleDescription = submoduleDescription;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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
