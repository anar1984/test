/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.cr.entity.EntityCrEntityLabel;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrListItemList;
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

   public static String convertDecimalToHex(Long num){
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
    
    public static String convertDecimalToHex(int num){
         return convertDecimalToHex(num);
    }
    
    public static String getUndefinedLabel() throws QException{
        return getLabel("___undefined___");
    }
    public static String getLabel(String arg) throws QException{
        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setLang(SessionManager.getCurrentLang());
        ent.setFieldName(arg.trim());
        Carrier c = EntityManager.select(ent);
        if (c.getTableRowCount(ent.toTableName())>0){
            arg = c.getValue(
                    ent.toTableName(),0,EntityCrEntityLabel.DESCRIPTION).toString();
        }
        return arg;
    }

    public static Carrier getListItem(String code) throws QException{
        EntityCrListItem  ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(code);
        Carrier tc = EntityManager.select(ent).
                getKVFromTable(ent.toTableName(), "itemKey","itemValue");
        return tc;
    }
    
    public static Carrier getListItem(String code, String itemValue) throws QException{
        EntityCrListItem  ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemValue(itemValue);
        ent.addDeepWhereStatementField("itemValue");
        ent.setItemCode(code);
        Carrier tc = EntityManager.select(ent).
                getKVFromTable(ent.toTableName(), "itemKey","itemValue");
        return tc;
    }
}
