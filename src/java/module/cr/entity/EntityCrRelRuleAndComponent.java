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
public class EntityCrRelRuleAndComponent extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String LI_RULE_KEY = "liRuleKey";
    public static String LI_COMPONENT_CODE = "liComponentCode";
    public static String LI_COMPONENT_KEY = "liComponentKey";
    public static String PERMISSION_TYPE = "permissionType";
    public static String INPUT_KEY = "inputKey";
    public static String INPUT_VALUE = "inputValue";
    public static String DESCRIPTION = "description";

    private String liRuleKey = "";
    private String liComponentCode = "";
    private String liComponentKey = "";
    private String permissionType = "";
    private String inputKey = "";
    private String inputValue = "";
    private String description = "";

    public String getLiRuleKey() {
        return liRuleKey;
    }

    public void setLiRuleKey(String liRuleKey) {
        this.liRuleKey = liRuleKey;
    }

    public String getLiComponentCode() {
        return liComponentCode;
    }

    public void setLiComponentCode(String liComponentCode) {
        this.liComponentCode = liComponentCode;
    }

    public String getLiComponentKey() {
        return liComponentKey;
    }

    public void setLiComponentKey(String liComponentKey) {
        this.liComponentKey = liComponentKey;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
