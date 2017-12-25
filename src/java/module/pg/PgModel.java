/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.pg;

import utility.Carrier;
import utility.QException;
import static j2html.TagCreator.*;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import label.CoreLabel;
import module.cr.CrModel;
import module.cr.entity.EntityCrAppointment;
import module.cr.entity.EntityCrAppointmentList;
import module.cr.entity.EntityCrInspection;
import module.cr.entity.EntityCrInspectionList;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrPatient;
import module.cr.entity.EntityCrPrivateAttribute;
import module.cr.entity.EntityCrPrivateSubmodule;
import module.cr.entity.EntityCrRelPriceListAndSubmodule;
import module.cr.entity.EntityCrSubmodule;
import module.cr.entity.EntityCrSubmoduleAttribute;
import module.cr.entity.EntityCrSubmoduleAttributeList;
import utility.QUtility;
import utility.SessionManager;
import utility.ValueTypeHtml;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author user
 */
public class PgModel {

    private static final String USER_CONTROLLER_TYPE_ENUB = "2";
    private static final String USER_CONTROLLER_TYPE_COMPONENT = "1";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE = "sa_";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER = "ha_";

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
    public static final String VALUE_TYPE_SOUND_URL = "18";
    public static final String VALUE_TYPE_SOUND_UPLOAD = "19";
    public static final String VALUE_TYPE_DATE = "20";
    public static final String RANGE_STRING_MULTI_MANUAL = "21";
    public static final String RANGE_STRING_MANUAL = "22";

    public static final String SUBMODULE_BUTTON_COLOR_IF_EMPTY = "#ABB2B9";
    public static final String SUBMODULE_BUTTON_COLOR_IF_FULL = "#00b289";
    public static final String SUBMODULE_BUTTON_COLOR_IF_HALF = "#EB984E";

    public static final String SUBMODULE_TYPE_ORDINARY = "1";
    public static final String SUBMODULE_TYPE_AGGREGATE = "2";
    public static final String SUBMODULE_TYPE_AVERAGE = "3";
    public static final String SUBMODULE_TYPE_TREATMENT = "4";
    public static final String SUBMODULE_TYPE_LAST_REMARK = "5";

    public static void main(String[] arg) throws QException {
        String id = "201705051736080211";
        String mid = "201706221522420346";
        String sid = "";
        String st = genSubmodule(mid, sid);
//        System.out.println(st);
    }

    private static Carrier getFilledCountOfSubmoduleBySession(String smId, String fkSessionId)
            throws QException {

        EntityCrSubmoduleAttribute entSA = new EntityCrSubmoduleAttribute();
        entSA.setDeepWhere(false);
        entSA.setFkSubmoduleId(smId);
        Carrier c = EntityManager.select(entSA);
        c.setValue("generalCount", c.getTableRowCount(entSA.toTableName()));

        String saIds = c.getValueLine(entSA.toTableName(),
                EntityCrSubmoduleAttribute.ID, CoreLabel.IN);

        EntityCrInspection entIns = new EntityCrInspection();
        entIns.setDeepWhere(false);
        entIns.setFkSubmoduleAttributeId(saIds);
//        entIns.setFkUserId(SessionManager.getCurrentUserId());
        entIns.setInspectionCode(fkSessionId);
        Carrier c1 = EntityManager.select(entIns);

        c.setValue("inspectionCount", c1.getTableRowCount(entIns.toTableName()));

        return c;
    }

    private static Carrier getFilledCountOfPrivateSubmoduleBySession(String prsmId, String fkSessionId)
            throws QException {

        EntityCrPrivateAttribute entPA = new EntityCrPrivateAttribute();
        entPA.setDeepWhere(false);
        entPA.setFkPrivateSubmoduleId(prsmId);;
        Carrier c = EntityManager.select(entPA);
        c.setValue("generalCount", c.getTableRowCount(entPA.toTableName()));

        String saIds = c.getValueLine(entPA.toTableName());

        EntityCrInspection entIns = new EntityCrInspection();
        entIns.setDeepWhere(false);
        entIns.setFkPrivateSubmoduleAttributeId(saIds);
        entIns.setInspectionCode(fkSessionId);
        Carrier c1 = EntityManager.select(entIns);

        c.setValue("inspectionCount", c1.getTableRowCount(entIns.toTableName()));

        return c;
    }

