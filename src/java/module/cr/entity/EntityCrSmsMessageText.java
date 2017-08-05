/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrSmsMessageText extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String MSG_CODE = "msgCode";
    public static String MSG_TEXT = "msgText";
    public static String LANG = "lang";

    private String msgCode = "";
    private String msgText = "";
    private String lang;

    public EntityCrSmsMessageText() {
        this.lang = "AZE";
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
