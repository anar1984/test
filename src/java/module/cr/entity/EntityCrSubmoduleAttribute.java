package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrSubmoduleAttribute extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_ORGAN_POINT_ID = "fkOrganPointId";
    public static String FK_ATTRIBUTE_ID = "fkAttributeId";
    public static String FK_VALUE_TYPE_ID = "fkValueTypeId";
    public static String FK_SUBMODULE_ID = "fkSubmoduleId";
    public static String SUBMODULE_VALUE = "submoduleValue";
    public static String SUBMODULE_DESCRIPTION = "submoduleDescription";
    public static String HAS_OTHER = "hasOther";
    public static String SORT_BY = "sortBy";
    public static String IS_VISIBLE = "isVisible";
    public static String LANG = "lang";
    public static String FK_MODULE_ID = "fkModuleId";
    private String fkModuleId = "";

    private String fkOrganPointId = "";
    private String fkAttributeId = "";
    private String fkValueTypeId = "";
    private String fkSubmoduleId = "";
    private String submoduleValue = "";
    private String submoduleDescription = "";
    private String hasOther = "";
    private String sortBy = "";
    private String isVisible = "";
    private String lang = "";

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
    }

    public String getFkOrganPointId() {
        return fkOrganPointId;
    }

    public void setFkOrganPointId(String fkOrganPointId) {
        this.fkOrganPointId = fkOrganPointId;
    }

    public String getFkAttributeId() {
        return fkAttributeId;
    }

    public void setFkAttributeId(String fkAttributeId) {
        this.fkAttributeId = fkAttributeId;
    }

    public String getFkValueTypeId() {
        return fkValueTypeId;
    }

    public void setFkValueTypeId(String fkValueTypeId) {
        this.fkValueTypeId = fkValueTypeId;
    }

    public String getFkSubmoduleId() {
        return fkSubmoduleId;
    }

    public void setFkSubmoduleId(String fkSubmoduleId) {
        this.fkSubmoduleId = fkSubmoduleId;
    }

    public String getSubmoduleValue() {
        return submoduleValue;
    }

    public void setSubmoduleValue(String submoduleValue) {
        this.submoduleValue = submoduleValue;
    }

    public String getSubmoduleDescription() {
        return submoduleDescription;
    }

    public void setSubmoduleDescription(String submoduleDescription) {
        this.submoduleDescription = submoduleDescription;
    }

    public String getHasOther() {
        return hasOther;
    }

    public void setHasOther(String hasOther) {
        this.hasOther = hasOther;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
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
