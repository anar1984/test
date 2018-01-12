/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var per_page = ["50", "100", "300", "500", "1000"];

var global_var = {
    "default_per_page": "50",
    "label_type": {"DATE": "DATE", "TIME": "TIME"}
};

var g_tbl = {
    "tbl_valuetype_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetValueTypeList",
        "delete_url": "srv/serviceCrDeleteValueType",
        "result_div_id": "valuetypelist",
        "form_update_popup_id": "updateValueType",
        "form_copy_popup_id": "insertNewValueType",
        "reload_buttion_id": "serviceCrGetValueTypeList",
        "table_block": "0"
    },
    "tbl_expense_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetExpenseList",
        "delete_url": "srv/serviceCrDeleteExpense",
        "result_div_id": "expenselist",
        "form_update_popup_id": "updateExpense",
        "form_copy_popup_id": "insertNewExpense",
        "reload_buttion_id": "serviceCrGetExpenseList",
        "table_block": "0"
    },
    "tbl_payment_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPaymentList",
        "delete_url": "srv/serviceCrDeletePayment",
        "result_div_id": "paymentlist",
        "form_update_popup_id": "updatePayment",
        "form_copy_popup_id": "insertNewPayment",
        "reload_buttion_id": "serviceCrGetPaymentList",
        "table_block": "0"
    },
    "tbl_incomereport_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetIncomeReportList",
        "delete_url": "",
        "result_div_id": "incomereportlist",
        "form_update_popup_id": "",
        "form_copy_popup_id": "",
        "reload_buttion_id": "",
        "table_block": "0"
    },
    "tbl_pricelist_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPriceListList",
        "delete_url": "srv/serviceCrDeletePriceList",
        "result_div_id": "pricelistlist",
        "form_update_popup_id": "updatePriceList",
        "form_copy_popup_id": "insertNewPriceList",
        "reload_buttion_id": "serviceCrGetPriceListList",
        "table_block": "0"
    },
    "tbl_pricelistandsubmodule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRelPriceListAndSubmoduleList",
        "delete_url": "srv/serviceCrDeleteRelPriceListAndSubmodule",
        "result_div_id": "pricelistlist",
        "form_update_popup_id": "assignSubmodule",
        "form_copy_popup_id": "assignSubmodule",
        "reload_buttion_id": "serviceCrGetRelPriceListAndSubmoduleList",
        "table_block": "0"
    },
    "tbl_user_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetUserList",
        "delete_url": "srv/serviceCrDeleteUser",
        "result_div_id": "userlist",
        "form_update_popup_id": "updateUser",
        "form_copy_popup_id": "insertNewUser",
        "reload_buttion_id": "serviceCrGetUserList",
        "table_block": "0"
    },
    "tbl_appointment_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetAppointmentList",
        "delete_url": "srv/serviceCrDeleteAppointment",
        "result_div_id": "patientlist",
        "form_update_popup_id": "",
        "form_copy_popup_id": "",
        "reload_buttion_id": "serviceCrGetAppointmentList",
        "table_block": "0"
    },
    "tbl_entitylabel_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetEntityLabelList",
        "delete_url": "srv/serviceCrDeleteEntityLabel",
        "result_div_id": "entitylabellist",
        "form_update_popup_id": "updateEntityLabel",
        "form_copy_popup_id": "insertNewEntityLabel",
        "reload_buttion_id": "serviceCrGetEntityLabelList",
        "table_block": "0"
    },
    "tbl_patient_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPatientList",
        "delete_url": "srv/serviceCrDeletePatient",
        "result_div_id": "patientlist",
        "form_update_popup_id": "updatePatient",
        "form_copy_popup_id": "insertNewPatient",
        "reload_buttion_id": "serviceCrGetPatientList",
        "table_block": "1"
    },
    "tbl_usertable_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetUserTableList",
        "delete_url": "srv/serviceCrDeleteUserTable",
        "result_div_id": "usertablelist",
        "form_update_popup_id": "updateUserTable",
        "form_copy_popup_id": "insertNewUserTable",
        "reload_buttion_id": "serviceCrGetUserTableList",
        "table_block": "0"
    },
    "tbl_paymenttype_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPaymentTypeList",
        "delete_url": "srv/serviceCrDeletePaymentType",
        "result_div_id": "paymenttypelist",
        "form_update_popup_id": "updatePaymentType",
        "form_copy_popup_id": "insertNewPaymentType",
        "reload_buttion_id": "serviceCrGetPaymentTypeList",
        "table_block": "0"
    },
    "tbl_companypayment_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetCompanyPaymentList",
        "delete_url": "srv/serviceCrDeleteCompanyPayment",
        "result_div_id": "companypaymentlist",
        "form_update_popup_id": "updateCompanyPayment",
        "form_copy_popup_id": "insertNewCompanyPayment",
        "reload_buttion_id": "serviceCrGetCompanyPaymentList",
        "table_block": "0"
    },
    "tbl_company_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetCompanyList",
        "delete_url": "",
        "result_div_id": "companylist",
        "form_update_popup_id": "updateCompany",
        "form_copy_popup_id": "",
        "reload_buttion_id": "serviceCrGetPatientList",
        "table_block": "0"
    },
    "tbl_inspectiomatrix_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionMatrixBodyList",
        "delete_url": "srv/serviceCrGetInspectionMatrixBodyList",
        "result_div_id": "patientlist",
        "form_update_popup_id": "srv/serviceCrGetInspectionMatrixBodyList",
        "form_copy_popup_id": "srv/serviceCrGetInspectionMatrixBodyList",
        "reload_buttion_id": "serviceCrGetPatientList",
        "table_block": "0"
    },
    "tbl_reportline_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetReportLineList",
        "delete_url": "srv/serviceCrDeleteReportLine",
        "result_div_id": "reportlinelist",
        "form_update_popup_id": "updateReportLine",
        "form_copy_popup_id": "insertNewReportLine",
        "reload_buttion_id": "serviceCrGetReportLineList",
        "table_block": "0"
    },
    "tbl_inspection_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionList",
        "delete_url": "",
        "result_div_id": "patientlist",
        "form_update_popup_id": "",
        "form_copy_popup_id": "",
        "reload_buttion_id": "serviceCrGetInspectionList",
        "table_block": "0",
        "show_checkbox": "false"
    },
    "tbl_inspection1_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionList",
        "delete_url": "srv/serviceCrDeleteInspection",
        "result_div_id": "appointmentlist",
        "form_update_popup_id": "updateInspection",
        "form_copy_popup_id": "insertNewInspection",
        "reload_buttion_id": "serviceCrGetInspectionList",
        "table_block": "0"
    }, "tbl_submoduleattribute_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetSubmoduleAttributeList",
        "delete_url": "srv/serviceCrDeleteSubmoduleAttribute",
        "result_div_id": "submoduleattributelist",
        "form_update_popup_id": "updateSubmoduleAttribute",
        "form_copy_popup_id": "insertNewSubmoduleAttribute",
        "reload_buttion_id": "serviceCrGetSubmoduleAttributeList",
        "table_block": "0"
    },
    "tbl_attribute_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetAttributeMainList",
        "delete_url": "srv/serviceCrDeleteAttribute",
        "result_div_id": "attributelist",
        "form_update_popup_id": "updateAttribute",
        "form_copy_popup_id": "insertNewAttribute",
        "reload_buttion_id": "serviceCrGetAttributeList",
        "table_block": "0"
    },
    "tbl_module_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetModuleMainList",
        "delete_url": "srv/serviceCrDeleteModule",
        "result_div_id": "listmodule",
        "form_update_popup_id": "updateModule",
        "form_copy_popup_id": "insertNewModule",
        "reload_buttion_id": "serviceCrGetModuleList",
        "table_block": "0"
    },
    "tbl_submodule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetSubmoduleMainList",
        "delete_url": "srv/serviceCrDeleteSubmodule",
        "result_div_id": "listsubmodule",
        "form_update_popup_id": "updateSubmodule",
        "form_copy_popup_id": "insertNewSubmodule",
        "reload_buttion_id": "serviceCrGetSubmoduleList",
        "table_block": "0"
    },
    "tbl_organpoint_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetOrganPointList",
        "delete_url": "srv/serviceCrDeleteOrganPoint",
        "result_div_id": "organpointlist",
        "form_update_popup_id": "updateOrganPoint",
        "form_copy_popup_id": "insertNewOrganPoint",
        "reload_buttion_id": "serviceCrGetOrganPointList",
        "table_block": "0"
    },
    "tbl_listitem_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetListItemMainList",
        "delete_url": "srv/serviceCrDeleteListItem",
        "result_div_id": "listitemlist",
        "form_update_popup_id": "updateListItem",
        "form_copy_popup_id": "insertNewListItem",
        "reload_buttion_id": "serviceCrGetListItemList",
        "table_block": "0"
    },
    "tbl_permission_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPermissionList",
        "delete_url": "srv/serviceCrDeletePermission",
        "result_div_id": "permissionList",
        "form_update_popup_id": "updatePermission",
        "form_copy_popup_id": "insertNewPermission",
        "reload_buttion_id": "serviceCrGetPermissionList",
        "table_block": "0"
    },
    "tbl_rule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRuleList",
        "delete_url": "srv/serviceCrDeleteRule",
        "result_div_id": "rulePermissionList",
        "form_update_popup_id": "updateRule",
        "form_copy_popup_id": "insertNewRule",
        "reload_buttion_id": "serviceCrGetRuleList",
        "table_block": "0"
    },
    "tbl_rule_permission_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRulePermissionList",
        "delete_url": "srv/serviceCrDeleteRulePermission",
        "result_div_id": "rulePermissionList",
        "form_update_popup_id": "assignPermission",
        "form_copy_popup_id": "assignPermission",
        "reload_buttion_id": "serviceCrGetRulePermissionList",
        "table_block": "0"
    },
    "tbl_role_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRoleList",
        "delete_url": "srv/serviceCrDeleteRole",
        "result_div_id": "roleRuleList",
        "form_update_popup_id": "updateRole",
        "form_copy_popup_id": "insertNewRole",
        "reload_buttion_id": "serviceCrGetRoleList",
        "table_block": "0"
    },
    "tbl_role_rule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRoleRuleList",
        "delete_url": "srv/serviceCrDeleteRoleRule",
        "result_div_id": "roleRuleList",
        "form_update_popup_id": "assignRule",
        "form_copy_popup_id": "assignRule",
        "reload_buttion_id": "serviceCrGetRoleRuleList",
        "table_block": "0"
    },
    "tbl_relpaymenttypeandrule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRelPaymentTypeAndRuleList",
        "delete_url": "srv/serviceCrDeleteRelPaymentTypeAndRule",
        "result_div_id": "relPaymentTypeAndRuleList",
        "form_update_popup_id": "insertNewRelPaymentTypeAndRule",
        "form_copy_popup_id": "insertNewRelPaymentTypeAndRule",
        "reload_buttion_id": "serviceCrGetRelPaymentTypeAndRuleList",
        "table_block": "0"
    },
    "tbl_relcompanyandrule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRelCompanyAndRuleList",
        "delete_url": "srv/serviceCrDeleteRelCompanyAndRule",
        "result_div_id": "relCompanyAndRuleList",
        "form_update_popup_id": "insertNewRelCompanyAndRule",
        "form_copy_popup_id": "insertNewRelCompanyAndRule",
        "reload_buttion_id": "serviceCrGetRelCompanyAndRuleList",
        "table_block": "0"
    },
    "tbl_privatesubmodule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPrivateSubmoduleList",
        "delete_url": "srv/serviceCrDeletePrivateSubmodule",
        "result_div_id": "privatesubmodulelist",
        "form_update_popup_id": "updatePrivateSubmodule",
        "form_copy_popup_id": "insertNewPrivateSubmodule",
        "reload_buttion_id": "serviceCrGetPrivateSubmoduleList",
        "table_block": "0"
    },
    "tbl_privateattribute_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPrivateAttributeList",
        "delete_url": "srv/serviceCrDeletePrivateAttribute",
        "result_div_id": "privatesubmodulelist",
        "form_update_popup_id": "updatePrivateAttribute",
        "form_copy_popup_id": "insertNewPrivateAttribute",
        "reload_buttion_id": "serviceCrGetPrivateAttributeList",
        "table_block": "0"
    }

};
//
//'srv/serviceCrGetAttributeList','srv/serviceCrDeleteAttribute','attributelist','updateAttribute','insertNewAttribute','serviceCrGetAttributeList'
//list_url, delete_url, sourceId, form_update_popup_id, form_copy_popup_id, reloadButtionId, tablename)

