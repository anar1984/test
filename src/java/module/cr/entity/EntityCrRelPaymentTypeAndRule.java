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
public class EntityCrRelPaymentTypeAndRule extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_RULE_ID = "fkRuleId";
    public static String FK_PAYMENT_TYPE_ID = "fkPaymentTypeId";
    public static String OWNER = "owner";
    public static String DEFAULT_PERIOD = "defaultPeriod";
    
    private String defaultPeriod = "";
    private String fkRuleId = "";
    private String fkPaymentTypeId = "";
    private String owner = "";

    public String getFkPaymentTypeId() {
        return fkPaymentTypeId;
    }

    public void setFkPaymentTypeId(String fkPaymentTypeId) {
        this.fkPaymentTypeId = fkPaymentTypeId;
    }

    
    public String getDefaultPeriod() {
        return defaultPeriod;
    }

    public void setDefaultPeriod(String defaultPeriod) {
        this.defaultPeriod = defaultPeriod;
    }

    public String getFkRuleId() {
        return fkRuleId;
    }

    public void setFkRuleId(String fkRuleId) {
        this.fkRuleId = fkRuleId;
    }

     

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
