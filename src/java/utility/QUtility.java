/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import module.cr.entity.EntityCrEntityLabel;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrListItemList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.sqlgenerator.EntityManager;

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
        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setLang(SessionManager.getCurrentLang());
        ent.setFieldName(arg.trim());
        Carrier c = EntityManager.select(ent);
        if (c.getTableRowCount(ent.toTableName()) > 0) {
            arg = c.getValue(
                    ent.toTableName(), 0, EntityCrEntityLabel.DESCRIPTION).toString();
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
        EntityManager.select(ent);
        return ent.getItemValue();
    }

    public static Carrier getListItem(String code, String itemValue4Search) throws QException {
        EntityCrListItem ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(code);
        ent.setItemValue(itemValue4Search);
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

    public static String checkLangLabel(File arg) throws QException, IOException {
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

    public static String checkLangLabel(Document doc) throws QException, IOException {
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