function showPanelByMenuClick() {
//    var el = $('li[data-tab^=tg_]').first();
//    var id = $(el).attr('data-tab'); 
//    $(el).addClass('selected-menu-item');
    $('.panel_entity').hide();
//    $('#' + id).show();
//    showList($('#' + id).attr('data-tab'));
}
function toggleSubmoduleButton() {
//    $('.apd-subm-attr-button').each(function () {
//        $(this).toggle();
//    });
    s_h_sm_attribute_buttons();
}

function loadInspectionListBySession(e) {
//    var json = {kv: {}};
//    json.kv.fkUserId = $('#fkDoctorUserId').val();bl_inspectiomatrix_list
//    json.kv.fkPatientId = $('#fkPatientId').val();

    $('#headertitle').html($(e).html());
    disableSubmoduleDiv();
//    s_h_sm_attribute_buttons();
    hideAndDisableAddInspection();
    loadTable('tbl_inspection_list');
    $('.toggle-on-click').hide(1000);
    $('.toggle-on-click-session').hide(1000);
}

function toggleNewSession(e) {
    $('#currentTime').prop('checked', false);
    $('#currentTime').click();
    var fkPatientId = $('#fkPatientId').attr("pid");
    if (fkPatientId != 'undefined' || (fkPatientId)) {
        $('#sessionPatientFullname').val($('#fkPatientId').val());
    } else {
        $('#sessionPatientFullname').val("");
    }
    toggleSessionDate($('#currentTime'));
}

