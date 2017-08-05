package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrUserControllerList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String FK_USER_ID = "fkUserId";
    public static String FK_COMPONENT_ID = "fkComponentId";
    public static String COMPONENT_NAME = "componentName";

    public static String PERMISSION_TYPE = "permissionType";
    public static String PERMISSION_TYPE_NAME = "PermissionTypeName";
    public static String COMPONENT_TYPE = "componentType";
    public static String COMPONENT_TYPE_NAME = "componentTypeName";
    public static String INPUT_KEY = "inputKey";
    public static String INPUT_VALUE = "inputValue";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String USERNAME = "username";
    public static String LI_COMPONENT_CODE = "liComponentCode";
    public static String ENUM_TYPE_NAME = "enumTypeName";
    public static String CONTROLLER_TYPE = "controllerType";

    private String enumTypeName = "";
    private String controllerType = "";
    private String liComponentCode = "";
    private String fkUserId = "";
    private String fkComponentId = "";
    private String permissionType = "";
    private String PermissionTypeName = "";
    private String componentType = "";
    private String componentTypeName = "";
    private String inputKey = "";
    private String inputValue = "";
    private String username = "";
    private String componentName = "";

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public String getEnumTypeName() {
        return enumTypeName;
    }

    public void setEnumTypeName(String enumTypeName) {
        this.enumTypeName = enumTypeName;
    }

    public String getLiComponentCode() {
        return liComponentCode;
    }

    public void setLiComponentCode(String liComponentCode) {
        this.liComponentCode = liComponentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getPermissionTypeName() {
        return PermissionTypeName;
    }

    public void setPermissionTypeName(String PermissionTypeName) {
        this.PermissionTypeName = PermissionTypeName;
    }

    public String getComponentTypeName() {
        return componentTypeName;
    }

    public void setComponentTypeName(String componentTypeName) {
        this.componentTypeName = componentTypeName;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getFkComponentId() {
        return fkComponentId;
    }

    public void setFkComponentId(String fkComponentId) {
        this.fkComponentId = fkComponentId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
