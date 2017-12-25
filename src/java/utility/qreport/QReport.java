/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility.qreport;

import java.sql.Connection;
import label.CoreLabel;
import module.cr.CrModel;
import module.cr.entity.EntityCrAppointment;
import module.cr.entity.EntityCrCompany;
import module.cr.entity.EntityCrReportLine;
import module.cr.entity.EntityCrUser;
import module.cr.entity.EntityCrUserList;
import module.pg.PgModel;
import org.apache.commons.lang.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import resources.config.Config;
import utility.CacheUtil;
import utility.Carrier;
import utility.QDate;
import utility.QException;
import utility.QUtility;
import utility.SessionManager;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author Lenovo
 */
public class QReport {

    public static void main(String arg[]) throws QException {

        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);
            SessionManager.setDomain(SessionManager.getCurrentThreadId(), "apd_23gemsb");

            QReportCarrier rc = new QReportCarrier();
            rc.setReportId("201711071606260935");
            rc.setSessionId("201711010545120104");
            rc.Patient.setPatientInfoById("201708221705210595");

            String st = getReport(rc);
//            System.out.println(st);

            conn.commit();
            conn.close();
        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.close();
            } catch (Exception ex1) {
            }
        }

    }

    public static String getReport(QReportCarrier rc) throws QException {
        String pureHtml = getPureReportHtml(rc.getReportId());
        rc.setText(pureHtml);
        String res = replaceTags(rc);
        return res;
    }

    private static String getPureReportHtml(String reportId) throws QException {
        String res = "";
        if (reportId.trim().length() == 0) {
            return res;
        }

        EntityCrReportLine ent = new EntityCrReportLine();
        ent.setId(reportId);
        EntityManager.select(ent);
        res = ent.getReportHtml();

        return res;
    }

    private static String replaceTags(QReportCarrier rc) throws QException {
        //movcud taglar
        //<qpatient data> patient melumatlari ucun
        //<qattr data="name" image width="50" height="40"> attribute adlari ucun
        //<qmodule data="name"> Module  
        //<qsmodule data="name"> Submodule  
        //<quser data="name">
        //<qlogo> istifadecinin daxil etdiyi logo.
        String txt = rc.getText();
        txt = txt.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&nbsp;", " ");

        Document doc = Jsoup.parse(txt, "UTF-8");

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("el deyilmemish html>>>>>>>>>>>>>>>");
//        System.out.println(txt);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("");
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("pure html>>>>>>>>>>>>>>>");
        System.out.println(doc.toString());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("");

        doc = doInspectionTag(doc, rc);
        doc = doPatientTag(doc, rc);
        doc = doAttributeTag(doc, rc);
        doc = doPaymentTag(doc, rc);
        doc = doCompanyTag(doc);
        doc = doUserTag(doc);
        doc = doDateTag(doc);
        doc = doLangTag(doc);

        return doc.toString();
    }

    /*
    Movcud datalar
    name,surname,middleName,birthDate,birthPlace,sex,occupation,maritualStatus
education,bloodGroup,rhFactor,mobile1
mobile2,telephone1,telephone2,email1,email2,country,city,postIndex,description
     */
    private static Document doPatientTag(Document doc, QReportCarrier rc) throws QException {

        Elements elements = doc.getElementsByTag("qpatient");
        for (Element element : elements) {
            String data = element.attr("data");
            setInspectionTagPatientInfo(element, data, rc);
//            element.remove();
        }

        return doc;
    }

    /*
    name,description,code
     */
    private static Document doAttributeTag(Document doc, QReportCarrier rcarrier) throws QException {
        String fkSessionId = rcarrier.getSessionId();
        if (fkSessionId.trim().length() == 1) {
            return doc;
        }

        String tn = CoreLabel.RESULT_SET;

        Carrier c = new Carrier();
        c.setValue("inspectionCode", fkSessionId);
        c = CrModel.getInspectionList(c);

        Elements elements = doc.getElementsByTag("qattr");
        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";
            String code = element.hasAttr("code") ? element.attr("code") : "";
            setInspectionTagAttributeInfo(element, data, code, c);

        }
        return doc;
    }

    private static Document doInspectionTag(Document doc, QReportCarrier rcarrier) throws QException {
        String fkSessionId = rcarrier.getSessionId();
        if (fkSessionId.trim().length() == 0) {
            return doc;
        }

        Carrier c = new Carrier();
        c.setValue("inspectionCode", fkSessionId);
        c = CrModel.getInspectionList(c);

        Elements elements = doc.getElementsByTag("qins");
        for (Element element : elements) {
            String type = element.hasAttr("type") ? element.attr("type") : "";
            String data = element.hasAttr("data") ? element.attr("data") : "";
            String code = element.hasAttr("code") ? element.attr("code") : "";

            if (type.trim().equals("patient")) {
                setInspectionTagPatientInfo(element, data, rcarrier);
            } else if (type.trim().equals("attribute")) {
                setInspectionTagAttributeInfo(element, data, code, c);
            } else if (type.trim().equals("doctor")) {
                setInspectionTagDoctorInfo(element, data, c);
            } else if (type.trim().equals("private")) {
                setInspectionTagPrivateAttributeInfo(element, data, code, c);
            }

        }
        return doc;
    }

    private static void setInspectionTagDoctorInfo(Element element, String data,
            Carrier c) throws QException {
        String tn = CoreLabel.RESULT_SET;

//        System.out.println("c XML" + c.toXML());
//        Carrier cprInsDoctor = 
//                c.getKeyValuesPairFromTable(tn, "attributeCode", "doctorFullname");
//         System.out.println("cprIns2 XML" + cprInsDoctor.toXML());
        if (data.trim().equals("fulname") || data.trim().equals("")) {
            String val1 = c.getValue(tn, 0, "doctorFullname").toString();
            element.html(val1);
        }
    }

    private static void setInspectionTagPrivateAttributeInfo(Element element, String data,
            String code, Carrier c) throws QException {
        String tn = CoreLabel.RESULT_SET;

        Carrier crpPA = new Carrier();
        crpPA.setValue("code", code);
        crpPA = CrModel.getPrivateAttributeList(crpPA);
        String fkPrivateSubmoduleAttributeId = crpPA.getValue("fkPrivateSubmoduleAttributeId").toString();

//                System.out.println("c XML" + c.toXML());
        Carrier cprIns = c.getKeyValuesPairFromTable(tn, "fkPrivateSubmoduleAttributeId", "finalValue");
        Carrier cprIns2 = c.getKeyValuesPairFromTable(tn, "fkPrivateSubmoduleAttributeId", "attributeName");
//        Carrier cprInsDoctor = c.getKeyValuesPairFromTable(tn, "attributeCode", "doctorFullname");
//         System.out.println("cprIns1 XML" + cprIns1.toXML());

        if (data.trim().equals("name") || data.trim().equals("")) {
            String val1 = cprIns2.getValue(fkPrivateSubmoduleAttributeId).toString();
            element.html(val1);
//                element.remove();
        } else if (data.trim().equals("inspection") || data.trim().equals("")) {
            String val = cprIns.getValue(fkPrivateSubmoduleAttributeId).toString();
            element.html(val);
//                element.remove();
        }
    }

    private static void setInspectionTagAttributeInfo(Element element, String data,
            String code, Carrier c) throws QException {
        String tn = CoreLabel.RESULT_SET;

//                System.out.println("c XML" + c.toXML());
        Carrier cprIns = c.getKeyValuesPairFromTable(tn, "attributeCode", "finalValue");
        Carrier cprIns2 = c.getKeyValuesPairFromTable(tn, "attributeCode", "attributeName");
        Carrier cprIns1 = c.getKeyValuesPairFromTable(tn, "attributeCode", "inspectionValue");
//        Carrier cprInsDoctor = c.getKeyValuesPairFromTable(tn, "attributeCode", "doctorFullname");
//         System.out.println("cprIns1 XML" + cprIns1.toXML());
        String val = cprIns.getValue(code).toString();

        if (data.trim().equals("name") || data.trim().equals("")) {
            String val1 = cprIns2.getValue(code).toString();

            element.html(val1);
//                element.remove();
        } else if (data.trim().equals("inspection") || data.trim().equals("")) {
            element.html(val);
//                element.remove();
        } else if (data.trim().equals("image")) {
            String h = element.hasAttr("height")
                    ? "height=\"" + element.attr("height") + "\""
                    : "";
            String w = element.hasAttr("width")
                    ? "width=\"" + element.attr("width") + "\""
                    : "";

            val = "<img src=\"" + Config.getDownloadPath()
                    + cprIns1.getValue(code).toString() + "\"" + " " + h + " " + w + ">";
            element.html(val);
//                element.remove();

        }
    }

    private static void setInspectionTagPatientInfo(Element element, String data, QReportCarrier rc) {
        switch (data) {
            case "fulname":
                element.html(rc.Patient.getName()
                        + " " + rc.Patient.getSurname()
                        + " " + rc.Patient.getMiddleName());
                break;
            case "name":
                element.html(rc.Patient.getName());
                break;
            case "surname":
                element.html(rc.Patient.getSurname());
                break;
            case "middleName":
                element.html(rc.Patient.getMiddleName());
                break;
            case "birthDate":
                element.html(getDateLine(rc.Patient.getBirthDate()));
                break;
            case "birthPlace":
                element.html(rc.Patient.getBirthPlace());
                break;
            case "sex":
                element.html(rc.Patient.getSex());
                break;
            case "occupation":
                element.html(rc.Patient.getOccupation());
                break;
            case "maritualStatus":
                element.html(rc.Patient.getMaritualStatus());
                break;
            case "education":
                element.html(rc.Patient.getEducation());
                break;
            case "bloodGroup":
                element.html(rc.Patient.getBloodGroup());
                break;
            case "rhFactor":
                element.html(rc.Patient.getRhFactor());
                break;
            case "mobile1":
                element.html(rc.Patient.getMobile1());
                break;
            case "mobile2":
                element.html(rc.Patient.getMobile2());
                break;
            case "telephone1":
                element.html(rc.Patient.getTelephone1());
                break;
            case "telephone2":
                element.html(rc.Patient.getTelephone2());
                break;
            case "email1":
                element.html(rc.Patient.getEmail1());
                break;
            case "email2":
                element.html(rc.Patient.getEmail2());
                break;
            case "country":
                element.html(rc.Patient.getCountry());
                break;
            case "city":
                element.html(rc.Patient.getCity());
                break;
            case "postIndex":
                element.html(rc.Patient.getPostIndex());
                break;
            case "description":
                element.html(rc.Patient.getDescription());
                break;
        }
    }

    private static Document doUserTag(Document doc) throws QException {
        String tn = CoreLabel.RESULT_SET;

        Carrier c = new Carrier();
        c.setValue("id", SessionManager.getCurrentUserId());
        c = CrModel.getUserList(c);

        EntityCrUserList ent = new EntityCrUserList();
        EntityManager.mapCarrierToEntity(c, tn, 0, ent);

        Elements elements = doc.getElementsByTag("quser");

        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";

            switch (data) {
                case "image":
//                    System.out.println(element.toString());
                    String h = element.hasAttr("height")
                            ? "height=\"" + element.attr("height") + "\""
                            : "";
                    String w = element.hasAttr("width")
                            ? "width=\"" + element.attr("width") + "\""
                            : "";

                    String val = "<img src=\"" + Config.getDownloadPath()
                            + ent.getUserImage() + "\"" + " " + h + " " + w + ">";
                    element.html(val);
                    break;
                case "name":
                    element.html(ent.getUserPersonName());
                    break;
                case "surname":
                    element.html(ent.getUserPersonSurname());
                    break;
                case "middlename":
                    element.html(ent.getUserPersonMiddlename());
                    break;
                case "birthDate":
                    element.html(ent.getUserBirthDate());
                    break;
                case "birthPlace":
                    element.html(ent.getUserBirthPlace());
                    break;
                case "sex":
                    element.html(ent.getSex());
                    break;
                case "mobile1":
                    element.html(ent.getMobile1());
                    break;
                case "mobile2":
                    element.html(ent.getMobile2());
                    break;
                case "telephone1":
                    element.html(ent.getTelephone1());
                    break;
                case "telephone2":
                    element.html(ent.getTelephone2());
                    break;
                case "email1":
                    element.html(ent.getEmail1());
                    break;
                case "email2":
                    element.html(ent.getEmail2());
                    break;
            }
//            element.remove();

        }
        return doc;
    }

    private static Document doPaymentTag(Document doc, QReportCarrier rc) throws QException {
        if (rc.getPaymentId().trim().length() == 0) {
            return doc;
        }

        String tn = CoreLabel.RESULT_SET;

        Carrier c = new Carrier();
        c.setValue("id", rc.getPaymentId());
        c = CrModel.getPaymentList(c);

        Elements elements = doc.getElementsByTag("qpayment");

        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";

            switch (data) {
                case "no":
                    element.html(c.getValue(tn, 0, "paymentNo").toString());
                    break;
                case "doctor":
                    element.html(c.getValue(tn, 0, "doctorFullname").toString());
                    break;
                case "patientId":
                    element.html(c.getValue(tn, 0, "patientId").toString());
                    break;
                case "patient":
                    String fname = c.getValue(tn, 0, "patientName").toString()
                            + " " + c.getValue(tn, 0, "patientSurname").toString()
                            + " " + c.getValue(tn, 0, "patientMiddleName").toString();
                    element.html(fname);
                    break;
                case "patientSex":
                    element.html(c.getValue(tn, 0, "sexName").toString());
                    break;
                case "date":
                    element.html(getDateLine(c.getValue(tn, 0, "paymentDate").toString()));
                    break;
                case "time":
                    element.html(getTimeLine(c.getValue(tn, 0, "paymentTime").toString()));
                    break;
                case "amount":
                    element.html(c.getValue(tn, 0, "paymentAmount").toString());
                    break;
                case "paymentType":
                    element.html(c.getValue(tn, 0, "paymentName").toString());
                    break;
                case "price":
                    element.html(c.getValue(tn, 0, "price").toString());
                    break;
                case "currency":
                    element.html(c.getValue(tn, 0, "currencyName").toString());
                    break;
                case "discount":
                    element.html(c.getValue(tn, 0, "paymentDiscount").toString());
                    break;
                case "description":
                    element.html(c.getValue(tn, 0, "description").toString());
                    break;
            }
//            element.remove();

        }
        return doc;
    }

    private static Document doCompanyTag(Document doc) throws QException {
        String tn = CoreLabel.RESULT_SET;

        Carrier c = new Carrier();
        c.setValue(EntityCrCompany.ID, SessionManager.getCurrentCompanyId());
        c = CrModel.getCompanyList(c);

        Elements elements = doc.getElementsByTag("qcompany");

        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";

            switch (data) {
                case "address":
                    element.html(c.getValue(tn, 0, EntityCrCompany.COMPANY_ADDRESS).toString());
                    break;
                case "currency":
                    element.html(c.getValue(tn, 0, EntityCrCompany.COMPANY_CURRENCY).toString());
                    break;
                case "name":
                    element.html(c.getValue(tn, 0, EntityCrCompany.COMPANY_NAME).toString());
                    break;
                case "logo":
                    String h = element.hasAttr("height")
                            ? "height=\"" + element.attr("height") + "\""
                            : "";
                    String w = element.hasAttr("width")
                            ? "width=\"" + element.attr("width") + "\""
                            : "width=\"150px\"";

                    String val = "<img src=\"" + Config.getDownloadPath()
                            + c.getValue(tn, 0, EntityCrCompany.LOGO_URL).toString()
                            + "\"" + " " + h + " " + w + ">";
                    element.html(val);
                    break;
            }
//            element.remove();

        }
        return doc;
    }

    private static Document doLangTag(Document doc) throws QException {

        Elements elements = doc.getElementsByTag("qlang");

        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";
            String v = data.trim().length() > 0
                    ? QUtility.getLabel(data) : "";
            element.html(v);

        }
        return doc;
    }

    private static Document doDateTag(Document doc) throws QException {

        Elements elements = doc.getElementsByTag("qdate");

        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";
            String qtype = element.hasAttr("type") ? element.attr("type") : "";

            switch (data) {
                case "date":
                    String v = "";
                    if (qtype.trim().equals("current")) {
                        v = QDate.getCurrentDate();
                        v = getDateLine(v);
                    }

                    element.html(v);
                    break;
                case "time":
                    v = "";
                    if (qtype.trim().equals("current")) {
                        v = QDate.getCurrentTime();
                        v = getTimeLine(v);
                    }
                    element.html(v);
                    break;

            }
//            element.remove();

        }
        return doc;
    }

    private static String getDateLine(String d) {
        return d.substring(6, 8) + "." + d.substring(4, 6) + "." + d.substring(0, 4);
    }

    private static String getTimeLine(String d) {
        return d.substring(0, 2) + ":" + d.substring(2, 4);
    }

    private static Document replaceReportStingWithTagsQp(Document doc,
            String fkSessionId) throws QException {

        EntityCrAppointment entAppt = new EntityCrAppointment();
        entAppt.setId(fkSessionId);
        EntityManager.select(entAppt);

        Carrier c = new Carrier();
        c.setValue("id", entAppt.getFkPatientId());
        c = CrModel.getPatientList(c);
        String[] cols = c.getTableColumnNames(CoreLabel.RESULT_SET);
//        EntityCrPatientList ent = new EntityCrPatientList();
//        ent.setId(entAppt.getFkPatientId());
//        ent.setFkOwnerUserId(SessionManager.getCurrentUserId());
//        Carrier c = EntityManager.select(ent);
//        EntityManager.mapCarrierToEntity(c,CoreLabel.RESULT_SET,0,ent);

        Elements elements = doc.getElementsByTag("qp");
        for (Element element : elements) {
            try {
                String k = element.getElementsByTag("span").get(0).html().trim();
                String v = "";
                if (ArrayUtils.contains(cols, k)) {
                    v = c.getValue(CoreLabel.RESULT_SET, 0, k).toString();
                    // Do some stuff.
                } else {
                    v = QUtility.getUndefinedLabel();
                }
                element.html(v);
            } catch (Exception e) {
            }
        }

        return doc;
    }

}
