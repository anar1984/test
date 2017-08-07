/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr;

import java.io.BufferedReader;
import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import label.CoreLabel;
import module.cr.entity.*;
import module.pg.PgModel;
import org.apache.commons.lang.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.CallDispatcher;
import utility.Carrier;
import utility.CommonConfigurationProperties;
import utility.DeepWhere;
import utility.GeneralProperties;
import utility.ListSequenceConfigurationProperties;
import utility.MailSender;
import utility.QDate;
import utility.QException;
import utility.QUtility;
import utility.SessionManager;
import utility.WhereSingle;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.IdGenerator;
import utility.sqlgenerator.SQLGenerator;

/**
 *
 * @author user
 */
public class CrModel {

    private static final String USER_CONTROLLER_TYPE_ENUB = "2";
    private static final String USER_CONTROLLER_TYPE_COMPONENT = "1";
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
    
	public Carrier getPage(Carrier carrier) throws QException {
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

            GeneralProperties prop = new GeneralProperties();
            String filename = prop.getWorkingDir() + "../page/" + pagename + ".html";
            String ln = "";
            File file = new File(filename);
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//
//            String ln = "";
//            String st = "";
//            while ((st = br.readLine()) != null) {
//                ln += st + "";
//            }
//
//            fr.close();
//            br.close();
//            System.out.println("1.ci "+ln);
            ln = QUtility.checkLangLabel(file);
//            System.out.println("2-ci "+ln);
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

    public static Carrier getListItemList(Carrier carrier) throws QException {
        try {
            EntityCrListItemList ent = new EntityCrListItemList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrListItemList.ITEM_CODE_NAME);
            ent.addAndStatementField(EntityCrListItemList.ITEM_VALUE);
            ent.setLang(SessionManager.getCurrentLang());
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            carrier.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
            carrier.addTableSequence(CoreLabel.RESULT_SET, EntityManager.getListSequenceByKey("getListItemList"));
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }
        return carrier;
    }

    public static Carrier getListItemMainList(Carrier carrier) throws QException {
        try {
            EntityCrListItemList ent = new EntityCrListItemList();
            ent.setDeepWhere(false);
            ent.addAndStatementField(EntityCrListItemList.ITEM_CODE_NAME);
            ent.addAndStatementField(EntityCrListItemList.ITEM_VALUE);
            ent.addAndStatementField(EntityCrListItemList.LANGUAGE_NAME);
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            carrier.removeKey("startLimit");
            carrier.removeKey("endLimit");

            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
            carrier.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));
            carrier.addTableSequence(CoreLabel.RESULT_SET, EntityManager.getListSequenceByKey("getListItemList"));
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
            //ent.setFkCompanyId(SessionManager.getCurrentUserId());
            EntityManager.insert(ent);

