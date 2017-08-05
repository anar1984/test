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
public class EntityCrRelUserRole extends CoreEntity {
    
    
    
    public static final String FK_USER_ID = "fkUserId";
    public static final String FK_ROLE_ID = "fkRoleId";
    
    private String fkUserId = "";
    private String fkRoleId = "";

    
    
    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FK_USER_ID, this.getFkUserId());
            obj.put(FK_ROLE_ID, this.getFkRoleId());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setId(obj.getString(FK_USER_ID));
            this.setFkRoleId(obj.getString(FK_ROLE_ID));
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

    
    
    
}