function hideAndDisableAddInspection() {
    var f = $('#newInspectionToggle').attr("f");
    if (f === '-1') {
        toggleNewInspection($('#newInspectionToggle'));
    }
    $('#newInspectionToggle').attr("disabled", "disabled");
}

function showAddInspection() {
    $('#newInspectionToggle').removeAttr("disabled");
}

function toggleNewInspection(e) {
//    $("#apd-submodule-button-list-id").fadeToggle("slide");
//    var f = (-1) * parseInt($(e).attr("f"));
//    $(e).attr("f", f);
//
//    if (f === -1) {
//        $(e).attr("style", "border-color:#D4AC0D;background-color:#D4AC0D;");
//    } else {
//        $(e).attr("style", "border-color:#00b289;background-color:#00b289;");
//    }
//    s_h_sm_attribute_buttons();
}

function loadSession(e) {
//    var json = {kv: {}};
//    json.kv.fkDoctorUserId = $('#fkDoctorUserId').val();
//    json.kv.fkPatientId = $('#fkPatientId').val();
    $('#headertitle').html($(e).html());
    loadTable('tbl_appointment_list');
//    enableSubmoduleDiv();
//    showAddInspection();
//    $('.toggle-on-click').show(1000);
    $('#newSessionToggle').attr("style", "border-color:#00b289;background-color:#00b289;");
    // $('.toggle-on-click').show(1000);
//    $('#tbl_appointment_list .apd-table-checkbox').click();
}

