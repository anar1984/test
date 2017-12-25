package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrAppointment extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String FK_DOCTOR_USER_ID = "fkDoctorUserId";
    public static String FK_PATIENT_ID = "fkPatientId";
    public static String APPOINTMENT_DATE = "appointmentDate";
    public static String APPOINTMENT_STATUS = "appointmentStatus";
    public static String DESCRIPTION = "description";
    public static String APPOINTMENT_TIME_1 = "appointmentTime1";
    public static String APPOINTMENT_TIME_2 = "appointmentTime2";
    public static String INSPECTION_CODE = "inspectionCode";
    public static String FK_PRICE_LIST_ID = "fkPriceListId";
    
    private String fkPriceListId = "";
    private String inspectionCode = "";
    private String appointmentTime2 = "";
    private String appointmentTime1 = "";
    private String fkDoctorUserId = "";
    private String fkPatientId = "";
    private String appointmentDate = "";
    private String appointmentStatus = "";
    private String description = "";

    public String getFkPriceListId() {
        return fkPriceListId;
    }

    public void setFkPriceListId(String fkPriceListId) {
        this.fkPriceListId = fkPriceListId;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
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

}
