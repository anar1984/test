/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import static module.cr.CrModel.getAttributeList4Cache;
import module.cr.entity.EntityCrEntityLabel;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrListItemList;
import module.cr.entity.EntityCrPermission;
import module.cr.entity.EntityCrRelCompanyAndRule;
import module.cr.entity.EntityCrRelRuleAndPermission;
import module.cr.entity.EntityCrRelUserAndRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.QLogger;

/**
 *
 * @author user
 */
public class QUtility {

    private static String SERVICE = "Service";
    private static String MODULE = "MD";

    public static String convertWSTitleToMethodFormat(String wsTitle) {
//  Veb servis standardı “Service” + MODULE(2 characters) + METHOD_NAME
//  “ServiceHrInsertNewEmployee”
        String t = wsTitle.substring(SERVICE.length() + MODULE.length());
        return t;
    }

    public static String getMethodNameFromWSTitle(String wsTitle) {
        String t = wsTitle.substring(SERVICE.length() + MODULE.length());
        t = t.substring(0, 1).toLowerCase() + t.substring(1);
        return t;
    }

    public static String getModuleNameFromWSTitle(String wsTitle) {
        String t = wsTitle.substring(SERVICE.length(), SERVICE.length()
                + MODULE.length()).toUpperCase();
        return t;
    }