    private static double getValueOfAggregateSubmodule(
            String fkSessionId, String fkSubmoduleId) throws QException {
        double res = 0;
        if (fkSessionId.length() == 0) {
            return 0;
        }

        EntityCrInspectionList ent = new EntityCrInspectionList();
        ent.setDeepWhere(false);
        ent.setInspectionCode(fkSessionId);
        ent.setFkSubmoduleId(fkSubmoduleId);
        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);

        String tn = ent.toTableName();
        int rc = c.getTableRowCount(tn);
        double sum = 0;
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(c, tn, i, ent);
            try {
                sum += Double.parseDouble(ent.getInspectionValue());
            } catch (Exception e) {
            }
        }
        DecimalFormat df = new DecimalFormat("#.####");
        sum = Double.valueOf(df.format(sum));
        res = sum;
        return res;
    }

    private static double getValueOfAverageSubmodule(
            String fkSessionId, String fkSubmoduleId) throws QException {
        double res = 0;

        if (fkSessionId.length() == 0) {
            return 0;
        }

        EntityCrInspectionList ent = new EntityCrInspectionList();
        ent.setDeepWhere(false);
        ent.setInspectionCode(fkSessionId);
        ent.setFkSubmoduleId(fkSubmoduleId);
        ent.setFkUserId(SessionManager.getCurrentUserId());
        Carrier c = EntityManager.select(ent);

        String tn = ent.toTableName();
        int rc = c.getTableRowCount(tn);
        double sum = 0;
        int idx = 1;
        for (int i = 0; i < rc; i++) {
            EntityManager.mapCarrierToEntity(c, tn, i, ent);
            try {
                sum += Double.parseDouble(ent.getFinalValue());
                idx++;
            } catch (Exception e) {
            }
        }
        double rs = sum / idx;
        DecimalFormat df = new DecimalFormat("#.####");
        rs = Double.valueOf(df.format(rs));
        res = rs;
        return res;
    }

    public static String genSubmodule(String fkModuleId, String fkSessionId) throws QException {
        if (fkModuleId.trim().length() == 0) {
            return "";
        }

        String ln = "";
        ln += getSubmoduleBtnListByCore(fkModuleId, fkSessionId);
        ln += "<br>";
        ln += getSubmoduleBtnListByPrivate(fkModuleId, fkSessionId);

//        System.out.println("submodule btn ln>>>>" + ln);
        return ln;
    }

    private static Carrier getSubmoduleList4BtnList(String fkModuleId,
            String fkSessionId) throws QException {
        //RelPriceListAndSubmodule-de submodullarin olmasi yoxlanilir.
        //eger yoxdursa o zaman butun submodullari getirir
        Carrier carrier = new Carrier();
        boolean f = false;

        EntityCrAppointmentList entAppt = new EntityCrAppointmentList();
        entAppt.setId(fkSessionId);
        Carrier tc = EntityManager.select(entAppt);

        if (tc.getTableRowCount(entAppt.toTableName()) == 0) {
            f = true;
        } else {
            Carrier tc1 = new Carrier();
            tc1.setValue("fkPriceListId", entAppt.getFkPriceListId());
            tc1 = CrModel.getRelPriceListAndSubmoduleList(tc1);
            if (tc1.getTableRowCount(CoreLabel.RESULT_SET) > 0) {
                String ids = tc1.getValueLine(CoreLabel.RESULT_SET, "fkSubmoduleId");
                carrier.setValue("id", ids);
                carrier.setValue("asc", "sortBy");
                carrier = CrModel.getSubmoduleList(carrier);
            } else {
                f = true;
            }
        }

        if (f) {
            carrier.setValue("fkModuleId", fkModuleId);
            carrier.setValue("asc", "sortBy");
            carrier = CrModel.getSubmoduleList(carrier);
        }

        return carrier;
    }

    private static Carrier getSubmoduleList4BtnListByPrivate(String fkModuleId,
            String fkSessionId) throws QException {
        //RelPriceListAndSubmodule-de submodullarin olmasi yoxlanilir.
        //eger yoxdursa o zaman butun submodullari getirir
        Carrier carrier = new Carrier();
        boolean f = false;

        EntityCrAppointmentList entAppt = new EntityCrAppointmentList();
        entAppt.setId(fkSessionId);
        Carrier tc = EntityManager.select(entAppt);

        if (tc.getTableRowCount(entAppt.toTableName()) == 0) {
            f = true;
        } else {
            Carrier tc1 = new Carrier();
            tc1.setValue("fkPriceListId", entAppt.getFkPriceListId());
            tc1 = CrModel.getRelPriceListAndSubmoduleList(tc1);
            if (tc1.getTableRowCount(CoreLabel.RESULT_SET) > 0) {
                String ids = tc1.getValueLine(CoreLabel.RESULT_SET, "fkSubmoduleId");
                carrier.setValue("id", ids);
                carrier.setValue("asc", "orderNo");
                carrier = CrModel.getPrivateSubmoduleList(carrier);
            } else {
                f = true;
            }
        }

        if (f) {
//            System.out.println("fkModuleId"+fkModuleId);
            carrier.setValue("fkModuleId", fkModuleId);
            carrier.setValue("asc", "orderNo");
            carrier = CrModel.getPrivateSubmoduleList(carrier);
        }

        return carrier;
    }

    private static String getSubmoduleBtnListByCore(String fkModuleId, String fkSessionId) throws QException {
        String ln = "";
        String insCode = "";

        Carrier carrier = getSubmoduleList4BtnList(fkModuleId, fkSessionId);

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        List<CsSubmodule> entArr = new ArrayList<>();

        for (int i = 0; i < rc; i++) {
            EntityCrSubmodule entTmp = new EntityCrSubmodule();
            EntityManager.mapCarrierToEntity(carrier, tn, i, entTmp);
            entTmp.setId(entTmp.getId());

            if (entTmp.getSubmoduleType().equals(SUBMODULE_TYPE_AGGREGATE)) {
                double sum = getValueOfAggregateSubmodule(fkSessionId, entTmp.getId());
                DecimalFormat df = new DecimalFormat("#.####");
                sum = Double.valueOf(df.format(sum));
                String name = sum > 0 ? entTmp.getSubmoduleName() + "(" + sum + ")"
                        : entTmp.getSubmoduleName();
                entTmp.setSubmoduleName(name);
            } else if (entTmp.getSubmoduleType().equals(SUBMODULE_TYPE_AVERAGE)) {
                double ave = getValueOfAverageSubmodule(fkSessionId, entTmp.getId());
                DecimalFormat df = new DecimalFormat("#.####");
                ave = Double.valueOf(df.format(ave));

                String name = ave > 0 ? entTmp.getSubmoduleName() + "(" + ave + ")"
                        : entTmp.getSubmoduleName();
                entTmp.setSubmoduleName(name);
            }

            CsSubmodule cs = new CsSubmodule();
            cs.setSubmodule(entTmp);

            cs.setColor(SUBMODULE_BUTTON_COLOR_IF_EMPTY);

            if (fkSessionId.trim().length() > 0) {
                Carrier c = getFilledCountOfSubmoduleBySession(
                        entTmp.getId(), fkSessionId);
                int generalCount = Integer.parseInt(c.getValue("generalCount").toString());
                int inspectionCount = Integer.parseInt(c.getValue("inspectionCount").toString());

                if (inspectionCount == 0) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_EMPTY);
                } else if (inspectionCount < generalCount) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_HALF);
                } else if (inspectionCount == generalCount) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_FULL);
                }
            }

            entArr.add(cs);
        }

        ln = getSubmoduleHtml(entArr, insCode);
        return ln;
    }

    private static String getSubmoduleBtnListByPrivate(String fkModuleId, String fkSessionId) throws QException {
        String ln = "";
        String insCode = "";

        Carrier carrier = getSubmoduleList4BtnListByPrivate(fkModuleId, fkSessionId);

        String tn = CoreLabel.RESULT_SET;
        int rc = carrier.getTableRowCount(tn);
        List<CsSubmodule> entArr = new ArrayList<>();

        for (int i = 0; i < rc; i++) {
            EntityCrPrivateSubmodule entTmp = new EntityCrPrivateSubmodule();
            EntityManager.mapCarrierToEntity(carrier, tn, i, entTmp);
            entTmp.setId(entTmp.getId());

            CsSubmodule cs = new CsSubmodule();
            cs.setPrivateSubmodule(entTmp);

            cs.setColor(SUBMODULE_BUTTON_COLOR_IF_EMPTY);

            if (fkSessionId.trim().length() > 0) {
                Carrier c = getFilledCountOfPrivateSubmoduleBySession(
                        entTmp.getId(), fkSessionId);
                int generalCount = Integer.parseInt(c.getValue("generalCount").toString());
                int inspectionCount = Integer.parseInt(c.getValue("inspectionCount").toString());

                if (inspectionCount == 0) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_EMPTY);
                } else if (inspectionCount < generalCount) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_HALF);
                } else if (inspectionCount >= generalCount) {
                    cs.setColor(SUBMODULE_BUTTON_COLOR_IF_FULL);
                }
            }

            entArr.add(cs);
        }

        ln = getPrivateSubmoduleHtml(entArr, insCode);
        return ln;
    }

    private static String getSubmoduleHtml(List<CsSubmodule> entArr, String insCode) {
        String ln
                = div().with(
                        div().withClass("col-md-12 apd-page-btn")
                        .with(
                                each(entArr, ent
                                        -> button(ent.getSubmodule().getSubmoduleName())
                                        .withType("button")
                                        .attr("data-toggle", "modal")
                                        .attr("data-target", "#popup1").
                                        attr("submodule_id", ent.getSubmodule().getId()).
                                        attr("style", "background-color:" + ent.getColor()
                                                + ";border-color:" + ent.getColor()).
                                        attr("ins_id", insCode).
                                        attr("sort_by", ent.getSubmodule().getSortBy()).
                                        withClass("btn apd-subm-attr-button btn-primary btn-md"))
                        )
                ).render();
        return ln;
    }

    private static String getPrivateSubmoduleHtml(List<CsSubmodule> entArr, String insCode) {
        String ln
                = div().with(
                        div().withClass("col-md-12 apd-page-btn")
                        .with(
                                each(entArr, ent
                                        -> button(ent.getPrivateSubmodule().getName())
                                        .withType("button")
                                        .attr("is_private", "")
                                        .attr("data-toggle", "modal")
                                        .attr("data-target", "#popup1").
                                        attr("submodule_id", ent.getPrivateSubmodule().getId()).
                                        attr("style", "background-color:" + ent.getColor() + ";border-color:" + ent.getColor()).
                                        attr("ins_id", insCode).
                                        attr("sort_by", ent.getPrivateSubmodule().getOrderNo()).
                                        withClass("btn apd-subm-attr-button btn-primary btn-md")),
                                hr()
                        )
                ).render();
        return ln;
    }

    public static String getPrivateSubmoduleFormBody(String fkSubmoduleId,
            String fkSessionId, Carrier crInspection) throws QException {

        Carrier cprIns = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkPrivateSubmoduleAttributeId", "inspectionValue");
        Carrier cprInsHa = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkPrivateSubmoduleAttributeId", "haInspectionValue");

        Carrier carrier = new Carrier();
        carrier.setValue("fkPrivateSubmoduleId", fkSubmoduleId);
        carrier.setValue("asc", "sortBy");
        carrier = CrModel.getPrivateAttributeList(carrier);
        String tn = CoreLabel.RESULT_SET;

        int rc = carrier.getTableRowCount(tn);
        ArrayList<CsTag> tags = new ArrayList<>();
        for (int i = 0; i < rc; i++) {
            EntityCrPrivateAttribute ent = new EntityCrPrivateAttribute();
            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);
            String insVal = cprIns.getValue(ent.getId()).toString();
            String haInsVal = cprInsHa.getValue(ent.getId()).toString();

            CsTag tag = new CsTag();
            Tag val = null;

            tag.setHeader(ent.getName());
            tag.setVal(val);
            tags.add(tag);
        }
        String ln = generateSubmoduleFormElementBody(tags, fkSessionId, fkSubmoduleId);
        try {
            ln = QUtility.checkLangLabel(ln);
        } catch (IOException ex) {

        }
        return ln;
    }

    private static String generateSubmoduleFormElementBody(ArrayList<CsTag> tagArr,
            String fkSessionId) throws QException {
        return generateSubmoduleFormElementBody(tagArr, fkSessionId, "");
    }

    private static EntityCrPatient getPatientInfoBySessionId(String fkSessionId)
            throws QException {
        EntityCrAppointment entAppt = new EntityCrAppointment();
        entAppt.setId(fkSessionId);
        EntityManager.select(entAppt);

        EntityCrPatient entPtnt = new EntityCrPatient();
        entPtnt.setId(entAppt.getFkPatientId());
        EntityManager.select(entPtnt);

        return entPtnt;
    }

    private static String generateSubmoduleFormElementBody(ArrayList<CsTag> tagArr,
            String fkSessionId, String fkSubmoduleId) throws QException {
//        EntityCrAppointment ent = new EntityCrAppointmentList();
//        ent.setDeepWhere(false);
//        ent.setId(fkSessionId);
//        EntityManager.select(ent);
        EntityCrSubmodule entSM = new EntityCrSubmodule();
        entSM.setId(fkSubmoduleId);
        EntityManager.select(entSM);

        String sortByNo = entSM.getSortBy();
        EntityCrPatient entPtnt = getPatientInfoBySessionId(fkSessionId);
        String patientFullname
                = entPtnt.getPatientName() + " " + entPtnt.getPatientMiddleName() + " "
                + entPtnt.getPatientSurname();

        String fkPatientId
                = entPtnt.getId();

        String ln = div()
                .with(
                        div().withClass("form-group col-md-12")
                        .with(
                                formLabel("patientName").attr("qlang", ""),
                                formInput("", patientFullname)
                                .attr("readonly", "readonly")
                                .attr("dont_clear", "")
                        ),
                        getVoiceAnalyseDiv(fkSubmoduleId),
                        //ses analiz hissesi hal hazirda static yazilibdir. 
                        //lakin bu hisse dinamik yazilmalidir
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel("fkPatientId"),
                                formInput("fkPatientId", "fkPatientId")
                                .withValue(fkPatientId)
                                .attr("dont_clear", "")
                                .withType("hidden")
                        ),
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel("sort_by"),
                                formInput("smOrderNo", "smOrderNo")
                                .withValue(sortByNo)
                                .attr("dont_clear", "")
                                .withType("hidden")
                        ),
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel(""),
                                formHiddenInput(fkSessionId)
                                .attr("dont_clear", "")
                        ),
                        each(tagArr, tag
                                -> div().withClass("form-group col-md-6")
                                .attr("style", "min-height: 60px")
                                .with(
                                        label(tag.getHeader()).withClass("float"),
                                        tag.getVal()
                                )
                        )
                ).render();
