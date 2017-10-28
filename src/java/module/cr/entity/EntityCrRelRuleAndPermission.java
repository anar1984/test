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
public class EntityCrRelRuleAndPermission extends CoreEntity {
    
    public static String ID = "id";
    
    public static final String FK_RULE_ID = "fkRuleId";
    public static final String FK_PERMISSION_ID = "fkPermissionId";
    
    private String fkRuleId = "";
    private String fkPermissionId = "";

    
    
    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FK_RULE_ID, this.getFkRuleId());
            obj.put(FK_PERMISSION_ID, this.getFkPermissionId());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setId(obj.getString(FK_RULE_ID));
            this.setFkPermissionId(obj.getString(FK_PERMISSION_ID));
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
     * @return the fkPermissionId
     */
    public String getFkPermissionId() {
        return fkPermissionId;
    }

    /**
     * @param fkPermissionId the fkPermissionId to set
     */
    public void setFkPermissionId(String fkPermissionId) {
        this.fkPermissionId = fkPermissionId;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }
    
    
    
}
