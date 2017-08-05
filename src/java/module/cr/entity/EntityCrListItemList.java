/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrListItemList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String ITEM_CODE = "itemCode";
    public static String ITEM_CODE_NAME = "itemCodeName";
    public static String ITEM_KEY = "itemKey";
    public static String ITEM_VALUE = "itemValue";
    public static String LANG = "lang";
    public static String LANGUAGE_NAME = "languageName";
    public static String PARAM_1 = "param1";
    public static String PARAM_2 = "param2";
    public static String PARAM_3 = "param3";
    public static String PARAM_4 = "param4";
    public static String PARAM_5 = "param5";

    
    private String itemCode = "";
    private String itemCodeName = "";
    private String itemKey = "";
    private String itemValue = "";
    private String lang = "";
    private String languageName = "";
    private String param1 = "";
    private String param2 = "";
    private String param3 = "";
    private String param4 = "";
    private String param5 = "";

    

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getItemCodeName() {
        return itemCodeName;
    }

    public void setItemCodeName(String itemCodeName) {
        this.itemCodeName = itemCodeName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
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
