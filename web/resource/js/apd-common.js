/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var per_page = ["25", "50", "100", "300", "500", "1000"];

var global_var = {
    "default_per_page": "25",
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
        "table_block": "0",
    },
    "tbl_expense_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetExpenseList",
        "delete_url": "srv/serviceCrDeleteExpense",
        "result_div_id": "expenselist",
        "form_update_popup_id": "updateExpense",
        "form_copy_popup_id": "insertNewExpense",
        "reload_buttion_id": "serviceCrGetExpenseList",
        "table_block": "0",
    },
    "tbl_payment_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPaymentList",
        "delete_url": "srv/serviceCrDeletePayment",
        "result_div_id": "paymentlist",
        "form_update_popup_id": "updatePayment",
        "form_copy_popup_id": "insertNewPayment",
        "reload_buttion_id": "serviceCrGetPaymentList",
        "table_block": "0",
    },
    "tbl_incomereport_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetIncomeReportList",
        "delete_url": "",
        "result_div_id": "incomereportlist",
        "form_update_popup_id": "",
        "form_copy_popup_id": "",
        "reload_buttion_id": "",
        "table_block": "0",
    },
    "tbl_pricelist_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPriceListList",
        "delete_url": "srv/serviceCrDeletePriceList",
        "result_div_id": "pricelistlist",
        "form_update_popup_id": "updatePriceList",
        "form_copy_popup_id": "insertNewPriceList",
        "reload_buttion_id": "serviceCrGetPriceListList",
        "table_block": "0",
    },
    "tbl_user_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetUserList",
        "delete_url": "srv/serviceCrDeleteUser",
        "result_div_id": "userlist",
        "form_update_popup_id": "updateUser",
        "form_copy_popup_id": "insertNewUser",
        "reload_buttion_id": "serviceCrGetUserList",
        "table_block": "0",
    },
    "tbl_appointment_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetAppointmentList",
        "delete_url": "srv/serviceCrDeleteAppointment",
        "result_div_id": "patientlist",
        "form_update_popup_id": "updateAppointment",
        "form_copy_popup_id": "insertNewAppointment",
        "reload_buttion_id": "serviceCrGetAppointmentList",
        "table_block": "0",
    },
    "tbl_entitylabel_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetEntityLabelList",
        "delete_url": "srv/serviceCrDeleteEntityLabel",
        "result_div_id": "entitylabellist",
        "form_update_popup_id": "updateEntityLabel",
        "form_copy_popup_id": "insertNewEntityLabel",
        "reload_buttion_id": "serviceCrGetEntityLabelList",
        "table_block": "0",
    },
    "tbl_patient_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPatientList",
        "delete_url": "srv/serviceCrDeletePatient",
        "result_div_id": "patientlist",
        "form_update_popup_id": "updatePatient",
        "form_copy_popup_id": "insertNewPatient",
        "reload_buttion_id": "serviceCrGetPatientList",
        "table_block": "1",
    },
    "tbl_inspectiomatrix_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionMatrixBodyList",
        "delete_url": "srv/serviceCrGetInspectionMatrixBodyList",
        "result_div_id": "patientlist",
        "form_update_popup_id": "srv/serviceCrGetInspectionMatrixBodyList",
        "form_copy_popup_id": "srv/serviceCrGetInspectionMatrixBodyList",
        "reload_buttion_id": "serviceCrGetPatientList",
        "table_block": "0",
    },
    "tbl_reportline_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetReportLineList",
        "delete_url": "srv/serviceCrDeleteReportLine",
        "result_div_id": "reportlinelist",
        "form_update_popup_id": "updateReportLine",
        "form_copy_popup_id": "insertNewReportLine",
        "reload_buttion_id": "serviceCrGetReportLineList",
        "table_block": "0",
    },
    "tbl_inspection_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionList",
        "delete_url": "srv/serviceCrDeleteInspection",
        "result_div_id": "patientlist",
        "form_update_popup_id": "updateInspection",
        "form_copy_popup_id": "insertNewInspection",
        "reload_buttion_id": "serviceCrGetInspectionList",
        "table_block": "0",
    },
    "tbl_inspection1_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetInspectionList",
        "delete_url": "srv/serviceCrDeleteInspection",
        "result_div_id": "appointmentlist",
        "form_update_popup_id": "updateInspection",
        "form_copy_popup_id": "insertNewInspection",
        "reload_buttion_id": "serviceCrGetInspectionList",
        "table_block": "0",
    }, "tbl_submoduleattribute_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetSubmoduleAttributeList",
        "delete_url": "srv/serviceCrDeleteSubmoduleAttribute",
        "result_div_id": "submoduleattributelist",
        "form_update_popup_id": "updateSubmoduleAttribute",
        "form_copy_popup_id": "insertNewSubmoduleAttribute",
        "reload_buttion_id": "serviceCrGetSubmoduleAttributeList",
        "table_block": "0",
    },
    "tbl_attribute_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetAttributeList",
        "delete_url": "srv/serviceCrDeleteAttribute",
        "result_div_id": "attributelist",
        "form_update_popup_id": "updateAttribute",
        "form_copy_popup_id": "insertNewAttribute",
        "reload_buttion_id": "serviceCrGetAttributeList",
        "table_block": "0",
    },
    "tbl_module_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetModuleMainList",
        "delete_url": "srv/serviceCrDeleteModule",
        "result_div_id": "listmodule",
        "form_update_popup_id": "updateModule",
        "form_copy_popup_id": "insertNewModule",
        "reload_buttion_id": "serviceCrGetModuleList",
        "table_block": "0",
    },
    "tbl_submodule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetSubmoduleList",
        "delete_url": "srv/serviceCrDeleteSubmodule",
        "result_div_id": "listsubmodule",
        "form_update_popup_id": "updateSubmodule",
        "form_copy_popup_id": "insertNewSubmodule",
        "reload_buttion_id": "serviceCrGetSubmoduleList",
        "table_block": "0",
    },
    "tbl_organpoint_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetOrganPointList",
        "delete_url": "srv/serviceCrDeleteOrganPoint",
        "result_div_id": "organpointlist",
        "form_update_popup_id": "updateOrganPoint",
        "form_copy_popup_id": "insertNewOrganPoint",
        "reload_buttion_id": "serviceCrGetOrganPointList",
        "table_block": "0",
    },
    "tbl_listitem_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetListItemMainList",
        "delete_url": "srv/serviceCrDeleteListItem",
        "result_div_id": "listitemlist",
        "form_update_popup_id": "updateListItem",
        "form_copy_popup_id": "insertNewListItem",
        "reload_buttion_id": "serviceCrGetListItemList",
        "table_block": "0",
    },
    "tbl_permission_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetPermissionList",
        "delete_url": "srv/serviceCrDeletePermission",
        "result_div_id": "permissionList",
        "form_update_popup_id": "updatePermission",
        "form_copy_popup_id": "insertNewPermission",
        "reload_buttion_id": "serviceCrGetPermissionList",
        "table_block": "0",
    },
    "tbl_rule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRuleList",
        "delete_url": "srv/serviceCrDeleteRule",
        "result_div_id": "rulePermissionList",
        "form_update_popup_id": "updateRule",
        "form_copy_popup_id": "insertNewRule",
        "reload_buttion_id": "serviceCrGetRuleList",
        "table_block": "0",
    },
    "tbl_rule_permission_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRulePermissionList",
        "delete_url": "srv/serviceCrDeleteRulePermission",
        "result_div_id": "rulePermissionList",
        "form_update_popup_id": "updateRulePermission",
        "form_copy_popup_id": "insertNewRulePermission",
        "reload_buttion_id": "serviceCrGetRulePermissionList",
        "table_block": "0",
    },
    "tbl_role_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRoleList",
        "delete_url": "srv/serviceCrDeleteRole",
        "result_div_id": "roleRuleList",
        "form_update_popup_id": "updateRole",
        "form_copy_popup_id": "insertNewRole",
        "reload_buttion_id": "serviceCrGetRoleList",
        "table_block": "0",
    },
    "tbl_role_rule_list": {
        "response_tn": "Response",
        "list_url": "srv/serviceCrGetRoleRuleList",
        "delete_url": "srv/serviceCrDeleteRoleRule",
        "result_div_id": "roleRuleList",
        "form_update_popup_id": "updateRoleRule",
        "form_copy_popup_id": "insertNewRoleRule",
        "reload_buttion_id": "serviceCrGetRoleRuleList",
        "table_block": "0",
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
    $('.apd-subm-attr-button').each(function () {
        $(this).toggle();
    });
    s_h_sm_attribute_buttons();
}

