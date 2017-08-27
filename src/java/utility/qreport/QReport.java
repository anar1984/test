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
import module.cr.entity.EntityCrReportLine;
import module.pg.PgModel;
import org.apache.commons.lang.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import resources.config.Config;
import utility.CacheUtil;
import utility.Carrier;
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
            rc.setReportId("201708090638410249");
            rc.setSessionId("201708221717120136");
            rc.Patient.setPatientInfoById("201708221705210595");

            String st = getReport(rc);
            System.out.println(st);

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
        txt = txt.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        Document doc = Jsoup.parse(txt, "UTF-8");

        doc = doPatientTag(doc, rc);
        doc = doAttributeTag(doc, rc);
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
            switch (data) {
                case "name":
                    element.after(rc.Patient.getName());
                    break;
                case "surname":
                    element.after(rc.Patient.getSurname());
                    break;
                case "middleName":
                    element.after(rc.Patient.getMiddleName());
                    break;
                case "birthDate":
                    element.after(rc.Patient.getBirthDate());
                    break;
                case "birthPlace":
                    element.after(rc.Patient.getBirthPlace());
                    break;
                case "sex":
                    element.after(rc.Patient.getSex());
                    break;
                case "occupation":
                    element.after(rc.Patient.getOccupation());
                    break;
                case "maritualStatus":
                    element.after(rc.Patient.getMaritualStatus());
                    break;
                case "education":
                    element.after(rc.Patient.getEducation());
                    break;
                case "bloodGroup":
                    element.after(rc.Patient.getBloodGroup());
                    break;
                case "rhFactor":
                    element.after(rc.Patient.getRhFactor());
                    break;
                case "mobile1":
                    element.after(rc.Patient.getMobile1());
                    break;
                case "mobile2":
                    element.after(rc.Patient.getMobile2());
                    break;
                case "telephone1":
                    element.after(rc.Patient.getTelephone1());
                    break;
                case "telephone2":
                    element.after(rc.Patient.getTelephone2());
                    break;
                case "email1":
                    element.after(rc.Patient.getEmail1());
                    break;
                case "email2":
                    element.after(rc.Patient.getEmail2());
                    break;
                case "country":
                    element.after(rc.Patient.getCountry());
                    break;
                case "city":
                    element.after(rc.Patient.getCity());
                    break;
                case "postIndex":
                    element.after(rc.Patient.getPostIndex());
                    break;
                case "description":
                    element.after(rc.Patient.getDescription());
                    break;
            }
            element.remove();
        }

        return doc;
    }

    /*
    name,description,code
     */
    private static Document doAttributeTag(Document doc, QReportCarrier rcarrier) throws QException {
        String fkSessionId = rcarrier.getSessionId();
        String tn = CoreLabel.RESULT_SET;

        Carrier c = new Carrier();
        c.setValue("inspectionCode", fkSessionId);
        c = CrModel.getInspectionList(c);
        Carrier cprIns = c.getKeyValuesPairFromTable(tn, "attibuteCode", "finalValue");

        Elements elements = doc.getElementsByTag("qpatient");
        for (Element element : elements) {
            String data = element.hasAttr("data") ? element.attr("data") : "";
            String code = element.hasAttr("code") ? element.attr("code") : "";
            String val = cprIns.getValue(code).toString();

            if (data.trim().equals("name") || data.trim().equals("")) {
                element.after(val);
                element.remove();
            } else if (data.trim().equals("image")) {
                String h = element.hasAttr("height") 
                        ? "height=\""+element.attr("height")+"\"" 
                        : "";
                String w = element.hasAttr("weight") 
                        ? "weight=\""+element.attr("weight")+"\""
                        : "";
                
                val = "<img src=\"" + Config.getDownloadPath() + val + "\""+ " "+h+" "+w+">";
                element.after(val);
                element.remove();

            }

        }


        return doc;
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
