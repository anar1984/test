/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author nikli
 */
public class EntityCrRelUserAndRule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_RULE_ID = "fkRuleId";
    public static String FK_USER_ID = "fkUserId";

    private String fkRuleId = "";
    private String fkUserId = "";

    public String getFkRuleId() {
        return fkRuleId;
    }

    public void setFkRuleId(String fkRuleId) {
        this.fkRuleId = fkRuleId;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

}