function finishSession(e) {
    var len = $('.apd-table-checkbox:checked').length;
    if (len != 1) {
        alert($(e).html() + ">>" + getMessage('chooseSession'));
        return;
    }
    var r = confirm(getMessage("sureToProseed_q"));
    if (r === false) {
        return;
    }

    var id = $('.apd-table-checkbox:checked').val();
    var json = {kv: {}};
    json.kv.id = id;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrFinishSession",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            loadTable('tbl_appointment_list');
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}


function toggleSessionDate(el) {
    var isnow = $(el).closest(".apd-form").find("#currentTime").prop('checked');


    if (isnow) {
        $(el).closest(".apd-form").find("#appointmentDate").children().attr("disabled", "disabled");
        $(el).closest(".apd-form").find("#appointmentTime1").attr("disabled", "disabled");
        $(el).closest(".apd-form").find("#appointmentTime2").attr("disabled", "disabled");


    } else {
        $(el).closest(".apd-form").find("#appointmentDate").children().removeAttr("disabled");
        $(el).closest(".apd-form").find("#appointmentTime1").removeAttr("disabled");
        $(el).closest(".apd-form").find("#appointmentTime2").removeAttr("disabled");
    }
}

function addAppointment(el) {
    var doctorId = $(el).closest(".apd-form").find("#fkDoctorUserId").val();
    if (!doctorId) {
        alert(getMessage('doctorIsNotEntered'));
        return;
    }
    var patientId = $(el).closest(".apd-page").find("#fkPatientId").attr("pid");
    if (!patientId) {
        alert(getMessage('patientIsNotEntered'));
        return;
    }

    var priceListId = $(el).closest(".apd-form").find("#fkPriceListId").val();
    var isnow = $(el).closest(".row").find("#currentTime").prop('checked');
    var createInvoice = $(el).closest(".row").find("#createInvoice").prop('checked') ? "1" : "0";
    var apdate = "";
    var apttime1 = "";
    var apttime2 = "";

    if (!isnow) {
        var apdate = getQDate($(el).closest(".apd-form").find("#appointmentDate").find('input'));
        var apttime1 = getQTime($(el).closest(".apd-form").find("#appointmentTime1"));
        var apttime2 = getQTime($(el).closest(".apd-form").find("#appointmentTime2"));
        if (!apdate || !apttime1 || !apttime2) {
            alert(getMessage('dateOrTimeIsNotEntered'));
            return;
        }
        if (apttime1 > apttime2) {
            alert(getMessage('time1ShoudbeGEtime2'));
            return;
        }
    }
    var desc = $(el).closest(".apd-form").find("#description").val();
    var json = {kv: {}};
    json.kv.fkPatientId = patientId;
    json.kv.fkDoctorUserId = doctorId;
    json.kv.appointmentDate = apdate;
    json.kv.appointmentTime1 = apttime1;
    json.kv.appointmentTime2 = apttime2;
    json.kv.isNow = isnow;
    json.kv.description = desc;
    json.kv.fkPriceListId = priceListId;
    json.kv.createInvoice = createInvoice;

    showAddInspection();
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrInsertNewAppointment",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
//            $('#fkPatientId').click();
//            $('#fkPatientId').change();
            $('#tbl_appointment_list').closest('div[class="custom-table"]')
                    .find('.table-filter-comp').first().change();

            $('#tbl_appointment_list').find('.apd-table-checkbox').first().attr("checked", false);
            $('#tbl_appointment_list').find('.apd-table-checkbox').first().click();
            alert(getMessage('newSessionIsAdded'));
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function getMessage(key) {
    var text = "";
    var json = {kv: {}};
    json.kv.messageCode = key;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/nasrv/serviceCrGetMessageText",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            text = res.kv.text;

        },
        error: function (res, status) {
            alert('CoreError');
        }
    });
    return text;
}

