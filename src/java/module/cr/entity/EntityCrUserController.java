package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrUserController extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String FK_USER_ID = "fkUserId";
    public static String FK_COMPONENT_ID = "fkComponentId";
    public static String PERMISSION_TYPE = "permissionType";
    public static String COMPONENT_TYPE = "componentType";
    public static String INPUT_KEY = "inputKey";
    public static String INPUT_VALUE = "inputValue";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String CONTROLLER_TYPE = "controllerType";

    private String controllerType = "";
    private String fkUserId = "";
    private String fkComponentId = "";
    private String permissionType = "";
    private String componentType = "";
    private String inputKey = "";
    private String inputValue = "";

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
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

}
