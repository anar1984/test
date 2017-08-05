package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrValueTypeList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String VALUE_TYPE_NAME = "valueTypeName";
    public static String VALUE_TYPE_DESCRIPTION = "valueTypeDescription";
    public static String VALUE_TYPE_PARAM_1 = "valueTypeParam1";
    public static String VALUE_TYPE_PARAM_2 = "valueTypeParam2";
    public static String VALUE_TYPE_STATUS = "valueTypeStatus";
    public static String LANG = "lang";
    public static String VALUE_TYPE_STATUS_NAME = "valueTypeStatusName";
    
    private String valueTypeStatusName = "";
    private String valueTypeName = "";
    private String valueTypeDescription = "";
    private String valueTypeParam1 = "";
    private String valueTypeParam2 = "";
    private String valueTypeStatus = "";
    private String lang = "";

    public String getValueTypeStatusName() {
        return valueTypeStatusName;
    }

    public void setValueTypeStatusName(String valueTypeStatusName) {
        this.valueTypeStatusName = valueTypeStatusName;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
    }

    public String getValueTypeDescription() {
        return valueTypeDescription;
    }

    public void setValueTypeDescription(String valueTypeDescription) {
        this.valueTypeDescription = valueTypeDescription;
    }

    public String getValueTypeParam1() {
        return valueTypeParam1;
    }

    public void setValueTypeParam1(String valueTypeParam1) {
        this.valueTypeParam1 = valueTypeParam1;
    }

    public String getValueTypeParam2() {
        return valueTypeParam2;
    }

    public void setValueTypeParam2(String valueTypeParam2) {
        this.valueTypeParam2 = valueTypeParam2;
    }

    public String getValueTypeStatus() {
        return valueTypeStatus;
    }

    public void setValueTypeStatus(String valueTypeStatus) {
        this.valueTypeStatus = valueTypeStatus;
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