function getMessage(key, lang) {
    var text = "";
    var json = {kv: {}};
    json.kv.messageCode = key;
    json.kv.lang = lang;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/nasrv/serviceCrGetMessageText",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            text = res.kv.text;

        },
        error: function (res, status) {
            alert('CoreError');
        }
    });
    return text;
}

function getLabel(key) {
    var text = "";
    var json = {kv: {}};
    json.kv.code = key;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/nasrv/serviceCrGetLabel",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            text = res.kv.text;

        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
    return text;
}
function getQDate(e) {
    var v = $(e).val();
    var inputDate = new Date(v);
    var y = inputDate.getFullYear();
    var m = inputDate.getMonth() + 1;
    var d = inputDate.getDate();
    if (m < 10) {
        m = '0' + m;
    }
    if (d < 10) {
        d = '0' + d;
    }
    v = y + m + d;
    return v;
}

function getQTime(e) {
    var inputTime = $(e).val();
    var v = '';
    if (typeof inputTime.value !== "" && inputTime) {
        var h = inputTime.split(":")[0];

        var m = inputTime.split(":")[1];
        var s = "00";

        v = h + m + s;
    }
    return v;
}



function fillCombobox(e, inData) {
    var dependence_id = $(e).attr('dependence_id');
    var main_id = $(e).attr('id');

    //dependence_id varsa ve inData yoxdursa o zaman sorgu gondermeyecek
    if (dependence_id && !inData) {
        return;
    }

    var async = $(e).attr('async');
    if (typeof async === 'undefined' || !async) {
        async = false;
    }

    if (!main_id) {
        return;
    }

    if (!main_id) {
        return;
    }

    var json;
    if (inData) {
        json = inData;
    } else {
        json = {kv: {}};
    }

    $(e).children().remove();

    var url = $(e).attr('srv_url');
    if (typeof url === 'undefined' || !url) {
        return;
    }
    //select text 1 den cox deyer ala biler
    var select_text = $(e).attr('select_text');
    var select_value = $(e).attr('select_value');
    var select_separator = $(e).attr('select_separator');
    var has_null = $(e).attr('has_null');
    //send_data="id=5,color=green" formatda olur
    var send_data = $(e).attr('send_data');
    var selected_value = $(e).attr('selected_value');
    var select_tn = $(e).attr('select_tn');
    var ph = $(e).attr('placeholder');
    var has_other = $(e).attr('has_other');
    var has_all = $(e).attr('has_all');

    if (typeof has_other === 'undefined' || !has_other) {
        has_other = '0';
    }

    if (typeof has_all === 'undefined' || !has_all) {
        has_all = '0';
    }

    if (typeof select_tn === 'undefined' || !select_tn) {
        select_tn = 'Response';
    }

    if (typeof select_separator === 'undefined' || !select_separator) {
        select_separator = ' - ';
    }
    if (typeof has_null === 'undefined' || !has_null) {
        has_null = '__2__';
    }
//    if (typeof ph === 'undefined' || !ph) {
//        has_null = '------';
//    }
//    console.log('url ='+url+','+has_null)
    if (has_null !== '__2__') {
        $(e).append($("<option />").val('').text(' '));
    }

//    if (has_all === '1') {
//                    $(e).append($("<option />").val('').text("All"));
//                }

    if (typeof selected_value === 'undefined' || !selected_value) {
        selected_value = '';
    }

    if (typeof send_data != 'undefined' && send_data) {
        var vs = send_data.split(",");
        for (var k = 0; k < vs.length; k++) {
            var arr = vs[k].split("=");
            var key = arr[0];
            var val = arr[1];
            json.kv[key] = val;
        }
    }

    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/" + url,
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: async,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = res.tbl;
            for (var i = 0; i < obj.length; i++) {
                var objChild = obj[i]['r'];


                for (var j = 0; j < objChild.length; j++) {
                    var t = '';
                    var v = objChild[j][select_value];
                    var vs = select_text.split(",");
                    for (var k = 0; k < vs.length; k++) {
                        t += objChild[j][vs[k]] + select_separator;
                    }
                    t = t.substr(0, t.length - select_separator.length);
                    $(e).append($("<option />").val(v).text(t));
                }
                if (has_other === '1') {
                    $(e).append($("<option />").val("__2__").text(getLabel("other")));
                }
            }
            var mv = $(e).attr("multiple");
            if (mv != 'multiple') {
                $(e).addClass('selectpicker');
                $(e).attr("data-show-subtext", "true");
                $(e).attr("data-live-search", "true");
                $('.selectpicker').selectpicker('refresh');
            }
            $(e).change();

        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
}

function isResultRedirect(data) {
    var input = '<!DOCTYPE html>';

    if (data.indexOf(input) !== -1) {
        document.location = "login.html";
    }

    var d = JSON.parse(data);
    for (var i in d.err) {
        if (d.err[i].code === 'general') {
            alert(d.err[i].val);
            return;
        }
    }
}

