package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrRelPriceListAndSubmodule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_PRICE_LIST_ID = "fkPriceListId";
    public static String FK_SUBMODULE_ID = "fkSubmoduleId";

    private String fkPriceListId = "";
    private String fkSubmoduleId = "";

    public String getFkPriceListId() {
        return fkPriceListId;
    }

    public void setFkPriceListId(String fkPriceListId) {
        this.fkPriceListId = fkPriceListId;
    }

    public String getFkSubmoduleId() {
        return fkSubmoduleId;
    }

    public void setFkSubmoduleId(String fkSubmoduleId) {
        this.fkSubmoduleId = fkSubmoduleId;
    }

}
