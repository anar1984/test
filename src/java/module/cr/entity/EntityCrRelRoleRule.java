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
public class EntityCrRelRoleRule extends CoreEntity {

    public static String ID = "id";
    public static final String FK_ROLE_ID = "fkRoleId";
    public static final String FK_RULE_ID = "fkRuleId";

    private String fkRoleId = "";
    private String fkRuleId = "";

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FK_ROLE_ID, this.getFkRoleId());
            obj.put(FK_RULE_ID, this.getFkRuleId());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setFkRoleId(obj.getString(FK_ROLE_ID));
            this.setId(obj.getString(FK_RULE_ID));
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the fkRuleId
     */
    public String getFkRuleId() {
        return fkRuleId;
    }

    /**
     * @param fkRuleId the fkRuleId to set
     */
    public void setFkRuleId(String fkRuleId) {
        this.fkRuleId = fkRuleId;
    }

    /**
     * @return the fkRoleId
     */
    public String getFkRoleId() {
        return fkRoleId;
    }

    /**
     * @param fkRoleId the fkRoleId to set
     */
    public void setFkRoleId(String fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
