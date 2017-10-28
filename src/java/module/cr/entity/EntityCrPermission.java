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
public class EntityCrPermission extends CoreEntity {
     
    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String DESCRIPTION = "description";
    public static String PERMISSION_STRING = "permissionString";
    public static String PERMISSION_TYPE = "permissionType";
    
    private String permissionString = "";
    private String permissionType = "";
    private String description = "";
    
    public enum PermissionType {
       PERMISSION("P"), MODEL("M"), SUB_MODEL("S");
       private String type;
       private PermissionType(String type) {
           this.type=type;
       }
       
       public String get(){
           return this.type;
       }
     };

    
    
    /**
     * @return the permissionString
     */
    public String getPermissionString() {
        return permissionString;
    }

    /**
     * @param permissionString the permissionString to set
     */
    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }
    
     
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

    /**
     * @return the permissionType
     */
    public String getPermissionType() {
        return permissionType;
    }

    /**
     * @param permissionType the permissionType to set
     */
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    

    
    
}
