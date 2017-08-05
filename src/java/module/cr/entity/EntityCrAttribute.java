package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrAttribute extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String ATTRIBUTE_CODE = "attributeCode";
    public static String ATTRIBUTE_NAME = "attributeName";
    public static String ATTRIBUTE_DESCRIPTION = "attributeDescription";
    public static String LI_ATTRIBUTE_STATUS = "liAttributeStatus";
    public static String LI_IS_GENERAL = "liIsGeneral";
    public static String LANG = "lang";

    private String attributeCode = "";
    private String attributeName = "";
    private String attributeDescription = "";
    private String liAttributeStatus = "";
    private String liIsGeneral = "";
    private String lang = "";

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeDescription() {
        return attributeDescription;
    }

    public void setAttributeDescription(String attributeDescription) {
        this.attributeDescription = attributeDescription;
    }

    public String getLiAttributeStatus() {
        return liAttributeStatus;
    }

    public void setLiAttributeStatus(String liAttributeStatus) {
        this.liAttributeStatus = liAttributeStatus;
    }

    public String getLiIsGeneral() {
        return liIsGeneral;
    }

    public void setLiIsGeneral(String liIsGeneral) {
        this.liIsGeneral = liIsGeneral;
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
