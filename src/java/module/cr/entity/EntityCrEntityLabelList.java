package module.cr.entity;


import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrEntityLabelList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String ENTITY_NAME = "entityName";
    public static String FIELD_NAME = "fieldName";
    public static String LANG = "lang";
    public static String LANGUAGE_NAME = "languageName";
    public static String LABEL_TYPE = "labelType";
    public static String DESCRIPTION = "description";
    public static String ENTITY_FULLNAME="entityFullname";


    private String entityName = "";
    private String fieldName = "";
    private String lang = "";
    private String languageName = "";
    private String labelType = "";
    private String description = "";
    private String entityFullname="";

    public String getEntityFullname() {
        return entityFullname;
    }

    public void setEntityFullname(String entityFullname) {
        this.entityFullname = entityFullname;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
