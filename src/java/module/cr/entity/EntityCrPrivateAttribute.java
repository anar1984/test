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
public class EntityCrPrivateAttribute extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_VALUE_TYPE_ID = "fkValueTypeId";
    public static String FK_MODULE_ID = "fkModuleId";
    public static String FK_PRIVATE_SUBMODULE_ID = "fkPrivateSubmoduleId";
    public static String VALUE = "value";
    public static String DESCRIPTION = "description";
    public static String SORT_BY = "sortBy";
    public static String NAME = "name";
    public static String CODE = "code";
    public static String HAS_OTHER = "hasOther";
    private String code = "";
    private String name = "";
    private String fkValueTypeId = "";
    private String fkModuleId = "";
    private String fkPrivateSubmoduleId = "";
    private String value = "";
    private String description = "";
    private String sortBy = "";
    private String hasOther="";

    
    public String getHasOther(){
        return hasOther;
    }
 
    public void setHasOther(String hasOther){
        this.hasOther=hasOther;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFkValueTypeId() {
        return fkValueTypeId;
    }

    public void setFkValueTypeId(String fkValueTypeId) {
        this.fkValueTypeId = fkValueTypeId;
    }

    public String getFkModuleId() {
        return fkModuleId;
    }

    public void setFkModuleId(String fkModuleId) {
        this.fkModuleId = fkModuleId;
    }

    public String getFkPrivateSubmoduleId() {
        return fkPrivateSubmoduleId;
    }

    public void setFkPrivateSubmoduleId(String fkPrivateSubmoduleId) {
        this.fkPrivateSubmoduleId = fkPrivateSubmoduleId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

}