    public static String fcLetter(String arg) {
        return arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length()).toLowerCase();
    }

    public static String convertDecimalToHex(Long num) {
        String str = "";
        char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        long rem;
        while (num > 0) {
            rem = num % 16;
            int y = (int) rem;
            str = hex[y] + str;
            num = num / 16;
        }
        return str;
    }

    public static String convertDecimalToHex(int num) {
        return convertDecimalToHex(num);
    }

    public static String getUndefinedLabel() throws QException {
        return getLabel("___undefined___");
    }

    public static String getLabel(String arg) throws QException {
        return getLabel(arg, SessionManager.getCurrentLang());
    }

    public static String getLabel(String arg, String lang) throws QException {
        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setDeepWhere(false);
        ent.setLang(lang);
        ent.setFieldName(arg.trim());
        Carrier c = EntityManager.select(ent);
        if (c.getTableRowCount(ent.toTableName()) > 0) {
            arg = c.getValue(
                    ent.toTableName(), 0, EntityCrEntityLabel.DESCRIPTION).toString();
        }
        return arg;
    }

    public static String getLabel(String arg, String[] params) throws QException {
        return getLabel(arg, SessionManager.getCurrentLang(), params);
    }

    public static String getLabel(String arg, String lang, String[] params) throws QException {
        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setDeepWhere(false);
        ent.setLang(lang);
        ent.setFieldName(arg.trim());
        Carrier c = EntityManager.select(ent);
        if (c.getTableRowCount(ent.toTableName()) > 0) {
            arg = c.getValue(
                    ent.toTableName(), 0, EntityCrEntityLabel.DESCRIPTION).toString();
        }

        for (int i = 0; i < params.length; i++) {
            arg.replace("@param" + i, params[i]);
        }
        return arg;
    }

    public static Carrier getListItem(String code) throws QException {
        EntityCrListItem ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(code);
        Carrier tc = EntityManager.select(ent).
                getKVFromTable(ent.toTableName(), "itemKey", "itemValue");
        return tc;
    }

    public static String getListItemValue(String code, String key) throws QException {
        EntityCrListItem ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(code);
        ent.setItemKey(key);
        ent.addSortBy(EntityCrListItemList.PARAM_1);
        ent.setSortByAsc(true);
        EntityManager.select(ent);
        return ent.getItemValue();
    }

    public static Carrier getListItem(String code, String itemValue4Search) throws QException {
        EntityCrListItem ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(code);
        ent.setItemValue(itemValue4Search);
        ent.addSortBy(EntityCrListItemList.PARAM_1);
        ent.setSortByAsc(true);
        ent.addDeepWhereStatementField("itemValue");
        Carrier tc = EntityManager.select(ent);

//        Carrier newC = new Carrier();
//        
//        Carrier tc = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_LISTITEM);
//        String tn = code + SessionManager.getCurrentLang();
//        int rc = tc.getTableRowCount(tn);
//
//        for (int i = 0; i < rc; i++) {
//            String itemKey = tc.getValue(tn,i,"itemKey").toString();
//            String itemValue = tc.getValue(tn,i,"itemValue").toString();
////            System.out.println("itemKey=>"+itemKey+"; itemvalue="+itemValue);
//            if (new DeepWhere(itemValue, itemValue4Search).isMatched()){
//                newC.setValue(itemKey, itemValue);
//            } 
//        }
        if (tc.getTableRowCount(ent.toTableName()) == 0) {
            ent.setLang("ENG");
            tc = EntityManager.select(ent);
        }

        tc = tc.getKVFromTable(ent.toTableName(), "itemKey", "itemValue");
        return tc;
    }

    public static void main(String[] arg) throws UnsupportedEncodingException, QException, IOException {
        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);
            SessionManager.setDomain(SessionManager.getCurrentThreadId(), "apd_23gemsb");

            String pagename = "page_patient";
            GeneralProperties prop = new GeneralProperties();
            String filename = prop.getWorkingDir() + "../page/" + pagename + ".html";
            String ln = "";
            File file = new File(filename);

            ln = QUtility.checkLangLabel(file);
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException ex1) {
            }
        }
    }

    public static String checkLangLabel(File arg) throws QException, IOException {
        Document doc = Jsoup.parse(arg, "UTF-8");
        
        return checkLangLabel(doc);

    }

    private static Document fillCombo(Document doc) throws QException {
        Carrier c = new Carrier();

        Elements elements = doc.getElementsByClass("apd-form-select-back");

        for (Element element : elements) {
            String url = element.hasAttr("srv_url") ? element.attr("srv_url") : "";
            String select_text = element.hasAttr("select_text") ? element.attr("select_text") : "";
            String select_value = element.hasAttr("select_value") ? element.attr("select_value") : "";
            String select_separator = element.hasAttr("select_separator") ? element.attr("select_separator") : " ";
//            String has_null = element.hasAttr("has_null") ? element.attr("has_null") : "";
            String send_data = element.hasAttr("send_data") ? element.attr("send_data") : "";
//            String selected_value = element.hasAttr("selected_value") ? element.attr("selected_value") : "";
//            String select_tn = element.hasAttr("select_tn") ? element.attr("select_tn") : "";
//            String ph = element.hasAttr("placeholder") ? element.attr("placeholder") : "";
            String has_other = element.hasAttr("has_other") ? element.attr("has_other") : "";
//            String has_all = element.hasAttr("has_all") ? element.attr("has_all") : "";

            String url_l = "";

            if (url.startsWith("li/") || url.startsWith("nali/")) {
                System.out.println("burdan kecibler");
                String itemCode = url.trim().split("/")[1];
                url_l = "serviceCrGetListItemByCode";
                c.setValue("itemCode", itemCode);
                c.setValue("asc", "itemValue");
            } else {
                String[] url_f = url.split("/");
                url_l = url_f.length > 0 ? url_f[url_f.length - 1] : url;
            }

            //set send_Data values
            if (send_data.trim().length() > 0) {
                String[] vs = send_data.split(",");
                for (String v : vs) {
                    String[] arr = v.split("=");
                    String key = arr[0];
                    String val = arr[1];
                    c.setValue(key, val);
                }
            }

            c = c.callService(url_l);

            String tn = CoreLabel.RESULT_SET;
            int rc = c.getTableRowCount(tn);
            String ln = element.hasAttr("has_null")
                    ? "<option value=''>" + " " + "</option>"
                    : "";
            for (int i = 0; i < rc; i++) {
                String val = c.getValue(tn, i, select_value).toString();
                String text = "";
                String[] vs1 = select_text.split(",");
                for (String v1 : vs1) {
                    text += c.getValue(tn, i, v1).toString() + select_separator;
                }
                ln += "<option value='" + val + "'>" + text + "</option>";
            }
            if (element.hasAttr("has_other")) {
                ln += "<option value='__2__'>" + getLabel("other") + "</option>";
            }
            element.append(ln);
        }

        return doc;
    }

    public static String checkLangLabel(Document doc) throws QException, IOException {
        doc = checkPermission(doc);
        Elements elements = doc.getElementsByAttribute("qlang");
        String langs = "";
        for (Element element : elements) {

            String val = element.hasAttr("data-content")
                    ? element.attr("data-content").trim() : element.html().trim();
            System.out.println(val);
            langs += val + CoreLabel.IN;

        }
//        System.out.println("langs->>>" + langs);

        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setFieldName(langs);
        ent.setLang(SessionManager.getCurrentLang());
        Carrier c = EntityManager.select(ent);

        c = c.getKeyValuesPairFromTable(ent.toTableName(),
                EntityCrEntityLabel.FIELD_NAME, EntityCrEntityLabel.DESCRIPTION);

        for (Element element : elements) {
            String val = element.hasAttr("data-content")
                    ? element.attr("data-content").trim() : element.html().trim();
            String nv = "";
            if (c.isKeyExist(val)){
                nv = c.getValue(val).toString();   
            }else{
                nv = val;
                QLogger.saveLabelLog(val);
            }

            if (element.hasAttr("data-content")) {
                element.attr("data-content", nv);
            } else {
                element.html(nv);
            }
        }
        doc = fillCombo(doc);
        return doc.toString();
    }

    public static Document checkPermission(Document doc) throws QException, IOException {
        Elements elements = doc.getElementsByAttribute("component_id");
        for (Element element : elements) {
            String comp_id = element.hasAttr("component_id")
                    ? element.attr("component_id").trim() : "";
            if (comp_id.trim().length() == 0) {
                continue;
            }

            if (!hasPermission(comp_id)){
              element.remove();
            }else{
                element.removeAttr("component_id");
            }
            
        }

        return doc;
    }

    public static boolean hasPermission(String componentId) throws QException {
        boolean hasPermission = true;

        EntityCrPermission entPermission = new EntityCrPermission();
        entPermission.setDeepWhere(false);
        entPermission.setPermissionString(componentId);
        entPermission.setStartLimit(0);
        entPermission.setEndLimit(0);
        EntityManager.select(entPermission);

        if (entPermission.getId().length() == 0) {
            return false;
        }

        EntityCrRelRuleAndPermission entRulePerm = new EntityCrRelRuleAndPermission();
        entRulePerm.setDeepWhere(false);
        entRulePerm.setFkPermissionId(entPermission.getId());
        Carrier crRulePerm = EntityManager.select(entRulePerm);

        if (crRulePerm.getTableRowCount(entRulePerm.toTableName()) == 0) {
            return false;
        }

        String rulePermIds = crRulePerm.getValueLine(entRulePerm.toTableName(),
                "fkRuleId");

        EntityCrRelCompanyAndRule ent = new EntityCrRelCompanyAndRule();
        ent.setDeepWhere(false);
        ent.setFkRuleId(rulePermIds);
        ent.setExpireDate(CoreLabel.GE + QDate.getCurrentDate());
        ent.setFkCompanyId(SessionManager.getCurrentCompanyId());
        Carrier crCompRule = EntityManager.select(ent);

        if (crCompRule.getTableRowCount(ent.toTableName()) == 0) {
            return false;
        }

        if (SessionManager.isCurrentUserCompanyAdmin()) {
            return true;
        } else {
            EntityCrRelUserAndRule entUsrRule = new EntityCrRelUserAndRule();
            entUsrRule.setDeepWhere(false);
            entUsrRule.setFkRuleId(rulePermIds);
            entUsrRule.setFkUserId(SessionManager.getCurrentUserId());
            Carrier crUsrRule = EntityManager.select(entUsrRule);
            if (crUsrRule.getTableRowCount(entUsrRule.toTableName())==0){
                return false;
            }
        }

        return hasPermission;
    }

    public static String checkLangLabel(String arg) throws QException, IOException {
        Document doc = Jsoup.parse(arg, "UTF-8");

        Elements elements = doc.getElementsByAttribute("qlang");
        String langs = "";
        for (Element element : elements) {
            String val = element.html().trim();
            langs += val + CoreLabel.IN;
        }

        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setFieldName(langs);
        ent.setLang(SessionManager.getCurrentLang());
        Carrier c = EntityManager.select(ent);

        c = c.getKeyValuesPairFromTable(ent.toTableName(),
                EntityCrEntityLabel.FIELD_NAME, EntityCrEntityLabel.DESCRIPTION);

        for (Element element : elements) {
            String val = element.html().trim();
            String nv = c.isKeyExist(val) ? c.getValue(val).toString() : val;
            element.html(nv);
        }

        return doc.toString();
    }

}
