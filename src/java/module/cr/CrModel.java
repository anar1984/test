/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr;

import auth.SessionHandler;
import controllerpool.ControllerPool;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import job.CompanyJob;
import label.CoreLabel;
import module.cr.entity.*;
import module.pg.PgModel;
import resources.config.Config;
//import smssender.Config;
import utility.CacheUtil;
import utility.Carrier;
import utility.DeepWhere;
import utility.GeneralProperties;
import utility.MailSender;
import utility.QDate;
import utility.QException;
import utility.QUtility;
import utility.SessionManager;
import utility.WhereSingle;
import utility.qreport.QReport;
import utility.qreport.QReportCarrier;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.IdGenerator;
import utility.sqlgenerator.QLogger;
import utility.sqlgenerator.SQLGenerator;

/**
 *
 * @author user
 */
public class CrModel {

    private static final String USER_CONTROLLER_TYPE_ENUB = "2";
    private static final String USER_CONTROLLER_TYPE_COMPONENT = "1";

    private static final String USER_TYPE_ADMIN = "A";
    private static final String USER_TYPE_DOCTOR = "D";
    private static final String USER_TYPE_ADMIN_AND_DOCTOR = "AD";

    private static final String USER_RULE_TYPE_BY_RULE = "rule";
    private static final String USER_RULE_TYPE_BY_ALL = "all";
    private static final String USER_RULE_TYPE_BY_OWN = "ownrule";
    private static final String USER_RULE_TYPE_BY_COMPONENT = "comprule";

    private static final String LOAD_PAGE_BEGINNER_PAGE = "page_";
    private static final String LOAD_PAGE_BEGINNER_GENERATOR_MODULE = "page_module_";
    private static final String LOAD_PAGE_BEGINNER_PAGE_DINAMIC = "page_dn_";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE = "sa_";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE_HA = "ha_";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE_HA_CODE = "__2__";
    private static final String APPOINTMENT_STATUS_ACTIVE = "3";
    private static final String APPOINTMENT_STATUS_PASSIVE = "6";
    private static final String APPOINTMENT_STATUS_FINISHED = "4";
    private static final String APPOINTMENT_STATUS_IN_PROCESS = "1";
    private static final String APPOINTMENT_STATUS_IN_QUEUE = "2";
    private static final String APPOINTMENT_STATUS_CANCELED = "5";

    private static final String LANG_TYPE_MODULE = "MD";
    private static final String LANG_TYPE_SUBMODULE = "SM";
    private static final String LANG_TYPE_SUBMODULE_ATTR = "SA";
    private static final String LANG_TYPE_PRIVATE_SUBMODULE_ATTR = "PSA";
    private static final String LANG_TYPE_ATTRIBUTE = "ATR";
    private static final String LANG_TYPE_VALUE_TYPE = "VT";

    private static final String LANG_FIELD_NAME = "NM";
    private static final String LANG_FIELD_DESC = "DSC";
    private static final String LANG_FIELD_VALUE = "VAL";

    public static final String VALUE_TYPE_INTEGER = "13";
    public static final String VALUE_TYPE_FLOAT = "7";
    public static final String VALUE_TYPE_POSITIVE_INTEGER = "11";
    public static final String VALUE_TYPE_POSITIVE_FLOAT = "6";
    public static final String VALUE_TYPE_TEXTAREA = "14";
    public static final String VALUE_TYPE_STRING = "12";
    public static final String VALUE_TYPE_RANGE_INTEGER = "10";
    public static final String VALUE_TYPE_RANGE_STRING = "9";
    public static final String VALUE_TYPE_RANGE_STRING_MULTI = "8";
    public static final String VALUE_TYPE_MIN_MAX_INTEGER = "5";
    public static final String VALUE_TYPE_MIN_MAX_FLOAT = "4";
    public static final String VALUE_TYPE_PICTURE = "3";
    public static final String VALUE_TYPE_PICTURE_URL = "16";
    public static final String VALUE_TYPE_VIDEO_UPLOAD = "2";
    public static final String VALUE_TYPE_VIDEO_URL = "1";
    public static final String VALUE_TYPE_RANGE_INTEGER_MULTI = "15";
    public static final String VALUE_TYPE_YOUTUBE_URL = "17";
    public static final String VALUE_TYPE_SOUND_UPLOAD = "19";

    public static final String REPORT_TYPE_APPOINTMENT = "1";
    public static final String REPORT_TYPE_PAYMENT = "2";

    public static final String PAYMENT_TYPE_OWNER_COMPANY = "C";
    public static final String PAYMENT_TYPE_OWNER_PERSONAL = "P";

