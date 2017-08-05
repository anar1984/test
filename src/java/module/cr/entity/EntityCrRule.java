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
public class EntityCrRule extends CoreEntity {
     
    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    
    public static final String RULE_NAME = "ruleName";
    
    private String ruleName = "";

    
    
    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ID, this.getId());
            obj.put(RULE_NAME, this.getRuleName());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setId(obj.getString(ID));
            this.setRuleName(obj.getString(RULE_NAME));
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrRule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the ruleName
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param ruleName the ruleName to set
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }
    
}
