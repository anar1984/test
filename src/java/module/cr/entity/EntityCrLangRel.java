package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrLangRel extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String REL_ID = "relId";
    public static String LANG_TYPE = "langType";
    public static String LANG_DEF = "langDef";
    public static String LANG = "lang";
    public static String LANG_FIELD = "langField";
    
    private String langField = "";
    private String relId = "";
    private String langType = "";
    private String langDef = "";
    private String lang = "";

    public String getLangField() {
        return langField;
    }

    public void setLangField(String langField) {
        this.langField = langField;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getLangDef() {
        return langDef;
    }

    public void setLangDef(String langDef) {
        this.langDef = langDef;
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
