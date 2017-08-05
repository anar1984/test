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
public class EntityCrRelRuleAndComponentList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String LI_RULE_KEY = "liRuleKey";
    public static String RULE_NAME = "ruleName";
    public static String LI_COMPONENT_CODE = "liComponentCode";
    public static String COMPONENT_CODE_NAME = "componentCodeName";
    public static String LI_COMPONENT_KEY = "liComponentKey";
    public static String COMPONENT_KEY_NAME = "componentKeyName";
    public static String DESCRIPTION = "description";
    public static String PERMISSION_TYPE = "permissionType";
    public static String PERMISSION_TYPE_NAME = "PermissionTypeName";
    public static String INPUT_KEY = "inputKey";
    public static String INPUT_VALUE = "inputValue";

    private String liRuleKey = "";
    private String ruleName = "";
    private String liComponentCode = "";
    private String componentCodeName = "";
    private String liComponentKey = "";
    private String componentKeyName = "";
    private String description = "";
    private String permissionType = "";
    private String permissionTypeName = "";
    private String inputKey = "";
    private String inputValue = "";

    public String getLiRuleKey() {
        return liRuleKey;
    }

    public void setLiRuleKey(String liRuleKey) {
        this.liRuleKey = liRuleKey;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getLiComponentCode() {
        return liComponentCode;
    }

    public void setLiComponentCode(String liComponentCode) {
        this.liComponentCode = liComponentCode;
    }

    public String getComponentCodeName() {
        return componentCodeName;
    }

    public void setComponentCodeName(String componentCodeName) {
        this.componentCodeName = componentCodeName;
    }

    public String getLiComponentKey() {
        return liComponentKey;
    }

    public void setLiComponentKey(String liComponentKey) {
        this.liComponentKey = liComponentKey;
    }

    public String getComponentKeyName() {
        return componentKeyName;
    }

    public void setComponentKeyName(String componentKeyName) {
        this.componentKeyName = componentKeyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionTypeName() {
        return permissionTypeName;
    }

    public void setPermissionTypeName(String permissionTypeName) {
        this.permissionTypeName = permissionTypeName;
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

}
