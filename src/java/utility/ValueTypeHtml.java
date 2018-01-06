/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static j2html.TagCreator.a;
import static j2html.TagCreator.audio;
import static j2html.TagCreator.br;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h4;
import static j2html.TagCreator.i;
import static j2html.TagCreator.img;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.option;
import static j2html.TagCreator.select;
import static j2html.TagCreator.source;
import static j2html.TagCreator.span;
import static j2html.TagCreator.textarea;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import label.CoreLabel;
import module.cr.CrModel;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrPrivateAttribute;
import module.cr.entity.EntityCrSubmoduleAttribute;
import module.cr.entity.EntityCrSubmoduleAttributeList;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author Lenovo
 */
public class ValueTypeHtml {

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
    public static final String VALUE_TYPE_RANGE_STRING_MULTI_MANUAL = "21";
    public static final String VALUE_TYPE_RANGE_STRING_MANUAL = "22";

    private static final String PREFIX_SUBMODULE_ATTRIBUTE = "sa_";
    private static final String PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER = "ha_";

    private EntityCrPrivateAttribute privateAttribute;
    private EntityCrSubmoduleAttributeList submoduleAttribute;
    private String value;
    private String inspectionValue;
    private String hasOtherInspectionValue;
    private boolean isPrivate;

    public ValueTypeHtml() {
        this.privateAttribute = new EntityCrPrivateAttribute();
        this.submoduleAttribute = new EntityCrSubmoduleAttributeList();
        this.value = "";
        this.inspectionValue = "";
        this.hasOtherInspectionValue = "";
        this.isPrivate = false;
    }

    public void isPrivate(boolean arg) {
        this.isPrivate = arg;
    }

    public boolean getIsPrivate() {
        return this.isPrivate;
    }

    public EntityCrPrivateAttribute getPrivateAttribute() {
        return privateAttribute;
    }

    public void setPrivateAttribute(EntityCrPrivateAttribute privateAttribute) {
        this.privateAttribute = privateAttribute;
    }

    public EntityCrSubmoduleAttributeList getSubmoduleAttribute() {
        return submoduleAttribute;
    }