    public static final String PAYMENT_STATUS_IS_PAID = "P";
    public static final String PAYMENT_STATUS_IS_NOT_PAID = "NP";
    public static final String[] ABC = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "V", "U", "W", "X", "Y", "Z"};
    public static final String[] ABC_ = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "v", "u", "w", "x", "y", "z"};
    public static final String[] NUMBER = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    public static final String[] PASSWORD_CHAR = new String[]{"!", "@", "#", "$", "^", "&", "*", "(", ")"};
    public static final String[] EMAIL_CHAR = new String[]{"@", "."};

    public static final String mandatoryRulesForCompanies = "mandatoryRulesForCompanies";

    public static Carrier getPage(Carrier carrier) throws QException {
        String page = carrier.getValue("page").toString();
        String ln = "";
        if (page.startsWith(LOAD_PAGE_BEGINNER_PAGE_DINAMIC)) {
            String pageid = page.split("_")[2];
            ln = PgModel.genPage(pageid);
        } else if (page.startsWith(LOAD_PAGE_BEGINNER_PAGE)) {
            ln = getStaticHtmlPageBody(page);
        }
        carrier.setValue("body", ln);
        return carrier;
    }

    public static Carrier genSubmoduleButtonList(Carrier carrier) throws QException {
        String fkModuleId = carrier.getValue("fkModuleId").toString();
        String fkSessionId = carrier.isKeyExist("fkSessionId")
                ? carrier.getValue("fkSessionId").toString() : "";
        String ln = PgModel.genSubmodule(fkModuleId, fkSessionId);
        carrier.setValue("body", ln);
        return carrier;
    }

    private static String getStaticHtmlPageBody(String pagename) throws QException {
        try {
            QLogger.savePageLog(pagename);

            GeneralProperties prop = new GeneralProperties();
            String filename = prop.getWorkingDir() + "../page/" + pagename + ".html";
            String ln = "";
            File file = new File(filename);

            ln = QUtility.checkLangLabel(file);

            return ln;
        } catch (QException | IOException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    /*private static String checkLangLabel(File arg) throws QException, IOException {
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
    }*/
    public static Carrier insertNewListItem(Carrier carrier) throws QException {
        try {
            EntityCrListItem ent = new EntityCrListItem();
            EntityManager.mapCarrierToEntity(carrier, ent);
            String st = ent.getLang().trim().equals("")
                    ? SessionManager.getCurrentLang()
                    : ent.getLang().trim();
            ent.setLang(st);
            EntityManager.insert(ent);
            carrier.setValue("id", ent.getId());
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier updateListItem(Carrier carrier) throws QException {
        try {
            EntityCrListItem entity = new EntityCrListItem();
            entity.setId(carrier.getValue(EntityCrListItem.ID).toString());
            EntityManager.select(entity);
            EntityManager.mapCarrierToEntity(carrier, entity, false);
            EntityManager.update(entity);
            carrier = EntityManager.select(entity);

            carrier.renameTableName(entity.toTableName(), CoreLabel.RESULT_SET);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return carrier;
    }

    public static Carrier deleteListItem(Carrier carrier) throws QException {
        try {
            EntityCrListItem entity = new EntityCrListItem();
            entity.setId(carrier.getValue(EntityCrListItem.ID).toString());
            EntityManager.delete(entity);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return carrier;
    }

    public Carrier getListItemList4ComboNali(Carrier carrier) throws QException {

        EntityCrListItemList ent = new EntityCrListItemList();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(carrier.getValue("itemCode").toString());
        ent.addSortBy(EntityCrListItemList.PARAM_1);
        ent.setSortByAsc(true);
        carrier = EntityManager.select(ent);
        
        if (carrier.getTableRowCount(ent.toTableName())==0){
            ent.setLang(SessionManager.DEFAULT_LANG);
            carrier = EntityManager.select(ent);
        }

        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

        return carrier;
    }
    
    public static Carrier getListItemByCode(Carrier carrier) throws QException {

        EntityCrListItemList ent = new EntityCrListItemList();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setItemCode(carrier.getValue("itemCode").toString());
        ent.addSortBy(EntityCrListItemList.PARAM_1);
        ent.setSortByAsc(true);
        carrier = EntityManager.select(ent);
        
        if (carrier.getTableRowCount(ent.toTableName())==0){
            ent.setLang(SessionManager.DEFAULT_LANG);
            carrier = EntityManager.select(ent);
        }

        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

        return carrier;
    }


    public static Carrier getListItemList(Carrier carrier) throws QException {

        try {
            EntityCrListItemList ent = new EntityCrListItemList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrListItemList.ITEM_CODE_NAME);
            ent.addAndStatementField(EntityCrListItemList.ITEM_VALUE);
            ent.setLang(SessionManager.getCurrentLang());
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.addSortBy(EntityCrListItemList.PARAM_1);
            ent.setSortByAsc(true);
            carrier = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            carrier.addTableRowCount(CoreLabel.RESULT_SET,
                    EntityManager.getRowCount(ent));
            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getListItemList"));
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return carrier;
    }

    public static Carrier getListItemByCodeOld(Carrier carrier) throws QException {
        Carrier resultCarrier = new Carrier();
        Carrier crResult = new Carrier();
        try {
//            long tm = System.currentTimeMillis();
            resultCarrier = CacheUtil.getFromCache(
                    CacheUtil.CACHE_KEY_LISTITEM);

//                System.out.println("+++++++++++++++++++++");
//            System.out.println("resultCarrier json >> " + resultCarrier.getJson());
//            System.out.println("+++++++++++++++++++++");
            String tn = carrier.getValue("itemCode").toString()
                    + SessionManager.getCurrentLang();
            int rc = resultCarrier.getTableRowCount(tn);

            for (int i = 0; i < rc; i++) {
                crResult.setValue(CoreLabel.RESULT_SET, i, "itemKey",
                        resultCarrier.getValue(tn, i, "itemKey"));
                crResult.setValue(CoreLabel.RESULT_SET, i, "itemValue",
                        resultCarrier.getValue(tn, i, "itemValue"));
            }

//            System.out.println("+++++++++++++++++++++");
//            System.out.println("crResult json >> " + crResult.getJson());
//            System.out.println("+++++++++++++++++++++");
            //carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            //carrier.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
            //carrier.addTableSequence(CoreLabel.RESULT_SET, EntityManager.getListSequenceByKey("getListItemList"));
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return crResult;
    }

    public static Carrier getListItemMainList(Carrier carrier) throws QException {
        try {
            EntityCrListItemList ent = new EntityCrListItemList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrListItemList.ITEM_CODE_NAME);
            ent.addAndStatementField(EntityCrListItemList.ITEM_VALUE);
            ent.addAndStatementField(EntityCrListItemList.LANGUAGE_NAME);
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.addSortBy(EntityCrListItemList.PARAM_1);
            ent.setSortByAsc(true);
            carrier = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            carrier.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
            carrier.addTableSequence(CoreLabel.RESULT_SET, EntityManager.getListSequenceByKey("getListItemList"));

            String tn = CoreLabel.RESULT_SET;
            int rc = carrier.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                String newTN = carrier.getValue(tn, i, "itemCode").toString()
                        + carrier.getValue(tn, i, "lang").toString();
                int rc1 = carrier.getTableRowCount(newTN);
                carrier.setValue(newTN, rc1, "itemKey", carrier.getValue(tn, i, "itemKey"));
                carrier.setValue(newTN, rc1, "itemValue", carrier.getValue(tn, i, "itemValue"));
            }
            // carrier = carrier.getKeyValuesPairFromTable(CoreLabel.RESULT_SET,
            //         new String[]{"itemCode","lang"}, 
            //         new String[]{"itemValue"})

        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return carrier;
    }

    public static Carrier insertNewUser(Carrier carrier) throws QException {
        try {
            EntityCrUser ent = new EntityCrUser();
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.setUserShortId(IdGenerator.getId());
            EntityManager.insert(ent);

            //set mandatory rules
            EntityCrRelUserAndRule entUserRule1 = new EntityCrRelUserAndRule();
            entUserRule1.setFkUserId(ent.getId());
            entUserRule1.setFkRuleId(getRuleIdByName(mandatoryRulesForCompanies));
            EntityManager.insert(entUserRule1);

            String tn = "permission";
            int rc = carrier.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                EntityCrRelUserAndRule entUserRule = new EntityCrRelUserAndRule();
                entUserRule.setFkUserId(ent.getId());
                entUserRule.setFkRuleId(carrier.getValue(tn, i, "fkRuleId").toString());
                EntityManager.insert(entUserRule);
            }

            return new Carrier();
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String getRuleIdByName(String arg) throws QException {
        EntityCrRule ent = new EntityCrRule();
        ent.setRuleName(arg);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);
        return ent.getId();
    }

    public static Carrier updateUser(Carrier carrier) throws QException {
        try {
            String oldUsername = "";
            String newUsername = carrier.getValue("username").toString();

            carrier.removeKey("password");
            EntityCrUser ent = new EntityCrUser();
            ent.setId(carrier.getValue(EntityCrPerson.ID).toString());
            EntityManager.select(ent);
            oldUsername = ent.getUsername();
            EntityManager.mapCarrierToEntity(carrier, ent, false);
            EntityManager.update(ent);

            updateCompanyUsername(oldUsername, newUsername);
            addPermissionsToUserOnUpdate(carrier, ent.getId());

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static void updateCompanyUsername(String oldUsername, String newUsername)
            throws QException {

        if (oldUsername.trim().length() == 0 || newUsername.trim().length() == 0) {
            return;
        }

        EntityCrCompany ent = new EntityCrCompany();
        ent.setId(SessionManager.getCurrentCompanyId());
        ent.setPersonUsername(oldUsername);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        Carrier c = EntityManager.select(ent);

        if (c.getTableRowCount(ent.toTableName()) > 0) {
            ent.setPersonUsername(newUsername);
            EntityManager.update(ent);
        }
    }

    private static void addPermissionsToUserOnUpdate(Carrier carrier, String fkUserId)
            throws QException {
        EntityCrRelUserAndRule entUserRule = new EntityCrRelUserAndRule();
        entUserRule.setDeepWhere(false);
        entUserRule.setFkUserId(fkUserId);
        Carrier cUserRule = EntityManager.select(entUserRule);

        int rc1 = cUserRule.getTableRowCount(entUserRule.toTableName());
        for (int i = 0; i < rc1; i++) {
            entUserRule = new EntityCrRelUserAndRule();
            entUserRule.setId(cUserRule.getValue(entUserRule.toTableName(), i, "id").toString());
            EntityManager.delete(entUserRule);
        }

        //EntityManager.delete(entUserRule);
        //set mandatory rules
        entUserRule = new EntityCrRelUserAndRule();
        entUserRule.setFkUserId(fkUserId);
        entUserRule.setFkRuleId(getRuleIdByName(mandatoryRulesForCompanies));
        EntityManager.insert(entUserRule);

        String tn = "permission";
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
//                System.out.println("id" + (i + 1) + carrier.getValue(tn, i, "ruleId").toString());
            entUserRule = new EntityCrRelUserAndRule();
            entUserRule.setFkUserId(fkUserId);
            entUserRule.setFkRuleId(carrier.getValue(tn, i, "fkRuleId").toString());
            EntityManager.insert(entUserRule);
        }
    }

    public static Carrier deleteUser(Carrier carrier) throws QException {

        try {
            EntityCrUser ent = new EntityCrUser();
            ent.setId(carrier.getValue(EntityCrPerson.ID).toString());
            EntityManager.delete(ent);
            carrier = EntityManager.select(ent);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getUserList(Carrier carrier) throws QException {
        try {
            EntityCrUserList ent = new EntityCrUserList();
            ent.setDeepWhere(false);
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");
            String entTN = ent.toTableName();

            String compIds = carrier.getValueLine(entTN, "fkCompanyId");

            EntityCrCompany entComp = new EntityCrCompany();
            entComp.setId(compIds);
            entComp.setDeepWhere(false);
            entComp.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
            Carrier crComp = EntityManager.select(entComp);

            String compTN = entComp.toTableName();
            Carrier cprComp = crComp.getKVFromTable(compTN, "id", "activationId");

            carrier.mergeCarrier(entTN, "fkCompanyId", "activationId", cprComp);

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getUserList"));
            carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public static Carrier getUserList4Combo(Carrier carrier) throws QException {
        try {
            String username = carrier.isKeyExist(EntityCrUser.USERNAME) ? carrier.getValue(EntityCrUser.USERNAME).toString() : "";
            Carrier c = new Carrier();
            c.addIncludedFields(EntityCrUser.ID);
            c.addIncludedFields(EntityCrUser.USERNAME);
            c.setValue(EntityCrUser.USERNAME, username);
            c.setValue("asc", EntityCrUser.USERNAME);
            carrier = c.callService("serviceCrGetUserList");
            int row = carrier.getTableRowCount(CoreLabel.RESULT_SET);
            carrier.setValue(CoreLabel.RESULT_SET, row, EntityCrUser.ID, "");
            carrier.setValue(CoreLabel.RESULT_SET, row, EntityCrUser.USERNAME, CoreLabel.NONE_DOT_LINES);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    /*private static void addSequence4UserList(Carrier carrier) throws QException {
        try {
            ListSequenceConfigurationProperties prop = new ListSequenceConfigurationProperties();
            String fields = prop.getProperty("getUserList");

            String[] fieldsArr = fields.split(",");
            carrier.addSequence(fieldsArr);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }
    }*/
    public static Carrier getCurrentDateAndTime(Carrier carrier) throws QException {
        try {
            carrier.setValue(CoreLabel.RESULT_SET, 0, "date", QDate.convertToDateString(QDate.getCurrentDate()));
            carrier.setValue(CoreLabel.RESULT_SET, 0, "time", QDate.convertToTimeString(QDate.getCurrentTime()));
            carrier.setValue(CoreLabel.RESULT_SET, 0, "dateStandard", QDate.getCurrentDate());
            carrier.setValue(CoreLabel.RESULT_SET, 0, "timeStandard", QDate.getCurrentTime());
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewUserController(Carrier carrier) throws QException {
        try {
            String[] liComponentIds = carrier.getValue(EntityCrUserController.FK_COMPONENT_ID).toString().trim().split(CoreLabel.SEPERATOR_VERTICAL_LINE);
            for (String compId : liComponentIds) {
                if (compId.trim().length() != 0) {
                    EntityCrUserController ent = new EntityCrUserController();
                    ent.setFkUserId(carrier.getValue(EntityCrUserController.FK_USER_ID).toString());
                    ent.setComponentType(carrier.getValue("liComponentCode").toString());
                    ent.setFkComponentId(compId);
                    Carrier c = EntityManager.select(ent);

                    if (c.getTableRowCount(ent.toTableName()) == 0) {
                        ent.setControllerType(USER_CONTROLLER_TYPE_COMPONENT);
                        ent.setComponentType(carrier.getValue(EntityCrRelRuleAndComponent.LI_COMPONENT_CODE).toString());
                        ent.setInputKey(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_KEY).toString());
                        ent.setInputValue(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_VALUE).toString());
                        ent.setPermissionType(carrier.getValue(EntityCrRelRuleAndComponent.PERMISSION_TYPE).toString());
                        EntityManager.insert(ent);
                    }
                }

            }
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewUserControllerByRule(Carrier carrier) throws QException {
        try {
            String userId = carrier.getValue(EntityCrUserController.FK_USER_ID).toString();
            String[] ruleKeys = carrier.getValue(EntityCrRelRuleAndComponent.LI_RULE_KEY).toString().trim().split(CoreLabel.SEPERATOR_VERTICAL_LINE);

            for (String ruleKey : ruleKeys) {
                if (!ruleKey.trim().equals("")) {
                    EntityCrRelRuleAndComponentList relEnt = new EntityCrRelRuleAndComponentList();
                    relEnt.setLiRuleKey(ruleKey);
                    Carrier tc = EntityManager.select(relEnt);
                    String tablename = relEnt.toTableName();
                    int c = tc.getTableRowCount(relEnt.toTableName());
                    for (int i = 0; i < c; i++) {
                        String permissionType = tc.getValue(tablename, i, EntityCrRelRuleAndComponentList.PERMISSION_TYPE).toString();
                        String inputKey = tc.getValue(tablename, i, EntityCrRelRuleAndComponentList.INPUT_KEY).toString();
                        String inputValue = tc.getValue(tablename, i, EntityCrRelRuleAndComponentList.INPUT_VALUE).toString();
                        String compCode = tc.getValue(tablename, i, EntityCrRelRuleAndComponentList.LI_COMPONENT_CODE).toString();
                        String compKey = tc.getValue(tablename, i, EntityCrRelRuleAndComponentList.LI_COMPONENT_KEY).toString();

                        EntityCrUserController ucEntity = new EntityCrUserController();
                        ucEntity.setComponentType(compCode);
                        ucEntity.setFkComponentId(compKey);
                        ucEntity.setFkUserId(userId);
                        Carrier ct = EntityManager.select(ucEntity);

                        if (ct.getTableRowCount(ucEntity.toTableName()) == 0) {
                            ucEntity.setInputKey(inputKey);
                            ucEntity.setInputValue(inputValue);
                            ucEntity.setPermissionType(permissionType);
                            EntityManager.insert(ucEntity);
                        }

                    }
                }
            }

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewUserControllerByEnum(Carrier carrier) throws QException {
        String[] liComponentIds = carrier.getValue(
                EntityCrUserController.FK_COMPONENT_ID).toString().trim().split(
                        CoreLabel.SEPERATOR_VERTICAL_LINE);

        for (String compId : liComponentIds) {
            if (compId.trim().length() != 0) {
                EntityCrUserController ent = new EntityCrUserController();
                ent.setFkUserId(carrier.getValue(EntityCrUserController.FK_USER_ID).toString());
                ent.setComponentType(carrier.getValue("liComponentCode").toString());
                ent.setFkComponentId(compId);
                Carrier c = EntityManager.select(ent);

                if (c.getTableRowCount(ent.toTableName()) == 0) {
                    ent.setControllerType(USER_CONTROLLER_TYPE_ENUB);
                    ent.setInputKey(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_KEY).toString());
                    ent.setInputValue(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_VALUE).toString());
                    ent.setPermissionType(carrier.getValue(EntityCrRelRuleAndComponent.PERMISSION_TYPE).toString());
                    EntityManager.insert(ent);
                }
            }

        }
        return carrier;
    }

    public static Carrier getUserControllerList(Carrier carrier) throws QException {
        try {
            EntityCrUserControllerList entity = new EntityCrUserControllerList();
            entity.setDeepWhere(false);
            EntityManager.mapCarrierToEntity(carrier, entity);
            carrier = EntityManager.select(entity);
            carrier.renameTableName(entity.toTableName(), CoreLabel.RESULT_SET);
            EntityManager.addSequence(carrier, "getUserControllerList");
            carrier.setValue("rowCount", EntityManager.getRowCount(entity));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier updateUserController(Carrier carrier) throws QException {
        try {
            EntityCrUserController entity = new EntityCrUserController();
            entity.setId(carrier.getValue(EntityCrUserController.ID).toString().trim());
            EntityManager.select(entity);
            EntityManager.mapCarrierToEntity(carrier, entity, false);
            entity.setComponentType(carrier.getValue("liComponentCode").toString());
            EntityManager.update(entity);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier deleteUserController(Carrier carrier) throws QException {
        try {
            EntityCrUserController entity = new EntityCrUserController();
            entity.setId(carrier.getValue(EntityCrUserController.ID).toString().trim());
            EntityManager.delete(entity);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewEntityLabel(Carrier carrier) throws QException {
        try {
            EntityCrEntityLabel ent = new EntityCrEntityLabel();
            ent.setDeepWhere(false);
            ent.setFieldName(carrier.getValue(EntityCrEntityLabel.FIELD_NAME).toString());
            ent.setLang(carrier.getValue("lang").toString());
            int rc = EntityManager.select(ent).getTableRowCount(ent.toTableName());
            if (rc > 0) {
                carrier.addController(EntityCrEntityLabel.FIELD_NAME,
                        EntityManager.getMessageText("valueIsAvailable"));
                return carrier;
            }
            EntityManager.mapCarrierToEntity(carrier, ent);
            EntityManager.insert(ent);
            carrier.setValue(EntityCrPerson.ID, ent.getId());
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier updateEntityLabel(Carrier carrier) throws QException {
        try {
            EntityCrEntityLabel ent = new EntityCrEntityLabel();
            ent.setId(carrier.getValue(EntityCrPerson.ID).toString());
            EntityManager.select(ent);
            EntityManager.mapCarrierToEntity(carrier, ent, false);
            EntityManager.update(ent);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier deleteEntityLabel(Carrier carrier) throws QException {

        try {
            EntityCrEntityLabel ent = new EntityCrEntityLabel();
            ent.setId(carrier.getValue(EntityCrPerson.ID).toString());
            EntityManager.delete(ent);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getEntityLabelList(Carrier carrier) throws QException {
        try {
            EntityCrEntityLabelList ent = new EntityCrEntityLabelList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrEntityLabelList.FIELD_NAME);
            ent.addAndStatementField(EntityCrEntityLabelList.DESCRIPTION);
            EntityManager.mapCarrierToEntity(carrier, ent);
            Carrier c = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            c.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getEntityLabelList"));

            EntityManager.mapCarrierToEntity(carrier, ent);
            c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getEntityLabelList4Cache(Carrier carrier) throws QException {
        try {
            EntityCrEntityLabelList ent = new EntityCrEntityLabelList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrEntityLabelList.FIELD_NAME);
            ent.addAndStatementField(EntityCrEntityLabelList.DESCRIPTION);
            Carrier c = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            String tn = CoreLabel.RESULT_SET;
            int rc = c.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                String newTN = c.getValue(tn, i, "fieldName").toString()
                        + c.getValue(tn, i, "lang").toString();
                String desc = c.getValue(tn, i, "description").toString();
                String labelType = c.getValue(tn, i, "labelType").toString();
//                System.out.println("------------------------------------------");
//                System.out.println("newTN=" + newTN + "; description=" + desc+ "; labelType=" + labelType);
//                System.out.println("-------------------------------------------");
                c.setValue(newTN, 0, "labelType", labelType);
                c.setValue(newTN, 0, "description", desc);
            }

            //get attribute name  
            Carrier cAttr = new Carrier();
            cAttr = CrModel.getAttributeMainList(cAttr);
//            System.out.println("xml->"+cAttr.toXML());
            String tn1 = CoreLabel.RESULT_SET;
            int rc1 = cAttr.getTableRowCount(tn1);
            for (int i = 0; i < rc1; i++) {
                String col = cAttr.getValue(tn1, i, "attributeCode").toString();
                String lang = cAttr.getValue(tn1, i, "lang").toString();
                String desc = cAttr.getValue(tn1, i, "attributeName").toString();
                String newTN = col + lang;
//                System.out.println("newTN=" + newTN + "; description=" + desc);
                c.setValue(newTN, 0, "labelType", "INTEGER");
                c.setValue(newTN, 0, "description", desc);
            }
//            System.out.println("c.getJson >>>>> "+c.toJson(CoreLabel.RESULT_SET));
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getListItemByComponentType(Carrier carrier) throws QException {
        try {
            Carrier newCarrier = new Carrier();
            newCarrier.setValue(EntityCrListItem.ITEM_CODE, carrier.getValue("liComponentCode"));
            if (carrier.isKeyExist(EntityCrListItem.ITEM_VALUE)) {
                newCarrier.setValue(EntityCrListItem.ITEM_VALUE, carrier.getValue(EntityCrListItem.ITEM_VALUE));
            }
            newCarrier.setValue("asc", EntityCrListItem.ITEM_VALUE);
            Carrier tc = newCarrier.callService("serviceCrGetListItemList");
//            System.out.println("json->" + tc.getJson());
            return tc;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewRelRuleAndComponent(Carrier carrier) throws QException {
        try {
            String[] compKeys = carrier.getValue(EntityCrRelRuleAndComponent.LI_COMPONENT_KEY).toString().trim().split(CoreLabel.SEPERATOR_VERTICAL_LINE);
            for (String compKey : compKeys) {
                EntityCrRelRuleAndComponent ent = new EntityCrRelRuleAndComponent();
                ent.setLiRuleKey(carrier.getValue(EntityCrRelRuleAndComponent.LI_RULE_KEY).toString());
                ent.setLiComponentCode(carrier.getValue(EntityCrRelRuleAndComponent.LI_COMPONENT_CODE).toString());
                ent.setLiComponentKey(compKey);
                Carrier c = EntityManager.select(ent);

                if (c.getTableRowCount(ent.toTableName()) == 0) {
                    ent.setInputKey(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_KEY).toString());
                    ent.setInputValue(carrier.getValue(EntityCrRelRuleAndComponent.INPUT_VALUE).toString());
                    ent.setPermissionType(carrier.getValue(EntityCrRelRuleAndComponent.PERMISSION_TYPE).toString());
                    EntityManager.insert(ent);
                }
            }
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getRelRuleAndComponentList(Carrier carrier) throws QException {
        try {
            EntityCrRelRuleAndComponentList ent = new EntityCrRelRuleAndComponentList();
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);

            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            EntityManager.addSequence(carrier, "getRelRuleAndComponentList");
            carrier.setValue(CoreLabel.RESULT_SET_ROW_COUNT, EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier updateRelRuleAndComponent(Carrier carrier) throws QException {
        try {
            EntityCrRelRuleAndComponent ent = new EntityCrRelRuleAndComponent();
            ent.setId(carrier.getValue(EntityCrRelRuleAndComponent.ID).toString());
            EntityManager.select(ent);
            EntityManager.mapCarrierToEntity(carrier, ent, false);
            EntityManager.update(ent);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier deleteRelRuleAndComponent(Carrier carrier) throws QException {
        try {
            EntityCrRelRuleAndComponent ent = new EntityCrRelRuleAndComponent();
            ent.setId(carrier.getValue(EntityCrRelRuleAndComponent.ID).toString());
            EntityManager.delete(ent);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier insertNewAttribute(Carrier carrier) throws QException {

        String entId = carrier.getValue("attributeUniqueId").toString();
        if (entId.length() == 0) {
            EntityCrAttribute ent = new EntityCrAttribute();
            ent.setAttributeCode(carrier.getValue("attributeCode").toString());
            ent.setLiAttributeStatus(carrier.getValue("liAttributeStatus").toString());
            ent.setLiIsGeneral(carrier.getValue("liIsGeneral").toString());
            EntityManager.insert(ent);
            entId = ent.getId();
        }

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(entId);
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_ATTRIBUTE);
        entLang.setLangDef(carrier.getValue(
                EntityCrAttribute.ATTRIBUTE_NAME).toString());
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(entId);
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_ATTRIBUTE);
        entLangDesc.setLangDef(carrier.getValue(
                EntityCrAttribute.ATTRIBUTE_DESCRIPTION).toString());
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLangDesc);

        return carrier;
    }

    public Carrier updateAttribute(Carrier carrier) throws QException {
        EntityCrLangRel entLangT = new EntityCrLangRel();
        entLangT.setId(carrier.getValue("id").toString());
        EntityManager.select(entLangT);

        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setId(entLangT.getRelId());
        EntityManager.select(ent);
        carrier.removeKey("id");
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(ent.getId());
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_ATTRIBUTE);
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLang);
        entLang.setLangDef(carrier.getValue(
                EntityCrAttribute.ATTRIBUTE_NAME).toString());
        EntityManager.update(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(ent.getId());
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_ATTRIBUTE);
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLangDesc);
        entLangDesc.setLangDef(carrier.getValue(
                EntityCrAttribute.ATTRIBUTE_DESCRIPTION).toString());
        EntityManager.update(entLangDesc);
        return carrier;
    }

    public Carrier deleteAttribute(Carrier carrier) throws QException {
        //gelen id submodulename lang id-sidir
        String id = carrier.getValue("id").toString();

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setId(id);
        EntityManager.select(entLang);

        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setId(entLang.getRelId());
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getAttributeList(Carrier carrier) throws QException {

        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setDeepWhere(false);
        ent.setAttributeCode(carrier.getValue("attributeCode").toString());
        ent.setLiAttributeStatus(carrier.getValue("liAttributeStatus").toString());
        ent.setLiIsGeneral(carrier.getValue("liIsGeneral").toString());
        ent.setId(carrier.getValue("id").toString());
        Carrier c = EntityManager.select(ent);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String tnAttribute = ent.toTableName();
        String ids = c.getValueLine(tnAttribute);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setLang(SessionManager.getCurrentLang());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_ATTRIBUTE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("attributeName").toString());
        entName.addDeepWhereStatementField("langDef");
        Carrier cprName = EntityManager.select(entName).getKVFromTable(
                entName.toTableName(), "relId", "langDef");

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setLang(SessionManager.getCurrentLang());
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_ATTRIBUTE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("attributeDescription").toString());
        entDesc.addDeepWhereStatementField("langDef");
        Carrier cprDesc = EntityManager.select(entDesc).getKVFromTable(
                entDesc.toTableName(), "relId", "langDef");

        Carrier cprAttributeStatus = QUtility.getListItem("attributeStatus",
                carrier.getValue("attributeStatusName").toString());

        Carrier cprAttributeIsGeneral = QUtility.getListItem("yesno",
                carrier.getValue("isGeneralName").toString());

        c.mergeCarrier(tnAttribute, "id", "attributeName", cprName);
        c.mergeCarrier(tnAttribute, "id", "attributeDescription", cprDesc);
        c.mergeCarrier(tnAttribute, "liAttributeStatus", "attributeStatusName",
                cprAttributeStatus);
        c.mergeCarrier(tnAttribute, "liIsGeneral", "isGeneralName",
                cprAttributeIsGeneral);

        c.renameTableName(tnAttribute, CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getAttributeList"));

        c.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return c;
    }

    public static Carrier getAttributeList4Cache(Carrier carrier) throws QException {

        try {
            EntityCrAttribute ent = new EntityCrAttribute();

            ent.setDeepWhere(false);
            Carrier c = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            String tnAttribute = ent.toTableName();
            String ids = c.getValueLine(tnAttribute);

            EntityCrLangRel entName = new EntityCrLangRel();
            entName.setDeepWhere(false);
            entName.setRelId(ids);
            entName.setLangType(LANG_TYPE_ATTRIBUTE);
            entName.setLangField(LANG_FIELD_NAME);
            Carrier crName = EntityManager.select(entName);

            Carrier outCarrier = new Carrier();
            String tn = entName.toTableName();
            int rc = crName.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                String newTN = crName.getValue(tn, i, "relId").toString()
                        + crName.getValue(tn, i, "lang").toString();
                String val = crName.getValue(tn, i, "langDef").toString();
                outCarrier.setValue(newTN, val);
            }

            String tn1 = ent.toTableName();
            int rc1 = c.getTableRowCount(tn1);
            for (int i = 0; i < rc1; i++) {
                String newTN = c.getValue(tn1, i, "id").toString();
                String attrCode = c.getValue(tn1, i, "attributeCode").toString();
                outCarrier.setValue(newTN, attrCode);
                outCarrier.setValue(attrCode, newTN);
            }

            return outCarrier;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Carrier();
    }

    public static Carrier getAttributeNameList(Carrier carrier) throws QException {

        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setDeepWhere(false);
        ent.setAttributeCode(carrier.getValue("attributeCode").toString());
        ent.setLiAttributeStatus(carrier.getValue("liAttributeStatus").toString());
        ent.setLiIsGeneral(carrier.getValue("liIsGeneral").toString());
        ent.setId(carrier.getValue("id").toString());
        Carrier c = EntityManager.select(ent);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String tnAttribute = ent.toTableName();
        String ids = c.getValueLine(tnAttribute);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setLang(SessionManager.getCurrentLang());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_ATTRIBUTE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("attributeName").toString());
        entName.addDeepWhereStatementField("langDef");
        Carrier cprName = EntityManager.select(entName).getKVFromTable(
                entName.toTableName(), "relId", "langDef");

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setLang(SessionManager.getCurrentLang());
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_ATTRIBUTE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("attributeDescription").toString());
        entDesc.addDeepWhereStatementField("langDef");
        Carrier cprDesc = EntityManager.select(entDesc).getKVFromTable(
                entDesc.toTableName(), "relId", "langDef");

        c.mergeCarrier(tnAttribute, "id", "attributeName", cprName);
        c.mergeCarrier(tnAttribute, "id", "attributeDescription", cprDesc);

        c.renameTableName(tnAttribute, CoreLabel.RESULT_SET);
        return c;
    }

    public static Carrier getAttributeMainList(Carrier carrier) throws QException {

        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setDeepWhere(false);
        ent.setId(carrier.getValue("attributeUniqueId").toString());
        ent.setAttributeCode(carrier.getValue("attributeCode").toString());
        ent.setLiAttributeStatus(carrier.getValue("liAttributeStatus").toString());
        ent.setLiIsGeneral(carrier.getValue("liIsGeneral").toString());
        ent.addDeepWhereStatementField("attributeCode");
        ent.addDeepWhereStatementField("id");
        Carrier c = EntityManager.select(ent);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String tnAttribute = ent.toTableName();
        String ids = c.getValueLine(tnAttribute);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setId(carrier.getValue("id").toString());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_ATTRIBUTE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("attributeName").toString());
        entName.setLang(carrier.getValue("lang").toString());
        entName.addDeepWhereStatementField("langDef");
        entName.addDeepWhereStatementField("lang");
        Carrier cName = EntityManager.select(entName);
        String tnName = entName.toTableName();

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_ATTRIBUTE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("attributeDescription").toString());
        entDesc.setLang(carrier.getValue("lang").toString());
        entDesc.addDeepWhereStatementField("langDef");
        entDesc.addDeepWhereStatementField("lang");
        Carrier cDesc = EntityManager.select(entDesc);
        Carrier cprDesc = cDesc.getKeyValuesPairFromTable(
                entDesc.toTableName(), new String[]{"relId", "lang"}, "langDef");

        Carrier cprAttributeStatus = QUtility.getListItem("attributeStatus",
                carrier.getValue("attributeStatusName").toString());

        Carrier cprAttributeIsGeneral = QUtility.getListItem("yesno",
                carrier.getValue("isGeneralName").toString());

        cName.mergeCarrier(tnName, new String[]{"relId", "lang"},
                "attributeDescription", cprDesc);
        cName.mergeCarrier(tnName, "relId", c, tnAttribute, "id",
                new String[]{"attributeCode", "liAttributeStatus", "liIsGeneral"});

        cName.mergeCarrier(tnName, "liAttributeStatus", "attributeStatusName", cprAttributeStatus);
        cName.mergeCarrier(tnName, "liIsGeneral", "isGeneralName", cprAttributeIsGeneral);

        cName.copyTableColumn(tnName, "relId", "attributeUniqueId");
        cName.renameTableColumn(tnName, "langDef", "attributeName");

        cName.renameTableName(tnName, CoreLabel.RESULT_SET);
        cName.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getAttributeMainList"));

        cName.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return cName;
    }

    public static Carrier insertNewAppointment(Carrier carrier) throws QException {
        EntityCrAppointment ent = new EntityCrAppointment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setInspectionCode(getNewSInspectionCodeLine());

        if (carrier.getValue("isNow").toString().equals("true")) {
            ent.setAppointmentDate(QDate.getCurrentDate());
            ent.setAppointmentTime1(QDate.getCurrentTime());
            ent.setAppointmentTime2(QDate.getCurrentTime());

        }
        if (ent.getAppointmentDate().equals(QDate.getCurrentDate())) {
            ent.setAppointmentStatus(APPOINTMENT_STATUS_IN_QUEUE);
        } else {
            ent.setAppointmentStatus(APPOINTMENT_STATUS_ACTIVE);
        }
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateAppointment(Carrier carrier) throws QException {
        EntityCrAppointment ent = new EntityCrAppointment();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteAppointment(Carrier carrier) throws QException {

        EntityCrAppointment ent = new EntityCrAppointment();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);

        if (ent.getInspectionCode().trim().length() == 0) {
            carrier.addController("general",
                    EntityManager.getMessageText("appointmentIsNotSelected"));
            return carrier;
        }

        EntityCrInspection entIns = new EntityCrInspection();
        entIns.setInspectionCode(ent.getInspectionCode());
        Carrier tc = EntityManager.select(entIns);
        int rc = tc.getTableRowCount(entIns.toTableName());

        if (rc == 0) {
            EntityManager.delete(ent);
        } else {
            carrier.addController("general",
                    EntityManager.getMessageText("operationWithValueCannotBeDeleted"));
            return carrier;
        }

        return carrier;
    }

    private static Carrier getDoctorsInfo(Carrier carrier) throws QException {
        EntityCrUserList entUser = new EntityCrUserList();
        entUser.setDeepWhere(false);
        entUser.setUserPersonName(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonSurname(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonMiddlename(carrier.getValue("doctorFullname").toString());
        entUser.addOrStatementField("userPersonName");
        entUser.addOrStatementField("userPersonSurname");
        entUser.addOrStatementField("userPersonMiddlename");
        Carrier crUser = EntityManager.select(entUser);
        crUser.renameTableName(entUser.toTableName(), CoreLabel.RESULT_SET);
        return crUser;
    }

    private static String convertArrayToFilterLine(String[] arr) {
        String st = "";
        for (String s : arr) {
            st += s + CoreLabel.IN;
        }
        return st;
    }

    public static Carrier getAppointmentList(Carrier carrier) throws QException {
        Carrier cprSex = QUtility.getListItem("sex",
                carrier.getValue("sexName").toString());
        String sex = convertArrayToFilterLine(cprSex.getKeys());

        Carrier cprApptmntStts = QUtility.getListItem("appointmentStatus",
                carrier.getValue("appointmentStatusName").toString());
        String apptmntStts = convertArrayToFilterLine(cprApptmntStts.getKeys());

        Carrier cprApptStatus = getAppointmentStatusForAppoinment(carrier);
        String apptStatusKeys = convertArrayToFilterLine(cprApptStatus.getKeys());

        EntityCrAppointmentList ent = new EntityCrAppointmentList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setSex(sex);
        ent.setStatus("A");
        ent.setAppointmentStatus(apptmntStts);
        ent.setAppointmentStatus(apptStatusKeys);
        if (!ent.hasSortBy()) {
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_STATUS);
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_DATE);
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_TIME_1);
            ent.setSortByAsc(true);
        }
        Carrier crAppt = EntityManager.select(ent);
        String tnAppt = ent.toTableName();

        Carrier cprPayment = getPaymentInfoForAppointment(carrier);

        crAppt.mergeCarrier(tnAppt, "sex", "sexName", cprSex);
        crAppt.mergeCarrier(tnAppt, "appointmentStatus",
                "appointmentStatusName", cprApptStatus);

        crAppt.mergeCarrier(tnAppt,
                new String[]{"fkPatientId", "fkDoctorUserId", "appointmentDate"},
                "paymentStatus", cprPayment, true);

        crAppt.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        crAppt.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getAppointmentList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        crAppt.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return crAppt;
    }

    private static Carrier getAppointmentStatusForAppoinment(Carrier carrier) throws QException {
        Carrier cprApptStatus = QUtility.getListItem("appointmentStatus",
                carrier.getValue("appointmentStatusName").toString());
        String[] keysStatus = cprApptStatus.getKeys();
        for (String key : keysStatus) {
            if (key.equals("1")) {
                cprApptStatus.setValue(key,
                        "<span style=\"color:blue\">"
                        + cprApptStatus.getValue(key).toString() + "</span>");
            } else if (key.equals("2")) {
                cprApptStatus.setValue(key,
                        "<span style=\"color:red\">"
                        + cprApptStatus.getValue(key).toString() + "</span>");
            } else if (key.equals("3")) {
                cprApptStatus.setValue(key,
                        "<span style=\"color:rgb(255,0,255)\">"
                        + cprApptStatus.getValue(key).toString() + "</span>");
            }
        }
        return cprApptStatus;
    }

    private static Carrier getPaymentInfoForAppointment(Carrier carrier) throws QException {
        EntityCrPriceList entPrcLst = new EntityCrPriceList();
        Carrier cprPrcLst = EntityManager.select(entPrcLst)
                .getKVFromTable(entPrcLst.toTableName(), "id",
                        EntityCrPriceList.PAYMENT_NAME);

        EntityCrPayment entPayment = new EntityCrPayment();
        entPayment.setDeepWhere(false);
        entPayment.setPaymentStatus("P");
        Carrier crPayment = EntityManager.select(entPayment);
        Carrier cprPayment = new Carrier();

        String tnPayment = entPayment.toTableName();
        int rcPayment = crPayment.getTableRowCount(tnPayment);
        for (int i = 0; i < rcPayment; i++) {
            EntityManager.mapCarrierToEntity(crPayment, tnPayment, i, entPayment);
            String key = entPayment.getFkPatientId()
                    + entPayment.getFkDoctorUserId() + entPayment.getPaymentDate();

            String val = cprPrcLst.getValue(entPayment.getFkPriceListId()).toString()
                    + ", " + entPayment.getPaymentAmount()
                    + " " + entPayment.getPaymentCurrency() + "<br>"
                    + cprPayment.getValue(key);

            cprPayment.setValue(key, val);
        }

        String[] keysPayment = cprPayment.getKeys();
        for (String key : keysPayment) {
            String v = "<a data-toggle=\"popover\" data-html=\"true\" "
                    + "class=\"apd-a-popover\" style=\"cursor:pointer\" "
                    + "data-content=\""
                    + cprPayment.getValue(key).toString()
                    + "\">$$$</a>";
            cprPayment.setValue(key, v);
        }
        return cprPayment;
    }

    public static Carrier insertNewModule(Carrier carrier) throws QException {
        String entId = carrier.getValue("moduleUniqueId").toString();
        if (entId.length() == 0) {
            EntityCrModule ent = new EntityCrModule();
            EntityManager.mapCarrierToEntity(carrier, ent);
            EntityManager.insert(ent);
            entId = ent.getId();
        }

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(entId);
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_MODULE);
        entLang.setLangDef(carrier.getValue(EntityCrModule.MODULE_NAME).toString());
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(entId);
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_MODULE);
        entLangDesc.setLangDef(carrier.getValue(EntityCrModule.MODULE_DESCRIPTION).toString());
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLangDesc);

        return carrier;
    }

    public Carrier updateModule(Carrier carrier) throws QException {
        EntityCrModule ent = new EntityCrModule();
        ent.setId(carrier.getValue("moduleUniqueId").toString());
        EntityManager.select(ent);
        ent.setLiModuleStatus(carrier.getValue("liModuleStatus").toString());
        ent.setFkPaymentTypeId(carrier.getValue("fkPaymentTypeId").toString());
        EntityManager.update(ent);

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(ent.getId());
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_MODULE);
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLang);
        entLang.setLangDef(carrier.getValue(EntityCrModule.MODULE_NAME).toString());
        EntityManager.update(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(ent.getId());
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_MODULE);
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLangDesc);
        entLangDesc.setLangDef(carrier.getValue(EntityCrModule.MODULE_DESCRIPTION).toString());
        EntityManager.update(entLangDesc);

        return carrier;
    }

    public Carrier deleteModule(Carrier carrier) throws QException {

        return carrier;
    }

    public static Carrier getModuleList4Cache(Carrier carrier) throws QException {
        String perid = "";//QUtility.getPermissionRelIdByCrrentUser();

        EntityCrModule ent = new EntityCrModule();
        ent.setDeepWhere(false);
        ent.setId(perid);
        Carrier c = EntityManager.select(ent);

        String tnModule = ent.toTableName();
        String ids = c.getValueLine(tnModule);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_MODULE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.addDeepWhereStatementField("langDef");
        Carrier crName = EntityManager.select(entName);

        Carrier outCarrier = new Carrier();
        String tn = entName.toTableName();
        int rc = crName.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String tnNew = crName.getValue(tn, i, "relId").toString()
                    + crName.getValue(tn, i, "lang").toString();
            String val = crName.getValue(tn, i, "langDef").toString();
            outCarrier.setValue(tnNew, val);
        }
        return outCarrier;
    }

    public static Carrier getModuleList4Combo(Carrier carrier) throws QException {
        Carrier c = new Carrier();

        Carrier cModule = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_MODULE);
        String[] keys = cModule.getKeys();
        int i = 0;
        for (String k : keys) {
            if (k.endsWith(SessionManager.getCurrentLang())) {
                c.setValue(CoreLabel.RESULT_SET, i, "id",
                        k.substring(0, k.length() - SessionManager.getCurrentLang().length()));
                c.setValue(CoreLabel.RESULT_SET, i, "name", cModule.getValue(k));
                i++;
            }
        }
        return c;
    }

    public static Carrier getModuleList4ComboNali(Carrier carrier) throws QException {
        Carrier c = new Carrier();

        Carrier cModule = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_MODULE);
        String[] keys = cModule.getKeys();
        int i = 0;
        for (String k : keys) {
            if (k.endsWith(SessionManager.getCurrentLang())) {
                c.setValue(CoreLabel.RESULT_SET, i, "id",
                        k.substring(i, k.length() - SessionManager.getCurrentLang().length()));
                c.setValue(CoreLabel.RESULT_SET, i, "name", cModule.getValue(k));
                i++;
            }
        }
        return c;
    }

    public static Carrier getModuleList(Carrier carrier) throws QException {
        String perid = "";//QUtility.getPermissionRelIdByCrrentUser();
        perid += carrier.getValue("id").toString();

        EntityCrModule ent = new EntityCrModule();
        ent.setDeepWhere(false);
        ent.setLiModuleStatus(carrier.getValue("liModuleStatus").toString());
        ent.setId(perid);
        Carrier c = EntityManager.select(ent);

        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String tnModule = ent.toTableName();
        String ids = c.getValueLine(tnModule);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setLang(SessionManager.getCurrentLang());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_MODULE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("moduleName").toString());
        entName.addDeepWhereStatementField("langDef");
        Carrier cprName = EntityManager.select(entName).getKVFromTable(
                entName.toTableName(), "relId", "langDef");

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setLang(SessionManager.getCurrentLang());
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_MODULE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("moduleDescription").toString());
        entDesc.addDeepWhereStatementField("langDef");
        Carrier cprDesc = EntityManager.select(entDesc).getKVFromTable(
                entDesc.toTableName(), "relId", "langDef");

        Carrier cprListItem = QUtility.getListItem("pa",
                carrier.getValue("moduleStatusName").toString());

        c.mergeCarrier(tnModule, "id", "moduleName", cprName);
        c.mergeCarrier(tnModule, "id", "moduleDescription", cprDesc);
        c.mergeCarrier(tnModule, "liModuleStatus", "moduleStatusName", cprListItem);

        c.renameTableName(tnModule, CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getModuleList"));

        c.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return c;
    }

    public Carrier getModuleMainList(Carrier carrier) throws QException {
        EntityCrModule ent = new EntityCrModule();
        ent.setDeepWhere(false);
        ent.setId(carrier.getValue("moduleUniqueId").toString());
        ent.setLiModuleStatus(carrier.getValue("liModuleStatus").toString());
        ent.addDeepWhereStatementField("id");
        Carrier c = EntityManager.select(ent);

        String tnModule = ent.toTableName();
        String ids = c.getValueLine(tnModule);
        Carrier cprModule = c.getKVFromTable(tnModule, "id", "liModuleStatus");
        Carrier cprModulePT = c.getKVFromTable(tnModule, "id", "fkPaymentTypeId");
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setId(carrier.getValue("id").toString());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_MODULE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("moduleName").toString());
        entName.setLang(carrier.getValue("lang").toString());
        entName.addDeepWhereStatementField("langDef");
        Carrier cName = EntityManager.select(entName);
        String tnName = entName.toTableName();

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_MODULE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("moduleDescription").toString());
        entDesc.setLang(carrier.getValue("lang").toString());
        entDesc.addDeepWhereStatementField("langDef");
        Carrier cDesc = EntityManager.select(entDesc);
        Carrier cprDesc = cDesc.getKeyValuesPairFromTable(
                entDesc.toTableName(), new String[]{"relId", "lang"}, "langDef");

        Carrier cprListItem = QUtility.getListItem("pa",
                carrier.getValue("moduleStatusName").toString());

        cName.mergeCarrier(tnName, new String[]{"relId", "lang"},
                "moduleDescription", cprDesc);
        cName.mergeCarrier(tnName, "relId", "liModuleStatus", cprModule);
        cName.mergeCarrier(tnName, "relId", "fkPaymentTypeId", cprModulePT);
        cName.mergeCarrier(tnName, "liModuleStatus", "moduleStatusName", cprListItem);

        cName.copyTableColumn(tnName, "relId", "moduleUniqueId");
//        cName.renameTableColumn(tnName, "relId", "id"); 
        cName.renameTableColumn(tnName, "langDef", "moduleName");

        cName.renameTableName(tnName, CoreLabel.RESULT_SET);
        cName.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getModuleMainList"));

        cName.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return cName;
    }

    public static Carrier insertNewSubmodule(Carrier carrier) throws QException {

        String entId = carrier.getValue("submoduleUniqueId").toString();
        if (entId.length() == 0) {
            EntityCrSubmodule ent = new EntityCrSubmodule();
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.setSubmoduleName("");
            ent.setSubmoduleDescription("");
            EntityManager.insert(ent);
            entId = ent.getId();
        }

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(entId);
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_SUBMODULE);
        entLang.setLangDef(carrier.getValue(
                EntityCrSubmodule.SUBMODULE_NAME).toString());
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(entId);
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_SUBMODULE);
        entLangDesc.setLangDef(carrier.getValue(
                EntityCrSubmodule.SUBMODULE_DESCRIPTION).toString());
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.insert(entLangDesc);

        return carrier;
    }

    public static Carrier updateSubmodule(Carrier carrier) throws QException {

        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setId(carrier.getValue("submoduleUniqueId").toString());
        EntityManager.select(ent);
        ent.setFkModuleId(carrier.getValue("fkModuleId").toString());
        ent.setSubmoduleType(carrier.getValue("submoduleType").toString());
        ent.setLiSubmoduleStatus(carrier.getValue("liSubmoduleStatus").toString());
        ent.setSortBy(carrier.getValue("sortBy").toString());
        ent.addDeepWhereStatementField("id");
        ent.addDeepWhereStatementField("sortBy");
        EntityManager.update(ent);

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setRelId(ent.getId());
        entLang.setLangField(LANG_FIELD_NAME);
        entLang.setLangType(LANG_TYPE_SUBMODULE);
        entLang.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLang);
        entLang.setLangDef(carrier.getValue(
                EntityCrSubmodule.SUBMODULE_NAME).toString());
        EntityManager.update(entLang);

        EntityCrLangRel entLangDesc = new EntityCrLangRel();
        entLangDesc.setRelId(ent.getId());
        entLangDesc.setLangField(LANG_FIELD_DESC);
        entLangDesc.setLangType(LANG_TYPE_SUBMODULE);
        entLangDesc.setLang(carrier.getValue("lang").toString());
        EntityManager.select(entLangDesc);
        entLangDesc.setLangDef(carrier.getValue(
                EntityCrSubmodule.SUBMODULE_DESCRIPTION).toString());
        EntityManager.update(entLangDesc);
        return carrier;
    }

    public Carrier finishSession(Carrier carrier) throws QException {
        EntityCrAppointment ent = new EntityCrAppointment();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        ent.setAppointmentStatus(APPOINTMENT_STATUS_FINISHED);
        EntityManager.update(ent);
        return carrier;
    }

    public static Carrier deleteSubmodule(Carrier carrier) throws QException {
        //gelen id submodulename lang id-sidir
        String id = carrier.getValue("id").toString();

        EntityCrLangRel entLang = new EntityCrLangRel();
        entLang.setId(id);
        EntityManager.select(entLang);

        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setId(entLang.getRelId());
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getSubmoduleList(Carrier carrier) throws QException {

        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setDeepWhere(false);
        ent.setId(carrier.getValue("id").toString());
        ent.setFkModuleId(carrier.getValue("fkModuleId").toString());
        ent.setSubmoduleType(carrier.getValue("submoduleType").toString());
        ent.setLiSubmoduleStatus(carrier.getValue("liSubmoduleStatus").toString());
        ent.setSortBy(carrier.getValue("sortBy").toString());
        ent.addSortBy("sortBy");
        ent.setSortByAsc(true);
        ent.addDeepWhereStatementField("sortBy");
        Carrier c = EntityManager.select(ent);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String tnSubmodule = ent.toTableName();
        String ids = c.getValueLine(tnSubmodule);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setLang(SessionManager.getCurrentLang());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_SUBMODULE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("submoduleName").toString());
        entName.addDeepWhereStatementField("langDef");
        Carrier cprName = EntityManager.select(entName).getKVFromTable(
                entName.toTableName(), "relId", "langDef");

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setLang(SessionManager.getCurrentLang());
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_SUBMODULE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("submoduleDescription").toString());
        entDesc.addDeepWhereStatementField("langDef");
        Carrier cprDesc = EntityManager.select(entDesc).getKVFromTable(
                entDesc.toTableName(), "relId", "langDef");

        Carrier cprSubmoduleStatus = QUtility.getListItem("pa",
                carrier.getValue("submoduleStatusName").toString());

//        Carrier cprSubmoduleType = QUtility.getListItem("submoduleType",
//                carrier.getValue("submoduleTypeName").toString());
        c.mergeCarrier(tnSubmodule, "id", "submoduleName", cprName);
        c.mergeCarrier(tnSubmodule, "id", "submoduleDescription", cprDesc);
        c.mergeCarrier(tnSubmodule, "liSubmoduleStatus", "submoduleStatusName",
                cprSubmoduleStatus);
//        c.mergeCarrier(tnSubmodule, "submoduleType", "submoduleTypeName",
//                cprSubmoduleType);

        c.renameTableName(tnSubmodule, CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getSubmoduleList"));

        c.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return c;
    }

    public static Carrier getSubmoduleList4Cache(Carrier carrier) throws QException {

        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setDeepWhere(false);
        ent.setSortBy(carrier.getValue("sortBy").toString());
        ent.addSortBy("sortBy");
        ent.setSortByAsc(true);
        ent.setEndLimit(30000);
        ent.addDeepWhereStatementField("sortBy");
        Carrier c = EntityManager.select(ent);

        String tnSubmodule = ent.toTableName();
        String ids = c.getValueLine(tnSubmodule);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_SUBMODULE);
        entName.setLangField(LANG_FIELD_NAME);
        Carrier crName = EntityManager.select(entName);

        Carrier cprSubmoduleStatus = QUtility.getListItem("pa",
                carrier.getValue("submoduleStatusName").toString());

        Carrier cprSubmoduleType = QUtility.getListItem("submoduleType",
                carrier.getValue("submoduleTypeName").toString());

        c.mergeCarrier(tnSubmodule, "liSubmoduleStatus", "submoduleStatusName",
                cprSubmoduleStatus);
        c.mergeCarrier(tnSubmodule, "submoduleType", "submoduleTypeName",
                cprSubmoduleType);

        c.renameTableName(tnSubmodule, CoreLabel.RESULT_SET);

        String tn = entName.toTableName();
        int rc = crName.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String tnNew = crName.getValue(tn, i, "relId").toString()
                    + crName.getValue(tn, i, "lang").toString();
            String val = crName.getValue(tn, i, "langDef").toString();
            c.setValue(tnNew, val);
        }

        return c;
    }

    public static Carrier getSubmoduleMainList(Carrier carrier) throws QException {

        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setDeepWhere(false);
        ent.setId(carrier.getValue("submoduleUniqueId").toString());
        ent.setFkModuleId(carrier.getValue("fkModuleId").toString());
        ent.setSubmoduleType(carrier.getValue("submoduleType").toString());
        ent.setLiSubmoduleStatus(carrier.getValue("liSubmoduleStatus").toString());
        ent.setSortBy(carrier.getValue("sortBy").toString());
        ent.addDeepWhereStatementField("id");
        ent.addDeepWhereStatementField("sortBy");
        Carrier c = EntityManager.select(ent);

        String tnSubmodule = ent.toTableName();
        String ids = c.getValueLine(tnSubmodule);

        EntityCrLangRel entName = new EntityCrLangRel();
        entName.setDeepWhere(false);
        entName.setId(carrier.getValue("id").toString());
        entName.setRelId(ids);
        entName.setLangType(LANG_TYPE_SUBMODULE);
        entName.setLangField(LANG_FIELD_NAME);
        entName.setLangDef(carrier.getValue("submoduleName").toString());
        entName.setLang(carrier.getValue("lang").toString());
        entName.addDeepWhereStatementField("langDef");
        entName.addDeepWhereStatementField("lang");
        Carrier cName = EntityManager.select(entName);
        String tnName = entName.toTableName();

        EntityCrLangRel entDesc = new EntityCrLangRel();
        entDesc.setDeepWhere(false);
        entDesc.setRelId(ids);
        entDesc.setLangType(LANG_TYPE_SUBMODULE);
        entDesc.setLangField(LANG_FIELD_DESC);
        entDesc.setLangDef(carrier.getValue("submoduleDescription").toString());
        entDesc.setLang(carrier.getValue("lang").toString());
        entDesc.addDeepWhereStatementField("langDef");
        entDesc.addDeepWhereStatementField("lang");
        Carrier cDesc = EntityManager.select(entDesc);
        Carrier cprDesc = cDesc.getKeyValuesPairFromTable(
                entDesc.toTableName(), new String[]{"relId", "lang"}, "langDef");

        Carrier cprSubmoduleStatus = QUtility.getListItem("pa",
                carrier.getValue("submoduleStatusName").toString());

        Carrier cprSubmoduleType = QUtility.getListItem("submoduleType",
                carrier.getValue("submoduleTypeName").toString());

        Carrier cModule = new Carrier();
        cModule.setValue("moduleName", carrier.getValue("moduleName"));
        cModule = getModuleList(cModule);
        Carrier cprModule = cModule.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "moduleName");

        cName.mergeCarrier(tnName, new String[]{"relId", "lang"},
                "submoduleDescription", cprDesc);

        cName.mergeCarrier(tnName, "relId", c, tnSubmodule, "id",
                new String[]{"fkModuleId", "submoduleType", "liSubmoduleStatus", "sortBy"});

        cName.mergeCarrier(tnName, "fkModuleId", "moduleName", cprModule);

        cName.mergeCarrier(tnName, "liSubmoduleStatus", "submoduleStatusName", cprSubmoduleStatus);
        cName.mergeCarrier(tnName, "submoduleType", "submoduleTypeName", cprSubmoduleType);

        cName.copyTableColumn(tnName, "relId", "submoduleUniqueId");
        cName.renameTableColumn(tnName, "langDef", "submoduleName");

        cName.renameTableName(tnName, CoreLabel.RESULT_SET);
        cName.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getSubmoduleMainList"));

        cName.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

        return cName;
    }

    public static Carrier insertNewOrganPoint(Carrier carrier) throws QException {
        EntityCrOrganPoint ent = new EntityCrOrganPoint();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateOrganPoint(Carrier carrier) throws QException {
        EntityCrOrganPoint ent = new EntityCrOrganPoint();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteOrganPoint(Carrier carrier) throws QException {
        EntityCrOrganPoint ent = new EntityCrOrganPoint();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public Carrier getOrganPointList(Carrier carrier) throws QException {
        EntityCrOrganPointList ent = new EntityCrOrganPointList();
        ent.setDeepWhere(false);
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setLang(SessionManager.getCurrentLang());
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getOrganPointList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return c;
    }

    public static Carrier insertNewValueType(Carrier carrier) throws QException {
        EntityCrValueType ent = new EntityCrValueType();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setLang("");
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateValueType(Carrier carrier) throws QException {
        EntityCrValueType ent = new EntityCrValueType();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        ent.setLang("");
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteValueType(Carrier carrier) throws QException {
        EntityCrValueType ent = new EntityCrValueType();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public Carrier getValueTypeList(Carrier carrier) throws QException {
        EntityCrValueTypeList ent = new EntityCrValueTypeList();
        ent.setDeepWhere(false);
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setLang(SessionManager.getCurrentLang());
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getValueTypeList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return c;
    }

    public static Carrier insertNewSubmoduleAttribute(Carrier carrier) throws QException {
        EntityCrSubmoduleAttribute ent = new EntityCrSubmoduleAttribute();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateSubmoduleAttribute(Carrier carrier) throws QException {
        EntityCrSubmoduleAttribute ent = new EntityCrSubmoduleAttribute();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteSubmoduleAttribute(Carrier carrier) throws QException {
        EntityCrSubmoduleAttribute ent = new EntityCrSubmoduleAttribute();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getSubmoduleAttributeList4Matrix(Carrier carrier)
            throws QException {
        carrier.setValue("fkModuleId", carrier.getValue("fkMainModuleId"));
        return getSubmoduleAttributeList(carrier);
    }

    public static Carrier getSubmoduleAttributeList(Carrier carrier)
            throws QException {
        EntityCrSubmoduleAttribute ent = new EntityCrSubmoduleAttribute();
        ent.setDeepWhere(false);
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.addDeepWhereStatementField("sortBy");
        ent.addSortBy("sortBy");
        Carrier carrierSA = EntityManager.select(ent);
        String tnSA = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        EntityCrOrganPointList entOP = new EntityCrOrganPointList();
        entOP.setDeepWhere(false);
        entOP.setId(carrierSA.getValueLine(tnSA, "fkOrganPointId"));
        entOP.setOrganPointName(carrier.getValue("organPointName").toString());
        entOP.addDeepWhereStatementField("organPointName");
        Carrier cprOP = EntityManager.select(entOP).
                getKVFromTable(entOP.toTableName(), "id", "organPointName");

        Carrier carrierAttr = new Carrier();
        carrierAttr.setValue("id", carrierSA.getValueLine(tnSA, "fkAttributeId"));
        carrierAttr.setValue("attributeName", carrier.getValue("attributeName"));
        carrierAttr = getAttributeList(carrierAttr);
        Carrier cprAttr = carrierAttr.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "attributeName");

        Carrier cprVT = QUtility.getListItem("valueType",
                carrier.getValue("valueTypeName").toString());

        Carrier carrierSM = new Carrier();
        carrierSM.setValue("id", carrierSA.getValueLine(tnSA, "fkSubmoduleId"));
        carrierSM.setValue("submoduleName", carrier.getValue("submoduleName"));
        carrierSM = getSubmoduleList(carrierSM);
        Carrier cprSM = carrierSM.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "submoduleName");

        Carrier cprHasOther = QUtility.getListItem("yesno",
                carrier.getValue("hasOtherName").toString());

        Carrier cprIsVisible = QUtility.getListItem("yesno",
                carrier.getValue("isVisible").toString());

        //merge statement
        carrierSA.mergeCarrier(tnSA, "fkOrganPointId", "organPointName", cprOP);
        carrierSA.mergeCarrier(tnSA, "fkAttributeId", "attributeName", cprAttr);
        carrierSA.mergeCarrier(tnSA, "fkValueTypeId", "valueTypeName", cprVT);
        carrierSA.mergeCarrier(tnSA, "fkSubmoduleId", "submoduleName", cprSM);
        carrierSA.mergeCarrier(tnSA, "hasOther", "hasOtherName", cprHasOther);
        carrierSA.mergeCarrier(tnSA, "isVisible", "isVisibleName", cprIsVisible);

        // final statement
        carrierSA.renameTableName(tnSA, CoreLabel.RESULT_SET);
        carrierSA.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getSubmoduleAttributeList"));

        carrierSA.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);

        return carrierSA;
    }

    public static Carrier getSubmoduleAttributeList4Cache(Carrier carrier)
            throws QException {
        EntityCrSubmoduleAttribute ent = new EntityCrSubmoduleAttribute();
        ent.setDeepWhere(false);
        ent.addDeepWhereStatementField("sortBy");
        ent.addSortBy("sortBy");
        ent.setEndLimit(80000);
        Carrier carrierSA = EntityManager.select(ent);
        String tnSA = ent.toTableName();

        EntityCrOrganPointList entOP = new EntityCrOrganPointList();
        entOP.setDeepWhere(false);
        entOP.setId(carrierSA.getValueLine(tnSA, "fkOrganPointId"));
        entOP.setOrganPointName(carrier.getValue("organPointName").toString());
        entOP.addDeepWhereStatementField("organPointName");
        Carrier cprOP = EntityManager.select(entOP).
                getKVFromTable(entOP.toTableName(), "id", "organPointName");

        Carrier carrierAttr = new Carrier();
        carrierAttr.setValue("id", carrierSA.getValueLine(tnSA, "fkAttributeId"));
        carrierAttr.setValue("attributeName", carrier.getValue("attributeName"));
        carrierAttr = getAttributeList(carrierAttr);
        Carrier cprAttr = carrierAttr.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "attributeName");

        Carrier carrierSM = new Carrier();
        carrierSM.setValue("id", carrierSA.getValueLine(tnSA, "fkSubmoduleId"));
        carrierSM.setValue("submoduleName", carrier.getValue("submoduleName"));
        carrierSM = getSubmoduleList(carrierSM);
        Carrier cprSM = carrierSM.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "submoduleName");

        //merge statement
        carrierSA.mergeCarrier(tnSA, "fkOrganPointId", "organPointName", cprOP);
        carrierSA.mergeCarrier(tnSA, "fkAttributeId", "attributeName", cprAttr);;
        carrierSA.mergeCarrier(tnSA, "fkSubmoduleId", "submoduleName", cprSM);

        // final statement
        carrierSA.renameTableName(tnSA, CoreLabel.RESULT_SET);
        return carrierSA;
    }

    public static Carrier getSubmoduleFormBody(Carrier carrier) throws QException {
        String fkSubmoduleId = carrier.getValue("fkSubmoduleId").toString();
        String fkSessionId = carrier.getValue("fkSessionId").toString();

        Carrier c = new Carrier();
        c.setValue("id", fkSubmoduleId);
        c = getSubmoduleList(c);
        String smName = c.getValue(CoreLabel.RESULT_SET, 0, "submoduleName").toString();

        Carrier crIns = new Carrier();
        crIns.setValue("fkSessionId", fkSessionId);
        crIns.setValue("fkSubmoduleId", fkSubmoduleId);
        crIns = getInspectionListBySession(crIns);

        String body = PgModel.getSubmoduleFormBody(fkSubmoduleId, fkSessionId, crIns);
        carrier.setValue("body", body);
        carrier.setValue("header", smName);
        return carrier;
    }

    public static Carrier insertNewPatient(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setFkOwnerUserId(SessionManager.getCurrentUserId());
        ent.setPatientId(genPatientId());
        EntityManager.insert(ent);
        return carrier;
    }

    public static Carrier getAgendaOfDoctor(Carrier carrier) throws QException {

        String fdate = QDate.convertDateToString(QDate.add(QDate.getCurrentDate(), 50));
        String sdate = QDate.convertDateToString(QDate.add(QDate.getCurrentDate(), -5));

        EntityCrAppointmentList ent = new EntityCrAppointmentList();
        ent.setDeepWhere(false);
        ent.setFkDoctorUserId(carrier.getValue("id").toString());
        ent.setAppointmentDate(sdate + CoreLabel.BN + fdate);
//        ent.setAppointmentDate("LE%" +);
        carrier = EntityManager.select(ent);

        String tn = ent.toTableName();
//        String patientIds = carrier.getValueLine(tn, EntityCrAppointment.FK_PATIENT_ID)
//                  +CoreLabel.IN+"-1";
//        System.out.println("patientIds:"+patientIds);
        int rc = carrier.getTableRowCount(tn);
//        carrier.removeKey("startLimit");
//        carrier.removeKey("endLimit");

//        EntityCrPatient entPtnt = new EntityCrPatient();
//        entPtnt.setId(patientIds);
//        Carrier cprPtnt = //EntityManager.select(entPtnt)
//                carrier.getKeyValuesPairFromTable(tn, "fkPatientId",
//                        new String[]{EntityCrPatient.PATIENT_NAME,
//                            EntityCrPatient.PATIENT_MIDDLE_NAME,
//                            EntityCrPatient.PATIENT_SURNAME});
//        Carrier cprPtnt1 = //EntityManager.select(entPtnt)
//                carrier.getKeyValuesPairFromTable(tn , "fkPatientId",
//                        EntityCrPatient.PATIENT_ID);
        String res = "[";
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);
            String fname = ent.getPatientName() + " " + ent.getPatientSurname() + " "
                    + ent.getPatientMiddleName();
            String title = " " + fname
                    + " (" + ent.getPatientId() + ")";

            String shour = ent.getAppointmentTime1().substring(0, 2);
            String ehour = ent.getAppointmentTime2().substring(0, 2);
            String sminute = ent.getAppointmentTime1().substring(2, 4);
            String eminute = ent.getAppointmentTime2().substring(2, 4);
            int day = 0;
            try {
                day = Integer.parseInt(QDate.getCurrentDate()) - Integer.parseInt(ent.getAppointmentDate());
            } catch (Exception e) {
            }

            String dt = ent.getAppointmentDate();
            dt = dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8);
            String sln = dt + "T" + QDate.convertToTimeString(ent.getAppointmentTime1());
            String eln = dt + "T" + QDate.convertToTimeString(ent.getAppointmentTime2());

            String ln = "{";
            ln += "\"title\": \"" + title + "\",";
            ln += "\"start\": \"" + sln + "\",";
            ln += "\"end\": \"" + eln + "\",";
            ln += "\"allDay\": false";
            ln += "}";
            res += ln;
            res += i + 1 < rc ? "," : "";
        }
        res += "]";
//        System.out.println("json" + res);
        carrier.setValue("res", res);
        return carrier;
    }

    private static String genPatientId() throws QException {
        String format = "170000001";
        String res = "170000001";

        EntityCrPatient ent = new EntityCrPatient();
        ent.setDeepWhere(false);
        ent.addSortBy(EntityCrPatient.PATIENT_ID);
        ent.setSortByAsc(false);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);

        try {
            res = String.valueOf(Integer.parseInt(ent.getPatientId()) + 1);
        } catch (Exception e) {
        }

        res = QDate.getCurrentYear().substring(2, 4) + res.substring(2, res.length());

        return res;
    }

    private static String genPaymentNo() throws QException {
        String format = "1700000001";
        String res = "1700000001";

        EntityCrPayment ent = new EntityCrPayment();
        ent.setDeepWhere(false);
        ent.addSortBy(EntityCrPayment.PAYMENT_NO);
        ent.setSortByAsc(false);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);

        try {
            res = String.valueOf(Integer.parseInt(ent.getPaymentNo()) + 1);
        } catch (Exception e) {
        }

        res = QDate.getCurrentYear().substring(2, 4) + res.substring(2, res.length());

        return res;

    }

    public Carrier updatePatient(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deletePatient(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getPatientList4Appointment(Carrier carrier) throws QException {
        Carrier c = carrier;
        c.setValue("startLimit", 0);
        c.setValue("endLimit", 10000);
        return getPatientList(c);
    }

    public static Carrier getPatientList(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        EntityManager.mapCarrierToEntity(carrier, ent);
        if (carrier.hasKey("fkPatientId")) {
            ent.setId(carrier.getValue("fkPatientId").toString());
        }
//        ent.setFkOwnerUserId(SessionManager.getCurrentUserId());
        Carrier crPtnt = EntityManager.select(ent);
        String tnPtnt = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprSex = QUtility.getListItem("sex",
                carrier.getValue("sexName").toString());
        crPtnt.mergeCarrier(tnPtnt, "sex", "sexName", cprSex, true);

        Carrier cprOcp = QUtility.getListItem("occupation",
                carrier.getValue("occupationName").toString());
        crPtnt.mergeCarrier(tnPtnt, "occupation", "occupationName", cprOcp, true);

        Carrier cprMrt = QUtility.getListItem("maritualStatus",
                carrier.getValue("maritualStatusName").toString());
        crPtnt.mergeCarrier(tnPtnt, "maritualStatus", "maritualStatusName", cprMrt, true);

        Carrier cprEdu = QUtility.getListItem("educationType",
                carrier.getValue("educationName").toString());
        crPtnt.mergeCarrier(tnPtnt, "education", "educationName", cprEdu, true);

        Carrier cprBloodGroup = QUtility.getListItem("bloodGroup",
                carrier.getValue("bloodGroupName").toString());
        crPtnt.mergeCarrier(tnPtnt, "bloodGroup", "bloodGroupName", cprBloodGroup, true);

        Carrier cprRhFactor = QUtility.getListItem("rhFactor",
                carrier.getValue("rhFactorName").toString());
        crPtnt.mergeCarrier(tnPtnt, "rhFactor", "rhFactorName", cprRhFactor, true);

        Carrier cprIsActive = QUtility.getListItem("pa",
                carrier.getValue("isActiveName").toString());
        crPtnt.mergeCarrier(tnPtnt, "isActive", "isActiveName", cprIsActive, true);

        crPtnt.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        crPtnt.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPatientList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        crPtnt.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return crPtnt;
    }

    public static Carrier getPatientList4Combo(Carrier carrier) throws QException {
        String fname = carrier.getValue("fullname").toString().trim();
        fname = fname.replaceAll("\\)", "");
        fname = fname.replaceAll("\\(", "");

        EntityCrPatient ent = new EntityCrPatient();
        String[] sp = fname.split(" ");
        String name = sp.length > 0 ? sp[0].trim() : "";
        String surname = sp.length > 1 ? sp[1].trim() : "";
        String middlename = sp.length > 2 ? sp[2].trim() : "";
        String bdate = sp.length > 3 ? sp[3].trim() : "";

        ent.setPatientName(name);
        ent.setPatientSurname(surname);
        ent.setPatientMiddleName(middlename);
        ent.setPatientBirthDate(bdate);
//        ent.addOrStatementField("patientName");
//        ent.addOrStatementField("patientSurname");
//        ent.addOrStatementField("patientMiddleName");
//        ent.addOrStatementField("patientId");

        if (fname.trim().length() == 0) {
            ent.setEndLimit(200);
            ent.addSortBy("id");
            ent.setSortByAsc(false);
        } else {
            ent.addSortBy("patientName");
            ent.setSortByAsc(true);
        }

        Carrier crPtnt = EntityManager.select(ent);
        String tn = ent.toTableName();
        int rc = crPtnt.getTableRowCount(tn);
        Carrier ocr = new Carrier();
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(crPtnt, tn, i, ent);
            String id = ent.getId();
            String fullname = ent.getPatientName() + " " + ent.getPatientSurname() + " "
                    + ent.getPatientMiddleName() + " (" + ent.getPatientBirthDate() + ")";
            ocr.setValue(CoreLabel.RESULT_SET, i, "patientName", fullname);
            ocr.setValue(CoreLabel.RESULT_SET, i, "id", id);
        }

        return ocr;
    }

    public static Carrier insertNewInspection1(Carrier carrier) throws QException {

        EntityCrInspection ent = new EntityCrInspection();
        ent.setInspectionDate(QDate.getCurrentDate());
        ent.setInspectionTime(QDate.getCurrentTime());
        ent.setFkUserId(SessionManager.getCurrentUserId());
        ent.setFkPatientId(carrier.getValue("fkPatientId").toString());
        ent.setInspectionCode(carrier.getValue("fkSessionId").toString());
//        ent.setLang(SessionManager.getCurrentLang());

        String keys[] = carrier.getKeys();
        for (String key : keys) {
            String k = key.startsWith(PREFIX_SUBMODULE_ATTRIBUTE)
                    ? key.split("_")[1] : "";
            String val = k.trim().length() > 0 ? carrier.getValue(key).toString() : "";

            if (k.trim().length() > 0) {
                String haVal = val.trim().equals(PREFIX_SUBMODULE_ATTRIBUTE_HA_CODE)
                        ? carrier.getValue(PREFIX_SUBMODULE_ATTRIBUTE_HA + k).toString() : "";
                ent.setHaInspectionValue(haVal);
                ent.setFkSubmoduleAttributeId(k);
                ent.setInspectionValue(val);
                EntityManager.insert(ent);
            }
        }

        return carrier;
    }

    public Carrier insertNewInspection(Carrier carrier) throws QException {
        String fkPatientId = carrier.getValue("fkPatientId").toString();
        String inspectionCode = carrier.getValue("fkSessionId").toString();

        String keys[] = carrier.getKeys();
        for (String key : keys) {
            String k = key.startsWith(PREFIX_SUBMODULE_ATTRIBUTE)
                    ? key.split("_")[1] : "";
            String val = k.trim().length() > 0 ? carrier.getValue(key).toString() : "";

            if (k.trim().length() > 0) {
                EntityCrInspection ent = new EntityCrInspection();
                ent.setFkPatientId(fkPatientId);
//                ent.setFkUserId(SessionManager.getCurrentUserId());
                ent.setInspectionCode(inspectionCode);
                ent.setFkSubmoduleAttributeId(k);
                Carrier c = EntityManager.select(ent);

                if (c.getTableRowCount(ent.toTableName()) > 0) {
                    String haVal = val.trim().equals(PREFIX_SUBMODULE_ATTRIBUTE_HA_CODE)
                            ? carrier.getValue(PREFIX_SUBMODULE_ATTRIBUTE_HA + k).toString() : "";
                    ent.setHaInspectionValue(haVal);
                    ent.setInspectionValue(val);
                    EntityManager.update(ent);
                } else {
                    String haVal = val.trim().equals(PREFIX_SUBMODULE_ATTRIBUTE_HA_CODE)
                            ? carrier.getValue(PREFIX_SUBMODULE_ATTRIBUTE_HA + k).toString() : "";
//                    if (haVal.trim().length() > 0 || val.trim().length() > 0) {
                    ent.setHaInspectionValue(haVal);
                    ent.setInspectionDate(QDate.getCurrentDate());
                    ent.setInspectionTime(QDate.getCurrentTime());
                    ent.setFkUserId(SessionManager.getCurrentUserId());
                    ent.setFkSubmoduleAttributeId(k);
                    ent.setInspectionValue(val);
                    EntityManager.insert(ent);
//                    }
                }
            }
        }

        EntityCrAppointment entAppt = new EntityCrAppointment();
        entAppt.setId(inspectionCode);
        EntityManager.select(entAppt);
        if (entAppt.getFkPatientId().length() > 0) {
            entAppt.setAppointmentStatus(APPOINTMENT_STATUS_IN_PROCESS);
            EntityManager.update(entAppt);
        }

        return carrier;
    }

//    public Carrier deleteInspection(Carrier carrier) throws QException {
//        EntityCrInspection ent = new EntityCrInspection();
//        EntityManager.mapCarrierToEntity(carrier, ent);
//        EntityManager.delete(ent);
//        return carrier;
//    }
    public static Carrier getInspectionListBySession(Carrier carrier) throws QException {
        EntityCrAppointment entApp = new EntityCrAppointment();
        entApp.setId(carrier.getValue("fkSessionId").toString());
        EntityManager.select(entApp);

        if (entApp.getFkDoctorUserId().length() == 0) {
            carrier.addController("general", EntityManager.getMessageText("sessionIsNotExist"));
            return carrier;
        }

        EntityCrSubmoduleAttribute entSA = new EntityCrSubmoduleAttribute();
        entSA.setFkSubmoduleId(carrier.getValue("fkSubmoduleId").toString());
        Carrier crSA = EntityManager.select(entSA);

        if (crSA.getTableRowCount(entSA.toTableName()) == 0) {
            return carrier;
        }

        String ids = crSA.getValueLine(entSA.toTableName());

        EntityCrInspection ent = new EntityCrInspection();
        ent.setInspectionCode(entApp.getId());
        ent.setFkPatientId(entApp.getFkPatientId());
        ent.setFkSubmoduleAttributeId(ids);
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
//        c.removeColoumn(CoreLabel.RESULT_SET, "fkCompanyId");
//        c.addTableSequence(CoreLabel.RESULT_SET,
//                EntityManager.getListSequenceByKey("getInspectionList"));

//        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
        return c;
    }

    private static Carrier getUserInfo4InsList(Carrier carrier) throws QException {
//          System.out.println("get inspection  -> "+   cIns.getJson());
        //get user info
        EntityCrUserList entUser = new EntityCrUserList();
        entUser.setDeepWhere(false);
        entUser.setUserPersonName(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonSurname(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonMiddlename(carrier.getValue("doctorFullname").toString());
        entUser.addOrStatementField("userPersonName");
        entUser.addOrStatementField("userPersonSurname");
        entUser.addOrStatementField("userPersonMiddlename");
        Carrier c = EntityManager.select(entUser);
        c.renameTableName(entUser.toTableName(), CoreLabel.RESULT_SET);
        return c;
    }

    private static String getIdsForInspectionList(Carrier carrier, String filterValue) throws QException {

        String st = "";
        try {
            String keys[] = carrier.getKeys();

            for (String k : keys) {
                String val = carrier.getValue(k).toString();
                if (val == null) {
                    continue;
                }

                if (new DeepWhere(val, filterValue).isMatched()) {
                    st += k.substring(0, k.length() - 3) + CoreLabel.IN;
                } else {
                    carrier.removeKey(k);
                }
            }
        } catch (Exception e) {

        }
        return st;
    }

    public static Carrier getInspectionList(Carrier carrier) throws QException {
        Carrier cIns = new Carrier();
        try {
            Carrier cprSex = QUtility.getListItem("sex",
                    carrier.getValue("sexName").toString());
            String sex = convertArrayToFilterLine(cprSex.getKeys());

            Carrier cAttr = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_ATTRIBUTE);//getAttributeList(carrier);
            String fkAttributeIds = getIdsForInspectionList(cAttr,
                    carrier.getValue("attributeName").toString());

            Carrier cSM = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_SUBMODULE);
            String fkSubmoduleIds = getIdsForInspectionList(cSM,
                    carrier.getValue("submoduleName").toString());

            Carrier cModule = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_MODULE);
            String fkModuleIds = getIdsForInspectionList(cModule,
                    carrier.getValue("moduleName").toString());

            EntityCrInspectionList ent = new EntityCrInspectionList();
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.setFkAttributeId(fkAttributeIds);
            ent.setFkModuleId(fkModuleIds);
            ent.setFkSubmoduleId(fkSubmoduleIds);
            ent.setSex(sex);
            String tnIns = ent.toTableName();
            cIns = EntityManager.select(ent);

            cIns.mergeCarrier(tnIns, "sex", "sexName", cprSex);

            cIns.mergeCarrier(tnIns, new String[]{"fkAttributeId", "LANG"},
                    "attributeName", cAttr);

            cIns.mergeCarrier(tnIns, new String[]{"fkModuleId", "LANG"},
                    "moduleName", cModule);

            cIns.mergeCarrier(tnIns, new String[]{"fkSubmoduleId", "LANG"},
                    "submoduleName", cSM);

            cIns.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
//            System.out.println(cIns.toXML());
            cIns = addFinalValue(cIns, carrier.getValue("finalValue").toString());

            cIns.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getInspectionList"));;

            cIns.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);

        } catch (Exception e) {
//            System.out.println("getInspectionList error >>>> " + e.getMessage());
            e.printStackTrace();
        }
        return cIns;
    }

    public static Carrier getInspectionList_cache_old(Carrier carrier) throws QException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(">>>>> start >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(" ");
        System.out.println(" ");

        String sl = carrier.getValue("startLimit").toString();
        String el = carrier.getValue("endLimit").toString();
        //get userlist
        Carrier crUser = getUserInfo4InsList(carrier);
        Carrier cprUser = crUser.getKeyValuesPairFromTable(CoreLabel.RESULT_SET,
                new String[]{"id"}, " ",
                new String[]{"userPersonName", "userPersonSurname", "userPersonMiddlename"}, " ");
        String fkUserIds = crUser.getValueLine(CoreLabel.RESULT_SET);

        //get patient info
        Carrier crPatient = getPatientList4Appointment(carrier);
        String fkPatientIds = crPatient.getValueLine(CoreLabel.RESULT_SET);

        //get  and merge submodule attribute info
        Carrier crSA = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_SUBMODULE_ATTRIBUTE); //getSubmoduleAttributeList(carrier);

        //get and merge attribute info
        Carrier cAttr = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_ATTRIBUTE);//getAttributeList(carrier);
//        System.out.println(" ");

        String fkAttributeIds = getIdsForInspectionList(cAttr,
                carrier.getValue("attributeName").toString());

        //get and merge module info
        Carrier cModule = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_MODULE);
        String fkModuleIds = getIdsForInspectionList(cModule,
                carrier.getValue("moduleName").toString());

        //get and merge submodule info
        Carrier cSM = CacheUtil.getFromCache(CacheUtil.CACHE_KEY_SUBMODULE);
        String fkSubmoduleIds = getIdsForInspectionList(cSM,
                carrier.getValue("submoduleName").toString());

        EntityCrInspection ent = new EntityCrInspection();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setFkUserId(fkUserIds);
        ent.setFkPatientId(fkPatientIds);
        Carrier cIns = EntityManager.select(ent);
        String tnIns = ent.toTableName();
        cIns.copyTableColumn(tnIns,
                "fkSubmoduleAttributeId", "saSubmoduleAttributeId");

        //merge statements;
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> cashler oxundu >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");
        cIns.mergeCarrier(tnIns, "fkUserId", "doctorFullname", cprUser);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> cprUser   >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, "fkPatientId", crPatient, CoreLabel.RESULT_SET,
                "id", new String[]{"patientId", "patientImage", "patientName",
                    "patientSurname", "patientMiddleName", "patientBirthDate",
                    "patientBirthPlace", "sex", "sexName"});
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> crPatient >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, "fkSubmoduleAttributeId", crSA, CoreLabel.RESULT_SET,
                "id", new String[]{"fkAttributeId", "fkValueTypeId", "fkModuleId",
                    "fkSubmoduleId", "submoduleValue", "hasOther",
                    "sortBy"});
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> crSA >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, new String[]{"fkAttributeId", "LANG"},
                "attributeName", cAttr);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> cAttr name >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, "fkAttributeId", "attributeCode", cAttr);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> cAttr code>>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, new String[]{"fkModuleId", "LANG"},
                "moduleName", cModule);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>>  cModule>>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        cIns.mergeCarrier(tnIns, new String[]{"fkSubmoduleId", "LANG"},
                "submoduleName", cSM);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> submoduleName  >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        ////
        cIns.renameTableName(tnIns, CoreLabel.RESULT_SET);

        ///  generate finalValue
        cIns = addFinalValue(cIns, carrier.getValue("finalValue").toString());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(">>>>> final value >>>>>>>>" + QDate.getCurrentSecond() + QDate.getCurrentMillisecond());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(" ");
