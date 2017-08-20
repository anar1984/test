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
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import module.cr.CrModel;
import module.cr.entity.EntityCrAppointment;
import module.cr.entity.EntityCrAppointmentList;
import module.cr.entity.EntityCrInspection;
import module.cr.entity.EntityCrInspectionList;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrModule;
import module.cr.entity.EntityCrPatient;
import module.cr.entity.EntityCrSubmodule;
import module.cr.entity.EntityCrSubmoduleAttribute;
import module.cr.entity.EntityCrSubmoduleAttributeList;
import utility.QUtility;
import utility.SessionManager;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.IdGenerator;
import utility.sqlgenerator.QLogger;

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
        String mid = "201704281931560472";
        String sid = "201706110825230248";
        String st = genSubmodule(mid, sid);
        System.out.println(st);
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

        String insCode = "";

        Carrier carrier = new Carrier();
        carrier.setValue("fkModuleId", fkModuleId);
        carrier.setValue("asc", "sortBy");
        carrier = CrModel.getSubmoduleList(carrier);

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

        String ln = getSubmoduleHtml(entArr, insCode);
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
                                        attr("style", "background-color:" + ent.getColor() + ";border-color:" + ent.getColor()).
                                        attr("ins_id", insCode).
                                        attr("sort_by", ent.getSubmodule().getSortBy()).
                                        withClass("btn apd-subm-attr-button btn-primary btn-md")),
                                hr()
                        )
                ).render();
        return ln;
    }

    public static String getSubmoduleFormBody(String fkSubmoduleId,
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
            if (ent.getFkValueTypeId().equals(VALUE_TYPE_STRING)) {
                val = generateStringAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_RANGE_STRING)) {
                val = generateRangeStringtAttributeVal(ent, insVal, haInsVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_RANGE_STRING_MULTI)) {
                val = generateRangeStringtMultiAttributeVal(ent, insVal, haInsVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_RANGE_INTEGER_MULTI)) {
                val = generateRangeIntegerMultiAttributeVal(ent, insVal, haInsVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_FLOAT)) {
                val = generateFloatAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_INTEGER)) {
                val = generateIntegerAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_MIN_MAX_FLOAT)) {
                val = generateMinMaxFloatAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_MIN_MAX_INTEGER)) {
                val = generateMinMaxIntegerAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_POSITIVE_FLOAT)) {
                val = generatePosiviteFloatAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_POSITIVE_INTEGER)) {
                val = generatePosiviteIntegerAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_RANGE_INTEGER)) {
                val = generateRangeNumbertAttributeVal(ent, insVal, haInsVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_TEXTAREA)) {
                val = generateTextareaAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_PICTURE)) {
                val = generatePictureAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_VIDEO_UPLOAD)) {
                val = generateVideoAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_VIDEO_URL)) {
                val = generateVideoUrlAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_PICTURE_URL)) {
                val = generatePictureUrlAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_YOUTUBE_URL)) {
                val = generateYoutubeUrlAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_SOUND_UPLOAD)) {
                val = generateSoundUploadAttributeVal(ent, insVal);
            } else if (ent.getFkValueTypeId().equals(VALUE_TYPE_DATE)) {
                val = generateDateAttributeVal(ent, insVal);
            }

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

    private static Tag generatePictureAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        String ds = insVal.length() == 0 ? "font-size:14px;color:#00b289;display: none"
                : "font-size:14px;color:#00b289;display: block";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input-file").
                                withValue(insVal).
                                withType("file").
                                attr("file_type", "image")
                        ),
                        div().withClass("col-md-1")
                        .with(
                                img().attr("style", "display: none; ")
                                .withClass("  apd-image-spinner")
                                .attr("src", "resources/upload/spinner.gif")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                img()
                                .attr("style", "display: none; ")
                                .withClass("  apd-image-uploaded")
                                .attr("src", "resources/upload/uploaded.png")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                i()
                                .attr("style", "font-size:22px;color:red;display: none; ")
                                .withClass("fa fa-warning apd-image-upload-error"),
                                i()
                                .withClass("apd-image-trigger fa  fa-picture-o  ")
                                .attr("style", ds)
                                .attr("apd_image_url", "")
                                .attr("apd_image_alt", "insValue")
                                .attr("data-toggle", "modal")
                                .attr("data-target", "#apdImageViewer")
                                .attr("aria-hidden", "true")
                                .attr("v_id", "sa_" + ent.getId())
                        )
                );

        return ln;
    }

    private static Tag generateVideoAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        String ds = insVal.length() == 0 ? "font-size:14px;color:#00b289;display: none"
                : "font-size:14px;color:#00b289;display: block";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input-file").
                                withType("file").
                                withValue(insVal).
                                attr("file_type", "video")
                        ),
                        div().withClass("col-md-1")
                        .with(
                                img().attr("style", "display: none; ")
                                .withClass("  apd-image-spinner")
                                .attr("src", "resources/upload/spinner.gif")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                img()
                                .attr("style", "display: none; ")
                                .withClass("  apd-image-uploaded")
                                .attr("src", "resources/upload/uploaded.png")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                i()
                                .attr("style", "font-size:22px;color:red;display: none; ")
                                .withClass("fa fa-warning apd-image-upload-error"),
                                i()
                                .withClass("apd-video-trigger fa fa-video-camera  ")
                                .attr("style", ds)
                                .attr("apd_video_url", "")
                                .attr("data-toggle", "modal")
                                .attr("data-target", "#apdVideoPlayer")
                                .attr("aria-hidden", "true")
                                .attr("v_id", "sa_" + ent.getId())
                        )
                );

        return ln;
    }

    private static Tag generateSoundUploadAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        String ds = insVal.length() == 0 ? "display:none;width:100px;"
                : "display:block;width:100px;";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input-file").
                                withValue(insVal).
                                withType("file").
                                attr("file_type", "audio")
                        ),
                        div().withClass("col-md-1")
                        .with(
                                img().attr("style", "display: none; ")
                                .withClass("  apd-image-spinner")
                                .attr("src", "resources/upload/spinner.gif")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                img()
                                .attr("style", "display: none; ")
                                .withClass("  apd-image-uploaded")
                                .attr("src", "resources/upload/uploaded.png")
                                .attr("height", "20px")
                                .attr("width", "20px"),
                                i()
                                .attr("style", "font-size:22px;color:red;display: none;")
                                .withClass("fa fa-warning apd-image-upload-error")
                        ),
                        div().withClass("col-md-3")
                        .with(
                                audio()
                                .attr("controls", "")
                                .attr("style", ds)
                                .attr("preload", "auto")
                                .withClass("apd-input-audio")
                                .withId("soundInput")
                                .attr("s_id", PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                                .with(
                                        source()
                                        .attr("src", "resources/upload/" + insVal)
                                        .attr("type", "audio/mpeg")
                                )
                        )
                );

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

    private static Tag generateFloatAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withValue(insVal).
                withType("number").
                attr("step", "0.00000001").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generatePosiviteIntegerAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withType("number").
                withValue(insVal).
                attr("step", "1").
                attr("min", "0").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generatePosiviteFloatAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                attr("step", "0.00000001").
                attr("min", "0").
                withValue(insVal).
                withType("number").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateIntegerAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withType("number").
                withValue(insVal).
                attr("step", "1").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateDateAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withType("date").
                withValue(insVal).
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateMinMaxIntegerAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        String min = ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[0];
        String max = ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[1];

        Tag ln = div()
                .with(
                        div()
                        .withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input").
                                withType("number").
                                attr("step", "1").
                                attr("min", min).
                                withValue(insVal).
                                attr("max", max).
                                withPlaceholder(ent.getAttributeName())
                        ),
                        div()
                        .withClass("col-md-4")
                        .with(
                                span("Min" + "=" + min + ", " + "Max" + "=" + max))
                );
        return ln;
    }

    private static Tag generateMinMaxFloatAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        String min = ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[0];
        String max = ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[1];

        Tag ln = div()
                .with(
                        div()
                        .withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input").
                                withType("number").
                                attr("step", "0.00000001").
                                attr("min", min).
                                attr("max", max).
                                withValue(insVal).
                                withPlaceholder(ent.getAttributeName())
                        ),
                        div()
                        .withClass("col-md-4")
                        .with(
                                span("Min" + "=" + min + ", " + "Max" + "=" + max))
                );
        return ln;
    }

    private static Tag generateTextareaAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = textarea().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-textarea").
                withText(insVal).
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateStringAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withValue(insVal).
                withType("text").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateVideoUrlAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withValue(insVal).
                withType("url").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generatePictureUrlAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                withClass("form-control apd-form-input").
                withValue(insVal).
                withType("url").
                withPlaceholder(ent.getAttributeName());
        return ln;
    }

    private static Tag generateYoutubeUrlAttributeVal(EntityCrSubmoduleAttributeList ent, String insVal) {
        Tag ln = div()
                .withClass("apd-div-youtube")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId()).
                                withClass("form-control apd-form-input").
                                withType("url").
                                withValue(insVal).
                                withPlaceholder(ent.getAttributeName())),
                        div().withClass("col-md-1")
                        .with(
                                i()
                                .attr("style", "font-size:22px;color:red;  ")
                                .withClass("fa fa-youtube apd-youtube-player")
                                .attr("v_id", "sa_" + ent.getId())
                        )
                );
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

    private static Tag generateRangeStringtAttributeVal(EntityCrSubmoduleAttributeList ent,
            String insVal, String haInsVal)
            throws QException {
        ArrayList<EntityCrListItem> list = getListItemBySMValue(ent.getSubmoduleValue());

        String selectCls = ent.getHasOther().equals("yes")
                ? "form-control apd-form-select apd-form-select-ho selectpicker"
                : "form-control apd-form-select selectpicker";
        String hasOtherVl = ent.getHasOther().equals("yes") ? "1" : "0";

        Tag nw = ent.getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input ")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withValue(haInsVal)
                .withType("text")
                .attr("style", haInsVal.length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("srv_url", "li/" + ent.getSubmoduleValue())
                        .attr("has_other", hasOtherVl)
                        .attr("has_null", "true")
                        .withClass(selectCls)
                        .with(
                                option("----").withValue(""),
                                each(list, l
                                        -> option(l.getItemValue())
                                        .withValue(l.getItemKey())
                                        .attr(l.getItemKey().equals(insVal) ? "selected" : "", "")
                                ),
                                ent.getHasOther().equals("yes")
                                ? option(CrModel.getLabel("other")).withValue("__2__")
                                .attr(insVal.equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        ent.getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private static Tag generateRangeStringtAttributeVal_old(EntityCrSubmoduleAttributeList ent) {
        String selectCls = ent.getHasOther().equals("yes")
                ? "form-control apd-form-select apd-form-select-ho"
                : "form-control apd-form-select";
        String hasOtherVl = ent.getHasOther().equals("yes") ? "1" : "0";

        Tag nw = ent.getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withType("text")
                .attr("style", "margin-top:4px;display:none;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("srv_url", "li/" + ent.getSubmoduleValue())
                        .attr("has_other", hasOtherVl)
                        .attr("has_null", "true")
                        .withClass(selectCls),
                        ent.getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private static Tag generateRangeStringtMultiAttributeVal(EntityCrSubmoduleAttributeList ent,
            String insVal, String haInsVal) throws QException {
        ArrayList<EntityCrListItem> list = getListItemBySMValue(ent.getSubmoduleValue());
        String[] insValArr = insVal.split(CoreLabel.SEPERATOR_VERTICAL_LINE);

        String selectCls = ent.getHasOther().equals("yes")
                ? "form-control apd-form-multiselect selectpicker apd-form-select-ho"
                : "form-control apd-form-multiselect selectpicker";
        String hasOtherVl = ent.getHasOther().equals("yes") ? "1" : "0";

        Tag nw = ent.getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withValue(haInsVal)
                .withType("text")
                .attr("style", haInsVal.length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("multiple", "multiple")
                        .attr("srv_url", "li/" + ent.getSubmoduleValue())
                        .attr("has_other", hasOtherVl)
                        .withClass(selectCls)
                        .with(
                                each(list, l
                                        -> option(l.getItemValue())
                                        .withValue(l.getItemKey())
                                        .attr(Arrays.asList(insValArr).contains(l.getItemKey()) ? "selected" : "", "")
                                ),
                                ent.getHasOther().equals("yes")
                                ? option(CrModel.getLabel("other")).withValue("__2__")
                                .attr(insVal.equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        ent.getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private static Tag generateRangeIntegerMultiAttributeVal(EntityCrSubmoduleAttributeList ent,
            String insVal, String haInsVal) throws QException {
        List<String> myList = new ArrayList<>(
                Arrays.asList(ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)));
        String[] insValArr = insVal.split(CoreLabel.SEPERATOR_VERTICAL_LINE);

        String selectCls = ent.getHasOther().equals("yes")
                ? "form-control apd-form-multiselect-manual selectpicker apd-form-select-ho"
                : "form-control apd-form-multiselect-manual selectpicker";
        String hasOtherVl = ent.getHasOther().equals("yes") ? "1" : "0";

        Tag nw = ent.getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withValue(haInsVal)
                .withType("text")
                .attr("style", haInsVal.length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withClass(selectCls)
                        .attr("has_other", hasOtherVl)
                        .attr("multiple", "multiple")
                        .with(
                                each(myList, l
                                        -> option(l).withValue(l)
                                        .attr(Arrays.asList(insValArr).contains(l) ? "selected" : "", "")
                                ),
                                ent.getHasOther().equals("yes")
                                ? option(CrModel.getLabel("other")).withValue("__2__")
                                .attr(insVal.equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        ent.getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private static Tag generateRangeNumbertAttributeVal(EntityCrSubmoduleAttributeList ent,
            String insVal, String haInsVal) throws QException {
        List<String> myList = new ArrayList<>(
                Arrays.asList(ent.getSubmoduleValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)));

        String selectCls = ent.getHasOther().equals("yes")
                ? "form-control apd-form-select-manual selectpicker apd-form-select-ho"
                : "form-control apd-form-select-manual selectpicker ";
        String hasOtherVl = ent.getHasOther().equals("yes") ? "1" : "0";

        Tag nw = ent.getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + ent.getId())
                .withValue(haInsVal)
                .withType("text")
                .attr("style", haInsVal.length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + ent.getId())
                        .withClass(selectCls)
                        .attr("has_other", hasOtherVl)
                        .with(
                                option("----").withValue(""),
                                each(myList, l
                                        -> option(l).withValue(l)
                                        .attr(l.equals(insVal) ? "selected" : "", "")
                                ),
                                ent.getHasOther().equals("yes")
                                ? option(CrModel.getLabel("other")).withValue("__2__")
                                .attr(insVal.equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        ent.getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    public static String genPage(String page) throws QException {
        if (page.trim().length() == 0) {
            return "";
        }

        if (page.trim().equals("patient")) {
            return pagePatient();
        } else if (page.trim().equals("patienthistory")) {
            return pagePatientHistory();
        }

        return "";
    }

    public static String pagePatient() {
        String ln
                = div()
                .withId("page_dn_patient")
                .withClass("row apd-page")
                .with(
                        div().withClass("col-md-12")
                        .with(
                                div().withClass("row text-center module-heading")
                                .with(
                                        label("Patient"))
                        ),
                        div().withClass("col-md-12 apd-page-btn")
                        .with(
                                div().withClass("col-md-6  text-left ")
                                .with(
                                        pageButtonList("patientList", "serviceCrGetPatientList", "tbl_patient_list"),
                                        span(" "),
                                        pageButtonList("inpectionList", "serviceCrGetInspectionList", "tbl_inspection_list"),
                                        span(" "),
                                        pageButtonInsert("newPatient", "btn_serviceCrInsertNewPatient", "insertNewPatient")),
                                div().withClass("col-md-2   text-right")
                                .with(formSelect("fkModuleId",
                                        "moduleName",
                                        "id", "srv/serviceCrGetModuleList").
                                        withClass("form-control apd-form-select apd-module-cmb-list")),
                                div().withClass("col-md-2   text-right")
                                .with(formSelect("inspectionCode",
                                        "inspectionDate,inspectionTime",
                                        "inspectionCode", "srv/serviceCrGetInspectionList").
                                        withClass("form-control apd-form-select apd-inspection-cmb-list").
                                        attr("apd-table-data-change", "\"Response\"")),
                                div().withClass("col-md-2   text-right")
                                .with(formSelect("fkReportId",
                                        "reportName",
                                        "id", "srv/serviceCrGetReportLineList").
                                        withClass("form-control apd-form-select apd-report-cmb-list").
                                        attr("has_null", "true").
                                        attr("disabled", "disabled"))
                        ),
                        div()
                        .withClass("col-md-12 ")
                        .with(
                                hr()
                        ),
                        div()
                        .withClass("col-md-12 apd-submodule-button-list")
                        .withId("apd-submodule-button-list-id"),
                        divPageBody("patientlist"),
                        div()
                        .withId("insertNewPatient")
                        .withClass("modal fade")
                        .attr("apd-form-reload-button-id", "serviceCrGetPatientList")
                        .attr("role", "dialog")
                        .with(
                                div().withClass("modal-dialog")
                                .with(
                                        div().withClass("modal-content")
                                        .with(
                                                formDivModalHeader("Insert New Patient"),
                                                div().withClass("modal-body-1")
                                                .with(
                                                        mainForm("form_insertNewPatient", "srv/serviceCrInsertNewPatient")
                                                        .with(
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Name"),
                                                                        formInput("patientName", "Name")
                                                                ),
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Surname"),
                                                                        formInput("patientSurname", "Surname")
                                                                ),
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Middle Name"),
                                                                        formInput("patientMiddleName", "Middle Name")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Birth Place"),
                                                                        formInput("patientBirthPlace", "Birth Place")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Birth Date"),
                                                                        formDate("patientBirthDate", "Birth Date")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Maritual Status"),
                                                                        formSelect("maritualStatus", "itemValue", "itemKey", "li/maritualStatus")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Occupation"),
                                                                        formSelect("occupation", "itemValue", "itemKey", "li/occupation")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("education"),
                                                                        formSelect("education", "itemValue", "itemKey", "li/educationType")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Sex"),
                                                                        formSelect("sex", "itemValue", "itemKey", "li/sex")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("bloodGroup"),
                                                                        formSelect("bloodGroup", "itemValue", "itemKey", "li/bloodGroup")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("rhFactor"),
                                                                        formSelect("rhFactor", "itemValue", "itemKey", "li/rhFactor")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("mobile1"),
                                                                        formInput("mobile1", "mobile1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("mobile2"),
                                                                        formInput("mobile2", "mobile2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("telephone1"),
                                                                        formInput("telephone1", "telephone1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("telephone2"),
                                                                        formInput("telephone2", "telephone2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("email1"),
                                                                        formEmail("email1", "email1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("email2"),
                                                                        formEmail("email2", "email2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("country"),
                                                                        formInput("country", "country")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("city"),
                                                                        formInput("city", "city")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("postIndex"),
                                                                        formInput("postIndex", "postIndex")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("isActive"),
                                                                        formSelect("isActive", "itemValue", "itemKey", "li/pa")
                                                                ),
                                                                div().withClass("form-group col-md-12")
                                                                .with(
                                                                        formLabel("desc"),
                                                                        formTextArea("description", "desc")
                                                                ),
                                                                div().withClass("form-group col-md-12").attr("align", "right")
                                                                .with(
                                                                        formButtonInsert("Insert"),
                                                                        formButtonClose()
                                                                )
                                                        )
                                                ),
                                                div().withClass("modal-footer")
                                        )
                                )
                        ),
                        div()
                        .withId("updatePatient")
                        .withClass("modal fade")
                        .attr("apd-form-reload-button-id", "serviceCrGetPatientList")
                        .attr("role", "dialog")
                        .with(
                                div().withClass("modal-dialog")
                                .with(
                                        div().withClass("modal-content")
                                        .with(
                                                formDivModalHeader("Update Patient"),
                                                div().withClass("modal-body-1")
                                                .with(
                                                        mainForm("form_updatePatient", "srv/serviceCrUpdatePatient")
                                                        .with(
                                                                div().withClass("form-group col-md-6 hidden")
                                                                .with(
                                                                        formLabel("id"),
                                                                        formInput("id", "id")
                                                                ),
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Name"),
                                                                        formInput("patientName", "Name")
                                                                ),
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Surname"),
                                                                        formInput("patientSurname", "Surname")
                                                                ),
                                                                div().withClass("form-group col-md-4")
                                                                .with(
                                                                        formLabel("Middle Name"),
                                                                        formInput("patientMiddleName", "Middle Name")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Birth Place"),
                                                                        formInput("patientBirthPlace", "Birth Place")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Birth Date"),
                                                                        formDate("patientBirthDate", "Birth Date")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Maritual Status"),
                                                                        formSelect("maritualStatus", "itemValue", "itemKey", "li/maritualStatus")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Occupation"),
                                                                        formSelect("occupation", "itemValue", "itemKey", "li/occupation")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("education"),
                                                                        formSelect("education", "itemValue", "itemKey", "li/educationType")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("Sex"),
                                                                        formSelect("sex", "itemValue", "itemKey", "li/sex")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("bloodGroup"),
                                                                        formSelect("bloodGroup", "itemValue", "itemKey", "li/bloodGroup")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("rhFactor"),
                                                                        formSelect("rhFactor", "itemValue", "itemKey", "li/rhFactor")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("mobile1"),
                                                                        formInput("mobile1", "mobile1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("mobile2"),
                                                                        formInput("mobile2", "mobile2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("telephone1"),
                                                                        formInput("telephone1", "telephone1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("telephone2"),
                                                                        formInput("telephone2", "telephone2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("email1"),
                                                                        formEmail("email1", "email1")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("email2"),
                                                                        formEmail("email2", "email2")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("country"),
                                                                        formInput("country", "country")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("city"),
                                                                        formInput("city", "city")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("postIndex"),
                                                                        formInput("postIndex", "postIndex")
                                                                ),
                                                                div().withClass("form-group col-md-6")
                                                                .with(
                                                                        formLabel("isActive"),
                                                                        formSelect("isActive", "itemValue", "itemKey", "li/pa")
                                                                ),
                                                                div().withClass("form-group col-md-12")
                                                                .with(
                                                                        formLabel("desc"),
                                                                        formTextArea("description", "desc")
                                                                ),
                                                                div().withClass("form-group col-md-12").attr("align", "right")
                                                                .with(
                                                                        formButtonUpdate("Update"),
                                                                        formButtonClose()
                                                                )
                                                        )
                                                ),
                                                div().withClass("modal-footer")
                                        )
                                )
                        )
                ).render();

        return ln;
    }

    public static String pagePatientHistory() {
        String ln
                = div()
                .withId("page_dn_patienthistory")
                .withClass("row apd-page")
                .with(
                        div().withClass("col-md-12")
                        .with(
                                div().withClass("row text-center module-heading")
                                .with(
                                        label("Patient History"))
                        ),
                        div().withClass("col-md-12 apd-page-btn")
                        .with(
                                pageButtonList("List", "serviceCrGetInspectionList", "tbl_inspection_list"),
                                span(" "),
                                hr()
                        ),
                        divPageBody("inspectionlist"),
                        div()
                        .withId("updateInspection")
                        .withClass("modal fade")
                        .attr("apd-form-reload-button-id", "serviceCrGetInspectionList")
                        .attr("role", "dialog")
                        .with(
                                div().withClass("modal-dialog")
                                .with(
                                        div().withClass("modal-content")
                                        .with(
                                                formDivModalHeader("Update Inspection"),
                                                div().withClass("modal-body-1")
                                                .with(
                                                        mainForm("form_updateInspection", "srv/serviceCrUpdateInspection")
                                                        .with(
                                                                div().withClass("form-group col-md-6 hidden")
                                                                .with(
                                                                        formLabel("id"),
                                                                        formInput("id", "id")
                                                                ),
                                                                div().withClass("form-group col-md-12").attr("align", "right")
                                                                .with(
                                                                        formButtonUpdate("Update"),
                                                                        formButtonClose()
                                                                )
                                                        )
                                                ),
                                                div().withClass("modal-footer")
                                        )
                                )
                        )
                ).render();

        return ln;
    }

    //<audio id="wavtag" controls style="vertical-align: middle;">
    //      <source src="resources/upload/file_2CC9C60622A5BD3.wav" type="audio/mpeg">
    //       Your browser does not support the audio element.
    //   </audio>
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

    public CsSubmodule() {
        this.sum = "";
        this.ave = "";
        this.color = "";
        this.submodule = null;
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

}

//                        ,
//                        div()
//                        .attr("id", "popup1")
//                        .attr("role", "dialog")
//                        .withClass("modal fade")
//                        .attr("apd-form-reload-button-id", "serviceCrGetInspectionList")
//                        .with(
//                                div().withClass("modal-dialog").
//                                with(
//                                        div().withClass("modal-content").
//                                        with(
//                                                div().withClass("modal-header")
//                                                .with(
//                                                        button("x").withClass("close").attr("type", "button").
//                                                        attr("data-dismiss", "modal"),
//                                                        h4("Submodule name")
//                                                        .withClass("modal-insert-title-name")
//                                                        .withId("modal-insert-title-name")
//                                                ),
//                                                div().withClass("modal-body-1").
//                                                with(
//                                                        form()
//                                                        .withId("form_insertNewInspection")
//                                                        .withMethod("POST")
//                                                        .withClass("apd-form")
//                                                        .withAction("srv/serticeCrInsertNewInspection")
//                                                        .with(
//                                                                div().withClass("form-insert-element-body").withId("form-insert-element-body"),
//                                                                div().withClass("form-group col-md-12")
//                                                                .attr("align", "right")
//                                                                .with(
//                                                                        button("Insert").withType("button")
//                                                                        .withClass("btn btn-default apd-form-btn apd-form-attr-btn")
//                                                                        .attr("apd-clear-form-data", "true"),
//                                                                        button("Close").withType("button")
//                                                                        .withClass("btn btn-default  ")
//                                                                        .attr("data-dismiss", "modal")
//                                                                        .attr("role", "button")
//                                                                )
//                                                        )
//                                                ),
//                                                div().withClass("modal-footer")
//                                        )
//                                )
//                        ),
//                        div()
//                        .attr("id", "popup2")
//                        .attr("role", "dialog")
//                        .withClass("modal fade")
//                        .attr("apd-form-reload-button-id", "serviceCrGetInspectionList")
//                        .with(
//                                div().withClass("modal-dialog").
//                                with(
//                                        div().withClass("modal-content").
//                                        with(
//                                                div().withClass("modal-header")
//                                                .with(
//                                                        button("x").withClass("close").attr("type", "button").
//                                                        attr("data-dismiss", "modal"),
//                                                        h4("Submodule name")
//                                                        .withClass("modal-update-title-name")
//                                                        .withId("modal-update-title-name")
//                                                ),
//                                                div().withClass("modal-body-1").
//                                                with(
//                                                        form()
//                                                        .withId("form_updateInspection")
//                                                        .withMethod("POST")
//                                                        .withClass("apd-form")
//                                                        .withAction("srv/serticeCrUpdateInspection")
//                                                        .with(
//                                                                div().withClass("form-group col-md-6 hidden")
//                                                                .with(
//                                                                        label("id"),
//                                                                        input()
//                                                                        .withClass("form-control apd-form-input")
//                                                                        .withType("hidden")
//                                                                        .withId("inspectionCode")
//                                                                        .withName("inspectionCode")
//                                                                ),
//                                                                div().withClass("form-update-element-body").withId("form-update-element-body"),
//                                                                div().withClass("form-group col-md-12")
//                                                                .attr("align", "right")
//                                                                .with(
//                                                                        formButtonUpdate("update"),
//                                                                        formButtonClose()
//                                                                )
//                                                        )
//                                                ),
//                                                div().withClass("modal-footer")
//                                        )
//                                )
//                        )