function loadInspectionListBySession(e) {
//    var json = {kv: {}};
//    json.kv.fkUserId = $('#fkDoctorUserId').val();
//    json.kv.fkPatientId = $('#fkPatientId').val();

    $('#headertitle').html($(e).html());
    disableSubmoduleDiv();
//    s_h_sm_attribute_buttons();
    hideAndDisableAddInspection();
    loadTable('tbl_inspection_list');
}

function toggleNewSession(e) {
    $("#newSessionDiv").fadeToggle("slide");
    var f = (-1) * parseInt($(e).attr("f"));
    $(e).attr("f", f);
    //show
    if (f === -1) {
        $(e).attr("style", "border-color:#D4AC0D;background-color:#D4AC0D;");
    } else {//hide
        $(e).attr("style", "border-color:#00b289;background-color:#00b289;");
    }
    s_h_sm_attribute_buttons();
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
    $("#apd-submodule-button-list-id").fadeToggle("slide");
    var f = (-1) * parseInt($(e).attr("f"));
    $(e).attr("f", f);

    if (f === -1) {
        $(e).attr("style", "border-color:#D4AC0D;background-color:#D4AC0D;");
    } else {
        $(e).attr("style", "border-color:#00b289;background-color:#00b289;");
    }
    s_h_sm_attribute_buttons();
}

