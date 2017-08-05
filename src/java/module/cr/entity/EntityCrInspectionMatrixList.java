package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrInspectionMatrixList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_USER_ID = "fkUserId";
    public static String MATRIX_TYPE = "matrixType";
    public static String MATRIX_NAME = "matrixName";
    public static String FK_PARENT_ID = "fkParentId";
    public static String FK_SUBMODULE_ATTRIBUTE_ID = "fkSubmoduleAttributeId";
    public static String SHORT_NAME = "shortName";
    public static String ATTRIBUTE_NAME = "attributeName";
    
    private String attributeName = "";
    private String fkUserId = "";
    private String matrixType = "";
    private String matrixName = "";
    private String fkParentId = "";
    private String fkSubmoduleAttributeId = "";
    private String shortName = "";

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getMatrixType() {
        return matrixType;
    }

    public void setMatrixType(String matrixType) {
        this.matrixType = matrixType;
    }

    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }

    public String getFkParentId() {
        return fkParentId;
    }

    public void setFkParentId(String fkParentId) {
        this.fkParentId = fkParentId;
    }

    public String getFkSubmoduleAttributeId() {
        return fkSubmoduleAttributeId;
    }

    public void setFkSubmoduleAttributeId(String fkSubmoduleAttributeId) {
        this.fkSubmoduleAttributeId = fkSubmoduleAttributeId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
