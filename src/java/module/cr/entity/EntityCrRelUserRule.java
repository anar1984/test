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
public class EntityCrRelUserRule extends CoreEntity {
    
    public static String ID = "id";
    
    public static final String FK_USER_ID = "fkUserId";
    public static final String FK_RULE_ID = "fkRuleId";
    
    private String fkUserId = "";
    private String fkRuleId = "";

    
    
    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FK_USER_ID, this.getFkUserId());
            obj.put(FK_RULE_ID, this.getFkRuleId());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setFkUserId(obj.getString(FK_USER_ID));
            this.setFkRuleId(obj.getString(FK_RULE_ID));
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the fkUserId
     */
    public String getFkUserId() {
        return fkUserId;
    }

    /**
     * @param fkUserId the fkUserId to set
     */
    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
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

    
    
    
}