function loadSession(e) {
//    var json = {kv: {}};
//    json.kv.fkDoctorUserId = $('#fkDoctorUserId').val();
//    json.kv.fkPatientId = $('#fkPatientId').val();
    $('#headertitle').html($(e).html());
    loadTable('tbl_appointment_list');
    enableSubmoduleDiv();
    showAddInspection();
    $('#tbl_appointment_list .apd-table-checkbox').click();
}

function finishSession(e) {
    var len = $('.apd-table-checkbox:checked').length;
    if (len != 1) {
        alert(getMessage('chooseSession'));
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
    var isnow = $(el).closest(".row").find("#currentTime").prop('checked');


    if (isnow) {
        $(el).closest(".row").find("#appointmentDate").attr("disabled", "disabled");
        $(el).closest(".row").find("#appointmentTime1").attr("disabled", "disabled");
        $(el).closest(".row").find("#appointmentTime2").attr("disabled", "disabled");


    } else {
        $(el).closest(".row").find("#appointmentDate").removeAttr("disabled");
        $(el).closest(".row").find("#appointmentTime1").removeAttr("disabled");
        $(el).closest(".row").find("#appointmentTime2").removeAttr("disabled");
    }
}

function addAppointment(el) {
    var doctorId = $(el).closest(".row").find("#fkDoctorUserId").val();
    if (!doctorId) {
        alert(getMessage('doctorIsNotEntered'));
        return;
    }
    var patientId = $(el).closest(".row").find("#fkPatientId").val();
    if (!patientId) {
        alert(getMessage('patientIsNotEntered'));
        return;
    }
    var isnow = $(el).closest(".row").find("#currentTime").prop('checked');
    var apdate = "";
    var apttime1 = "";
    var apttime2 = "";

    if (!isnow) {
        var apdate = getQDate($(el).closest(".row").find("#appointmentDate"));
        var apttime1 = getQTime($(el).closest(".row").find("#appointmentTime1"));
        var apttime2 = getQTime($(el).closest(".row").find("#appointmentTime2"));
        if (!apdate || !apttime1 || !apttime2) {
            alert(getMessage('dateOrTimeIsNotEntered'));
            return;
        }
        if (apttime1 > apttime2) {
            alert(getMessage('time1ShoudbeGEtime2'));
            return;
        }
    }
    var desc = $(el).closest(".row").find("#description").val();
    var json = {kv: {}};
    json.kv.fkPatientId = patientId;
    json.kv.fkDoctorUserId = doctorId;
    json.kv.appointmentDate = apdate;
    json.kv.appointmentTime1 = apttime1;
    json.kv.appointmentTime2 = apttime2;
    json.kv.isNow = isnow;
    json.kv.description = desc;
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
            loadTable('tbl_appointment_list');

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

function fillSwitchList(e,inData) {
 
    
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
    
    var select_text = $(e).attr('select_text');
    var select_value = $(e).attr('select_value');
    var select_id = $(e).attr('select_id');
    var select_separator = $(e).attr('select_separator');
    var name = $(e).attr('name');
    var send_data = $(e).attr('send_data');
    
    if (typeof selected_value === 'undefined' || !selected_value) {
        selected_value = '';
    }
    if (typeof select_separator === 'undefined' || !select_separator) {
        select_separator = ' - ';
    }
    
    if (typeof send_data !== 'undefined' && send_data) {
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
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = res.tbl;
            for (var i = 0; i < obj.length; i++) {
                var objChild = obj[i]['r'];


                for (var j = 0; j < objChild.length; j++) {
                    var t = '';
                    var v = objChild[j][select_value];
                    var val = objChild[j][select_id];
                    var checked='';
                    if (v==='1') {
                        checked='checked';
                    }
                    var vs = select_text.split(",");
                    for (var k = 0; k < vs.length; k++) {
                        t += objChild[j][vs[k]] + select_separator;
                    }
                    t = t.substr(0, t.length - select_separator.length);
                    var li='<li class="list-group-item">'+t+'<div class="material-switch pull-right">';
                    li += '<input id="'+name+j+'" name="'+name+'" type="checkbox" '+checked+' value="'+val+'"/>';
                    li += '<label for="'+name+j+'" class="label-success"></label>';
                    li += '</div> </li>';
                    
                    
                    
                    $(e).append($(li));
                }
                /*if (has_other === '1') {
                    $(e).append($("<option />").val("__2__").text("Other"));
                }*/
            }
            
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
    
}

function fillCombobox(e, inData) {
    
    var dependence_id = $(e).attr('dependence_id');
    var main_id = $(e).attr('id');
    var kv = $(e).attr('apd-form-fill-kv');
    console.log("fillCombobox: "+kv);
    console.log("fillCombobox: "+JSON.stringify(inData));
    console.log("fillCombobox: "+main_id);
    console.log("fillCombobox: "+dependence_id);
    //dependence_id varsa ve inData yoxdursa o zaman sorgu gondermeyecek
    if (dependence_id && !inData) {
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
        has_null = '';
    }
    if (typeof ph === 'undefined' || !ph) {
        has_null = '------';
    }
    
    if (has_null.length > 0) {
        $(e).append($("<option hidden style='color:#C7C7C7'/>").val('').text(ph));
    }
    
    if (has_all === '1') {
                    $(e).append($("<option />").val('').text("All"));
                }

    if (typeof selected_value === 'undefined' || !selected_value) {
        selected_value = '';
    }

    if (typeof send_data !== 'undefined' && send_data) {
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
        async: false,
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
                    $(e).append($("<option />").val("__2__").text("Other"));
                }
                
            }
            var mv = $(e).attr("multiple");
            if (typeof mv !== 'undefined' && mv !== 'multiple') {
                $(e).addClass('selectpicker');
                $(e).attr("data-show-subtext", "true");
                $(e).attr("data-live-search", "true");
                $('.selectpicker').selectpicker('refresh');
            }
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
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
            alert(getMessage('somethingww'));
        }
    });
    return r;
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