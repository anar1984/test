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
public class EntityCrPaymentType extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String INSERT_DATE = "insertDate";
    public static String PAYMENT_TYPE_NAME = "paymentTypeName";
    public static String PAYMENT_TYPE_SHORTNAME = "paymentTypeShortname";
    public static String DEFAULT_PRICE = "defaultPrice";
    public static String DEFAULT_DISCOUNT = "defaultDiscount";
    public static String DEFAULT_PAYMENT_PERIOD = "defaultPaymentPeriod";
    public static String IS_PUBLIC = "isPublic";
    public static String TYPE = "type";
    public static String USER_LISENCE_COUNT = "userLisenceCount";
    public static String USER_LISENCE_MONTH_RANGE = "userLisenceMonthRange";
    public static String CURRENCY = "currency";
    
    private String currency = "";
    private String paymentTypeName = "";
    private String paymentTypeShortname = "";
    private String defaultPrice = "";
    private String defaultDiscount = "";
    private String defaultPaymentPeriod = "";
    private String isPublic = "";
    private String type = "";
    private String userLisenceCount = "";
    private String userLisenceMonthRange = "";

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getPaymentTypeShortname() {
        return paymentTypeShortname;
    }

    public void setPaymentTypeShortname(String paymentTypeShortname) {
        this.paymentTypeShortname = paymentTypeShortname;
    }

    public String getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(String defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getDefaultDiscount() {
        return defaultDiscount;
    }

    public void setDefaultDiscount(String defaultDiscount) {
        this.defaultDiscount = defaultDiscount;
    }

    public String getDefaultPaymentPeriod() {
        return defaultPaymentPeriod;
    }

    public void setDefaultPaymentPeriod(String defaultPaymentPeriod) {
        this.defaultPaymentPeriod = defaultPaymentPeriod;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserLisenceCount() {
        return userLisenceCount;
    }

    public void setUserLisenceCount(String userLisenceCount) {
        this.userLisenceCount = userLisenceCount;
    }

    public String getUserLisenceMonthRange() {
        return userLisenceMonthRange;
    }

    public void setUserLisenceMonthRange(String userLisenceMonthRange) {
        this.userLisenceMonthRange = userLisenceMonthRange;
    }

    public enum PaymentType {
        USER_LISENCE("UL"),
        PAMENT_BASES("PB"); 
        private final String status;

        PaymentType(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
