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
public class EntityCrRole extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";

    public static final String ROLE_NAME = "roleName";
    public static final String DESCRIPTION = "description";

    private String roleName = "";
    private String description = "";

    /**
     * @return the ruleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param ruleName the ruleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }
    

}