function merge_options(obj1, obj2) {
    var obj3 = {};
    for (var attrname in obj1) {
        obj3[attrname] = obj1[attrname];
    }
    for (var attrname in obj2) {
        obj3[attrname] = obj2[attrname];
    }
    return obj3;
}


function logout() {
    document.cookie = "apdtok=; expires=Thu, 01 Jan 1970 00:00:01 UTC; path=/";
    location.reload();
}


function getAgendaOfDoctor(fkDoctorUserId) {
    var r = [];
    var json = {kv: {}};
    json.kv.id = fkDoctorUserId;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetAgendaOfDoctor",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));

            r = res.kv.res;
            return r;
        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
    return r;
}

function getDiscountedPrice(e) {
    var paymentDiscount = $(e).closest("form").find('#paymentDiscount').val();
    paymentDiscount = parseFloat(paymentDiscount);
    if (isNaN(paymentDiscount)) {
        paymentDiscount = 0;
    }
    var fkPriceListId = $(e).closest("form").find('#fkPriceListId').val();
    var json = {kv: {}};
    json.kv.paymentDiscount = paymentDiscount;
    json.kv.fkPriceListId = fkPriceListId;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetDiscountedPrice",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            if (res.kv.price) {
                $(e).closest("form").find('#paymentAmount').val(res.kv.price);
            }

        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
}

function showAgenda() {
    var doctorId = $('#fkDoctorUserId').val();

    var evts = getAgendaOfDoctor(doctorId);
    var agenda2 = JSON.parse(evts);

    var calendar = $('#calendar');
    calendar.empty();
    calendar.fullCalendar({
        header: {
            left: 'title',
            center: 'agendaDay,agendaWeek,month',
            right: 'prev,next today'
        },
        events: agenda2
    });
    $('.fc-button.fc-button-agendaWeek.fc-state-default').click();
    $('.fc-button.fc-button-month.fc-state-default').click();
}

function fillStatisticField(matrixId) {
    $('#f4statistic').html("");
    $('#f4figure').html("");
    $('#f4figureSingle').html("");


    $('#tbl_inspectiomatrix_list').closest('div[class=custom-table]').find('.apd-table-cols').each(function () {
        var el = $(this);
        var cn = el.attr("cn");
//        console.log(cn);
        var cnm = el.attr("cnm");
        var cnt = el.attr("cntitle");

//        if (!el.is(":checked")) {
//             console.log('lolo- '+cn);
        $('#f4statistic').append($("<option />")
                .val(cn)
                .text(cnt));
        $('#f4figure').append($("<option />")
                .val(cn)
                .text(cnt));
        $('#f4figureSingle').append($("<option />")
                .val(cn)
                .text(cnt));

        $('#f4figure').multiselect('rebuild');
        $('#f4figureSingle').multiselect('rebuild');
//        }


    });

//    var json = {kv: {}};
//    json.kv.id = matrixId;
//    var data = JSON.stringify(json);
//    $.ajax({
//        url: "api/post/srv/serviceCrGetInspectionMatrixList",
//        type: "POST",
//        data: data,
//        contentType: "application/json",
//        crossDomain: true,
//        async: false,
//        success: function (res) {
//            isResultRedirect(JSON.stringify(res));
//            if (res.tbl[0].r.length === 0) {
//                return;
//            }
//            var matrixName = res.tbl[0].r[0].matrixName;
////            $('#f4statistic').clear();
//            $('#f4statistic').html("");
//
//            var rd = res.tbl[0].r;
//            for (var i = 0; i < rd.length; i++) {
//                var txt = (rd[i].shortName) ? rd[i].shortName
//                        : rd[i].attributeName;
//
//                $('#f4statistic').append($("<option />")
//                        .val(rd[i].fkSubmoduleAttributeId)
//                        .text(txt));
//
//            }
//        },
//        error: function (res, status) {
//            alert(getMessage('somethingww'));
//        }
//    });
}

function getVoiceAnalyse(filename) {
    if (!filename) {
        return '';
    }
    var res = {};
    var json = {kv: {}};
    json.kv.filename = filename;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetVoiceAnalyse",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
//            console.log(JSON.stringify(res));
            if (res.kv) {
                $('#sa_201707081329440581').val(res.kv.median);
                $('#sa_201707081330020388').val(res.kv.jitter_loc);
                $('#sa_201707081540110846').val(res.kv.min);
                $('#sa_201707081403510470').val(res.kv.max);
                $('#sa_201707081330200120').val(res.kv.shimmer_loc);
                $('#sa_201707081403160657').val(res.kv.std);
                $('#sa_201707081541200471').val(res.kv.hnr);
                $('#sa_201707081330340526').val(res.kv.mean);
                //sound 1
                $('#sa_201707081544140842').attr("file_value", filename);
                $('#sa_201707081544140842').closest(".fileuploader")
                        .find(".apd-image-uploaded").show();
            }
//            return res;
//            if (res.kv.price) {
//                $(e).closest("form").find('#paymentAmount').val(res.kv.price);
//            }
        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
    return res;
}

