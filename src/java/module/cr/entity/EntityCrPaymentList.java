package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrPaymentList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_DOCTOR_USER_ID = "fkDoctorUserId";
    public static String DOCTOR_FULLNAME = "doctorFullname";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String PATIENT_ID = "patientId";
    public static String PATIENT_FULLNAME = "patientFullname";
    public static String PAYMENT_DATE = "paymentDate";
    public static String PAYMENT_AMOUNT = "paymentAmount";
    public static String FK_PRICE_LIST_ID = "fkPriceListId";
    public static String PAYMENT_NAME = "paymentName";
    public static String PAYMENT_UNIT_PRICE = "paymentUnitPrice";
    public static String PAYMENT_CURRENCY = "paymentCurrency";
    public static String PAYMENT_DISCOUNT = "paymentDiscount";
    public static String DESCRIPTION = "description";
    public static String PAYMENT_TIME = "paymentTime";
    public static String PAYMENT_CURRENCY_NAME = "paymentCurrencyName";
    public static String PAYMENT_STATUS = "paymentStatus";
    private String paymentStatus = "";
    public static String PAYMENT_STATUS_NAME = "paymentStatusName";

    public static String PAYMENT_NO = "paymentNo";
    private String paymentNo = "";

    private String paymentStatusName = "";

    private String paymentCurrencyName = "";
    private String paymentTime = "";

    private String fkDoctorUserId = "";
    private String doctorFullname = "";
    private String fkPatientId = "";
    private String patientId = "";
    private String patientFullname = "";
    private String paymentDate = "";
    private String paymentAmount = "";
    private String fkPriceListId = "";
    private String paymentName = "";
    private String paymentUnitPrice = "";
    private String paymentCurrency = "";
    private String paymentDiscount = "";
    private String description = "";

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public String getPaymentCurrencyName() {
        return paymentCurrencyName;
    }

    public void setPaymentCurrencyName(String paymentCurrencyName) {
        this.paymentCurrencyName = paymentCurrencyName;
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

    public String getDoctorFullname() {
        return doctorFullname;
    }

    public void setDoctorFullname(String doctorFullname) {
        this.doctorFullname = doctorFullname;
    }

    public String getFkPatientId() {
        return fkPatientId;
    }

    public void setFkPatientId(String fkPatientId) {
        this.fkPatientId = fkPatientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientFullname() {
        return patientFullname;
    }

    public void setPatientFullname(String patientFullname) {
        this.patientFullname = patientFullname;
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

    public String getFkPriceListId() {
        return fkPriceListId;
    }

    public void setFkPriceListId(String fkPriceListId) {
        this.fkPriceListId = fkPriceListId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentUnitPrice() {
        return paymentUnitPrice;
    }

    public void setPaymentUnitPrice(String paymentUnitPrice) {
        this.paymentUnitPrice = paymentUnitPrice;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
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
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
