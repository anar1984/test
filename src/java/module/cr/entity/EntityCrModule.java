package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrModule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String MODULE_NAME = "moduleName";
    public static String LI_MODULE_STATUS = "liModuleStatus";
    public static String MODULE_DESCRIPTION = "moduleDescription";
    public static String LANG = "lang";

    private String moduleName = "";
    private String liModuleStatus = "";
    private String moduleDescription = "";
    private String lang = "";

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLiModuleStatus() {
        return liModuleStatus;
    }

    public void setLiModuleStatus(String liModuleStatus) {
        this.liModuleStatus = liModuleStatus;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
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