function createArrayFromTableCol(e) {
    var r = [];
    var cn = $(e).val();
    $('#tbl_inspectiomatrix_list').find('.' + cn).each(function () {
        var el = $(this);
        var val = el.html();
        var arg = parseFloat(val);
        if (!isNaN(arg)) {
            r.push(arg);
        }
    });
    return r;
}

function fillStatistic(e) {
    var n = createArrayFromTableCol(e);
//    console.log(JSON.stringi  fy(n))
//    var n = [44, 55, 66, 77];
    var mean = jStat.mean(n);
    var min = jStat.min(n);
    var max = jStat.max(n);
    var popsd = jStat.stdev(n);
    var vr_popsd = popsd * popsd;
    var sampsd = jStat.stdev(n, true);
    var vr_sampsd = sampsd * sampsd;
    var N = jStat.rows(n);
    var sum = jStat.sum(n);
    var se = sampsd / Math.sqrt(N);

    var table = $('<table></table>').addClass(' table ');

    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("sampsd")))
            .append($('<td></td>').append(sampsd)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("vr_sampsd")))
            .append($('<td></td>').append(vr_sampsd)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("popsd")))
            .append($('<td></td>').append(popsd)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("vr_popsd")))
            .append($('<td></td>').append(vr_popsd)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("total_number")))
            .append($('<td></td>').append(N)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("summarize")))
            .append($('<td></td>').append(sum)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("mean")))
            .append($('<td></td>').append(mean)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("minimum")))
            .append($('<td></td>').append(min)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("maximum")))
            .append($('<td></td>').append(max)));
    table.append($('<tr></tr>').append($('<td></td>').append(getLabel("sdandard_error")))
            .append($('<td></td>').append(se)));

    var div = $('<div></div>').append(table);
    $('#basicStatInfo').html(div.html());

    var table1 = $('<table></table>').addClass(' table ');
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("68.3%, SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 1 * se) + ' - ' + parseFloat(mean + 1 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("90%, 1.645*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 1.645 * se) + ' - ' + parseFloat(mean + 1.645 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("95%, 1.960*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 1.960 * se) + ' - ' + parseFloat(mean + 1.960 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("99%, 2.576*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 2.576 * se) + ' - ' + parseFloat(mean + 2.576 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("99.9%, 3.291*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 3.291 * se) + ' - ' + parseFloat(mean + 3.291 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("99.99%, 3.891*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 3.891 * se) + ' - ' + parseFloat(mean + 3.891 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("99.999%, 4.417*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 4.417 * se) + ' - ' + parseFloat(mean + 4.417 * se))));
    table1.append($('<tr></tr>')
            .append($('<td></td>').append(("99.9999%, 4.892*SE_x̄")))
            .append($('<td></td>').append(parseFloat(mean - 4.892 * se) + ' - ' + parseFloat(mean + 4.892 * se))));

    var div1 = $('<div></div>').append(table1);
    $('#confedendeLevelInfo').html(div1.html());

}



function patientSelectAction(e) {

//    filterPatientCombo(e);
    var val = $(e).attr("pid");
//    console.log("pid on select " + val);
    if (typeof val === typeof undefined || val === false) {
        val = "";
        $('#btn_serviceCrUpdatePatient').attr("disabled", "disabled");
    }

    var st = 'id=' + val;
    var st1 = 'fkPatientId=' + val;
    $('#btn_serviceCrUpdatePatient').attr("apd-form-fill-kv", st);
    $('#btn_serviceCrUpdatePatient').removeAttr("disabled");
    g_tbl['tbl_appointment_list'].kv = st1;
    g_tbl['tbl_inspection_list'].kv = st1;

    //refresh table
    var is_hidden = $('#tbl_appointment_list').is(":visible");
    console.log("is hidden=" + is_hidden);
    if (is_hidden) {
        console.log("is not  hidden=" + is_hidden);
        $('#tbl_appointment_list').closest('div[class="custom-table"]')
                .find('.table-filter-comp').first().change();
        $('#tbl_appointment_list').find('.apd-table-checkbox').first().attr("checked", false);
        $('#tbl_appointment_list').find('.apd-table-checkbox').first().click();
    } else {
        $('#tbl_inspection_list').closest('div[class="custom-table"]')
                .find('.table-filter-comp').first().change();
    }

//    if (!$('#tbl_appointment_list').find('.apd-table-checkbox').first().is(':checked')) {
//        $('#tbl_appointment_list').find('.apd-table-checkbox').first().click();
//    }
    generalActionOnPatientFilter();
}

function filterPatientCombo(hideCombo) {
    $('.es-list').empty();
    $("#fkPatientId").attr("pid", "");
    var pid = $("#fkPatientId").attr("pid");
    var val = $("#fkPatientId").val();
//    console.log("pid=" + pid);
//    console.log("val=" + val)

    json = {kv: {}};
    json.kv.fullname = val;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetPatientList4Combo",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: true,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
//                console.log(JSON.stringify(res))
//                    $('#fkPatientId').attr('size', 6);
            var obj = res.tbl;
//                    for (var i = 0; i < obj.length; i++) {
            if (obj.length == 0) {
                generalActionOnPatientFilter();
                return;
            }
            var objChild = obj[0]['r'];
            for (var j = 0; j < objChild.length; j++) {
                var v = objChild[j]['id'];
                var t = objChild[j]['patientName'];
//                $('#fkPatientId').append($("<option />").val(v).text(t));
                if (v) {
                    $('.es-list')
                            .append($("<li/>")
                                    .text(t)
                                    .attr("class", "es-visible apd-editable-combo-li")
                                    .attr("pid", v)
                                    );
                } else {
                    generalActionOnPatientFilter();
                }
            }
            if (hideCombo == true) {
                $('.es-list').hide();
            } else {
                $('.es-list').show();
            }
        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
}

function clearAndshowAllPatientListCombo() {
    $("#fkPatientId").val("");
    $("#fkPatientId").attr("pid", "");
    filterPatientCombo(true);
    patientSelectAction();
    $("#ul_fkPatientId  li.es-visible").each(function (e) {
        var v = $(e).attr("pid");
        console.log("res for find = " + v);
    });
    var v = $(".apd-editable-combo-li").first().attr("pid");
    console.log("res = " + v);
}

function toogleOccupationOther(e) {
    var val = $(e).val();
    if (val === "__2__") {
        $(e).closest("form[class='apd-form']").find("#occupationOther").removeAttr("disabled");
    } else {
        $(e).closest("form[class='apd-form']").find("#occupationOther").prop('disabled', true);
        $(e).closest("form[class='apd-form']").find("#occupationOther").val('');
    }
}

function confirmPayment(e) {
    var id = $(e).closest('div[class="row apd-page"]').find('table[id=tbl_payment_list]')
            .find(".apd-table-checkbox:checked").first().val();

    json = {kv: {}};
    json.kv.id = id;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrConfirmPayment",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: true,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            alert(getMessage('successOperation'));
            $(e).closest('div[class="row apd-page"]').find('table[id=tbl_payment_list]')
                    .find('.table-filter-comp').first().change();
        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });

}

function findPatientOnPayment(e, hideCombo) {
    var el = $(e);
    var div = el.closest("div");
//    console.log(div.find("label").first().html())

    div.find('.es-list').empty();
    var id = el.attr("id");
    var val = el.val();

    json = {kv: {}};
    json.kv.fullname = val;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetPatientList4Combo",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = res.tbl;
//                    for (var i = 0; i < obj.length; i++) {
            if (obj.length == 0) {
                return;
            }
            var objChild = obj[0]['r'];
            for (var j = 0; j < objChild.length; j++) {
                var v = objChild[j]['id'];
                var t = objChild[j]['patientName'];
                div.find('.es-list')
                        .append($("<li/>")
                                .text(t)
                                .attr("class", "es-visible apd-editable-combo-li")
                                .attr("pid", v)
                                );
            }
            if (hideCombo == true) {
                div.find('.es-list').hide();
            } else {
                div.find('.es-list').show();
            }
        },
        error: function (res, status) {
//            alert(getMessage('somethingww'));
        }
    });
}

