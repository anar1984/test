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
public class EntityCrRelCompanyAndRule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_RULE_ID = "fkRuleId";
    public static String FK_COMPANY_ID = "fkCompanyId";
    public static String REL_TYPE = "relType";
    public static String EXPIRE_DATE = "expireDate";

    private String fkRuleId = "";
    private String fkCompanyId = "";
    private String relType = "";
    private String expireDate = "";

    public String getFkRuleId() {
        return fkRuleId;
    }

    public void setFkRuleId(String fkRuleId) {
        this.fkRuleId = fkRuleId;
    }

    public String getFkCompanyId() {
        return fkCompanyId;
    }

    public void setFkCompanyId(String fkCompanyId) {
        this.fkCompanyId = fkCompanyId;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
