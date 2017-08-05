package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrPayment extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_DOCTOR_USER_ID = "fkDoctorUserId";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String PAYMENT_DATE = "paymentDate";
    public static String PAYMENT_AMOUNT = "paymentAmount";
    public static String PAYMENT_CURRENCY = "paymentCurrency";
    public static String FK_PRICE_LIST_ID = "fkPriceListId";
    public static String PAYMENT_STATUS = "paymentStatus";
    public static String PAYMENT_DISCOUNT = "paymentDiscount";
    public static String DESCRIPTION = "description";
    public static String PAYMENT_TIME = "paymentTime";
    public static String PAYMENT_NO = "paymentNo";
    private String paymentNo = "";
    private String paymentTime = "";
    private String fkDoctorUserId = "";
    private String fkPatientId = "";
    private String paymentDate = "";
    private String paymentAmount = "";
    private String paymentCurrency = "";
    private String fkPriceListId = "";
    private String paymentStatus = "";
    private String paymentDiscount = "";
    private String description = "";

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getFkDoctorUserId() {
        return fkDoctorUserId;
    }

    public void setFkDoctorUserId(String fkDoctorUserId) {
        this.fkDoctorUserId = fkDoctorUserId;
    }

    public String getFkPatientId() {
        return fkPatientId;
    }

    public void setFkPatientId(String fkPatientId) {
        this.fkPatientId = fkPatientId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getFkPriceListId() {
        return fkPriceListId;
    }

    public void setFkPriceListId(String fkPriceListId) {
        this.fkPriceListId = fkPriceListId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

}