function  setCustomSelectValue(e) {
    console.log("done beee")
}

function progresBarStart() {
    console.log($('#popupnav').html());
    $('#popupnav').attr("style","width = '100%'");
    document.getElementById("popupnav").style.width = "100%";
}

function progresBarStop() {
    document.getElementById("popupnav").style.width = "0%";
}


(function ($) {
    $.fn.extend({
        tableAddCounter: function (options) {

// set up default options 
            var defaults = {
                title: '#',
                start: 1,
                id: false,
                cssClass: false
            };
            // Overwrite default options with user provided
            var options = $.extend({}, defaults, options);
            return $(this).each(function () {
// Make sure this is a table tag
                if ($(this).is('table')) {

// Add column title unless set to 'false'
                    if (!options.title)
                        options.title = '';
                    $('th:first-child, thead td:first-child', this).each(function () {
                        var tagName = $(this).prop('tagName');
                        $(this).before('<' + tagName + ' rowspan="' + $('thead tr').length + '" class="' + options.cssClass + '" id="' + options.id + '">' + options.title + '</' + tagName + '>');
                    });
                    // Add counter starting counter from 'start'
                    $('tbody td:first-child', this).each(function (i) {
                        $(this).before('<td>' + (options.start + i) + '</td>');
                    });
                }
            });
        }
    });
})(jQuery);


