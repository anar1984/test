/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr.entity;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import utility.CoreEntity;

/**
 *
 * @author nikli
 */
public class EntityCrRelRuleAndPermissionList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_RULE_ID = "fkRuleId";
    public static String RULE_NAME = "ruleName";
    public static String FK_PERMISSION_ID = "fkPermissionId";
    public static String PERMISSION_STRING = "permissionString";

    private String fkRuleId = "";
    private String ruleName = "";
    private String fkPermissionId = "";
    private String permissionString = "";

    public String getFkRuleId() {
        return fkRuleId;
    }

    public void setFkRuleId(String fkRuleId) {
        this.fkRuleId = fkRuleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getFkPermissionId() {
        return fkPermissionId;
    }

    public void setFkPermissionId(String fkPermissionId) {
        this.fkPermissionId = fkPermissionId;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