//        System.out.println("sa body-" + ln);
        return ln;
    }

    private static String generateSubmoduleFormElementBodyByPrivate(ArrayList<CsTag> tagArr,
            String fkSessionId, String sortByNo) throws QException {
//        EntityCrAppointment ent = new EntityCrAppointmentList();
//        ent.setDeepWhere(false);
//        ent.setId(fkSessionId);
//        EntityManager.select(ent);

        EntityCrPatient entPtnt = getPatientInfoBySessionId(fkSessionId);
        String patientFullname
                = entPtnt.getPatientName() + " " + entPtnt.getPatientMiddleName() + " "
                + entPtnt.getPatientSurname();

        String fkPatientId
                = entPtnt.getId();

        String ln = div()
                .with(
                        div().withClass("form-group col-md-12")
                        .with(
                                formLabel("patientName").attr("qlang", ""),
                                formInput("", patientFullname)
                                .attr("readonly", "readonly")
                                .attr("dont_clear", "")
                        ),
                        div().withClass("form-group col-md-12")
                        .with(
                                formInput("isPrivate", "1")
                                .attr("readonly", "readonly")
                                .attr("dont_clear", "")
                                .withType("hidden")
                                .withValue("1")
                        ),
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel("fkPatientId"),
                                formInput("fkPatientId", "fkPatientId")
                                .withValue(fkPatientId)
                                .attr("dont_clear", "")
                                .withType("hidden")
                        ),
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel("sort_by"),
                                formInput("smOrderNo", "smOrderNo")
                                .withValue(sortByNo)
                                .attr("dont_clear", "")
                                .withType("hidden")
                        ),
                        div().withClass("form-group col-md-12 hidden")
                        .with(
                                formLabel(""),
                                formHiddenInput(fkSessionId)
                                .attr("dont_clear", "")
                        ),
                        each(tagArr, tag
                                -> div().withClass("form-group col-md-6")
                                .attr("style", "min-height: 60px")
                                .with(
                                        label(tag.getHeader()).withClass("float"),
                                        tag.getVal()
                                )
                        )
                ).render();
