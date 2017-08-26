/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility.qreport;

/**
 *
 * @author Lenovo
 */
public class QReportCarrier {

    public QRPatient Patient;
    public QRAttribute Attribute;
    public QRModule Module;
    public QRSubmodule Submodule;
    private String reportId="";
    private String sessionId="";
    private String text="";

    public QReportCarrier() {
        Patient = new QRPatient();
        Attribute = new QRAttribute();
        Module = new QRModule();
        Submodule = new QRSubmodule();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
    

}
