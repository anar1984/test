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
public class EntityCrCompanyPayment extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String INSERT_DATE = "insertDate";
    public static String FK_COMPANY_ID = "fkCompanyId";
    public static String FK_PAYMENT_TYPE_ID = "fkPaymentTypeId";
    public static String PAYMENT_DATE = "paymentDate";
    public static String PAYMENT_TIME = "paymentTime";
    public static String PAYMENT_AMOUNT = "paymentAmount";
    public static String PAYMENT_DISCOUNT = "paymentDiscount";
    public static String DESCRIPTION = "description";
    public static String CURRENCY = "currency";
    
    private String currency = "";
    private String fkCompanyId = "";
    private String fkPaymentTypeId = "";
    private String paymentDate = "";
    private String paymentTime = "";
    private String paymentAmount = "";
    private String paymentDiscount = "";
    private String description = "";

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFkCompanyId() {
        return fkCompanyId;
    }

    public void setFkCompanyId(String fkCompanyId) {
        this.fkCompanyId = fkCompanyId;
    }

    public String getFkPaymentTypeId() {
        return fkPaymentTypeId;
    }

    public void setFkPaymentTypeId(String fkPaymentTypeId) {
        this.fkPaymentTypeId = fkPaymentTypeId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDiscount() {
        return paymentDiscount;
    }

    public void setPaymentDiscount(String paymentDiscount) {
        this.paymentDiscount = paymentDiscount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public enum PaymentType {
//        USER_LISENCE("UL"),
//        PAMENT_BASES("PB"); 
//        private final String status;
//
//        PaymentType(String status) {
//            this.status = status;
//        }
//
//        public String toString() {
//            return this.status;
//        }
//    }
    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