    public void setSubmoduleAttribute(EntityCrSubmoduleAttributeList submoduleAttribute) {
        this.submoduleAttribute = submoduleAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInspectionValue() {
        return inspectionValue;
    }

    public void setInspectionValue(String inspectionValue) {
        this.inspectionValue = inspectionValue;
    }

    public String getHasOtherInspectionValue() {
        return hasOtherInspectionValue;
    }

    public void setHasOtherInspectionValue(String hasOtherInspectionValue) {
        this.hasOtherInspectionValue = hasOtherInspectionValue;
    }

    public Tag getHTML() throws QException {
        Tag val = null;
        String type = getAttributeType();
//        System.out.println("type =" + type);
        switch (type) {
            case VALUE_TYPE_STRING:
                val = generateStringAttributeVal();
                break;
            case VALUE_TYPE_RANGE_STRING:
                val = generateRangeStringtAttributeVal();
                break;
            case VALUE_TYPE_RANGE_STRING_MULTI:
                val = generateRangeStringtMultiAttributeVal();
                break;
            case VALUE_TYPE_RANGE_INTEGER_MULTI:
                val = generateRangeIntegerMultiAttributeVal();
                break;
            case VALUE_TYPE_FLOAT:
                val = generateFloatAttributeVal();
                break;
            case VALUE_TYPE_INTEGER:
                val = generateIntegerAttributeVal();
                break;
            case VALUE_TYPE_MIN_MAX_FLOAT:
                val = generateMinMaxFloatAttributeVal();
                break;
            case VALUE_TYPE_MIN_MAX_INTEGER:
                val = generateMinMaxIntegerAttributeVal();
                break;
            case VALUE_TYPE_POSITIVE_FLOAT:
                val = generatePosiviteFloatAttributeVal();
                break;
            case VALUE_TYPE_POSITIVE_INTEGER:
                val = generatePosiviteIntegerAttributeVal();
                break;
            case VALUE_TYPE_RANGE_INTEGER:
                val = generateRangeNumbertAttributeVal();
                break;
            case VALUE_TYPE_TEXTAREA:
                val = generateTextareaAttributeVal();
                break;
            case VALUE_TYPE_PICTURE:
                val = generatePictureAttributeVal();
                break;
            case VALUE_TYPE_VIDEO_UPLOAD:
                val = generateVideoAttributeVal();
                break;
            case VALUE_TYPE_VIDEO_URL:
                val = generateVideoUrlAttributeVal();
                break;
            case VALUE_TYPE_PICTURE_URL:
                val = generatePictureUrlAttributeVal();
                break;
            case VALUE_TYPE_YOUTUBE_URL:
                val = generateYoutubeUrlAttributeVal();
                break;
            case VALUE_TYPE_SOUND_UPLOAD:
                val = generateSoundUploadAttributeVal();
                break;
            case VALUE_TYPE_DATE:
                val = generateDateAttributeVal();
                break;
            case VALUE_TYPE_RANGE_STRING_MANUAL:
                val = generateRangeNumbertAttributeVal();
                break;
            case VALUE_TYPE_RANGE_STRING_MULTI_MANUAL:
                val = generateRangeIntegerMultiAttributeVal();
                break;
            default:
                break;
        }
        return val;
    }

    private static ArrayList<EntityCrListItem> getListItemBySMValue(String submoduleValue) throws QException {
        ArrayList<EntityCrListItem> list = new ArrayList<>();

        if (submoduleValue.trim().length() == 0) {
            return list;
        }

        Carrier c = new Carrier();
        c.setValue("itemCode", submoduleValue);
//        c = CrModel.getListItemByCode(c);
        c = CrModel.getListItemByCodeFromCache(c);
        String tn = CoreLabel.RESULT_SET;
        int rc = c.getTableRowCount(tn);
        for (int i = 0; i < rc; i++) {
            EntityCrListItem ent = new EntityCrListItem();
            EntityManager.mapCarrierToEntity(c, tn, i, ent);
            list.add(ent);
        }
        return list;
    }

    private Tag generateRangeStringtAttributeVal()
            throws QException {
        ArrayList<EntityCrListItem> list = getListItemBySMValue(getValue());

        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-select apd-form-select-ho selectpicker"
                : "form-control apd-form-select selectpicker";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input ")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withValue(getHasOtherInspectionValue())
                .withType("text")
                .attr("style", getHasOtherInspectionValue().length() == 0
                        ? "margin-top:4px;display:none;"
                        : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("srv_url", "li/" + getValue())
                        .attr("has_other", hasOtherVl)
                        .attr("has_null", "true")
                        .withClass(selectCls)
                        .with(
                                option("----").withValue(""),
                                each(list, l
                                        -> option(l.getItemValue())
                                        .withValue(l.getItemKey())
                                        .attr(l.getItemKey().equals(getInspectionValue()) ? "selected" : "", "")
                                ),
                                getHasOther().equals("yes")
                                ? option(QUtility.getLabel("other")).withValue("__2__")
                                .attr(getInspectionValue().equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generateRangeStringtAttributeVal_old(EntityCrSubmoduleAttributeList ent) {
        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-select apd-form-select-ho"
                : "form-control apd-form-select";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withType("text")
                .attr("style", "margin-top:4px;display:none;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("srv_url", "li/" + getValue())
                        .attr("has_other", hasOtherVl)
                        .attr("has_null", "true")
                        .withClass(selectCls),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generateRangeStringtMultiAttributeVal() throws QException {
        ArrayList<EntityCrListItem> list = getListItemBySMValue(getValue());
        String[] haInsArr = getInspectionValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE);

        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-multiselect selectpicker apd-form-select-ho"
                : "form-control apd-form-multiselect selectpicker";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withValue(getHasOtherInspectionValue())
                .withType("text")
                .attr("style", getHasOtherInspectionValue().length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .attr("select_text", "itemValue")
                        .attr("select_value", "itemKey")
                        .attr("multiple", "multiple")
                        .attr("srv_url", "li/" + getValue())
                        .attr("has_other", hasOtherVl)
                        .withClass(selectCls)
                        .with(
                                each(list, l
                                        -> option(l.getItemValue())
                                        .withValue(l.getItemKey())
                                        .attr(Arrays.asList(haInsArr).contains(l.getItemKey()) ? "selected" : "", "")
                                ),
                                getHasOther().equals("yes")
                                ? option(QUtility.getLabel("other")).withValue("__2__")
                                .attr(getInspectionValue().equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generateRangeStringMultiManualAttributeVal() throws QException {
        List<String> myList = new ArrayList<>(
                Arrays.asList(getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)));
        String[] insArr = getInspectionValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE);

        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-multiselect-manual selectpicker apd-form-select-ho"
                : "form-control apd-form-multiselect-manual selectpicker";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withValue(getHasOtherInspectionValue())
                .withType("text")
                .attr("style", getHasOtherInspectionValue().length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withClass(selectCls)
                        .attr("has_other", hasOtherVl)
                        .attr("multiple", "multiple")
                        .with(
                                each(myList, l
                                        -> option(l).withValue(l)
                                        .attr(Arrays.asList(insArr).contains(l) ? "selected" : "", "")
                                ),
                                getHasOther().equals("yes")
                                ? option(QUtility.getLabel("other")).withValue("__2__")
                                .attr(getInspectionValue().equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generateRangeIntegerMultiAttributeVal() throws QException {
        List<String> myList = new ArrayList<>(
                Arrays.asList(getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)));
        String[] insArr = getInspectionValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE);

        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-multiselect-manual selectpicker apd-form-select-ho"
                : "form-control apd-form-multiselect-manual selectpicker";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withValue(getHasOtherInspectionValue())
                .withType("text")
                .attr("style", getHasOtherInspectionValue().length() == 0
                        ? "margin-top:4px;display:none;"
                        : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withClass(selectCls)
                        .attr("has_other", hasOtherVl)
                        .attr("multiple", "multiple")
                        .with(
                                each(myList, l
                                        -> option(l).withValue(l)
                                        .attr(Arrays.asList(insArr).contains(l)
                                                ? "selected" : "", "")
                                ),
                                getHasOther().equals("yes")
                                ? option(QUtility.getLabel("other")).withValue("__2__")
                                .attr(getInspectionValue().equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generateFloatAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withValue(getInspectionValue()).
                withType("number").
                attr("step", "0.00000001").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generatePosiviteIntegerAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withType("number").
                withValue(getInspectionValue()).
                attr("step", "1").
                attr("min", "0").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generatePosiviteFloatAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                attr("step", "0.00000001").
                attr("min", "0").
                withValue(getInspectionValue()).
                withType("number").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateIntegerAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withType("number").
                withValue(getInspectionValue()).
                attr("step", "1").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateDateAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withType("date").
                withValue(getInspectionValue()).
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateMinMaxIntegerAttributeVal() {
        String min = getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[0];
        String max = getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[1];

        Tag ln = div()
                .with(
                        div()
                        .withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input").
                                withType("number").
                                attr("step", "1").
                                attr("min", min).
                                withValue(getInspectionValue()).
                                attr("max", max).
                                withPlaceholder(getAttributeName())
                        ),
                        div()
                        .withClass("col-md-4")
                        .with(
                                span("Min" + "=" + min + ", " + "Max" + "=" + max))
                );
        return ln;
    }

    private Tag generateMinMaxFloatAttributeVal() {
        String min = getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[0];
        String max = getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)[1];

        Tag ln = div()
                .with(
                        div()
                        .withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input").
                                withType("number").
                                attr("step", "0.00000001").
                                attr("min", min).
                                attr("max", max).
                                withValue(getInspectionValue()).
                                withPlaceholder(getAttributeName())
                        ),
                        div()
                        .withClass("col-md-4")
                        .with(
                                span("Min" + "=" + min + ", " + "Max" + "=" + max))
                );
        return ln;
    }

    private Tag generateTextareaAttributeVal() {
        Tag ln = textarea().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-textarea").
                withText(getInspectionValue()).
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateStringAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withValue(getInspectionValue()).
                withType("text").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateVideoUrlAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withValue(getInspectionValue()).
                withType("url").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generatePictureUrlAttributeVal() {
        Tag ln = input().
                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                withClass("form-control apd-form-input").
                withValue(getInspectionValue()).
                withType("url").
                withPlaceholder(getAttributeName());
        return ln;
    }

    private Tag generateYoutubeUrlAttributeVal() {
        Tag ln = div()
                .withClass("apd-div-youtube")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input").
                                withType("url").
                                withValue(getInspectionValue()).
                                withPlaceholder(getAttributeName())),
                        div().withClass("col-md-1")
                        .with(
                                i()
                                .attr("style", "font-size:22px;color:red;  ")
                                .withClass("fa fa-youtube apd-youtube-player")
                                .attr("v_id", "sa_" + getAttributeId())
                        )
                );
        return ln;
    }

    private Tag generateRangeNumbertAttributeVal() throws QException {
        List<String> myList = new ArrayList<>(
                Arrays.asList(getValue().split(CoreLabel.SEPERATOR_VERTICAL_LINE)));

        String selectCls = getHasOther().equals("yes")
                ? "form-control apd-form-select-manual selectpicker apd-form-select-ho"
                : "form-control apd-form-select-manual selectpicker ";
        String hasOtherVl = getHasOther().equals("yes") ? "1" : "0";

        Tag nw = getHasOther().equals("yes")
                ? input().withClass("form-control apd-form-input")
                .withId(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withName(PREFIX_SUBMODULE_ATTRIBUTE_HAS_OTHER + getAttributeId())
                .withValue(getHasOtherInspectionValue())
                .withType("text")
                .attr("style", getHasOtherInspectionValue().length() == 0 ? "margin-top:4px;display:none;" : "margin-top:4px;display:block;")
                : span();

        Tag ln = div()
                .with(
                        select()
                        .withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                        .withClass(selectCls)
                        .attr("has_other", hasOtherVl)
                        .with(
                                option("----").withValue(""),
                                each(myList, l
                                        -> option(l).withValue(l)
                                        .attr(l.equals(getInspectionValue()) ? "selected" : "", "")
                                ),
                                getHasOther().equals("yes")
                                ? option(QUtility.getLabel("other")).withValue("__2__")
                                .attr(getInspectionValue().equals("__2__") ? "selected" : "", "")
                                : span()
                        ),
                        getHasOther().equals("yes") ? br() : span(),
                        nw
                );
        return ln;
    }

    private Tag generatePictureAttributeVal() {
        String ds = getInspectionValue().length() == 0 ? "font-size:14px;color:#00b289;display: none"
                : "font-size:14px;color:#00b289;display: block";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input-file").
                                withValue(getInspectionValue()).
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
                                .attr("apd_image_url", "resources/upload/" + getInspectionValue())
                                .attr("apd_image_alt", "getInspectionValue()ue")
                                .attr("data-toggle", "modal")
                                .attr("data-target", "#apdImageViewer")
                                .attr("aria-hidden", "true")
                                .attr("v_id", "sa_" + getAttributeId())
                        )
                );

        return ln;
    }

    private Tag generateVideoAttributeVal() {
        String ds = getInspectionValue().length() == 0 ? "font-size:14px;color:#00b289;display: none"
                : "font-size:14px;color:#00b289;display: block";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-11")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input-file").
                                withType("file").
                                withValue(getInspectionValue()).
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
                                .attr("apd_video_url", "resources/upload/" + getInspectionValue())
                                .attr("data-toggle", "modal")
                                .attr("data-target", "#apdVideoPlayer")
                                .attr("aria-hidden", "true")
                                .attr("v_id", "sa_" + getAttributeId())
                        )
                );

        return ln;
    }

    private Tag generateSoundUploadAttributeVal() {
        String ds = getInspectionValue().length() == 0 ? "display:none;width:100px;"
                : "display:block;width:100px;";

        Tag ln = div()
                .withClass("fileuploader")
                .with(
                        div().withClass("col-md-8")
                        .with(
                                input().
                                withId(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withName(PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId()).
                                withClass("form-control apd-form-input-file").
                                withValue(getInspectionValue()).
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
                                .attr("s_id", PREFIX_SUBMODULE_ATTRIBUTE + getAttributeId())
                                .with(
                                        source()
                                        .attr("src", "resources/upload/" + getInspectionValue())
                                        .attr("type", "audio/mpeg")
                                )
                        )
                );

        return ln;
    }

    private String getAttributeName() {
        return isPrivate ? privateAttribute.getName() : submoduleAttribute.getAttributeName();
    }

    private String getAttributeId() {
        return isPrivate ? privateAttribute.getId() : submoduleAttribute.getId();
    }

    private String getAttributeType() {
        return isPrivate ? privateAttribute.getFkValueTypeId()
                : submoduleAttribute.getFkValueTypeId();
    }

    private String getHasOther() {
        if(isPrivate) {
            return privateAttribute.getHasOther();
		}
        return submoduleAttribute.getHasOther();
    }

}