//        System.out.println("sa body-" + ln);
        return ln;
    }

    private static ArrayList<EntityCrListItem> getListItemBySMValue(String submoduleValue) throws QException {
        ArrayList<EntityCrListItem> list = new ArrayList<>();

        if (submoduleValue.trim().length() == 0) {
            return list;
        }

        Carrier c = new Carrier();
        c.setValue("itemCode", submoduleValue);
        c = CrModel.getListItemByCode(c);
        String tn = CoreLabel.RESULT_SET;
        int rc = c.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            EntityCrListItem ent = new EntityCrListItem();
            EntityManager.mapCarrierToEntity(c, tn, i, ent);
            list.add(ent);
        }
        return list;
    }

    public static Tag getVoiceAnalyseDiv(String submoduleId) {
        //bu hisse dinamik yazilmalidir
        if (submoduleId.equals("201707071617340418")) {
            return div()
                    .withClass("form-group col-md-12")
                    .attr("style", "margin-bottom:20px")
                    .with(
                            label("recording").attr("qlang", ""),
                            a()
                            .withClass("button recordButton")
                            .withId("record")
                            .attr("qlang", "")
                            .withText("record"),
                            a()
                            .withClass("button disabled one")
                            .withId("save")
                            .attr("qlang", "")
                            .withText("stopAndAnalyse"),
                            audio().withId("wavtag")
                            .attr("controls", "")
                            .attr("style", "vertical-align: middle;")
                            .with(
                                    source().withSrc("").withType("audio/mpeg")
                            )
                    );
        } else {
            return div();
        }
    }

    public static Tag formInput(String id, String placeholder) {
        return input()
                .withId(id)
                .withName(id)
                .withClass("form-control apd-form-input")
                .withType("text")
                .withPlaceholder(placeholder);
    }

    public static Tag formHiddenInput(String id) {
        return input()
                .withId("fkSessionId")
                .withName("fkSessionId")
                .withClass("form-control apd-form-input")
                .withType("hidden")
                .withValue(id);
    }

    public static Tag formTextArea(String id, String placeholder) {
        return textarea()
                .withId(id)
                .withName(id)
                .withClass("form-control apd-form-textarea")
                .withType("text")
                .withPlaceholder(placeholder);
    }

    public static Tag formEmail(String id, String placeholder) {
        return input()
                .withId(id)
                .withName(id)
                .withClass("form-control apd-form-email")
                .withType("email")
                .withPlaceholder(placeholder);
    }

    public static Tag formDate(String id, String placeholder) {
        return input()
                .withId(id)
                .withName(id)
                .withClass("form-control apd-form-date")
                .withType("date")
                .withPlaceholder(placeholder);
    }

    public static ContainerTag formSelect(String id, String selectText,
            String selectValue, String url) {
        return select()
                .withId(id)
                .withName(id)
                .attr("select_text", selectText)
                .attr("select_value", selectValue)
                .attr("srv_url", url)
                .withClass("form-control apd-form-select");
    }

    public static Tag formLabel(String name) {
        return label(name).withClass("float");
    }

    public static Tag formDivModalHeader(String header) {
        return div().withClass("modal-header")
                .with(
                        button("x")
                        .withType("button")
                        .withClass("close")
                        .attr("data-dismiss", "modal"),
                        h4(header)
                        .withClass("modal-title")
                );
    }

    public static ContainerTag mainForm(String id, String url) {
        return form()
                .withId(id)
                .withClass("apd-form")
                .withMethod("POST")
                .withAction(url);
    }

    public static Tag divPageBody(String id) {
        return div()
                .withClass("col-md-12 apd-page-body")
                .withId(id);
    }

    public static Tag formButtonInsert(String title) {
        return button(title)
                .withClass("btn btn-default apd-form-btn  apd-form-btn-insert")
                .withType("button")
                .attr("apd-clear-form-data", "true");
    }

    public static Tag formButtonUpdate(String title) {
        return button(title)
                .withClass("btn btn-default apd-form-btn apd-form-btn-update")
                .withType("button")
                .attr("apd-clear-form-data", "false");
    }

    public static Tag formButtonClose() {
        return button("close")
                .withClass("btn btn-default  apd-form-btn-close")
                .withType("button")
                .attr("apd-clear-form-data", "true")
                .attr("data-dismiss", "modal")
                .attr("role", "button");
    }

    public static Tag pageButtonList(String title, String id, String loadTable) {
        return button(title)
                .withClass("btn btn-md task-button apd-task-load apd-task-page-loader")
                .withId(id)
                .attr("onclick", "loadTable('" + loadTable + "')")
                .withType("button");
    }

    public static Tag pageButtonInsert(String title, String id, String pageId) {
        return button(title)
                .withClass("btn btn-md task-button apd-task-create apd-task-form")
                .withId(id)
                .attr("data-toggle", "modal")
                .attr("data-target", "#" + pageId)
                .withType("button");

    }

    public static String getSubmoduleFormBody(String fkSubmoduleId,
            String fkSessionId, Carrier crInspection) throws QException {
        String ln = "";
        boolean isPrivate = isSubmodulePrivate(fkSubmoduleId);

        if (isPrivate) {
            ln = getSubmoduleFormBodyByPrivate(fkSubmoduleId, fkSessionId, crInspection);
        } else {
            ln = getSubmoduleFormBodyByCore(fkSubmoduleId, fkSessionId, crInspection);
        }
        return ln;
    }

    private static boolean isSubmodulePrivate(String fkSubmoduleId) throws QException {
        EntityCrPrivateSubmodule ent = new EntityCrPrivateSubmodule();
        ent.setId(fkSubmoduleId);
        return EntityManager.select(ent).getTableRowCount(ent.toTableName()) > 0;
    }

    private static String getSubmoduleFormBodyByCore(String fkSubmoduleId,
            String fkSessionId, Carrier crInspection) throws QException {
        Carrier cprIns = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkSubmoduleAttributeId", "inspectionValue");
        Carrier cprInsHa = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkSubmoduleAttributeId", "haInspectionValue");

        Carrier carrier = new Carrier();
        carrier.setValue("fkSubmoduleId", fkSubmoduleId);
        carrier.setValue("asc", "sortBy");
        carrier = CrModel.getSubmoduleAttributeList(carrier);
        String tn = CoreLabel.RESULT_SET;

        int rc = carrier.getTableRowCount(tn);
        ArrayList<CsTag> tags = new ArrayList<>();
        for (int i = 0; i < rc; i++) {
            EntityCrSubmoduleAttributeList ent
                    = new EntityCrSubmoduleAttributeList();
            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);
            String insVal = cprIns.getValue(ent.getId()).toString();
            String haInsVal = cprInsHa.getValue(ent.getId()).toString();

            CsTag tag = new CsTag();
            Tag val = null;

            ValueTypeHtml vt = new ValueTypeHtml();
            vt.setHasOtherInspectionValue(haInsVal);
            vt.setInspectionValue(insVal);
            vt.setValue(ent.getSubmoduleValue());
            vt.setSubmoduleAttribute(ent);
            val = vt.getHTML();

            tag.setHeader(ent.getAttributeName());
            tag.setVal(val);
            tags.add(tag);
        }
        String ln = generateSubmoduleFormElementBody(tags, fkSessionId, fkSubmoduleId);
        try {
            ln = QUtility.checkLangLabel(ln);
        } catch (IOException ex) {

        }
        return ln;
    }

    private static String getSubmoduleFormBodyByPrivate(String fkSubmoduleId,
            String fkSessionId, Carrier crInspection) throws QException {
        Carrier cprIns = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkPrivateSubmoduleAttributeId", "inspectionValue");

//        System.out.println("crInspection xml >>>" + crInspection.toXML());
        Carrier cprInsHa = crInspection.getKVFromTable(CoreLabel.RESULT_SET,
                "fkPrivateSubmoduleAttributeId", "haInspectionValue");

        Carrier carrier = new Carrier();
        carrier.setValue("fkPrivateSubmoduleId", fkSubmoduleId);
        carrier.setValue("asc", "sortBy");
        carrier = CrModel.getPrivateAttributeList(carrier);
//        System.out.println("carrier >>>"+carrier.toXML());
        String tn = CoreLabel.RESULT_SET;

        int rc = carrier.getTableRowCount(tn);
        ArrayList<CsTag> tags = new ArrayList<>();
        for (int i = 0; i < rc; i++) {
            EntityCrPrivateAttribute ent = new EntityCrPrivateAttribute();
            EntityManager.mapCarrierToEntity(carrier, tn, i, ent);
            String insVal = cprIns.getValue(ent.getId()).toString();
            String haInsVal = cprInsHa.getValue(ent.getId()).toString();

//            System.out.println("insVal  >>" + insVal);
            CsTag tag = new CsTag();
            Tag val = null;

            ValueTypeHtml vt = new ValueTypeHtml();
            vt.isPrivate(true);
            vt.setHasOtherInspectionValue(haInsVal);
            vt.setInspectionValue(insVal);
            vt.setValue(ent.getValue());
            vt.setPrivateAttribute(ent);
            val = vt.getHTML();

            tag.setHeader(ent.getName());
            tag.setVal(val);
            tags.add(tag);
        }

        EntityCrPrivateSubmodule entSM = new EntityCrPrivateSubmodule();
        entSM.setId(fkSubmoduleId);
        EntityManager.select(entSM);

        String sortByNo = entSM.getOrderNo();

        String ln = generateSubmoduleFormElementBodyByPrivate(tags, fkSessionId, sortByNo);
        try {
            ln = QUtility.checkLangLabel(ln);
        } catch (IOException ex) {

        }
        return ln;
    }
}

class CsTag {

    private String header;
    private Tag val;
    private String id;

    public CsTag() {
        this.header = "";
        this.val = null;
        this.id = "";
    }

    public CsTag(String header, Tag val, String id) {
        this.header = header;
        this.val = val;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Tag getVal() {
        return val;
    }

    public void setVal(Tag val) {
        this.val = val;
    }

}

class CsSubmodule {

    private String sum;
    private String ave;
    private String color;
    private EntityCrSubmodule submodule;
    private EntityCrPrivateSubmodule privateSubmodule;

    public CsSubmodule() {
        this.sum = "";
        this.ave = "";
        this.color = "";
        this.submodule = null;
        this.privateSubmodule = null;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAve() {
        return ave;
    }

    public void setAve(String ave) {
        this.ave = ave;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EntityCrSubmodule getSubmodule() {
        return submodule;
    }

    public void setSubmodule(EntityCrSubmodule submodule) {
        this.submodule = submodule;
    }

    public EntityCrPrivateSubmodule getPrivateSubmodule() {
        return privateSubmodule;
    }

    public void setPrivateSubmodule(EntityCrPrivateSubmodule privateSubmodule) {
        this.privateSubmodule = privateSubmodule;
    }

}
