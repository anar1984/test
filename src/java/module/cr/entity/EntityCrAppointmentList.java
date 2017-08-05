package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrAppointmentList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_DOCTOR_USER_ID = "fkDoctorUserId";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String APPOINTMENT_DATE = "appointmentDate";
    public static String APPOINTMENT_STATUS = "appointmentStatus";
    public static String DESCRIPTION = "description";
    public static String PATIENT_ID = "patientId";
    public static String PATIENT_IMAGE = "patientImage";
    public static String PATIENT_NAME = "patientName";
    public static String PATIENT_SURNAME = "patientSurname";
    public static String PATIENT_MIDDLE_NAME = "patientMiddleName";
    public static String PATIENT_BIRTH_DATE = "patientBirthDate";
    public static String PATIENT_BIRTH_PLACE = "patientBirthPlace";
    public static String FK_OWNER_USER_ID = "fkOwnerUserId";
    public static String SEX = "sex";

    public static String APPOINTMENT_TIME_1 = "appointmentTime1";
    public static String APPOINTMENT_TIME_2 = "appointmentTime2";
    public static String APPOINTMENT_STATUS_NAME = "appointmentStatusName";
    public static String INSPECTION_CODE = "inspectionCode";
    public static String DOCTOR_FULLNAME = "doctorFullname";
    public static String PAYMENT_STATUS = "paymentStatus";
    private String paymentStatus = "";
    private String doctorFullname = "";
    private String inspectionCode = "";
    private String appointmentStatusName = "";
    private String appointmentTime2 = "";
    private String appointmentTime1 = "";
    private String fkDoctorUserId = "";
    private String fkPatientId = "";
    private String appointmentDate = "";
    private String appointmentStatus = "";
    private String description = "";
    private String patientId = "";
    private String patientImage = "";
    private String patientName = "";
    private String patientSurname = "";
    private String patientMiddleName = "";
    private String patientBirthDate = "";
    private String patientBirthPlace = "";
    private String fkOwnerUserId = "";
    private String sex = "";

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDoctorFullname() {
        return doctorFullname;
    }

    public void setDoctorFullname(String doctorFullname) {
        this.doctorFullname = doctorFullname;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public String getAppointmentStatusName() {
        return appointmentStatusName;
    }

    public void setAppointmentStatusName(String appointmentStatusName) {
        this.appointmentStatusName = appointmentStatusName;
    }

    public String getAppointmentTime2() {
        return appointmentTime2;
    }

    public void setAppointmentTime2(String appointmentTime2) {
        this.appointmentTime2 = appointmentTime2;
    }

    public String getAppointmentTime1() {
        return appointmentTime1;
    }

    public void setAppointmentTime1(String appointmentTime1) {
        this.appointmentTime1 = appointmentTime1;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientImage() {
        return patientImage;
    }

    public void setPatientImage(String patientImage) {
        this.patientImage = patientImage;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }

    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public String getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(String patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getPatientBirthPlace() {
        return patientBirthPlace;
    }

    public void setPatientBirthPlace(String patientBirthPlace) {
        this.patientBirthPlace = patientBirthPlace;
    }

    public String getFkOwnerUserId() {
        return fkOwnerUserId;
    }

    public void setFkOwnerUserId(String fkOwnerUserId) {
        this.fkOwnerUserId = fkOwnerUserId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