            String tn = "permission";
            int rc = carrier.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                System.out.println("id" + (i + 1) + carrier.getValue(tn, i, "ruleId").toString());
                EntityCrRelUserRule entUserRule = new EntityCrRelUserRule();
                entUserRule.setFkUserId(ent.getId());
                entUserRule.setFkRuleId(carrier.getValue(tn, i, "ruleId").toString());
                EntityManager.insert(entUserRule);
            }

            carrier.setValue(EntityCrPerson.ID, ent.getId());
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier updateUser(Carrier carrier) throws QException {
        try {
            EntityCrUser ent = new EntityCrUser();
            ent.setId(carrier.getValue(EntityCrPerson.ID).toString());
            EntityManager.select(ent);
            EntityManager.mapCarrierToEntity(carrier, ent, false);
            EntityManager.update(ent);

            EntityCrRelUserRule entUserRule = new EntityCrRelUserRule();
            entUserRule.setDeepWhere(false);
            entUserRule.setFkUserId(ent.getId());
            Carrier cUserRule = EntityManager.select(entUserRule);

            int rc1 = cUserRule.getTableRowCount(entUserRule.toTableName());
            for (int i = 0; i < rc1; i++) {
                entUserRule = new EntityCrRelUserRule();
                entUserRule.setId(cUserRule.getValue(entUserRule.toTableName(), i, EntityCrRelUserRule.ID).toString());
                EntityManager.delete(entUserRule);
            }

            //EntityManager.delete(entUserRule);
            String tn = "permission";
            int rc = carrier.getTableRowCount(tn);
            for (int i = 0; i < rc; i++) {
                System.out.println("id" + (i + 1) + carrier.getValue(tn, i, "ruleId").toString());
                entUserRule = new EntityCrRelUserRule();
                entUserRule.setFkUserId(ent.getId());
                entUserRule.setFkRuleId(carrier.getValue(tn, i, "ruleId").toString());
                EntityManager.insert(entUserRule);
            }

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

    private static void addSequence4UserList(Carrier carrier) throws QException {
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
    }

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
        EntityCrAttribute ent = new EntityCrAttribute();
        ent.setId(carrier.getValue("id").toString());
        EntityManager.select(ent);
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

        if (ent.getAppointmentStatus().equals(APPOINTMENT_STATUS_ACTIVE)
                || ent.getAppointmentStatus().equals(APPOINTMENT_STATUS_PASSIVE)) {
            EntityManager.delete(ent);
        } else {
            carrier.addController("general",
                    EntityManager.getMessageText("operationCannotBeDoneInThisStatus"));
            return carrier;
        }

        return carrier;
    }

    public static Carrier getAppointmentList(Carrier carrier) throws QException {
        EntityCrAppointment ent = new EntityCrAppointment();
        EntityManager.mapCarrierToEntity(carrier, ent);
        if (!ent.hasSortBy()) {
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_STATUS);
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_DATE);
            ent.addSortBy(EntityCrAppointment.APPOINTMENT_TIME_1);
            ent.setSortByAsc(true);
        }
        Carrier crAppt = EntityManager.select(ent);
        String tnAppt = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        EntityCrUserList entUser = new EntityCrUserList();
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

        crAppt.mergeCarrier(tnAppt, "fkDoctorUserId", "doctorFullname", cprUser);

        carrier.setValue("id", crAppt.getValueLine(tnAppt, "fkPatientId"));
        Carrier crPatient = getPatientList(carrier);
        crAppt.mergeCarrier(tnAppt, "fkPatientId", crPatient, CoreLabel.RESULT_SET,
                "id", new String[]{"patientId", "patientImage", "patientName",
                    "patientSurname", "patientMiddleName", "patientBirthDate",
                    "patientBirthPlace", "fkOwnerUserId", "sex"});

        Carrier cprSex = QUtility.getListItem("sex",
                carrier.getValue("sexName").toString());
        crAppt.mergeCarrier(tnAppt, "sex", "sexName", cprSex);

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
        crAppt.mergeCarrier(tnAppt, "appointmentStatus",
                "appointmentStatusName", cprApptStatus);

        EntityCrPaymentList entPayment = new EntityCrPaymentList();
        entPayment.setDeepWhere(false);
        entPayment.setPaymentStatus(carrier.getValue("paymentStatus").toString());
        entPayment.setFkPatientId(crAppt.getValueLine(tnAppt, "fkPatientId"));
        entPayment.setFkDoctorUserId(crAppt.getValueLine(tnAppt, "fkDoctorUserId"));
        entPayment.setPaymentDate(crAppt.getValueLine(tnAppt, "appointmentDate"));
        entPayment.setPaymentStatus("P");
        Carrier crPayment = EntityManager.select(entPayment);
        Carrier cprPayment = new Carrier();

        String tnPayment = entPayment.toTableName();
        int rcPayment = crPayment.getTableRowCount(tnPayment);
        for (int i = 0; i < rcPayment; i++) {
            String key = crPayment.getValue(tnPayment, i, "fkPatientId").toString()
                    + crPayment.getValue(tnPayment, i, "fkDoctorUserId").toString()
                    + crPayment.getValue(tnPayment, i, "paymentDate").toString();
            String val = crPayment.getValue(tnPayment, i, "paymentName").toString() + ", "
                    + crPayment.getValue(tnPayment, i, "paymentAmount").toString() + " "
                    + crPayment.getValue(tnPayment, i, "paymentCurrencyName").toString() + "<br>"
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

        Carrier cprSubmoduleType = QUtility.getListItem("submoduleType",
                carrier.getValue("submoduleTypeName").toString());

        c.mergeCarrier(tnSubmodule, "id", "submoduleName", cprName);
        c.mergeCarrier(tnSubmodule, "id", "submoduleDescription", cprDesc);
        c.mergeCarrier(tnSubmodule, "liSubmoduleStatus", "submoduleStatusName",
                cprSubmoduleStatus);
        c.mergeCarrier(tnSubmodule, "submoduleType", "submoduleTypeName",
                cprSubmoduleType);

        c.renameTableName(tnSubmodule, CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getSubmoduleList"));

        c.addTableRowCount(CoreLabel.RESULT_SET,
                (EntityManager.getRowCount(ent) + 1));

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

        carrierSA.mergeCarrier(tnSA, "fkOrganPointId", "organPointName", cprOP);

        Carrier carrierAttr = new Carrier();
        carrierAttr.setValue("id", carrierSA.getValueLine(tnSA, "fkAttributeId"));
        carrierAttr.setValue("attributeName", carrier.getValue("attributeName"));
        carrierAttr = getAttributeList(carrierAttr);
        Carrier cprAttr = carrierAttr.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "attributeName");

        carrierSA.mergeCarrier(tnSA, "fkAttributeId", "attributeName", cprAttr);

        Carrier cprVT = QUtility.getListItem("valueType",
                carrier.getValue("valueTypeName").toString());
        carrierSA.mergeCarrier(tnSA, "fkValueTypeId", "valueTypeName", cprVT);

        Carrier carrierSM = new Carrier();
        carrierSM.setValue("id", carrierSA.getValueLine(tnSA, "fkSubmoduleId"));
        carrierSM.setValue("submoduleName", carrier.getValue("submoduleName"));
        carrierSM = getSubmoduleList(carrierSM);
        Carrier cprSM = carrierSM.getKVFromTable(CoreLabel.RESULT_SET,
                "id", "submoduleName");

        carrierSA.mergeCarrier(tnSA, "fkSubmoduleId", "submoduleName", cprSM);

        Carrier cprHasOther = QUtility.getListItem("yesno",
                carrier.getValue("hasOtherName").toString());
        carrierSA.mergeCarrier(tnSA, "hasOther", "hasOtherName", cprHasOther);

        Carrier cprIsVisible = QUtility.getListItem("yesno",
                carrier.getValue("isVisible").toString());
        carrierSA.mergeCarrier(tnSA, "isVisible", "isVisibleName", cprIsVisible);

        carrierSA.renameTableName(tnSA, CoreLabel.RESULT_SET);
        carrierSA.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getSubmoduleAttributeList"));

        carrierSA.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent) + 1);

        return carrierSA;
    }

    public static Carrier getSubmoduleFormBody(Carrier carrier) throws QException {
        String fkSubmoduleId = carrier.getValue("fkSubmoduleId").toString();
        String fkSessionId = carrier.getValue("fkSessionId").toString();

        Carrier c = new Carrier();
        c.setValue("id", fkSubmoduleId);
        c = getSubmoduleList(c);
        String smName = c.getValue(CoreLabel.RESULT_SET, 0, "submoduleName").toString();

        String body = PgModel.getSubmoduleFormBody(fkSubmoduleId, fkSessionId);
        carrier.setValue("body", body);
        carrier.setValue("header", smName);
        return carrier;
    }

    public static Carrier insertNewPatient(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setFkOwnerUserId(SessionManager.getCurrentUserId());
        ent.setPatientId(genPatientId());
        EntityManager.insert(ent);
        return carrier;
    }

    public static Carrier getAgendaOfDoctor(Carrier carrier) throws QException {

        String fdate = QDate.convertDateToString(QDate.add(QDate.getCurrentDate(), 100));
        String sdate = QDate.convertDateToString(QDate.add(QDate.getCurrentDate(), -30));

        EntityCrAppointmentList ent = new EntityCrAppointmentList();
        ent.setDeepWhere(false);
        ent.setFkDoctorUserId(carrier.getValue("id").toString());
        ent.setAppointmentDate("GE%" + sdate);
        ent.setAppointmentDate("LE%" + fdate);
        carrier = EntityManager.select(ent);
        String tn = ent.toTableName();
        int rc = carrier.getTableRowCount(tn);
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        String res = "[";
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);

            String title = ent.getPatientName()
                    + " " + ent.getPatientMiddleName() + " "
                    + ent.getPatientSurname() + " (" + ent.getPatientId() + ")";

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
        carrier.setValue("res", res);
        return carrier;
    }

    private static String genPatientId() throws QException {
        String format = "170000001";
        String res = "170000001";

        EntityCrPatient ent = new EntityCrPatient();
        ent.addSortBy(EntityCrPatient.PATIENT_ID);
        ent.setSortByAsc(false);
        ent.setStartLimit(0);
        ent.setEndLimit(1);
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
        ent.addSortBy(EntityCrPayment.PAYMENT_NO);
        ent.setSortByAsc(false);
        ent.setStartLimit(0);
        ent.setEndLimit(1);
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

    public static Carrier getPatientList(Carrier carrier) throws QException {
        EntityCrPatient ent = new EntityCrPatient();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setFkOwnerUserId(SessionManager.getCurrentUserId());
        Carrier crPtnt = EntityManager.select(ent);
        String tnPtnt = ent.toTableName();
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        Carrier cprSex = QUtility.getListItem("sex",
                carrier.getValue("sexName").toString());
        crPtnt.mergeCarrier(tnPtnt, "sex", "sexName", cprSex);

        Carrier cprOcp = QUtility.getListItem("occupation",
                carrier.getValue("occupationName").toString());
        crPtnt.mergeCarrier(tnPtnt, "occupation", "occupationName", cprOcp);

        Carrier cprMrt = QUtility.getListItem("maritualStatus",
                carrier.getValue("maritualStatusName").toString());
        crPtnt.mergeCarrier(tnPtnt, "maritualStatus", "maritualStatusName", cprMrt);

        Carrier cprEdu = QUtility.getListItem("educationType",
                carrier.getValue("educationName").toString());
        crPtnt.mergeCarrier(tnPtnt, "education", "educationName", cprEdu);

        Carrier cprBloodGroup = QUtility.getListItem("bloodGroup",
                carrier.getValue("bloodGroupName").toString());
        crPtnt.mergeCarrier(tnPtnt, "bloodGroup", "bloodGroupName", cprBloodGroup);

        Carrier cprRhFactor = QUtility.getListItem("rhFactor",
                carrier.getValue("rhFactorName").toString());
        crPtnt.mergeCarrier(tnPtnt, "rhFactor", "rhFactorName", cprRhFactor);

        Carrier cprIsActive = QUtility.getListItem("pa",
                carrier.getValue("isActiveName").toString());
        crPtnt.mergeCarrier(tnPtnt, "isActive", "isActiveName", cprIsActive);

        crPtnt.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        crPtnt.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getPatientList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        crPtnt.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return crPtnt;
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
                ent.setFkUserId(SessionManager.getCurrentUserId());
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
                    ent.setHaInspectionValue(haVal);
                    ent.setInspectionDate(QDate.getCurrentDate());
                    ent.setInspectionTime(QDate.getCurrentTime());
                    ent.setFkUserId(SessionManager.getCurrentUserId());
                    ent.setFkSubmoduleAttributeId(k);
                    ent.setInspectionValue(val);
                    EntityManager.insert(ent);
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
    public Carrier getInspectionListBySession(Carrier carrier) throws QException {
        EntityCrAppointment entApp = new EntityCrAppointment();
        entApp.setId(carrier.getValue("fkSessionId").toString());
        EntityManager.select(entApp);

        if (entApp.getFkDoctorUserId().length() == 0) {
            carrier.addController("general", EntityManager.getMessageText("sessionIsNotExist"));
            return carrier;
        }

        EntityCrInspectionList ent = new EntityCrInspectionList();
        ent.setInspectionCode(entApp.getId());
        ent.setFkPatientId(entApp.getFkPatientId());
        ent.setFkSubmoduleId(carrier.getValue("fkSubmoduleId").toString());
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.removeColoumn(CoreLabel.RESULT_SET, "fkCompanyId");
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getInspectionList"));

        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return c;
    }

    public static Carrier getInspectionList(Carrier carrier) throws QException {
        EntityCrInspection ent = new EntityCrInspection();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier cIns = EntityManager.select(ent);
        String tnIns = ent.toTableName();
        cIns.copyTableColumn(tnIns,
                "fkSubmoduleAttributeId", "saSubmoduleAttributeId");
        carrier.removeKey("startLimit");
        carrier.removeKey("endLimit");

        //get user info
        EntityCrUserList entUser = new EntityCrUserList();
        entUser.setDeepWhere(false);
        entUser.setId(cIns.getValueLine(tnIns, "fkUserId"));
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
        cIns.mergeCarrier(tnIns, "fkUserId", "doctorFullname", cprUser);

        //get and merge patient info
        carrier.setValue("id", cIns.getValueLine(tnIns, "fkPatientId"));
        Carrier crPatient = getPatientList(carrier);
        cIns.mergeCarrier(tnIns, "fkPatientId", crPatient, CoreLabel.RESULT_SET,
                "id", new String[]{"patientId", "patientImage", "patientName",
                    "patientSurname", "patientMiddleName", "patientBirthDate",
                    "patientBirthPlace", "sex"});

        //get and merge patient sex info
        Carrier cprSex = QUtility.getListItem("sex",
                carrier.getValue("sexName").toString());
        cIns.mergeCarrier(tnIns, "sex", "sexName", cprSex);

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
        EntityCrReportLineList ent = new EntityCrReportLineList();
        EntityManager.mapCarrierToEntity(carrier, ent);
        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        c.addTableSequence(CoreLabel.RESULT_SET,
                EntityManager.getListSequenceByKey("getReportLineList"));

        EntityManager.mapCarrierToEntity(carrier, ent);
        c.addTableRowCount(CoreLabel.RESULT_SET, EntityManager.getRowCount(ent));

        return c;
    }

    public Carrier getReportLineList4Print(Carrier carrier) throws QException {
        EntityCrReportLineList ent = new EntityCrReportLineList();
        EntityManager.mapCarrierToEntity(carrier, ent);
//        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);

        if (c.getTableRowCount(ent.toTableName()) > 0) {
            String arg = c.getValue(
                    ent.toTableName(), 0, EntityCrReportLineList.REPORT_HTML).toString();
            arg = replaceReportStingWithTags(arg,
                    carrier.getValue("fkSessionId").toString());
            c.setValue(
                    ent.toTableName(), 0, EntityCrReportLineList.REPORT_HTML, arg);
        }

        c.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);
        return c;
    }

    private static String replaceReportStingWithTags(String arg, String fkSessionId) throws QException {
        //movcud taglar
        //<qa></qa> attribute adlari ucun
        //<qp><qp> patient melumatlari ucun
        //<qimg></qimg> attribute de olan sekiller
        //<qpimg></qpimg> patient sekli haqqinda
        //<qlogo> istifadecinin daxil etdiyi logo.
        arg = arg.replace("&lt;", "<").replace("&gt;", ">");
        Document doc = Jsoup.parse(arg, "UTF-8");

        doc = replaceReportStingWithTagsQa(doc, fkSessionId);
        doc = replaceReportStingWithTagsQp(doc, fkSessionId);

        return doc.toString();
    }

    private static Document replaceReportStingWithTagsQa(Document doc, String fkSessionId) throws QException {

//        EntityCrInspectionList ent = new EntityCrInspectionList();
//        ent.setFkPatientId(fkPatientId);
//        ent.setFkUserId(SessionManager.getCurrentUserId());
//        ent.setInspectionCode(fkSessionId);
        Carrier c = new Carrier();
        c.setValue("inspectionCode", fkSessionId);
        c = CrModel.getInspectionList(c);

        Elements elements = doc.getElementsByTag("qa");
        Elements elementImgs = doc.getElementsByTag("qimg");
        String tn = CoreLabel.RESULT_SET;
        int rc = c.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            String key = c.getValue(tn, i, "attributeName").toString();
            String val = c.getValue(tn, i, "finalValue").toString();
            String insVal = c.getValue(tn, i, "inspectionValue").toString();
            String type = c.getValue(tn, i, "fkValueTypeId").toString();
            try {

                for (Element element : elements) {
                    String html = element.html().trim();
                    if (html.contains(key.trim()) || html.equals(key.trim())) {
                        String v = html.contains(key) || html.equals(key.trim())
                                ? html.replace(key, val)
                                : QUtility.getUndefinedLabel();
                        element.html(v);
                    }

                }
            } catch (Exception e) {
            }
            try {
                for (Element element : elementImgs) {
                    String html = element.html().trim();
                    String height = element.hasAttr("h") ? element.attr("h") : "";
                    String width = element.hasAttr("w") ? element.attr("w") : "";
                    if (html.contains(key.trim()) || html.equals(key.trim())) {
                        String v = "";
                        if (type.equals(PgModel.VALUE_TYPE_PICTURE)) {
                            v += "<img ";
                            v += height.length() > 0 ? " height='" + height + "'; " : "";
                            v += width.length() > 0 ? " width='" + width + "'; " : "";
                            v += " src='resources/upload/" + insVal + "'>";
                        } else if (type.equals(PgModel.VALUE_TYPE_PICTURE_URL)) {
                            v += "<img ";
                            v += height.length() > 0 ? " height='" + height + "'; " : "";
                            v += width.length() > 0 ? " width='" + width + "'; " : "";
                            v += " src='" + insVal + "'>";
                        }
                        element.html(v);
                    }

                }
            } catch (Exception e) {
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
        getPraatAnalyseResult("a.wav") ;
//        Connection conn = null;
                //0.4
                //        try {
                //            conn = new DBConnection().getConnection();
                //            conn.setAutoCommit(false);
                //            SessionManager.setConnection(Thread.currentThread().getId(), conn);
                //            String json = " {\"kv\":{\"id\":\"201706060832030053\","
                //                    + "\"fkSessionId\":\"201706110823490222 \","
                //                    + "\"fkModuleId\":\"201706221522420346\"}}";
                //            String servicename = "serviceCrGetReportLineList4Print";
                ////
                //
                //            Carrier c = new Carrier();
                //            c.setServiceName(servicename);
                //            c.fromJson(json);
                //            System.out.println(c.getJson());
                ////            System.out.println(c.getJson());
                //            CallDispatcher.callService(c);
                //            conn.commit();
                //            conn.close();
                //    }
                //    catch (Exception ex
                //
                //    
                //        ) {
                //            try {
                //            conn.rollback();
                //            conn.close();
                //        } catch (SQLException ex1) {
                //        }
                //    }
    }

	public static Carrier getVoiceAnalyse(Carrier carrier)  throws QException{
        String filename = carrier.getValue("filename").toString();
        carrier = getPraatAnalyseResult(filename);
        return carrier;
    }

	private static Carrier getPraatAnalyseResult(String filename) {
        Carrier carrier = new Carrier();
        try {

            String ext = ".wav";
            CommonConfigurationProperties prop = new CommonConfigurationProperties();
            String praatline = prop.getProperty("praatline");
            praatline += filename;
            System.out.println("praat line-> "+praatline);
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

        System.out.println("sql->" + ln);

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
        carrier.setValue("text", EntityManager.getMessageText(
                carrier.getValue("messageCode").toString()));
        return carrier;
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
        carrier.setValue(EntityCrUser.LI_USER_PERMISSION_CODE, "D");
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
        EntityCrEntityLabelList ent = new EntityCrEntityLabelList();
        ent.setDeepWhere(false);
        ent.setLang(SessionManager.getCurrentLang());
        ent.setFieldName(carrier.getValue("code").toString());
        EntityManager.select(ent);

        String t = ent.getDescription().trim().length() == 0
                ? "{" + carrier.getValue("code").toString() + "}"
                : ent.getDescription();
        carrier.setValue("text", t);
        return carrier;
    }
    
     public static Carrier getBasicStatistics(Carrier carrier)  throws QException{
        String matrixId = carrier.getValue("fkModuleId").toString();
        String saId = carrier.getValue("fkSubmoduleAttributeId").toString();
        
        return carrier;
     }

    public static Carrier signupCompany(Carrier carrier) throws QException {
        try {
            EntityCrCompany entCompany = new EntityCrCompany();
            EntityManager.mapCarrierToEntity(carrier, entCompany);
            entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
            String activationId = IdGenerator.nextRandomSessionId();
            entCompany.setActivationId(activationId);
            entCompany.setCompanyType(EntityCrCompany.CompanyType.COMPANY.toString());

            entCompany.setCompanyDb("apd_" + IdGenerator.nextDbId());
            EntityManager.insert(entCompany);

            EntityCrUser entUser = new EntityCrUser();
            EntityManager.mapCarrierToEntity(carrier, entUser);
            entUser.setUserShortId(IdGenerator.getId());
            entUser.setFkCompanyId(entCompany.getId());//SessionManager.getCurrentUserId()
            EntityManager.insert(entUser);
            carrier.setValue(EntityCrPerson.ID, entUser.getId());

            MailSender.send(entUser.getEmail1(), "Company registered",
                    "Activate company " + entCompany.getCompanyName() + "  http://localhost:8080/apd/api/post/signup/activate/" + activationId);

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier signupPersonal(Carrier carrier) throws QException {
        try {
            EntityCrCompany entCompany = new EntityCrCompany();
            EntityManager.mapCarrierToEntity(carrier, entCompany);
            entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
            String activationId = IdGenerator.nextRandomSessionId();
            entCompany.setActivationId(activationId);
            entCompany.setCompanyDb("apd_" + IdGenerator.nextDbId());
            entCompany.setCompanyDomain("prs_" + carrier.getValue("username"));
            entCompany.setCompanyName("prs_" + carrier.getValue("username"));
            entCompany.setCompanyType(EntityCrCompany.CompanyType.PERSONAL.toString());
            EntityManager.insert(entCompany);

            EntityCrUser entUser = new EntityCrUser();
            EntityManager.mapCarrierToEntity(carrier, entUser);
            entUser.setUserShortId(IdGenerator.getId());
            entUser.setFkCompanyId(entCompany.getId());//SessionManager.getCurrentUserId()
            EntityManager.insert(entUser);
            carrier.setValue(EntityCrPerson.ID, entUser.getId());

            MailSender.send(entUser.getEmail1(), "Your account created",
                    "Activate account " + entCompany.getCompanyName() + "  http://localhost:8080/apd/api/post/signup/activate/" + activationId);

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier activateCompany(Carrier carrier) throws QException {
        try {
            EntityCrCompany entCompany = new EntityCrCompany();
            EntityManager.mapCarrierToEntity(carrier, entCompany);
            entCompany.setStatus(EntityCrCompany.CompanyStatus.VERIFY.toString());
            EntityManager.select(entCompany);
            entCompany.setStatus(EntityCrCompany.CompanyStatus.PENDING.toString());
            EntityManager.update(entCompany);

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
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
            //carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

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
        EntityCrRelRulePermission ent = new EntityCrRelRulePermission();
        EntityManager.mapCarrierToEntity(carrier, ent);
        //ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
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

            carrier = EntityManager.selectBySql("select rp.id, r.rule_name, p.permission_string from cr_rule r, cr_rel_rule_permission rp, cr_permission p "
                    + " where r.id=rp.fk_rule_id and rp.fk_permission_id=p.id "
                    + " and r.status='A' and rp.status='A' and p.status='A'");

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getRulePermissionList"));
            carrier.addTableRowCount("rowCount", 1000);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public Carrier deleteRulePermission(Carrier carrier) throws QException {
        EntityCrRelRulePermission ent = new EntityCrRelRulePermission();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getRoleList(Carrier carrier) throws QException {
        try {
            EntityCrRole ent = new EntityCrRole();
            ent.setDeepWhere(false);
            EntityManager.mapCarrierToEntity(carrier, ent);
            carrier = EntityManager.select(ent);
            //carrier.removeColoumn(ent.toTableName(), EntityCrUser.PASSWORD);
            carrier.renameTableName(ent.toTableName(), CoreLabel.RESULT_SET);

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getRoleList"));
            carrier.addTableRowCount("rowCount", EntityManager.getRowCount(ent));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }

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
        EntityCrRelRoleRule ent = new EntityCrRelRoleRule();
        EntityManager.mapCarrierToEntity(carrier, ent);
        //ent.setLang(SessionManager.getCurrentLang());
        EntityManager.insert(ent);
        return carrier;
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
    public static Carrier getRoleRuleList(Carrier carrier) throws QException {
        try {

            carrier = EntityManager.selectBySql("select rp.id, r.role_name, p.rule_name from " + SessionManager.getCurrentDomain() + ".cr_role r, "
                    + SessionManager.getCurrentDomain() + ".cr_rel_role_rule rp, apdvoice.cr_rule p "
                    + " where r.id=rp.fk_role_id and rp.fk_rule_id=p.id "
                    + " and r.status='A' and rp.status='A' and p.status='A' ");

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getRoleRuleList"));
            carrier.addTableRowCount("rowCount", 1000);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public Carrier deleteRoleRule(Carrier carrier) throws QException {
        EntityCrRelRoleRule ent = new EntityCrRelRoleRule();
        EntityManager.mapCarrierToEntity(carrier, ent);
        EntityManager.delete(ent);
        return carrier;
    }

    public static Carrier getUserRuleList(Carrier carrier) throws QException {
        try {
            String id = carrier.getValue("id").toString();
            String fkRoleId = carrier.getValue("fkRoleId").toString();
            System.out.println("id=" + id);
            System.out.println("fkRoleId=" + fkRoleId);

            if (fkRoleId == null || fkRoleId.isEmpty()) {
                carrier = EntityManager.selectBySql("select r.id, r.rule_name, if(ru.fk_rule_id is null,0,1) rule_access  from cr_rule r "
                        + "left outer join " + SessionManager.getCurrentDomain() + ".cr_rel_user_rule ru on r.id=ru.fk_rule_id and ru.fk_user_id=? and ru.status='A' "
                        + "where r.status='A'", new String[]{id});

            } else {
                carrier = EntityManager.selectBySql(" select r.id, r.rule_name, if(ru.fk_rule_id is null,0,1) rule_access  from cr_rule r "
                        + " join " + SessionManager.getCurrentDomain() + ".cr_rel_role_rule ro on r.id=ro.fk_rule_id and ro.status='A' and ro.fk_role_id=? "
                        + " left outer join " + SessionManager.getCurrentDomain() + ".cr_rel_user_rule ru on r.id=ru.fk_rule_id and ru.fk_user_id=? and ru.status='A' "
                        + " where r.status='A'", new String[]{fkRoleId, id});
            }

            carrier.addTableSequence(CoreLabel.RESULT_SET,
                    EntityManager.getListSequenceByKey("getUserRuleList"));
            carrier.addTableRowCount("rowCount", 1000);
            return carrier;

        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
                    }.getClass().getEnclosingMethod().getName(), ex);
        }

    }
}