//        System.out.println(" ");

        /////
        cIns.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getInspectionList"));

        cIns.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);
        return cIns;
    }

    private static Carrier getUserInfo4InsListNew(Carrier carrier, String id) throws QException {
        EntityCrUserList entUser = new EntityCrUserList();
        entUser.setDeepWhere(false);
        entUser.setId(id);
        entUser.setUserPersonName(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonSurname(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonMiddlename(carrier.getValue("doctorFullname").toString());
        entUser.addOrStatementField("userPersonName");
        entUser.addOrStatementField("userPersonSurname");
        entUser.addOrStatementField("userPersonMiddlename");
        Carrier crUser = EntityManager.select(entUser);
        crUser.renameTableName(entUser.toTableName(), CoreLabel.RESULT_SET);
        return crUser;
    }

    public static Carrier getInspectionList__(Carrier carrier) throws QException {
        String sl = carrier.getValue("startLimit").toString();
        String el = carrier.getValue("endLimit").toString();

        EntityCrInspection ent = new EntityCrInspection();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier cIns = EntityManager.select(ent);
        String tnIns = ent.toTableName();
        cIns.copyTableColumn(tnIns,
                "fkSubmoduleAttributeId", "saSubmoduleAttributeId");
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");
        String fkUserIds = cIns.getValueLine(tnIns, EntityCrInspection.FK_USER_ID);

//        System.out.println("get inspection  -> "+   cIns.getJson());
        //get user info
        Carrier crUser = getUserInfo4InsListNew(carrier, fkUserIds);
        Carrier cprUser = crUser.getKeyValuesPairFromTable(CoreLabel.RESULT_SET,
                new String[]{"id"}, " ",
                new String[]{"userPersonName", "userPersonSurname", "userPersonMiddlename"}, " ");
        cIns.mergeCarrier(tnIns, "fkUserId", "doctorFullname", cprUser);

//         out.println("merget with EntityCrUserList  -> "+   cIns.getJson());
        //get and merge patient info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkPatientId"));
        Carrier crPatient = getPatientList(carrier);
        cIns.mergeCarrier(tnIns, "fkPatientId", crPatient, CoreLabel.RESULT_SET,
                "id", new String[]{"patientId", "patientImage", "patientName",
                    "patientSurname", "patientMiddleName", "patientBirthDate",
                    "patientBirthPlace", "sex", "sexName"});

        //get  and merge submodule attribute info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkSubmoduleAttributeId"));
        Carrier crSA = getSubmoduleAttributeList(carrier);
        cIns.mergeCarrier(tnIns, "fkSubmoduleAttributeId", crSA, CoreLabel.RESULT_SET,
                "id", new String[]{"fkAttributeId", "fkValueTypeId", "fkModuleId",
                    "fkSubmoduleId", "submoduleValue", "hasOther",
                    "sortBy"});

        //get and merge attribute info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkAttributeId"));
        Carrier cAttr = getAttributeList(carrier);
        cIns.mergeCarrier(tnIns, "fkAttributeId", cAttr, CoreLabel.RESULT_SET,
                "id", new String[]{"attributeCode", "attributeName"});

        //get and merge module info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkModuleId"));
        Carrier cModule = getModuleList(carrier);
        cIns.mergeCarrier(tnIns, "fkModuleId", cModule, CoreLabel.RESULT_SET,
                "id", new String[]{"moduleName"});

        //get and merge submodule info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkSubmoduleId"));
        Carrier cSM = getSubmoduleList(carrier);
        cIns.mergeCarrier(tnIns, "fkSubmoduleId", cSM, CoreLabel.RESULT_SET,
                "id", new String[]{"submoduleName"});

        ////
        cIns.renameTableName(tnIns, CoreLabel.RESULT_SET);

        ///  generate finalValue
        cIns = addFinalValue(cIns, carrier.getValue("finalValue").toString());

        /////
        cIns.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getInspectionList"));

        cIns.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);

        return cIns;
    }

    private static Carrier addFinalValue(Carrier cr, String filterValue) throws QException {
        String tn = CoreLabel.RESULT_SET;
        int rc = cr.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String insValue = cr.getValue(tn, i, "inspectionValue").toString();
            String haValue = cr.getValue(tn, i, "haInspectionValue").toString();
            String vtId = cr.getValue(tn, i, "fkValueTypeId").toString();
            String finalVal = "";
            String haEmptyVal = "__2__";
            if (insValue.equals(haEmptyVal)) {
                finalVal = haValue;
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_RANGE_STRING)) {
                String smVal = cr.getValue(tn, i, "submoduleValue").toString();
                finalVal = QUtility.getListItemValue(smVal, insValue);
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_RANGE_STRING_MULTI)) {
                String smVal = cr.getValue(tn, i, "submoduleValue").toString();
                String[] insValMulti = insValue.split(CoreLabel.SEPERATOR_VERTICAL_LINE);
                String val = "";
                for (String v : insValMulti) {
                    if (v.trim().length() > 0) {
                        val += QUtility.getListItemValue(smVal, v) + ", ";
                    }
                }
                val = val.substring(0, val.length() - 2);
                finalVal = val;
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_PICTURE_URL)) {
                finalVal = "<i class=\"apd-image-trigger fa fa-picture-o\" "
                        + " style=\" font-size:14px;color:#00b289\""
                        + " apd_image_url=\"" + insValue + "\""
                        + " apd_image_alt=\"" + insValue + "\""
                        + " data-toggle=\"modal\" "
                        + " data-target=\"#apdImageViewer\" "
                        + " aria-hidden=\"true\" ></i>";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_PICTURE)) {
                finalVal = "<i class=\"apd-image-trigger fa fa-picture-o\" "
                        + " style=\" font-size:14px;color:#00b289\""
                        + " apd_image_url=\"resources/upload/" + insValue + "\"  "
                        + " apd_image_alt=\"+insValue+\""
                        + " data-toggle=\"modal\" data-target=\"#apdImageViewer\""
                        + " aria-hidden=\"true\" ></i>";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_VIDEO_URL)) {
                finalVal = "<i class=\"apd-video-trigger fa fa-video-camera\" "
                        + " style=\" font-size:14px;color:#00b289\""
                        + " apd_video_url=\"" + insValue + "\""
                        + " data-toggle=\"modal\" data-target=\"#apdVideoPlayer\""
                        + " aria-hidden=\"true\" ></i>";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_VIDEO_UPLOAD)) {
                finalVal = " <i class=\"apd-video-trigger fa fa-video-camera\""
                        + " style=\" font-size:14px;color:#00b289\""
                        + " apd_video_url=\"resources/upload/" + insValue + "\"  "
                        + " data-toggle=\"modal\" data-target=\"#apdVideoPlayer\""
                        + " aria-hidden=\"true\" ></i> ";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_SOUND_UPLOAD)) {
//                finalVal = " <i class=\"apd-sound-trigger fa fa-microphone\""
//                        + " style=\" font-size:14px;color:#00b289\""
//                        + " apd_sound_url=\"resources/upload/" + insValue + "\"  "
//                        + " data-toggle=\"modal\" data-target=\"#apdSoundPlayer\""
//                        + " aria-hidden=\"true\" ></i> ";
                finalVal = "<audio controls style='width: 100px;'> "
                        + "<source src=\"resources/upload/" + insValue + "\" type=\"audio/mpeg\"> "
                        + "</audio>";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_YOUTUBE_URL)) {
                finalVal = " <a class=\"youtube\" "
                        + " href=\"" + insValue + "\">"
                        + " <i class=\"fa fa-youtube-square\" "
                        + " style=\" font-size:16px;color:#00b289\""
                        + " aria-hidden=\"true\" ></i></a>";
            } else if (!insValue.equals(haEmptyVal) && vtId.equals(VALUE_TYPE_RANGE_INTEGER_MULTI)) {
                finalVal = insValue.replace("|", ", ");
            } else {
                finalVal = insValue;
            }
            if (new DeepWhere(finalVal, filterValue).isMatched()) {
                cr.setValue(tn, i, "finalValue", finalVal);
            } else {
                cr.removeRow(tn, i);
                i--;
                rc--;
            }
        }
        return cr;

    }

    public Carrier getInspectionCodeList(Carrier carrier) throws QException {

        EntityCrInspectionList ent = new EntityCrInspectionList();
        ent.setFkModuleId(carrier.getValue("fkModuleId").toString());
        ent.setFkUserId(SessionManager.getCurrentUserId());
        ent.setFkPatientId(carrier.getValue("fkPatientId").toString());
        ent.addDistinctField("inspectionCode");
        ent.addSortBy("inspectionCode");
        ent.setSortByAsc(false);
        carrier = EntityManager.select(ent);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier tc = new Carrier();
        tc.setValue(CoreLabel.RESULT_SET, 0, "inspectionCode", "-2");
        tc.setValue(CoreLabel.RESULT_SET, 0, "inspectionValue", "Inspections");

        tc.setValue(CoreLabel.RESULT_SET, 1, "inspectionCode", "-1");
        tc.setValue(CoreLabel.RESULT_SET, 1, "inspectionValue", "New Inspection");

        String tn = ent.toTableName();
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {

            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);
            String insCode = ent.getInspectionCode();
            String val = "";
            try {
                val += QDate.convertToDateString(insCode.substring(0, 8));
                val += " / ";
                val += QDate.convertToTimeString(insCode.substring(8, 15));
            } catch (Exception e) {
            }
            if (val.trim().length() > 0) {
                tc.setValue(CoreLabel.RESULT_SET, (i + 2), "inspectionCode", insCode);
                tc.setValue(CoreLabel.RESULT_SET, (i + 2), "inspectionValue", val);
            }

        }

        return tc;
    }

    public static Carrier getNewSInspectionCode(Carrier carrier) throws QException {
        String insCode = getNewSInspectionCodeLine();
        carrier.setValue("insCode", insCode);
        return carrier;
    }

    private static String getNewSInspectionCodeLine() throws QException {
        String insCode = IdGenerator.getId();
        return insCode;
    }

    public static Carrier insertNewReportLine(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setFkUserId(SessionManager.getCurrentUserId());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateReportLine(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteReportLine(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public Carrier getReportLineList(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        EntityManager.mapCarrierToEntity(carrier, ent);
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getReportLineList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
        return c;
    }

    public Carrier getReportLineList4Appt(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        ent.setReportType(REPORT_TYPE_APPOINTMENT);
        Carrier c = EntityManager.select(ent);

        int rc = c.getTableRowCount(ent.toTableName());
        Carrier nc = new Carrier();
        for (int i = 0; i < rc; i++) {
            nc.setValue(CoreLabel.RESULT_SET, i, "id",
                    c.getValue(ent.toTableName(), i, "id"));
            nc.setValue(CoreLabel.RESULT_SET, i, "name",
                    c.getValue(ent.toTableName(), i, "reportName"));
        }

        return nc;
    }

    public Carrier getReportLineList4Payment(Carrier carrier) throws QException {
        EntityCrReportLine ent = new EntityCrReportLine();
        ent.setReportType(REPORT_TYPE_PAYMENT);
        Carrier c = EntityManager.select(ent);

        int rc = c.getTableRowCount(ent.toTableName());
        Carrier nc = new Carrier();
        for (int i = 0; i < rc; i++) {
            nc.setValue(CoreLabel.RESULT_SET, i, "id",
                    c.getValue(ent.toTableName(), i, "id"));
            nc.setValue(CoreLabel.RESULT_SET, i, "name",
                    c.getValue(ent.toTableName(), i, "reportName"));
        }

        return nc;
    }

    public Carrier getReportLineList4Print(Carrier carrier) throws QException {
        EntityCrAppointment ent = new EntityCrAppointment();
        ent.setId(carrier.getValue("fkSessionId").toString());
        EntityManager.select(ent);

        QReportCarrier rc = new QReportCarrier();
        rc.Patient.setPatientInfoById(ent.getFkPatientId());
        rc.setReportId(carrier.getValue("id").toString());
        rc.setSessionId(carrier.getValue("fkSessionId").toString());
//        rc.setPaymentId("201708230752140570");
        String arg = QReport.getReport(rc);
        Carrier c = new Carrier();
        c.setValue(CoreLabel.RESULT_SET, 0, "reportHtml", arg);

        return c;
    }

    public Carrier getReportLineList4PrintPayment(Carrier carrier) throws QException {
        QReportCarrier rc = new QReportCarrier();
        rc.setReportId(carrier.getValue("id").toString());
        rc.setPaymentId(carrier.getValue("fkPaymentId").toString());

        String arg = QReport.getReport(rc);
        Carrier c = new Carrier();
        c.setValue(CoreLabel.RESULT_SET, 0, "reportHtml", arg);

        return c;
    }

    public static Carrier insertNewInspectionMatrix(Carrier carrier) throws QException {
        int rc = carrier.getTableRowCount("matrixTbl");
        if (rc == 0) {
            return carrier;
        }

        EntityCrInspectionMatrixMain ent = new EntityCrInspectionMatrixMain();
        ent.setDeepWhere(false);
        ent.setFkUserId(SessionManager.getCurrentUserId());
        ent.setMatrixName(carrier.getValue("matrixName").toString());
        Carrier c = EntityManager.select(ent);

        if (c.getTableRowCount(ent.toTableName()) == 0) {
            EntityManager.insert(ent);
        } else {
            EntityCrInspectionMatrix entC = new EntityCrInspectionMatrix();
            entC.setFkParentId(ent.getId());
            String ids = EntityManager.select(entC).
                    getValueLine(entC.toTableName(), "id", CoreLabel.IN);
            if (ids.length() > 0) {
                entC.setId(ids);
                EntityManager.delete(entC);
            }
        }

        for (int i = 0; i < rc; i++) {
            EntityCrInspectionMatrix entC = new EntityCrInspectionMatrix();
            entC.setFkParentId(ent.getId());
            entC.setFkSubmoduleAttributeId(
                    carrier.getValue("matrixTbl", i, "fkSubmoduleAttibutesId").toString());
            entC.setShortName(
                    carrier.getValue("matrixTbl", i, "shortName").toString());
            EntityManager.insert(entC);
        }
        return carrier;
    }

    public Carrier deleteInspectionMatrix(Carrier carrier) throws QException {
        String id = carrier.getValue("id").toString();
        if (id.length() == 0) {
            return carrier;
        }

        EntityCrInspectionMatrixMain ent = new EntityCrInspectionMatrixMain();
        ent.setId(id);
        EntityManager.delete(ent);

        EntityCrInspectionMatrix entC = new EntityCrInspectionMatrix();
        entC.setFkParentId(id);
        String ids = EntityManager.select(entC).
                getValueLine(entC.toTableName(), "id", CoreLabel.IN);
        if (ids.length() > 0) {
            entC.setId(ids);
            EntityManager.delete(entC);
        }

        return carrier;
    }

    public static Carrier getInspectionMatrixList(Carrier carrier) throws QException {
        EntityCrInspectionMatrixMain entMain = new EntityCrInspectionMatrixMain();
        EntityManager.mapCarrierToEntity(carrier, entMain);
        entMain.setFkUserId(SessionManager.getCurrentUserId());
        Carrier cMain = EntityManager.select(entMain);
        String tnMain = entMain.toTableName();
        Carrier cprMain = cMain.getKVFromTable(tnMain, "id", "matrixName");
        String ids = cMain.getValueLine(tnMain);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        EntityCrInspectionMatrix entInsMat = new EntityCrInspectionMatrix();
        entInsMat.setFkParentId(ids);
        Carrier cInsMat = EntityManager.select(entInsMat);
        String tnInsMat = entInsMat.toTableName();
        String saIds = cInsMat.getValueLine(entInsMat.toTableName(),
                "fkSubmoduleAttributeId");

        cInsMat.mergeCarrier(tnInsMat, "fkParentId",
                "matrixName", cprMain);

        EntityCrSubmoduleAttribute entSA = new EntityCrSubmoduleAttribute();
        entSA.setId(saIds);
        Carrier cSA = EntityManager.select(entSA);
        Carrier cprSA = cSA.getKeyValuesPairFromTable(entSA.toTableName(),
                "id", "fkAttributeId");
        String attrIds = cSA.getValueLine(entSA.toTableName(),
                EntityCrSubmoduleAttribute.FK_ATTRIBUTE_ID);

        cInsMat.mergeCarrier(tnInsMat, "fkSubmoduleAttributeId",
                "fkAttributeId", cprSA);

        Carrier cAttr = new Carrier();
        cAttr.setValue("id", attrIds);
        cAttr = CrModel.getAttributeList(cAttr);
        Carrier cprAttr = cAttr.getKeyValuesPairFromTable(
                CoreLabel.RESULT_SET, "id", "attributeName");

        cInsMat.mergeCarrier(tnInsMat, "fkAttributeId",
                "attributeName", cprAttr);

        cInsMat.renameTableName(tnInsMat, CoreLabel.RESULT_SET);
        return cInsMat;
    }

    public Carrier getInspectionMatrixMainList(Carrier carrier) throws QException {
        EntityCrInspectionMatrixMain ent = new EntityCrInspectionMatrixMain();
        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);
        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        return c;
    }

    private String getSplitedValue(String key, String value) {
        value = value.replace(CoreLabel.FAIZ, " ");
        value = value.replace(CoreLabel.IN, " ");
        String[] subValue = value.trim().split(" ");
        String ln = "";
        for (String n : subValue) {
            if (n.trim().length() != 0) {
                String tvalue = CoreLabel.FAIZ + n + CoreLabel.FAIZ;
                ln += key + " LIKE '" + tvalue + "'" + " OR ";
            }
        }
        ln = ln.trim().length() > 0 ? ln.substring(0, ln.length() - 3) : ln;
        return ln;
    }

    public static void main(String[] arg) {

        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            SessionManager.setConnection(Thread.currentThread().getId(), conn);
            SessionManager.setDomain(SessionManager.getCurrentThreadId(), "apd_1lqliu3");
            SessionManager.setUserId(SessionManager.getCurrentThreadId(), "201710230029010648");
            SessionManager.setCompanyId(SessionManager.getCurrentThreadId(), "201710221851270308");

            String json = "{\"kv\":{\"fkSubmoduleId\":\"201707071628090571\",\"fkSessionId\":\"201711030736460852 \"}}"
                    + "";

//            String servicename = "serviceCrGetTermPage";
            //
            Carrier c = new Carrier();
            c.fromJson(json);
            c = getSubmoduleFormBody(c);
//
//            c.setServiceName(servicename);
//            c.fromJson(json);
////            System.out.println(c.getJson());
//            //            System.out.println(c.getJson());
//            Response callService = CallDispatcher.callService(c);
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

    public static Carrier getVoiceAnalyse(Carrier carrier) throws QException {
        String filename = carrier.getValue("filename").toString();
        carrier = getPraatAnalyseResult(filename);
        return carrier;
    }

    private static Carrier getPraatAnalyseResult(String filename) {
        Carrier carrier = new Carrier();
        try {

            String ext = ".wav";
            String praatline = Config.getPraatCommand();
            praatline += filename;
//            System.out.println("praat line-> " + praatline);
            Process p = Runtime.getRuntime().exec(praatline);

            p.waitFor();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );

            BufferedReader stdError = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            //'Min','Max','Mean','Median','Std','Jitter(local,%)','Shimmer(local,%)','HNR','NHR'
            //data ardiciliigi
//            String min = "undefined", max = "undefined", mean = "undefined",
//                    median = "undefined", std = "undefined";
//            String jitter_loc = "undefined", shimmer_loc = "undefined", 
//                    hnr = "undefined", nhr = "undefined";
            String min = "-1", max = "-1", mean = "-1",
                    median = "-1", std = "-1";
            String jitter_loc = "-1", shimmer_loc = "-1",
                    hnr = "-1", nhr = "-1";

            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println("praat resposse line"+line);
                String[] st = line.split(",");
                if (st.length == 9) {
                    min = st[0];
                    max = st[1];
                    mean = st[2];
                    median = st[3];
                    std = st[4];
                    jitter_loc = st[5];
                    shimmer_loc = st[6];
                    hnr = st[7];
                    nhr = st[8];
                }
            }

            carrier.setValue("min", min);
            carrier.setValue("max", max);
            carrier.setValue("mean", mean);
            carrier.setValue("median", median);
            carrier.setValue("std", std);
            carrier.setValue("jitter_loc", jitter_loc);
            carrier.setValue("shimmer_loc", shimmer_loc);
            carrier.setValue("hnr", hnr);
            carrier.setValue("nhr", nhr);

            line = "";
            while ((line = stdError.readLine()) != null) {
                System.err.println(line);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        return carrier;
    }

    public static Carrier getInspectionMatrixBodyList(Carrier carrier) throws QException {

        if (!carrier.isKeyExist("matrixId")) {
            return carrier;
        }

        EntityCrInspectionMatrix entMain = new EntityCrInspectionMatrix();
        entMain.setFkParentId(carrier.getValue("matrixId").toString());
        Carrier cInsMat = EntityManager.select(entMain);
        String tnInsMat = entMain.toTableName();
        Carrier cprSaShortName = cInsMat.getKeyValuesPairFromTable(tnInsMat,
                "fkSubmoduleAttributeId", "shortName");
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        if (cInsMat.getTableRowCount(tnInsMat) == 0) {
            return carrier;
        }

        String saIds = cInsMat.getValueLine(tnInsMat, "fkSubmoduleAttributeId");
        carrier.setValue("fkSubmoduleAttributeId", saIds);
        Carrier cIns = getInspectionList(carrier);
        String tnIns = CoreLabel.RESULT_SET;

        int rcIns = cIns.getTableRowCount(tnIns);
        Carrier cFinal = new Carrier();
        Map<String, Integer> sessionIdNRow = new HashMap<>();
        int idx = 0;

        String[] seqArr = {"doctorFullname", "inspectionDate", "inspectionTime",
            "patientName", "patientSurname", "patientMiddleName",
            "patientBirthDate", "patientBirthPlace",
            "sexName", "moduleName", "submoduleName"};
        ArrayList<String> seqList = new ArrayList<>(Arrays.asList(seqArr));
        ArrayList<Integer> removeRows = new ArrayList<>();

        for (int i = 0; i < rcIns; i++) {
            String sessId = cIns.getValue(tnIns, i, "inspectionCode").toString();
            String saId = cIns.getValue(tnIns, i, "fkSubmoduleAttributeId").toString();
//            String attrName = cIns.getValue(tnIns, i, "attributeName").toString();
            String attrCode = cIns.getValue(tnIns, i, "attributeCode").toString();
            String finalVal = cIns.getValue(tnIns, i, "finalValue").toString();

            if (!sessionIdNRow.containsKey(sessId)) {
                cFinal.setValue(tnIns, idx, "id", sessId);
                cFinal.setValue(tnIns, idx, "doctorFullname",
                        cIns.getValue(tnIns, i, "doctorFullname"));
                cFinal.setValue(tnIns, idx, "inspectionDate",
                        cIns.getValue(tnIns, i, "inspectionDate"));
                cFinal.setValue(tnIns, idx, "inspectionTime",
                        cIns.getValue(tnIns, i, "inspectionTime"));
                cFinal.setValue(tnIns, idx, "patientName",
                        cIns.getValue(tnIns, i, "patientName"));
                cFinal.setValue(tnIns, idx, "patientSurname",
                        cIns.getValue(tnIns, i, "patientSurname"));
                cFinal.setValue(tnIns, idx, "patientMiddleName",
                        cIns.getValue(tnIns, i, "patientMiddleName"));
                cFinal.setValue(tnIns, idx, "patientBirthDate",
                        cIns.getValue(tnIns, i, "patientBirthDate"));
                cFinal.setValue(tnIns, idx, "patientBirthPlace",
                        cIns.getValue(tnIns, i, "patientBirthPlace"));
                cFinal.setValue(tnIns, idx, "sexName",
                        cIns.getValue(tnIns, i, "sexName"));
                cFinal.setValue(tnIns, idx, "moduleName",
                        cIns.getValue(tnIns, i, "moduleName"));
                cFinal.setValue(tnIns, idx, "submoduleName",
                        cIns.getValue(tnIns, i, "submoduleName"));

                sessionIdNRow.put(sessId, idx);
                idx++;
            }

            int idy = sessionIdNRow.get(sessId);
            //do filter by attirbutes
            String filterVal = carrier.getValue(attrCode).toString();
            cFinal.setValue(tnIns, idy, attrCode, finalVal);
            if (!new DeepWhere(finalVal, filterVal).isMatched()) {
                removeRows.add(idy);
            }

            if (!seqList.contains(attrCode)) {
                seqList.add(attrCode);
            }
        }

        int remIdx = 0;
        for (int k = 0; k < removeRows.size(); k++) {
            cFinal.removeRow(tnIns, removeRows.get(k) - remIdx);
            remIdx++;
        }

        String seq = Arrays.toString(seqList.toArray());
        seq = seq.substring(1, seq.length() - 1).replace(", ", ",");
        cFinal.addTableSequence(CoreLabel.RESULT_SET, seq);
        cFinal.addTableRowCount(CoreLabel.RESULT_SET, 10000);
        cFinal.setMatrixId(carrier.getValue("matrixId").toString());
//        System.out.println("json final->" + cFinal.getJson());
        return cFinal;
    }

    public Carrier getInspectionMatrixBodyList_(Carrier carrier) throws QException {

        if (!carrier.isKeyExist("matrixId")) {
            return carrier;
        }

        ArrayList<String> arr = new ArrayList();
        String tblSeq = "patientFullname,coreDate,coreTime,";
        String[] cols = carrier.getKeys();
        String line = "";
        for (String st : cols) {
            if (st.startsWith("sa")) {
                String id = st.substring(2, st.length());
                String val = carrier.getValue(st).toString();
//                val = getSplitedValue("sa_" + id, val);
                val = new WhereSingle("sa_" + id, val, arr).exec();
                line += val.trim().length() > 0 ? " (" + val + ") AND" : "";
            } else if (tblSeq.contains(st)) {
                String val = carrier.getValue(st).toString();
                st = SQLGenerator.seperateTableFieldNameWithUnderscore(st).toUpperCase();
//                val = getSplitedValue(st, val);
                val = new WhereSingle(st, val, arr).exec();
                line += val.trim().length() > 0 ? " (" + val + ") AND" : "";
            }
        }
        line = line.trim().length() > 0 ? line.substring(0, line.length() - 3) : line;

        EntityCrInspectionMatrixList ent = new EntityCrInspectionMatrixList();
        ent.setFkParentId(carrier.getValue("matrixId").toString());
        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);

        String tn = ent.toTableName();
        int rc = c.getTableRowCount(tn);
        String stMax = "";
        for (int i = 0; i < rc; i++) {
            String said = c.getValue(tn, i, "fkSubmoduleAttributeId").toString();
            stMax += " max(CASE WHEN FK_SUBMODULE_ATTRIBUTE_ID =";
            stMax += "'" + said + "'";
            stMax += " THEN FINAL_VALUE END) AS sa_" + said;
            stMax += (i < rc - 1) ? ", \n" : "";

            tblSeq += "sa" + said;
            tblSeq += (i < rc - 1) ? "," : "";
        }

        String distrinctFields = carrier.isKeyExist("distinctFields")
                ? SQLGenerator.seperateTableFieldNameWithUnderscore(
                        carrier.getValue("distinctFields").toString()).toUpperCase() : "";
        String ln = " SELECT ";
        ln += distrinctFields.trim().length() > 0 ? " distinct " + distrinctFields : " * ";;
        ln += " FROM (";
        ln += " SELECT PATIENT_FULLNAME,CORE_DATE,CORE_TIME,";
        ln += stMax + "\n ";
        ln += " FROM CR_INSPECTION_LIST " + "\n ";
        ln += " GROUP BY PATIENT_FULLNAME,CORE_DATE,CORE_TIME";
        ln += ") T";
        ln += line.trim().length() > 0 ? " WHERE " + line : "";

//        System.out.println("sql->" + ln);
        String[] st1 = new String[arr.size()];
        int idx = 0;
        for (String arg : arr) {
            st1[idx] = arr.get(idx);
            idx++;
        }

        Carrier tc = EntityManager.selectBySql(ln, st1);
        tc.addTableSequence(CoreLabel.RESULT_SET, tblSeq);
        tc.addTableRowCount(CoreLabel.RESULT_SET, "1000");
        tc.setMatrixId(carrier.getValue("matrixId").toString());
        return tc;
    }

    public static Carrier getMessageText(Carrier carrier) throws QException {
        String lang = carrier.getValue("lang").toString();
        Carrier out = new Carrier();
        String messageCode = carrier.getValue("messageCode").toString();
        out.setValue("text", EntityManager.getMessageText(messageCode, lang));
        return out;
    }

    public static Carrier insertNewPriceList(Carrier carrier) throws QException {
        EntityCrPriceList ent = new EntityCrPriceList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updatePriceList(Carrier carrier) throws QException {
        EntityCrPriceList ent = new EntityCrPriceList();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deletePriceList(Carrier carrier) throws QException {
        EntityCrPriceList ent = new EntityCrPriceList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getPriceListList(Carrier carrier) throws QException {
        EntityCrPriceList ent = new EntityCrPriceList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        Carrier c = EntityManager.select(ent);
        String tnPrclst = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprCrrncy = QUtility.getListItem("currency",
                carrier.getValue("currencyName").toString());
        c.mergeCarrier(tnPrclst, "currency", "currencyName", cprCrrncy);

        Carrier cprLstStts = QUtility.getListItem("pa",
                carrier.getValue("listStatusName").toString());
        c.mergeCarrier(tnPrclst, "listStatus", "listStatusName", cprLstStts);

        c.renameTableName(tnPrclst, CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPriceList"));

        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);

        return c;
    }

    public static Carrier getCurrencyOfCompany(Carrier carrier) throws QException {
        //itemCode = currency
        carrier.setValue(CoreLabel.RESULT_SET, 0, "itemKey", "AZN");
        carrier.setValue(CoreLabel.RESULT_SET, 0, "itemValue", "AZN");
        return carrier;
    }

    public Carrier getDoctorList(Carrier carrier) throws QException {
        carrier.setValue(EntityCrUser.LI_USER_PERMISSION_CODE, "AD%IN%D");
        carrier = getUserList(carrier);
        return carrier;
    }

    public static Carrier insertNewPayment(Carrier carrier) throws QException {
        EntityCrPayment ent = new EntityCrPayment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setPaymentDate(QDate.getCurrentDate());
        ent.setPaymentTime(QDate.getCurrentTime());
        ent.setPaymentNo(genPaymentNo());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier getDiscountedPrice(Carrier carrier) throws QException {
        if (carrier.getValue("paymentDiscount").toString().length() == 0
                || carrier.getValue("fkPriceListId").toString().length() == 0) {
            carrier.setValue("price", "0");
            return carrier;
        }

        EntityCrPriceList ent = new EntityCrPriceList();
        ent.setId(carrier.getValue("fkPriceListId").toString());
        EntityManager.select(ent);
        double price = 0;
        try {
            price = Double.parseDouble(ent.getPrice());
        } catch (Exception e) {
        }

        double disprice = 0;
        try {
            double discount = Double.parseDouble(
                    carrier.getValue("paymentDiscount").toString());
            disprice = price - (price * discount / 100);
        } catch (Exception e) {
        }
        carrier.setValue("price", disprice);
        return carrier;
    }

    public Carrier updatePayment(Carrier carrier) throws QException {
        EntityCrPayment ent = new EntityCrPayment();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deletePayment(Carrier carrier) throws QException {
        EntityCrPayment ent = new EntityCrPayment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    private static Carrier getDoctorFullname(Carrier carrier) throws QException {
        EntityCrUserList entUser = new EntityCrUserList();
        EntityManager.mapCarrierToEntity(carrier, entUser);
        entUser.setDeepWhere(false);
        entUser.setUserPersonName(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonSurname(carrier.getValue("doctorFullname").toString());
        entUser.setUserPersonMiddlename(carrier.getValue("doctorFullname").toString());
        entUser.addOrStatementField("userPersonName");
        entUser.addOrStatementField("userPersonSurname");
        entUser.addOrStatementField("userPersonMiddlename");
        Carrier crUser = EntityManager.select(entUser);
        Carrier cprUser = crUser.getKeyValuesPairFromTable(entUser.toTableName(),
                new String[]{"id"}, " ",
                new String[]{"userPersonName", "userPersonSurname", "userPersonMiddlename"}, " ");
        return cprUser;
    }

    public static Carrier getPaymentList(Carrier carrier) throws QException {
        EntityCrPayment entPymnt = new EntityCrPayment();
        EntityManager.mapCarrierToEntity(carrier, entPymnt);
//        entPymnt.setFkDoctorUserId(SessionManager.getCurrentUserId());
        Carrier cPymnt = EntityManager.select(entPymnt);
        String tnPymnt = entPymnt.toTableName();
        String ptntIds = cPymnt.getValueLine(tnPymnt, "fkPatientId");
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        carrier.setValue("id", cPymnt.getValueLine(
                tnPymnt, "fkDoctorUserId").toString());
        Carrier cprDoctor = getDoctorFullname(carrier);
        cPymnt.mergeCarrier(tnPymnt, "fkDoctorUserId", "doctorFullname", cprDoctor);
        String tnPtnt = CoreLabel.RESULT_SET;

        carrier.setValue("id", ptntIds);
        Carrier cPtnt = getPatientList(carrier);
        cPymnt.mergeCarrier(tnPymnt, "fkPatientId", cPtnt, tnPtnt, "id",
                new String[]{"patientId", "patientName", "patientSurname",
                    "patientMiddleName", "sexName"});

        carrier.setValue("id", cPymnt.getValueLine(
                tnPymnt, "fkPriceListId").toString());
        Carrier cPrcLst = getPriceListList(carrier);
        cPymnt.mergeCarrier(tnPymnt, "fkPriceListId", cPrcLst, tnPtnt, "id",
                new String[]{"paymentName", "price"});

        Carrier cprCrrncy = QUtility.getListItem("currency",
                carrier.getValue("currencyName").toString());
        cPymnt.mergeCarrier(tnPymnt, "paymentCurrency", "currencyName",
                cprCrrncy);

        Carrier cprPymntStts = QUtility.getListItem("paymentStatus",
                carrier.getValue("paymentStatusName").toString());
        cPymnt.mergeCarrier(tnPymnt, "paymentStatus", "paymentStatusName",
                cprPymntStts);

        cPymnt.renameTableName(tnPymnt, CoreLabel.RESULT_SET);
        cPymnt.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPaymentList"));

        cPymnt.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(entPymnt) + 1);

        return cPymnt;
    }

    public static Carrier insertNewExpense(Carrier carrier) throws QException {
        EntityCrExpense ent = new EntityCrExpense();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateExpense(Carrier carrier) throws QException {
        EntityCrExpense ent = new EntityCrExpense();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteExpense(Carrier carrier) throws QException {
        EntityCrExpense ent = new EntityCrExpense();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getExpenseList(Carrier carrier) throws QException {
        EntityCrExpense ent = new EntityCrExpense();
        EntityManager.mapCarrierToEntity(carrier, ent);
        Carrier cExpns = EntityManager.select(ent);
        String tnExpns = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprCrrncy = QUtility.getListItem("currency",
                carrier.getValue("expenseCurrencyName").toString());
        cExpns.mergeCarrier(tnExpns, "expenseCurrency", "expenseCurrencyName",
                cprCrrncy);

        cExpns.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        cExpns.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getExpenseList"));

        cExpns.addTableRowCount(CoreLabel.RESULT_SET,
                EntityManager.getRowCount(ent) + 1);

        return cExpns;
    }

    public Carrier getIncomeReportList(Carrier carrier) throws QException {
        EntityCrIncomeReportList ent = new EntityCrIncomeReportList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        Carrier c = EntityManager.select(ent);
        String tnExpns = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprCrrncy = QUtility.getListItem("currency",
                carrier.getValue("currency").toString());
        c.mergeCarrier(tnExpns, "currency", "currency",
                cprCrrncy);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getIncomeReportList"));

        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return c;
    }

    public Carrier getUserFullname(Carrier carrier) throws QException {
        EntityCrUserList ent = new EntityCrUserList();
        ent.setId(SessionManager.getCurrentUserId());
        EntityManager.select(ent);
        carrier.setValue("userFullname", ent.getUserPersonName());
        return carrier;
    }

    public Carrier getLabel(Carrier carrier) throws QException {
        String code = carrier.getValue("code").toString();
        String t = QUtility.getLabel(code);

        carrier.setValue("text", t);
        return carrier;
    }

    public static Carrier getBasicStatistics(Carrier carrier) throws QException {
        String matrixId = carrier.getValue("fkModuleId").toString();
        String saId = carrier.getValue("fkSubmoduleAttributeId").toString();

        return carrier;
    }

    public static Carrier signupCompany(Carrier carrier) throws QException {

        EntityCrTempUser entUser = new EntityCrTempUser();
        EntityManager.mapCarrierToEntity(carrier, entUser);
        entUser.setUserShortId(IdGenerator.getId());
        entUser.setLiUserPermissionCode(USER_TYPE_ADMIN_AND_DOCTOR);
        entUser.setModule(carrier.getValue("fkModuleId").toString());
        EntityManager.insert(entUser);

        String activationId = IdGenerator.nextRandomSessionId();
        EntityCrCompany entCompany = new EntityCrCompany();
        EntityManager.mapCarrierToEntity(carrier, entCompany);
        entCompany.setCompanyLang(SessionManager.getCurrentLang());
        entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
        entCompany.setActivationId(activationId);
        entCompany.setCompanyType(EntityCrCompany.CompanyType.COMPANY.toString());
        entCompany.setActiveUserCount("1");
        entCompany.setFkUserId(entUser.getId());
        entCompany.setCompanyDb("apd_" + IdGenerator.nextDbId());
        EntityManager.insert(entCompany);

        try {

            String txt = QUtility.getLabel("mailActivationBody",
                    new String[]{entCompany.getCompanyName(), activationId, entCompany.getCompanyLang()});
            System.out.println("Activation mail body >>" + txt);
            MailSender.send(entUser.getEmail1(),
                    QUtility.getLabel("mailActivationSubject"), txt);
            System.out.println("activation mail sent to " + entCompany.getCompanyName());

        } catch (Exception e) {
            System.out.println("excepiton oldu");
        }
        return carrier;

    }

    public static Carrier signupPersonal(Carrier carrier) throws QException {

        EntityCrTempUser entUser = new EntityCrTempUser();
        EntityManager.mapCarrierToEntity(carrier, entUser);
        entUser.setUserShortId(IdGenerator.getId());
        entUser.setLiUserPermissionCode(USER_TYPE_ADMIN_AND_DOCTOR);
        entUser.setModule(carrier.getValue("fkModuleId").toString());
        EntityManager.insert(entUser);

        String activationId = IdGenerator.nextRandomSessionId();
        EntityCrCompany entCompany = new EntityCrCompany();
        EntityManager.mapCarrierToEntity(carrier, entCompany);
        entCompany.setCompanyLang(SessionManager.getCurrentLang());
        entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
        entCompany.setActivationId(activationId);
        entCompany.setCompanyType(EntityCrCompany.CompanyType.PERSONAL.toString());
        entCompany.setActiveUserCount("1");
        entCompany.setFkUserId(entUser.getId());
        entCompany.setCompanyDb("apd_" + IdGenerator.nextDbId());
        entCompany.setPersonUsername(entUser.getUsername());
        entCompany.setCompanyDomain("prs_" + carrier.getValue("username"));
        entCompany.setCompanyName("prs_" + carrier.getValue("username"));
        EntityManager.insert(entCompany);

        try {

            String txt = QUtility.getLabel("mailActivationBody",
                    new String[]{entCompany.getCompanyName(), activationId, entCompany.getCompanyLang()});
            System.out.println("Activation mail body >>" + txt);
            MailSender.send(entUser.getEmail1(),
                    QUtility.getLabel("mailActivationSubject"), txt);
            System.out.println("activation mail sent to " + entCompany.getCompanyName());

        } catch (Exception e) {
            System.out.println("excepiton oldu");
        }
        return carrier;

    }

    public static Carrier activateCompany(Carrier carrier) throws QException {
        try {

            Carrier outCarrier = new Carrier();

            EntityCrCompany entCompany = new EntityCrCompany();
            entCompany.setActivationId(carrier.getValue("activationId").toString());
            entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
            Carrier c1 = EntityManager.select(entCompany);

            if (c1.getTableRowCount(entCompany.toTableName()) == 0) {
                outCarrier.setValue("res", QUtility.getLabel("activationIdIsNotAvailable"));
                return outCarrier;
            }

//            entCompany.setStatus(EntityCrCompany.CompanyStatus.PENDING.toString());
//            EntityManager.update(entCompany);
            String fkTempUserId = entCompany.getFkUserId();

            //CREATE LOCAL_DATABASE
            createDBOnActivateCompany(entCompany.getCompanyDb().trim());

            //create tables and views
            createTableAndViewOnActivateCompany(entCompany.getCompanyDb().trim());

            //generate insert script
            insertScriptOnActivateCompany(entCompany.getCompanyDb().trim());

            //insert admin user info
            String id = insertAdminUserOnActivateCompany(entCompany.getFkUserId(),
                    entCompany.getCompanyDb());

            //add default permission and add default payment within the permission section
            String fkCompanyId = entCompany.getId();
            String fkModuleId = getModuleIdByUserId(fkTempUserId);

            if (entCompany.getCompanyType().equals(EntityCrCompany.CompanyType.COMPANY.toString())) {
                addDefaultPermissionToActivateCompany(fkCompanyId);
                addDefaultModulePermissionToActivateCompany(fkModuleId, fkCompanyId);
            } else if (entCompany.getCompanyType().equals(EntityCrCompany.CompanyType.PERSONAL.toString())) {
                addDefaultPermissionToActivatePersonal(fkCompanyId);
                addDefaultModulePermissionToActivatePersonal(fkModuleId, fkCompanyId);
            }

            //activate account
            entCompany.setStatus(EntityCrCompany.CompanyStatus.ACTIVE.toString());
            entCompany.setFkUserId(id);
            EntityManager.update(entCompany);

            //send email about activation
            sendActivationEmail(fkTempUserId, fkCompanyId, entCompany.getCompanyName());

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static void sendActivationEmail(String fkTempUserId, String dbname, String domain) {
        try {
            EntityCrTempUser ent = new EntityCrTempUser();
            ent.setDbname(dbname);
            ent.setId(fkTempUserId);
            ent.setStartLimit(0);
            ent.setEndLimit(0);
            EntityManager.select(ent);

            String fullname = ent.getUserPersonSurname() + " " + ent.getUserPersonName() + " "
                    + ent.getUserPersonMiddlename();

            MailSender.send(ent.getEmail1(),
                    QUtility.getLabel("mailCompanyActivatedSubject"),
                    QUtility.getLabel("mailCompanyActivatedBody",
                            new String[]{fullname, domain})
            );
        } catch (Exception e) {
            System.out.println("excepiton oldu");
        }
    }

    private static void addDefaultPaymentOnActivateCompany(
            String fkCompanyId, String fkPaymentTypeId) throws QException {
        Carrier carrier = new Carrier();
        carrier.setValue(EntityCrCompanyPayment.CURRENCY, "USD");
        carrier.setValue(EntityCrCompanyPayment.FK_COMPANY_ID, fkCompanyId);
        carrier.setValue(EntityCrCompanyPayment.FK_PAYMENT_TYPE_ID, fkPaymentTypeId);
        carrier.setValue(EntityCrCompanyPayment.PAYMENT_AMOUNT, "0");
        carrier.setValue(EntityCrCompanyPayment.PAYMENT_DISCOUNT, "100");
        carrier.setValue(EntityCrCompanyPayment.PAYMENT_DATE, QDate.getCurrentDate());
        carrier.setValue(EntityCrCompanyPayment.PAYMENT_TIME, QDate.getCurrentTime());
        insertNewCompanyPayment(carrier);
    }

    private static String getPaymentTypeIdByShortname(String shortname) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        ent.setPaymentTypeShortname(shortname);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);
        return ent.getId();
    }

    private static void addDefaultPermissionToActivateCompany(String fkCompanyId)
            throws QException {
        String[] paymentTypes = Config.getSignUpCompanyPaymentType().split(",");

        for (String pt : paymentTypes) {
            if (pt.trim().length() == 0) {
                continue;
            }
            String fkPaymentTypeId = getPaymentTypeIdByShortname(pt);

            if (fkPaymentTypeId.trim().length() == 0) {
                continue;
            }

            addDefaultPaymentOnActivateCompany(fkCompanyId, fkPaymentTypeId);

            //get rule by payment type
            EntityCrRelPaymentTypeAndRule ent = new EntityCrRelPaymentTypeAndRule();
            ent.setFkPaymentTypeId(fkPaymentTypeId);
            ent.setOwner(PAYMENT_TYPE_OWNER_COMPANY);
            Carrier c = EntityManager.select(ent);

            String tn = ent.toTableName();
            int rc = c.getTableRowCount(tn);
            for (int j = 0; j < rc; j++) {
                EntityCrRelPaymentTypeAndRule ent1 = new EntityCrRelPaymentTypeAndRule();
                EntityManager.mapCarrierToEntity(c, tn, j, ent1);

                if (ent1.getFkRuleId().trim().length() == 0) {
                    continue;
                }

                int addDay = ent1.getDefaultPeriod().trim().length() == 0
                        ? 0 : Integer.parseInt(ent1.getDefaultPeriod().trim());
                String expDate = QDate.convertDateToString(
                        QDate.add(QDate.getCurrentDate(), addDay));

                EntityCrRelCompanyAndRule entRel = new EntityCrRelCompanyAndRule();
                entRel.setFkCompanyId(fkCompanyId);
                entRel.setFkRuleId(ent1.getFkRuleId());
                entRel.setExpireDate(expDate);
                EntityManager.insert(entRel);

            }
        }
    }

    private static void addDefaultPermissionToActivatePersonal(String fkCompanyId)
            throws QException {
        String[] paymentTypes = Config.getSignUpPersonalPaymentType().split(",");

        for (String pt : paymentTypes) {
            if (pt.trim().length() == 0) {
                continue;
            }
            String fkPaymentTypeId = getPaymentTypeIdByShortname(pt);

            if (fkPaymentTypeId.trim().length() == 0) {
                continue;
            }

            addDefaultPaymentOnActivateCompany(fkCompanyId, fkPaymentTypeId);

            //get rule by payment type
            EntityCrRelPaymentTypeAndRule ent = new EntityCrRelPaymentTypeAndRule();
            ent.setFkPaymentTypeId(fkPaymentTypeId);
            ent.setOwner(PAYMENT_TYPE_OWNER_PERSONAL);
            Carrier c = EntityManager.select(ent);

            String tn = ent.toTableName();
            int rc = c.getTableRowCount(tn);
            for (int j = 0; j < rc; j++) {
                EntityCrRelPaymentTypeAndRule ent1 = new EntityCrRelPaymentTypeAndRule();
                EntityManager.mapCarrierToEntity(c, tn, j, ent1);

                if (ent1.getFkRuleId().trim().length() == 0) {
                    continue;
                }

                int addDay = ent1.getDefaultPeriod().trim().length() == 0
                        ? 0 : Integer.parseInt(ent1.getDefaultPeriod().trim());
                String expDate = QDate.convertDateToString(
                        QDate.add(QDate.getCurrentDate(), addDay));

                EntityCrRelCompanyAndRule entRel = new EntityCrRelCompanyAndRule();
                entRel.setFkCompanyId(fkCompanyId);
                entRel.setFkRuleId(ent1.getFkRuleId());
                entRel.setExpireDate(expDate);
                EntityManager.insert(entRel);

            }
        }
    }

    private static String getModuleIdByUserId(String fkUserId) throws QException {

        if (fkUserId.trim().length() == 0) {
            return "";
        }

        EntityCrTempUser ent = new EntityCrTempUser();
        ent.setId(fkUserId);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);

        return ent.getModule();
    }

    private static void addDefaultModulePermissionToActivateCompany(String fkModuleId,
            String fkCompanyId)
            throws QException {

        if (fkModuleId.trim().length() == 0) {
            return;
        }

        //get rule by payment type
        EntityCrModule ent = new EntityCrModule();
        ent.setId(fkModuleId);
        Carrier c = EntityManager.select(ent);
        String paymentTypeId = ent.getFkPaymentTypeId();

        if (c.getTableRowCount(ent.toTableName()) == 0
                || paymentTypeId.trim().length() == 0) {
            return;
        }

        //get rule by payment type
        EntityCrRelPaymentTypeAndRule ent2
                = new EntityCrRelPaymentTypeAndRule();
        ent2.setFkPaymentTypeId(paymentTypeId);
        ent2.setOwner(PAYMENT_TYPE_OWNER_COMPANY);
        Carrier tc = EntityManager.select(ent2);

        addDefaultPaymentOnActivateCompany(fkCompanyId, paymentTypeId);

        String tn = ent2.toTableName();
        int rc = tc.getTableRowCount(tn);
        for (int j = 0; j < rc; j++) {
            EntityCrRelPaymentTypeAndRule ent1 = new EntityCrRelPaymentTypeAndRule();
            EntityManager.mapCarrierToEntity(tc, tn, j, ent1);

            if (ent1.getFkRuleId().trim().length() == 0) {
                continue;
            }

            int addDay = ent1.getDefaultPeriod().trim().length() == 0
                    ? 0 : Integer.parseInt(ent1.getDefaultPeriod().trim());
            String expDate = QDate.convertDateToString(
                    QDate.add(QDate.getCurrentDate(), addDay));

            EntityCrRelCompanyAndRule entRel = new EntityCrRelCompanyAndRule();
            entRel.setFkCompanyId(fkCompanyId);
            entRel.setFkRuleId(ent1.getFkRuleId());
            entRel.setExpireDate(expDate);
            EntityManager.insert(entRel);
        }

    }

    private static void addDefaultModulePermissionToActivatePersonal(String fkModuleId,
            String fkCompanyId)
            throws QException {

        if (fkModuleId.trim().length() == 0) {
            return;
        }

        //get rule by payment type
        EntityCrModule ent = new EntityCrModule();
        ent.setId(fkModuleId);
        Carrier c = EntityManager.select(ent);
        String paymentTypeId = ent.getFkPaymentTypeId();

        if (c.getTableRowCount(ent.toTableName()) == 0
                || paymentTypeId.trim().length() == 0) {
            return;
        }

        //get rule by payment type
        EntityCrRelPaymentTypeAndRule ent2
                = new EntityCrRelPaymentTypeAndRule();
        ent2.setFkPaymentTypeId(paymentTypeId);
        ent2.setOwner(PAYMENT_TYPE_OWNER_PERSONAL);
        Carrier tc = EntityManager.select(ent2);

        addDefaultPaymentOnActivateCompany(fkCompanyId, paymentTypeId);

        String tn = ent2.toTableName();
        int rc = tc.getTableRowCount(tn);
        for (int j = 0; j < rc; j++) {
            EntityCrRelPaymentTypeAndRule ent1 = new EntityCrRelPaymentTypeAndRule();
            EntityManager.mapCarrierToEntity(tc, tn, j, ent1);

            if (ent1.getFkRuleId().trim().length() == 0) {
                continue;
            }

            int addDay = ent1.getDefaultPeriod().trim().length() == 0
                    ? 0 : Integer.parseInt(ent1.getDefaultPeriod().trim());
            String expDate = QDate.convertDateToString(
                    QDate.add(QDate.getCurrentDate(), addDay));

            EntityCrRelCompanyAndRule entRel = new EntityCrRelCompanyAndRule();
            entRel.setFkCompanyId(fkCompanyId);
            entRel.setFkRuleId(ent1.getFkRuleId());
            entRel.setExpireDate(expDate);
            EntityManager.insert(entRel);
        }

    }

    private static String insertAdminUserOnActivateCompany(String userId, String companyDB)
            throws QException {
        if (userId.trim().length() == 0) {
            throw new QException("Not Found");
        }

        EntityCrTempUser entTU = new EntityCrTempUser();
        entTU.setId(userId);
        Carrier c = EntityManager.select(entTU);

        if (c.getTableRowCount(entTU.toTableName()) != 1) {
            throw new QException("Not Found");
        }

        EntityCrUser entU = new EntityCrUser();
        entU.setDbname(companyDB);
        entU.setUsername(entTU.getUsername());
        entU.setPassword(entTU.getPassword());
        entU.setUserPersonName(entTU.getUserPersonName());
        entU.setUserPersonSurname(entTU.getUserPersonSurname());
        entU.setUserPersonMiddlename(entTU.getUserPersonMiddlename());
        entU.setSex(entTU.getSex());
        entU.setEmail1(entTU.getEmail1());
        entU.setMobile1(entTU.getMobile1());
        entU.setLiUserPermissionCode(entTU.getLiUserPermissionCode());
        EntityManager.insert(entU);

        return entU.getId();

    }

    private static void createDBOnActivateCompany(String companyDb) throws QException {
        String query = "CREATE DATABASE IF NOT EXISTS " + companyDb + ";";
        EntityManager.executeUpdateByQuery(query);
    }

    private static void createTableAndViewOnActivateCompany(String companyDb) throws QException {
        Pattern p = Pattern.compile("\\$\\{companyDb\\}");
        List<EntityCrUserTable> viewList = new ArrayList<>();
        List<EntityCrUserTable> tableList = new ArrayList<>();

        String types = EntityCrUserTable.Type.TABLE.toString()
                + CoreLabel.IN + EntityCrUserTable.Type.VIEW.toString();

        EntityCrUserTable entUsrTbl = new EntityCrUserTable();
        entUsrTbl.setType(types);
        entUsrTbl.addSortBy(EntityCrUserTable.TYPE);
        entUsrTbl.addSortBy(EntityCrUserTable.SEQNUM);
        entUsrTbl.setSortByAsc(true);
        Carrier crUsrTbl = EntityManager.select(entUsrTbl);

        int rc = crUsrTbl.getTableRowCount(entUsrTbl.toTableName());
        for (int i = 0; i < rc; i++) {
            EntityCrUserTable entUsrTbl1 = new EntityCrUserTable();
            EntityManager.mapCarrierToEntity(crUsrTbl,
                    entUsrTbl.toTableName(), i, entUsrTbl1, true);
            if (entUsrTbl1.getType().equals(EntityCrUserTable.Type.TABLE.toString())) {
                tableList.add(entUsrTbl1);
            } else if (entUsrTbl1.getType().equals(EntityCrUserTable.Type.VIEW.toString())) {
                viewList.add(entUsrTbl1);
            }

        }

        for (int i = 0; i < tableList.size(); i++) {
            String q = tableList.get(i).getTableScript();
            if (q.trim().length() > 0) {
                String vs = p.matcher(q).replaceAll(companyDb);
                System.out.println("query=" + vs);
                EntityManager.executeUpdateByQuery(vs);
                System.out.println("table " + tableList.get(i).getTableName().trim() + " created");
            }
        }

        for (int i = 0; i < viewList.size(); i++) {
            String q = viewList.get(i).getTableScript();
            if (q.trim().length() > 0) {
                String vs = p.matcher(q).replaceAll(companyDb);
                System.out.println("query=" + vs);
                EntityManager.executeUpdateByQuery(vs);
                System.out.println("view " + viewList.get(i).getTableName().trim() + " created");
            }
        }
    }

    private static void insertScriptOnActivateCompany(String companyDb) throws QException {
        Pattern p = Pattern.compile("\\$\\{companyDb\\}");

        String types = EntityCrUserTable.Type.SCRIPT.toString();

        EntityCrUserTable entUsrTbl = new EntityCrUserTable();
        entUsrTbl.setType(types);
        entUsrTbl.addSortBy(EntityCrUserTable.TYPE);
        entUsrTbl.addSortBy(EntityCrUserTable.SEQNUM);
        entUsrTbl.setSortByAsc(true);
        Carrier crUsrTbl = EntityManager.select(entUsrTbl);

        int rc = crUsrTbl.getTableRowCount(entUsrTbl.toTableName());
        for (int i = 0; i < rc; i++) {
            EntityCrUserTable entUsrTbl1 = new EntityCrUserTable();
            EntityManager.mapCarrierToEntity(crUsrTbl,
                    entUsrTbl.toTableName(), i, entUsrTbl1, true);
            String q = entUsrTbl.getTableScript();
            if (q.trim().length() > 0) {
                String vs = p.matcher(q).replaceAll(companyDb);
                System.out.println("query=" + vs);
                EntityManager.executeUpdateByQuery(vs);
                System.out.println("table " + entUsrTbl.getTableName().trim() + " created");
            }

        }

    }

    public static Carrier resendEmail(Carrier carrier) throws QException {
        try {
            EntityCrUser entUser = new EntityCrUser();
            entUser.setId(carrier.getValue("userId").toString());
            EntityManager.select(entUser);

            EntityCrCompany entCompany = new EntityCrCompany();
            entCompany.setId(entUser.getFkCompanyId());
            EntityManager.select(entCompany);

            if (entCompany.getStatus().equals(EntityCrCompany.CompanyStatus.VERIFY.toString())) {
                String activationId = IdGenerator.nextRandomSessionId();
                entCompany.setActivationId(activationId);
                EntityManager.update(entCompany);
                MailSender.send(entUser.getEmail1(), "Your account created",
                        "Activate account " + entCompany.getCompanyName() + "  http://localhost:8080/apd/api/post/signup/activate/" + activationId);
            }

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getPermissionList(Carrier carrier) throws QException {
        try {
            EntityCrPermission ent = new EntityCrPermission();
            EntityManager.mapCarrierToEntity(carrier, ent);
            ent.setDeepWhere(false);
            carrier = EntityManager.select(ent);
            //carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getPermissionList"));
            carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public static Carrier insertNewPermission(Carrier carrier) throws QException {
        EntityCrPermission ent = new EntityCrPermission();
        EntityManager.mapCarrierToEntity(carrier, ent);
        //ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updatePermission(Carrier carrier) throws QException {
        EntityCrPermission ent = new EntityCrPermission();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deletePermission(Carrier carrier) throws QException {
        EntityCrPermission ent = new EntityCrPermission();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getRuleList(Carrier carrier) throws QException {
        try {
            EntityCrRule ent = new EntityCrRule();
            ent.setDeepWhere(false);
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            carrier.copyTableColumn(CoreLabel.RESULT_SET, "ruleName", "ruleNameMain");

            addRuleLabel(carrier);

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getRuleList"));
            carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public static Carrier insertNewRule(Carrier carrier) throws QException {
        EntityCrRule ent = new EntityCrRule();
        EntityManager.mapCarrierToEntity(carrier, ent);
        //ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateRule(Carrier carrier) throws QException {
        EntityCrRule ent = new EntityCrRule();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteRule(Carrier carrier) throws QException {
        EntityCrRule ent = new EntityCrRule();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier assignPermissionRule(Carrier carrier) throws QException {
        String fkRuleId = carrier.getValue("fkRuleId").toString();;
        EntityCrRelRuleAndPermission ent = new EntityCrRelRuleAndPermission();
        ent.setFkRuleId(fkRuleId);
        Carrier crRel = EntityManager.select(ent);

//        if (crRel.getTableRowCount(ent.toTableName()) > 0) {
//            String ids = crRel.getValueLine(ent.toTableName());
//            ent.setId(ids);
//            EntityManager.delete(ent);
//        }
        String[] permissions = carrier.getValue("fkPermissionId").toString()
                .split(CoreLabel.SEPERATOR_VERTICAL_LINE);
        for (String p : permissions) {
            if (p.trim().length() > 0) {
                EntityCrRelRuleAndPermission entNew = new EntityCrRelRuleAndPermission();
                entNew.setFkPermissionId(p);
                entNew.setFkRuleId(fkRuleId);
                Carrier tc = EntityManager.select(entNew);
                if (tc.getTableRowCount(entNew.toTableName()) > 0) {
                    continue;
                }
                EntityManager.insert(entNew);
            }
        }
        return new Carrier();
    }

    /*public static Carrier getPermissionListByRule(Carrier carrier) throws QException {
        try {
            EntityCrPermission ent = new EntityCrPermission();
            ent.setDeepWhere(false);
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            //carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getPermissionList"));
            carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }*/
    public static Carrier getRulePermissionList(Carrier carrier) throws QException {
        try {
            EntityCrRelRuleAndPermissionList entM = new EntityCrRelRuleAndPermissionList();
            EntityManager.mapCarrierToEntity(carrier, entM);
            Carrier crOut = EntityManager.select(entM);
            String tnMain = entM.toTableName();

            if (carrier.hasKey("id")) {
                EntityCrRelRuleAndPermission ent1 = new EntityCrRelRuleAndPermission();
                ent1.setId(carrier.getValue("id").toString());
                EntityManager.select(ent1);

                if (ent1.getFkRuleId().trim().length() > 0) {
                    EntityCrRelRuleAndPermission ent2 = new EntityCrRelRuleAndPermission();
                    ent2.setFkRuleId(ent1.getFkRuleId());
                    Carrier tc1 = EntityManager.select(ent2);

                    String res = "";
                    String tn = ent2.toTableName();
                    int rc = tc1.getTableRowCount(tn);
                    for (int i = 0; i < rc; i++) {
                        res += tc1.getValue(tn, i, "fkPermissionId").toString()
                                + "|";
                    }
                    crOut.setValue(tnMain, 0, "fkPermissionId", res);
                }

            }

            crOut.renameTableName(tnMain, CoreLabel.RESULT_SET);

            addRuleLabel(crOut);
            //
            crOut.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getRulePermissionList"));
            crOut.addTableRowCount("rowCount", EntityManager.getRowCount(entM));
            return crOut;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    private static void addRuleLabel(Carrier carrier) throws QException {

        Map<String, String> lang = new HashMap<>();

        String col = "ruleName";

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String val = carrier.getValue(tn, i, col).toString();
            if (lang.containsKey(val)) {
                val = lang.get(val);
            } else {
                String v = QUtility.getLabel(val);
                lang.put(val, v);
                val = v;
            }
            carrier.setValue(tn, i, "ruleName", val);
        }
    }

    private static void addRuleLabelById(Carrier carrier) throws QException {

        Map<String, String> lang = new HashMap<>();

        String col = "ruleName";
        String colId = "fkRuleId";

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String valId = carrier.getValue(tn, i, colId).toString();
            EntityCrRule entRule = new EntityCrRule();
            entRule.setId(valId);
            entRule.setStartLimit(0);
            entRule.setEndLimit(0);
            EntityManager.select(entRule);

            if (entRule.getRuleName().trim().length() == 0) {
                carrier.setValue(tn, i, col, "");
                continue;
            }

            String val = entRule.getRuleName().trim();
            if (lang.containsKey(val)) {
                val = lang.get(val);
            } else {
                String v = QUtility.getLabel(val);
                lang.put(val, v);
                val = v;
            }
            carrier.setValue(tn, i, col, val);
        }
    }

    private static void addRoleLabelById(Carrier carrier) throws QException {

        Map<String, String> lang = new HashMap<>();

        String col = "roleName";
        String colOut = "roleFullname";
        String colId = "fkRoleId";

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String valId = carrier.getValue(tn, i, colId).toString();
            EntityCrRole entRule = new EntityCrRole();
            entRule.setId(valId);
            entRule.setStartLimit(0);
            entRule.setEndLimit(0);
            EntityManager.select(entRule);

            if (entRule.getRoleName().trim().length() == 0) {
                continue;
            }

            String val = entRule.getRoleName().trim();
            if (lang.containsKey(val)) {
                val = lang.get(val);
            } else {
                String v = QUtility.getLabel(val);
                lang.put(val, v);
                val = v;
            }
            carrier.setValue(tn, i, colOut, val);
        }
    }

    public Carrier deleteRulePermission(Carrier carrier) throws QException {
        EntityCrRelRuleAndPermission ent = new EntityCrRelRuleAndPermission();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getRoleList(Carrier carrier) throws QException {

        EntityCrRole ent = new EntityCrRole();
        ent.setDeepWhere(false);
        EntityManager.mapCarrierToEntity(carrier, ent);
        carrier = EntityManager.select(ent);
        //carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        carrier.copyTableColumn(CoreLabel.RESULT_SET, "id", "fkRoleId");
        addRoleLabelById(carrier);

        carrier.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getRoleList"));
        carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
        return carrier;

    }

    public static Carrier getRoleList4Combo(Carrier carrier) throws QException {

        EntityCrRole ent = new EntityCrRole();
        carrier = EntityManager.select(ent);

        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        carrier.copyTableColumn(CoreLabel.RESULT_SET, "id", "fkRoleId");
        addRoleLabelById(carrier);

        return carrier;

    }

    public static Carrier insertNewRole(Carrier carrier) throws QException {
        EntityCrRole ent = new EntityCrRole();
        EntityManager.mapCarrierToEntity(carrier, ent);
        //ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateRole(Carrier carrier) throws QException {
        EntityCrRole ent = new EntityCrRole();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteRole(Carrier carrier) throws QException {
        EntityCrRole ent = new EntityCrRole();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier assignRuleRole(Carrier carrier) throws QException {
        String fkRoleId = carrier.getValue("fkRoleId").toString();;

        EntityCrRelRoleRule ent = new EntityCrRelRoleRule();
        ent.setDeepWhere(false);
        ent.setFkRoleId(fkRoleId);
        Carrier crRel = EntityManager.select(ent);

        if (crRel.getTableRowCount(ent.toTableName()) > 0) {
            String ids = crRel.getValueLine(ent.toTableName());
            ent.setId(ids);
            EntityManager.delete(ent);
        }

        String[] fkRuleId = carrier.getValue("fkRuleId").toString()
                .split(CoreLabel.SEPERATOR_VERTICAL_LINE);
        for (String p : fkRuleId) {
            if (p.trim().length() > 0) {
                EntityCrRelRoleRule entNew = new EntityCrRelRoleRule();
                entNew.setDeepWhere(false);
                entNew.setFkRuleId(p);
                entNew.setFkRoleId(fkRoleId);
                Carrier tc = EntityManager.select(entNew);
                if (tc.getTableRowCount(entNew.toTableName()) > 0) {
                    continue;
                }
                EntityManager.insert(entNew);
            }
        }
        return new Carrier();
    }

    public static Carrier getRoleRuleList(Carrier carrier) throws QException {

        EntityCrRelRoleRule entM = new EntityCrRelRoleRule();
        EntityManager.mapCarrierToEntity(carrier, entM);
        Carrier crOut = EntityManager.select(entM);
        String tnMain = entM.toTableName();

        if (carrier.hasKey("id")) {
            EntityCrRelRoleRule ent1 = new EntityCrRelRoleRule();
            ent1.setId(carrier.getValue("id").toString());
            EntityManager.select(ent1);

            if (ent1.getFkRoleId().trim().length() > 0) {
                EntityCrRelRoleRule ent2 = new EntityCrRelRoleRule();
                ent2.setFkRoleId(ent1.getFkRoleId());
                Carrier tc1 = EntityManager.select(ent2);

                String res = "";
                String tn = ent2.toTableName();
                int rc = tc1.getTableRowCount(tn);
                for (int i = 0; i < rc; i++) {
                    res += tc1.getValue(tn, i, "fkRuleId").toString()
                            + "|";
                }
                crOut.setValue(tnMain, 0, "fkRuleId", res);
            }

        }

        crOut.renameTableName(tnMain, CoreLabel.RESULT_SET);

        addRuleLabelById(crOut);
        addRoleLabelById(crOut);
        //
        crOut.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getRoleRuleList"));
        crOut.addTableRowCount("rowCount", EntityManager.getRowCount(entM));
        return crOut;

    }

    public Carrier deleteRoleRule(Carrier carrier) throws QException {
        EntityCrRelRoleRule ent = new EntityCrRelRoleRule();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getUserRuleList(Carrier carrier) throws QException {

        String type = carrier.getValue("type").toString();
//        System.out.println("type=" + type);

        if (type.trim().equals(USER_RULE_TYPE_BY_RULE)) {
            carrier = getUserRuleListByRole(carrier);
        } else if (type.trim().equals(USER_RULE_TYPE_BY_OWN)) {
            carrier = getUserRuleListByOwn(carrier);
        } else if (type.trim().equals(USER_RULE_TYPE_BY_ALL)) {
            carrier = getUserRuleListByAll(carrier);
        }
        return carrier;
    }

    private static Carrier getUserRuleListByRole(Carrier carrier) throws QException {
        String id = carrier.getValue("fkUserId").toString();
        String fkRoleId = carrier.getValue("fkRoleId").toString();
//        System.out.println("fkUserId=" + id);
//        System.out.println("fkRoleId=" + fkRoleId);

        if (fkRoleId == null || fkRoleId.isEmpty()) {
            return carrier;
        }

        EntityCrRelRoleRule ent = new EntityCrRelRoleRule();
        ent.setFkRoleId(fkRoleId);
        ent.setFkRuleId(getPublishedRuleIdsByCompany());
        carrier = EntityManager.select(ent);
        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        addRoleLabelById(carrier);
        addRuleLabelById(carrier);

        String[] assignedRuleIds = null;
        if (id.trim().length() == 0) {
            assignedRuleIds = new String[]{""};
        } else {
            EntityCrRelUserAndRule entRel = new EntityCrRelUserAndRule();
            entRel.setFkUserId(id);
            Carrier crRel = EntityManager.select(entRel);
            assignedRuleIds = crRel.getValue(entRel.toTableName(),
                    EntityCrRelUserAndRule.FK_RULE_ID);
        }

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String ruleId = carrier.getValue(tn, i, "fkRuleId").toString();
            String access = Arrays.asList(assignedRuleIds).contains(ruleId) ? "1" : "0";
            carrier.setValue(tn, i, "ruleAccess", access);
        }

        return carrier;
    }

    private static String getPublishedRuleIdsBy() throws QException {
        EntityCrRule ent = new EntityCrRule();
        ent.setIsPublic("1");
        return EntityManager.select(ent).getValueLine(ent.toTableName());
    }

    private static String getPublishedRuleIdsByCompany() throws QException {
        String defaultId = "__2__" + CoreLabel.IN;
        EntityCrRelCompanyAndRule ent = new EntityCrRelCompanyAndRule();
        ent.setDeepWhere(false);
        ent.setFkRuleId(getPublishedRuleIdsBy());
        ent.setExpireDate(CoreLabel.GE + QDate.getCurrentDate());
        ent.setFkCompanyId(SessionManager.getCurrentCompanyId());
        return defaultId + EntityManager.select(ent).getValueLine(ent.toTableName(), "fkRuleId");
    }

    private static Carrier getUserRuleListByAll(Carrier carrier) throws QException {
        String id = carrier.getValue("fkUserId").toString();

        EntityCrRelCompanyAndRule ent = new EntityCrRelCompanyAndRule();
        ent.setDeepWhere(false);
        ent.setExpireDate(CoreLabel.GE + QDate.getCurrentDate());
        ent.setFkRuleId(getPublishedRuleIdsBy());
        ent.setFkCompanyId(SessionManager.getCurrentCompanyId());
        carrier = EntityManager.select(ent);

        carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        addRuleLabelById(carrier);

        String[] assignedRuleIds = null;
        if (id.trim().length() == 0) {
            assignedRuleIds = new String[]{""};
        } else {
            EntityCrRelUserAndRule entRel = new EntityCrRelUserAndRule();
            entRel.setFkUserId(id);
            Carrier crRel = EntityManager.select(entRel);
            assignedRuleIds = crRel.getValue(entRel.toTableName(),
                    EntityCrRelUserAndRule.FK_RULE_ID);
        }

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String ruleId = carrier.getValue(tn, i, "fkRuleId").toString();
            String access = Arrays.asList(assignedRuleIds).contains(ruleId) ? "1" : "0";
            carrier.setValue(tn, i, "ruleAccess", access);
        }

        return carrier;
    }

    private static Carrier getUserRuleListByOwn(Carrier carrier) throws QException {
        String fkUserId = carrier.getValue("fkUserId").toString();
//        System.out.println("fkUserId=" + fkUserId);

        if (fkUserId == null || fkUserId.isEmpty()) {
            return carrier;
        }

        EntityCrRelUserAndRule entRel = new EntityCrRelUserAndRule();
        entRel.setFkUserId(fkUserId);
        entRel.setFkRuleId(getPublishedRuleIdsByCompany());
        Carrier crRel = EntityManager.select(entRel);

        String tn = entRel.toTableName();
        int rc = crRel.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String access = "1";
            crRel.setValue(tn, i, "ruleAccess", access);
        }

        crRel.renameTableName(tn, CoreLabel.RESULT_SET);
        addRuleLabelById(crRel);

        return crRel;
    }

    public Carrier getNextSubmoduleOrderNo(Carrier carrier) throws QException {
        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setDeepWhere(false);
        ent.setSortBy(CoreLabel.GT + carrier.getValue("currentNo").toString());
        ent.addSortBy(EntityCrSubmodule.SORT_BY);
        ent.setSortByAsc(true);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);
        carrier.setValue("nextNo", ent.getSortBy());
        return carrier;
    }

    public Carrier getPreviousSubmoduleOrderNo(Carrier carrier) throws QException {
        EntityCrSubmodule ent = new EntityCrSubmodule();
        ent.setDeepWhere(false);
        ent.setSortBy(CoreLabel.LT + carrier.getValue("currentNo").toString());
        ent.addSortBy(EntityCrSubmodule.SORT_BY);
        ent.setSortByAsc(false);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        EntityManager.select(ent);
        carrier.setValue("nextNo", ent.getSortBy());
        return carrier;
    }

    public static Carrier getLastPatientInfo(Carrier carrier) throws QException {

        EntityCrPatient ent = new EntityCrPatient();
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        ent.addSortBy("id");
        ent.setSortByAsc(false);
        Carrier crPtnt = EntityManager.select(ent);
        String tn = ent.toTableName();
        int rc = crPtnt.getTableRowCount(tn);
        Carrier ocr = new Carrier();
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(crPtnt, tn, i, ent);
            String id = ent.getId();
            String fullname = ent.getPatientName() + " " + ent.getPatientSurname() + " "
                    + ent.getPatientMiddleName() + " (" + ent.getPatientBirthDate() + ")";
            ocr.setValue("patientName", fullname);
            ocr.setValue("pid", id);
        }

        return ocr;
    }

    public static Carrier confirmPayment(Carrier carrier) throws QException {
//        System.out.println("carrier>>>" + carrier.toXML());
        Carrier c = new Carrier();
        String id = carrier.getValue("id").toString().trim();
//        System.out.println("id->" + id);
        if (id.length() == 0) {
            return c;
        }

        EntityCrPayment ent = new EntityCrPayment();
        ent.setId(id);
        ent.setPaymentStatus(PAYMENT_STATUS_IS_NOT_PAID);
        Carrier tc = EntityManager.select(ent);

        if (tc.getTableRowCount(ent.toTableName()) == 0) {
            return c;
        }

        ent.setPaymentStatus(PAYMENT_STATUS_IS_PAID);
        EntityManager.update(ent);
        return c;
    }

    public Carrier getPatientFullnameById(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        ent.setId(carrier.getValue("fkPatientId").toString());
        EntityManager.select(ent);

        String fullname = ent.getPatientName() + " " + ent.getPatientSurname() + " "
                + ent.getPatientMiddleName() + " (" + ent.getPatientBirthDate() + ")";
        carrier.setValue(CoreLabel.RESULT_SET, 0, "patientName", fullname);
        return carrier;
    }

    public Carrier isCompanyDomainAvailable(Carrier carrier) throws QException {
        EntityCrCompany ent = new EntityCrCompany();
        ent.setStatus("");
        ent.setCompanyDomain(carrier.getValue("companyDomain").toString());
        Carrier c = EntityManager.select(ent);

        int f = c.getTableRowCount(ent.toTableName()) > 0 ? 1 : 0;
        Carrier nc = new Carrier();
        nc.setValue("res", f);
        return nc;
    }

    public static Carrier getTermPage(Carrier carrier) throws QException {
        try {
            String lang = carrier.getValue("lang").toString();
            lang = SessionHandler.isLangAvailable(lang) ? lang : "ENG";
            SessionManager.setLang(SessionManager.getCurrentThreadId(), lang);

            String filename = "page_term_" + lang;
            GeneralProperties prop = new GeneralProperties();
            String fpath = prop.getWorkingDir() + "../page/" + filename + ".html";
            File file = new File(fpath);

            if (!file.exists()) {
                filename = "page_term_ENG";
            }

            Carrier nc = new Carrier();
            nc.setValue("page", filename);
            nc = getPage(nc);
//        System.out.println("xml->>"+nc.toXML());
            return nc;
        } catch (UnsupportedEncodingException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier isFieldValid(Carrier carrier) throws QException {
        Carrier resCr = new Carrier();
        String res = "0";

        String t = carrier.getValue("type").toString();
        String val = carrier.getValue("value").toString();

        if (t.trim().length() == 0) {
            resCr.setValue("res", 0);
            return resCr;
        }

        if (t.equals("domain") && isDomainFieldValid(val)) {
            res = "1";
        } else if (t.equals("username") && isUsernameFieldValid(val)) {
            res = "1";
        } else if (t.equals("password") && isPasswordFieldValid(val)) {
            res = "1";
        } else if (t.equals("email") && isEmailFieldValid(val)) {
            res = "1";
        }

        resCr.setValue("res", res);
        return resCr;
    }

    public static boolean isUsernameFieldValid(String arg) {

        boolean f = false;
        arg = arg.trim();
        if (arg.length() > 6 && arg.length() < 25 && isStartsWithAlphabet(arg)
                && isContainsAlphabetAndNumber(arg)) {
            f = true;
        }
        return f;
    }

    public static boolean isDomainFieldValid(String arg) {
        boolean f = false;
        arg = arg.trim();
        if (arg.length() > 6 && arg.length() < 25 && isStartsWithAlphabet(arg) && isContainsAlphabetAndNumber(arg)) {
            f = true;
        }
        return f;
    }

    public static boolean isEmailFieldValid(String arg) {
        boolean f = false;
        arg = arg.trim();
        if (arg.length() >= 6 && arg.length() < 100
                && arg.contains("@")
                && arg.contains(".")
                && isContainsEmailCombination(arg)) {
            f = true;
        }
        return f;
    }

    public static boolean isPasswordFieldValid(String arg) {
        boolean f = false;
        arg = arg.trim();
        if (arg.length() > 7 && arg.length() < 40
                && isContainsPwdCombination(arg)) {
            f = true;
        }
        return f;
    }

    public static boolean isStartsWithAlphabet(String arg) {
        boolean f = false;
        for (String s : ABC) {
            if (arg.startsWith(s) || arg.startsWith(s.toLowerCase())) {
//                System.out.println(s);
                f = true;
                break;
            }
        }
        return f;
    }

    public static boolean isContainsAlphabetAndNumber(String arg) {
        boolean f = true;
        for (int i = 0; i < arg.length(); i++) {
            String v = arg.substring(i, i + 1);
            if (!(Arrays.asList(ABC).contains(v.toUpperCase())
                    || Arrays.asList(NUMBER).contains(v))) {
                f = false;
                break;
            }
        }
        return f;
    }

    public static boolean isContainsPwdCombination(String arg) {
        boolean f = true;
        for (int i = 0; i < arg.length(); i++) {
            String v = arg.substring(i, i + 1);
            if (!(Arrays.asList(ABC).contains(v)
                    || Arrays.asList(ABC_).contains(v)
                    || Arrays.asList(PASSWORD_CHAR).contains(v)
                    || Arrays.asList(NUMBER).contains(v))) {
                f = false;
                break;
            }
        }
        return f;
    }

    public static boolean isContainsEmailCombination(String arg) {
        boolean f = true;
        for (int i = 0; i < arg.length(); i++) {
            String v = arg.substring(i, i + 1);
            if (!(Arrays.asList(ABC).contains(v)
                    || Arrays.asList(ABC_).contains(v)
                    || Arrays.asList(EMAIL_CHAR).contains(v)
                    || Arrays.asList(NUMBER).contains(v))) {
                f = false;
                break;
            }
        }
        return f;
    }

    public static Carrier getCompanyList(Carrier carrier) throws QException {
        EntityCrCompany entComp = new EntityCrCompany();
        EntityManager.mapCarrierToEntity(carrier, entComp);
        entComp.setStatus("");
        Carrier cComp = EntityManager.select(entComp);
        String tnComp = entComp.toTableName();

        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprLang = QUtility.getListItem("language",
                carrier.getValue("companyLangName").toString());
        cComp.mergeCarrier(tnComp, "companyLang", "companyLangName",
                cprLang, true);

        Carrier cprCntry = QUtility.getListItem("country",
                carrier.getValue("companyCountryName").toString());
        cComp.mergeCarrier(tnComp, "companyCountry", "companyCountryName",
                cprCntry, true);

        Carrier cprTZ = QUtility.getListItem("timezone",
                carrier.getValue("companyTimeZoneName").toString());
        cComp.mergeCarrier(tnComp, "companyTimeZone", "companyTimeZoneName",
                cprTZ, true);

        Carrier cprCrrnc = QUtility.getListItem("currency",
                carrier.getValue("companyCurrencyName").toString());
        cComp.mergeCarrier(tnComp, "companyCurrency", "companyCurrencyName",
                cprCrrnc, true);

        cComp.renameTableColumn(tnComp, "insertDate", "registerDate");
        cComp.renameTableColumn(tnComp, "status", "companyStatus");

        cComp.renameTableName(tnComp, CoreLabel.RESULT_SET);
        cComp.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getCompanyList"));

        cComp = addActivationUrlToCompanyList(cComp);

        cComp.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(entComp) + 1);

        return cComp;
    }

    private static Carrier addActivationUrlToCompanyList(Carrier carrier) throws QException {
        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String url = carrier.getValue(tn, i, EntityCrCompany.ACTIVATION_ID).toString();
            String lang = carrier.getValue(tn, i, EntityCrCompany.COMPANY_LANG).toString();
//            System.out.println("Config.getCompanyActivatePath()>>>"+Config.getCompanyActivatePath());
            String ln = Config.getCompanyActivatePath();
            ln = ln.replaceFirst("@param0", url);
            ln = ln.replaceFirst("@param1", lang);
//            System.out.println("url>>>"+url);
            carrier.setValue(tn, i, EntityCrCompany.ACTIVATION_ID, ln);
        }
        return carrier;
    }

    public static Carrier insertNewUserTable(Carrier carrier) throws QException {
        EntityCrUserTable ent = new EntityCrUserTable();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateUserTable(Carrier carrier) throws QException {
        EntityCrUserTable ent = new EntityCrUserTable();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteUserTable(Carrier carrier) throws QException {
        EntityCrUserTable ent = new EntityCrUserTable();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getUserTableList(Carrier carrier) throws QException {
        EntityCrUserTable ent = new EntityCrUserTable();
        ent.setDeepWhere(false);
        EntityManager.mapCarrierToEntity(carrier, ent);
        if (!ent.hasSortBy()) {
            ent.addSortBy(EntityCrUserTable.TYPE);
            ent.addSortBy(EntityCrUserTable.SEQNUM);
        }

        Carrier cUserTable = EntityManager.select(ent);
        String tn = ent.toTableName();

        cUserTable.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        cUserTable.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getUserTableList"));

        cUserTable.addTableRowCount(CoreLabel.RESULT_SET,
                EntityManager.getRowCount(ent) + 1);

        return cUserTable;
    }

    public static Carrier insertNewRelPaymentTypeAndRule(Carrier carrier) throws QException {
        String paymentType = carrier.getValue("fkPaymentTypeId").toString();;
        String defPeriod = carrier.getValue("defaultPeriod").toString();

        EntityCrRelPaymentTypeAndRule ent = new EntityCrRelPaymentTypeAndRule();
        ent.setFkPaymentTypeId(paymentType);
        ent.setDefaultPeriod(defPeriod);
        Carrier crPaymentType = EntityManager.select(ent);

        if (crPaymentType.getTableRowCount(ent.toTableName()) > 0) {
            String ids = crPaymentType.getValueLine(ent.toTableName());
            ent.setId(ids);
            EntityManager.delete(ent);
        }
        String[] ruleIds = carrier.getValue("fkRuleId").toString()
                .split(CoreLabel.SEPERATOR_VERTICAL_LINE);
        for (String id : ruleIds) {
            if (id.trim().length() > 0) {
                EntityCrRelPaymentTypeAndRule entNew = new EntityCrRelPaymentTypeAndRule();
                entNew.setFkRuleId(id);
                entNew.setFkPaymentTypeId(paymentType);
                entNew.setOwner(carrier.getValue("owner").toString());
                entNew.setDefaultPeriod(defPeriod);
                Carrier tc = EntityManager.select(entNew);
                if (tc.getTableRowCount(entNew.toTableName()) > 0) {
                    continue;
                }
                EntityManager.insert(entNew);
            }
        }
        return new Carrier();
    }

    public Carrier deleteRelPaymentTypeAndRule(Carrier carrier) throws QException {
        EntityCrRelPaymentTypeAndRule ent = new EntityCrRelPaymentTypeAndRule();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getRelPaymentTypeAndRuleList(Carrier carrier) throws QException {
        EntityCrRelPaymentTypeAndRule entM = new EntityCrRelPaymentTypeAndRule();
        EntityManager.mapCarrierToEntity(carrier, entM);
        Carrier crOut = EntityManager.select(entM);
        String tnMain = entM.toTableName();

        String paymentTypeIds = crOut.getValueLine(tnMain, "fkPaymentTypeId");
        Carrier crPaymentType = new Carrier();
        crPaymentType.setValue("id", paymentTypeIds);
        crPaymentType = getPaymentTypeList(crPaymentType);
        Carrier cprPaymentType = crPaymentType
                .getKVFromTable(CoreLabel.RESULT_SET, "id", "paymentTypeShortname");
        crOut.mergeCarrier(tnMain, "fkPaymentTypeId", "paymentTypeName",
                cprPaymentType, true);

        if (carrier.hasKey("id")) {
            EntityCrRelPaymentTypeAndRule ent1 = new EntityCrRelPaymentTypeAndRule();
            ent1.setId(carrier.getValue("id").toString());
            EntityManager.select(ent1);

            if (ent1.getFkPaymentTypeId().trim().length() > 0) {
                EntityCrRelPaymentTypeAndRule ent2 = new EntityCrRelPaymentTypeAndRule();
                ent2.setFkPaymentTypeId(ent1.getFkPaymentTypeId());
                ent2.setDefaultPeriod(ent1.getDefaultPeriod());
                Carrier tc1 = EntityManager.select(ent2);

                String res = "";
                String tn = ent2.toTableName();
                int rc = tc1.getTableRowCount(tn);
                for (int i = 0; i < rc; i++) {
                    res += tc1.getValue(tn, i, "fkRuleId").toString()
                            + "|";
                }
                crOut.setValue(tnMain, 0, "fkRuleId", res);
            }
        }

        Carrier cprPymntTpWnr = QUtility.getListItem("paymentTypeOwner",
                carrier.getValue("ownerName").toString());
        crOut.mergeCarrier(tnMain, "owner", "ownerName",
                cprPymntTpWnr, true);

        crOut.renameTableName(tnMain, CoreLabel.RESULT_SET);

        addRuleLabelById(crOut);
        //
        crOut.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getRelPaymentTypeAndRuleList"));
        crOut.addTableRowCount("rowCount", EntityManager.getRowCount(entM));
        return crOut;

    }

    public static Carrier insertNewRelCompanyAndRule(Carrier carrier) throws QException {
        String company = carrier.getValue("fkCompanyId").toString();;
        String expDate = carrier.getValue("expireDate").toString();;
        EntityCrRelCompanyAndRule ent = new EntityCrRelCompanyAndRule();
        ent.setFkCompanyId(company);
        Carrier crCompany = EntityManager.select(ent);

//        if (crCompany.getTableRowCount(ent.toTableName()) > 0) {
//            String ids = crCompany.getValueLine(ent.toTableName());
//            ent.setId(ids);
//            EntityManager.delete(ent);
//        }
        String[] ruleIds = carrier.getValue("fkRuleId").toString()
                .split(CoreLabel.SEPERATOR_VERTICAL_LINE);
        for (String id : ruleIds) {
            if (id.trim().length() > 0) {
                EntityCrRelCompanyAndRule entNew = new EntityCrRelCompanyAndRule();
                entNew.setFkRuleId(id);
                entNew.setFkCompanyId(company);
                entNew.setStartLimit(0);
                entNew.setEndLimit(0);
                Carrier tc = EntityManager.select(entNew);
                if (tc.getTableRowCount(entNew.toTableName()) > 0) {
                    entNew.setExpireDate(expDate);
                    EntityManager.update(entNew);
                } else {
                    entNew.setExpireDate(expDate);
                    EntityManager.insert(entNew);
                }
            }
        }
        return new Carrier();
    }

    public Carrier deleteRelCompanyAndRule(Carrier carrier) throws QException {
        EntityCrRelCompanyAndRule ent = new EntityCrRelCompanyAndRule();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getRelCompanyAndRuleList(Carrier carrier) throws QException {
        EntityCrRelCompanyAndRule entM = new EntityCrRelCompanyAndRule();
        EntityManager.mapCarrierToEntity(carrier, entM);
        Carrier crOut = EntityManager.select(entM);
        String tnMain = entM.toTableName();

        if (carrier.hasKey("id")) {
            EntityCrRelCompanyAndRule ent1 = new EntityCrRelCompanyAndRule();
            ent1.setId(carrier.getValue("id").toString());
            EntityManager.select(ent1);

            if (ent1.getFkCompanyId().trim().length() > 0) {
                EntityCrRelCompanyAndRule ent2 = new EntityCrRelCompanyAndRule();
                ent2.setFkCompanyId(ent1.getFkCompanyId());
                Carrier tc1 = EntityManager.select(ent2);

                String res = "";
                String tn = ent2.toTableName();
                int rc = tc1.getTableRowCount(tn);
                for (int i = 0; i < rc; i++) {
                    res += tc1.getValue(tn, i, "fkRuleId").toString()
                            + "|";
                }
                crOut.setValue(tnMain, 0, "fkRuleId", res);
            }
        }

        crOut.renameTableName(tnMain, CoreLabel.RESULT_SET);

        addRuleLabelById(crOut);

        Carrier crCompany = getCompanyList(new Carrier());
        crOut.mergeCarrier(CoreLabel.RESULT_SET, "fkCompanyId",
                crCompany, CoreLabel.RESULT_SET, "id",
                new String[]{"companyType", "companyName", "companyStatus"});

        //
        crOut.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getRelCompanyAndRuleList"));
        crOut.addTableRowCount("rowCount", EntityManager.getRowCount(entM));
        return crOut;

    }

    public static Carrier insertNewPaymentType(Carrier carrier) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updatePaymentType(Carrier carrier) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deletePaymentType(Carrier carrier) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getPaymentTypeList(Carrier carrier) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        EntityManager.mapCarrierToEntity(carrier, ent);

        Carrier cPaymentType = EntityManager.select(ent);

        cPaymentType.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        cPaymentType.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPaymentTypeList"));

        cPaymentType.addTableRowCount(CoreLabel.RESULT_SET,
                EntityManager.getRowCount(ent) + 1);

        return cPaymentType;
    }

    public static Carrier getPaymentTypeList4Combo(Carrier carrier) throws QException {
        EntityCrPaymentType ent = new EntityCrPaymentType();
        EntityManager.mapCarrierToEntity(carrier, ent);

        Carrier cPaymentType = EntityManager.select(ent);

        cPaymentType.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        cPaymentType.copyTableColumn(CoreLabel.RESULT_SET,
                "paymentTypeShortname", "paymentTypeDefinition");
        addPaymentTypeLabel(cPaymentType);

        cPaymentType.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPaymentTypeList"));

        cPaymentType.addTableRowCount(CoreLabel.RESULT_SET,
                EntityManager.getRowCount(ent) + 1);

        return cPaymentType;
    }

    private static void addPaymentTypeLabel(Carrier carrier) throws QException {

        Map<String, String> lang = new HashMap<>();

        String col = "paymentTypeDefinition";

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String val = carrier.getValue(tn, i, col).toString();
            if (lang.containsKey(val)) {
                val = lang.get(val);
            } else {
                String v = QUtility.getLabel(val);
                lang.put(val, v);
                val = v;
            }
            carrier.setValue(tn, i, col, val);
        }
    }

    public static Carrier insertNewCompanyPayment(Carrier carrier) throws QException {
        EntityCrCompanyPayment ent = new EntityCrCompanyPayment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.insert(ent);
        return carrier;
    }

    public Carrier updateCompanyPayment(Carrier carrier) throws QException {
        EntityCrCompanyPayment ent = new EntityCrCompanyPayment();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
        EntityManager.mapCarrierToEntity(carrier, ent, false);
        EntityManager.update(ent);
        return carrier;
    }

    public Carrier deleteCompanyPayment(Carrier carrier) throws QException {
        EntityCrCompanyPayment ent = new EntityCrCompanyPayment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getCompanyPaymentList(Carrier carrier) throws QException {
        EntityCrCompanyPaymentList ent = new EntityCrCompanyPaymentList();
        EntityManager.mapCarrierToEntity(carrier, ent);

        Carrier cCompanyPayment = EntityManager.select(ent);

        cCompanyPayment.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        cCompanyPayment.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getCompanyPaymentList"));

        cCompanyPayment.addTableRowCount(CoreLabel.RESULT_SET,
                EntityManager.getRowCount(ent) + 1);

        return cCompanyPayment;
    }

    public static Carrier isPersonalUsernameExist(Carrier carrier) throws QException {
        EntityCrCompany ent = new EntityCrCompany();
        ent.setDeepWhere(false);
        ent.setPersonUsername(carrier.getValue("username").toString());
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        Carrier c = EntityManager.select(ent);
        int i = c.getTableRowCount(ent.toTableName()) > 0 ? 1 : 0;
        carrier.setValue("res", i);
        return carrier;
    }

    public static Carrier changePassword(Carrier carrier) throws QException {
        String currentPwd = carrier.getValue("currentPassword").toString();
        String newPwd = carrier.getValue("newPassword").toString();
        String confirmNewPwd = carrier.getValue("confirmNewPassword").toString();

        if (!newPwd.trim().equals(confirmNewPwd.trim())) {
            carrier.setValue("res", QUtility.getLabel("passwordsDoesntMatch"));
            return carrier;
        }

        EntityCrUser ent = new EntityCrUser();
        ent.setDeepWhere(false);
        ent.setDbname(SessionManager.getCurrentDomain());
        ent.setUsername(SessionManager.getCurrentUsername());
        ent.setPassword(currentPwd);
        ent.setStartLimit(0);
        ent.setEndLimit(0);
        Carrier c = EntityManager.select(ent);
        int rc = c.getTableRowCount(ent.toTableName()) > 0 ? 1 : 0;

        if (rc == 0) {
            carrier.setValue("res", QUtility.getLabel("currentPasswordIsNotCorrect"));
            return carrier;
        } else {
            ent.setPassword(newPwd);
            EntityManager.update(ent);
            carrier.setValue("res", QUtility.getLabel("passwordChangedSuccesfully"));
        }

        return carrier;
    }

}
