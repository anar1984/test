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
    
    public static String PERMISSION_STRING = "permissionString";
    public static String PERMISSION_TYPE = "permissionType";
    
    private String permissionString = "";
    private PermissionType permissionType = PermissionType.PERMISSION;
    
    public enum PermissionType {
       PERMISSION("P"), MODEL("M"), SUB_MODEL("S");
       private String type;
       private PermissionType(String type) {
           this.type=type;
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
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ID, this.getId());
            obj.put(PERMISSION_STRING, this.getPermissionString());
            obj.put(PERMISSION_TYPE, this.getPermissionType());
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrPermission.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    public void fromString(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            this.setId(obj.getString(ID));
            this.setPermissionString(obj.getString(PERMISSION_STRING));
            this.setPermissionType((PermissionType)obj.get(PERMISSION_TYPE));
        } catch (JSONException ex) {
            Logger.getLogger(EntityCrPermission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

    /**
     * @return the permissionType
     */
    public PermissionType getPermissionType() {
        return permissionType;
    }

    /**
     * @param permissionType the permissionType to set
     */
    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    
    
}
