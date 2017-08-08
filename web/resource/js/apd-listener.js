function init() {
    fileListeners();
    comboModuleListListener();
    comboInspectionCodeListener();
    formActivateListeners();
    menuListeners();
    panelListener();
    tableFilterListener();
    formButtonListener();
    buttonFillFormListener();
    buttonTaskTriggerListener();
    formSelectListeners();
    onload();
    subModuleFormShowListeners();
    reportComboListeners();
    tablePatientClickListener();
    matrixBtnClickListener();
    matrixDeleteBtnClickListener();
    matrixItemClickListener();
    matrixItemEditClickListener();
    selectHasOtherChangeListener();
    tableImageBtnListener();
    tableVideoBtnListener();
    youtubeListener();
    apopoverListener();  
    tableColumnCheckListener();
    tableSortAscListener();
    tableSortDescListener();
    tableColumnFilterListener();
    tableGeneralFilterListener();
    tableNavNextListener();
    tableNavPreviousListener();
    tableNavFirstListener();
    tableNavLastListener();
    tableNavPerPageListener();


}



function tableNavPerPageListener() {
    $(document).on("change", '.table-per-page', function (evt) {
        //end limiti sifirla
        var el = $(evt.target);
        var var_tablename = el.closest('[global_var]').attr('global_var');
        var vl = el.val();
        g_tbl[var_tablename].end_limit = vl;
        g_tbl[var_tablename].start_limit = 0;
        var div = el.closest('div[class="custom-table"]');
        console.log(g_tbl[var_tablename].start_limit + ' - ' + g_tbl[var_tablename].end_limit);
        div.find(".table-filter-comp").first().change();
    });
}


function tableNavPreviousListener() {
    $(document).on("click", '.apd-tbl-nav-prvs', function (evt) {
        var el = $(evt.target);
        var var_tablename = el.closest('[global_var]').attr('global_var');
        var per_page = el.closest('div[class=apd-tbl-nav').
                find('.table-per-page').val();
        var end_limit = g_tbl[var_tablename].start_limit;
        end_limit = typeof end_limit === 'undefined' || !end_limit ? per_page
                : parseInt(end_limit) - 1;
        var start_limit = end_limit > per_page
                ? parseInt(end_limit) - parseInt(per_page) + 1
                : 0;
        g_tbl[var_tablename].start_limit = start_limit;
        g_tbl[var_tablename].end_limit = end_limit;
        var div = el.closest('div[class="custom-table"]');
        console.log(g_tbl[var_tablename].start_limit + ' - ' + g_tbl[var_tablename].end_limit);
        div.find(".table-filter-comp").first().change();
    });
}

function tableNavFirstListener() {
    $(document).on("click", '.apd-tbl-nav-frst', function (evt) {
        var el = $(evt.target);
        var var_tablename = el.closest('[global_var]').attr('global_var');
        var per_page = el.closest('div[class=apd-tbl-nav').
                find('.table-per-page').val();
        var end_limit = per_page;
        var start_limit = 0;
        g_tbl[var_tablename].start_limit = start_limit;
        g_tbl[var_tablename].end_limit = end_limit;
        var div = el.closest('div[class="custom-table"]');
        console.log(g_tbl[var_tablename].start_limit + ' - ' + g_tbl[var_tablename].end_limit);
        div.find(".table-filter-comp").first().change();
    });
}

function tableNavLastListener() {
    $(document).on("click", '.apd-tbl-nav-lst', function (evt) {

    });
}

function tableNavNextListener() {
    $(document).on("click", '.apd-tbl-nav-nxt', function (evt) {
        var el = $(evt.target);
        var var_tablename = el.closest('[global_var]').attr('global_var');
        var per_page = el.closest('div[class=apd-tbl-nav').
                find('.table-per-page').val();
        var start_limit = g_tbl[var_tablename].end_limit;
        start_limit = typeof start_limit === 'undefined' || !start_limit ? 0
                : parseInt(start_limit) + 1;
        var end_limit = parseInt(start_limit) + parseInt(per_page);
        end_limit = start_limit === 0 ? end_limit : end_limit - 1;
        g_tbl[var_tablename].start_limit = start_limit;
        g_tbl[var_tablename].end_limit = end_limit;
        var div = el.closest('div[class="custom-table"]');
        console.log(g_tbl[var_tablename].start_limit + ' - ' + g_tbl[var_tablename].end_limit);
        div.find(".table-filter-comp").first().change();
    });
}

function tableGeneralFilterListener() {
    $(document).on("click", '.apd-table-general-filter', function (evt) {
        $('.table-filter-comp').first().change();
    });
}



function tableColumnFilterListener() {
    $(document).on("click", '.apd-table-col-filter', function (evt) {
        var el = $(evt.target);
        var cname = el.attr("cnm");
        var tid = el.closest("table").attr("id");
        $('#' + tid).find('.table-filter').show();
        el.closest("table").find("#" + cname).focus();
    });
}



function tableColumnCheckListener() {
    $(document).on("click", '.apd-table-cols', function (evt) {
        var el = $(evt.target);
        var cn = el.attr("cn");
        if (el.is(":checked")) {
            $("." + cn).show();
        } else {
            $("." + cn).hide();
        }
    });
}

function tableSortAscListener() {
    $(document).on("click", '.apd-table-sasc', function (evt) {
        var el = $(evt.target);
        var tid = el.closest("table").attr("id");
        var cn = el.attr("cn");
        $('.apd-table-sasc').attr("style", "color:#0f7ba7");
        $('.apd-table-sdesc').attr("style", "color:#0f7ba7");
        el.attr("style", "color:red");
        sortTable(tid, cn, false);
    });
}

function tableSortDescListener() {
    $(document).on("click", '.apd-table-sdesc', function (evt) {
        var el = $(evt.target);
        var tid = el.closest("table").attr("id");
        var cn = el.attr("cn");
        $('.apd-table-sasc').attr("style", "color:#0f7ba7");
        $('.apd-table-sdesc').attr("style", "color:#0f7ba7");
        el.attr("style", "color:red");
        sortTable(tid, cn, true);
    });
}

function clearSortIcons() {
    $('.apd-table-sasc').attr("style", "color:#0f7ba7");
    $('.apd-table-sdesc').attr("style", "color:#0f7ba7");
}

function refreshPatientListener() {
    $(document).on("click", '.apd-form-btn-add-patient', function (evt) {
      
    });
}



function apopoverListener() {
    $(document).on("click", '.apd-a-popover', function (evt) {
        $(this).popover();
    });
}



function youtubeListener() {
    $(document).on("click", '.youtube', function (evt) {
         
        $(this).YouTubeModal({autoplay: 0, width: 640, height: 480});
    });
}
    
function tableImageBtnListener() {
    $(document).on("click", '.apd-image-trigger', function (evt) {
        var el = $(evt.target);
        var url_s = [];
        var alt_s = [];
        el.closest("table").find(".apd-image-trigger").each(function () {
            var u = $(this).attr("apd_image_url");
            var a = $(this).attr("apd_image_alt");
            url_s.push(u);
            alt_s.push(a);
        });
        var url = el.attr("apd_image_url");
        var alt = el.attr("apd_image_alt");
        var ol = $('<ol></ol>');
        var div = $('<div></div>');
        for (var i in url_s) {
            var li = $('<li></li>');
            var div_c = $('<div></div>');
            var img = $('<img/>');
            li.attr("data-target", "#myCarousel211")
                    .attr("data-slide-to", i);
            if (url_s[i] === url) {
                li.attr("class", "active");
                div_c.addClass("item active");
            } else {
                div_c.addClass("item");
            }
            ol.append(li);
            img.attr("src", url_s[i]).attr("alt", alt_s[i]).attr("style", "width:100%;");
            div_c.append(img);
            div.append(div_c);
        }
        var s = $('#apdImageViewer').find('.carousel-inner').html();
        $('#apdImageViewer').find('.carousel-inner').html(div.html());
        $('#apdImageViewer').find('.carousel-indicators').html(ol.html());
    });
}

function tableVideoBtnListener() {
    $(document).on("click", '.apd-video-trigger', function (evt) {
        var el = $(evt.target);
        var u = el.attr("apd_video_url");
        var div = $('<div></div>');
        var video = $('<video></video>');
        video.attr("width", "100%").attr("controls", "");
        var src = $('<source/>');
        src.addClass("video_src");
        src.attr("src", u);
        src.attr("type", "video/mp4");
        video.append(src);
        div.append(video);
        $('#apdVideoPlayer').find('.modal-body-1').html(div.html());
    });
}


function fileListeners() {
    $(document).on("change", '.apd-form-input-file', function (evt) {

        var filepicker = this;
        var files = evt.target.files;
        var file_type = $(filepicker).attr("file_type");
        var file = files[0];
        var fileext = file['name'].split('.').pop();
        if (files && file) {
            var reader = new FileReader();
            reader.onload = function (readerEvt) {
                var binaryString = readerEvt.target.result;
                uploadFile(fileext, btoa(binaryString), filepicker, file_type);
            };
            reader.readAsBinaryString(file);
        }
    });
}

function uploadFile(fileext, file_base_64, filepicker, file_type) {
    $(filepicker).closest(".fileuploader").find(".apd-image-spinner").show();
    $(filepicker).closest(".fileuploader").find(".apd-image-uploaded").hide();
    $(filepicker).closest(".fileuploader").find(".apd-image-upload-error").hide();
    $(filepicker).attr('value', "");
    var d = new Object();
    d.file_base_64 = file_base_64;
    d.file_extension = fileext;
    d.file_type = file_type;
    conf = JSON.parse('{"kv":{}}');
    conf['kv'] = d;
    var dat = JSON.stringify(conf);
    $.ajax({
        url: "api/post/upload",
        type: "POST",
        data: dat,
        contentType: "application/json",
        async: true,
        success: function (data) {
            var res = data['kv'];
            if (res.msg) {
                $(filepicker).closest(".fileuploader").find(".apd-form-error-msg").remove();
                $(filepicker).after('<p class=\'apd-form-error-msg\'>' + res.msg + '</p>');
                $(filepicker).closest(".fileuploader").find(".apd-image-spinner").hide();
                $(filepicker).closest(".fileuploader").find(".apd-image-upload-error").show();
            } else {
                $(filepicker).closest(".fileuploader").find(".apd-form-error-msg").remove();
                $(filepicker).attr('file_value', res['uploaded_file_name']);
                $(filepicker).closest(".fileuploader").find(".apd-image-spinner").hide();
                $(filepicker).closest(".fileuploader").find(".apd-image-uploaded").show();
            }
        },
        error: function () {
            $(filepicker).closest(".input-group").find(".apd-image-spinner").hide();
            $(filepicker).closest(".input-group").find(".apd-image-upload-error").show();
        }
    });
}


function selectHasOtherChangeListener() {
    $(document).on("change", '.apd-form-select-ho', function (e) {
        var el = $(e.target);
        var val = el.find(":selected").val();
        var id = el.attr("id");
        var val = el.find(":selected").val();
        if (val === '__2__') {
            var pid = id.split("_")[1];
            $('#ha_' + pid).attr("style", "margin-top:4px;display:block;");
        } else {
            var pid = id.split("_")[1];
            $('#ha_' + pid).attr("style", "margin-top:4px;display:none;");
        }


    });
}

function matrixItemEditClickListener() {
    $(document).on("click", '.apd-patient-matrix-item-edit', function (e) {
        var el = $(e.target);
        var id = el.attr("id");
        var json = {kv: {}};
        json.kv.id = id;
        var data = JSON.stringify(json);
        $.ajax({
            url: "api/post/srv/serviceCrGetInspectionMatrixList",
            type: "POST",
            data: data,
            contentType: "application/json",
            crossDomain: true,
            async: false,
            success: function (res) {
                isResultRedirect(JSON.stringify(res));
                if (res.tbl[0].r.length === 0) {
                    return;
                }
                var matrixName = res.tbl[0].r[0].matrixName;
                $('#insertNewInspectionMatrix').find("[id=matrixName]").val(matrixName);
                $('#insertNewInspectionMatrix').find("[id=id]").val(id);
                var tbl = $('#insertNewInspectionMatrix').find("[id=matrixTbl]");
                //clear table;
                $(tbl).children('tbody').html("");
                var rd = res.tbl[0].r;
                for (var i = 0; i < rd.length; i++) {

                    var tr = $('<tr></tr>')
                            .append("<td ><span class=\"glyphicon glyphicon-move\"></span>" + (i + 1) + "</td>")
                            .append("<td cn='fkSubmoduleAttibutesId' style=\"display:none;\">" + 
                            rd[i].fkSubmoduleAttributeId + "</td>")
                            .append("<td >" + rd[i].attributeName + "</td>")
                            .append("<td cn='shortName'>" + rd[i].shortName + "</td>")
                            .append("<td > <i class=\"fa fa-remove\" \n\
                        style=\"color:red\" onClick = \"$(this).closest('tr').remove();\"></i>  </td>");
                    $(tbl).children('tbody').append(tr);
                }

                $('.table-sortable tbody').sortable({
                    handle: 'span'
                });
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}

function disableSubmoduleDiv() {

}

function enableSubmoduleDiv() {

}

function matrixItemClickListener() {
    $(document).on("click", '.apd-patient-matrix-item', function (e) {
        var el = $(e.target);
        var id = el.attr("id");
        if (id !== '-1') {
            var json = {kv: {}};
            json.kv.matrixId = id;
            loadTable('tbl_inspectiomatrix_list', json);
            disableSubmoduleDiv();
            hideAndDisableAddInspection();
            fillStatisticField(id);
            setMatrixItemName(el.html());
        }

    });
}

function setMatrixItemName(name){
    $('#matrixListName').html(name);
}

function matrixDeleteBtnClickListener() {
    $(document).on("click", '.apd-form-matrix-delete-btn', function (e) {
        var r = confirm("Are you sure to proceed operation?");
        if (r == false) {
            return;
        }

        var matid = $(this).closest(".apd-form").find("input[id=id]").val();
        if (typeof matid === 'undefined' || !matid) {
            return;
        }

        var json = {kv: {}};
        json.kv.id = matid;
        var data = JSON.stringify(json);
        $.ajax({
            url: "api/post/srv/serviceCrDeleteInspectionMatrix",
            type: "POST",
            data: data,
            contentType: "application/json",
            crossDomain: true,
            async: false,
            success: function (res) {
                isResultRedirect(JSON.stringify(res));
                alert(getMessage('successOperation'));
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}


function matrixBtnClickListener() {
    $(document).on("click", '.apd-form-matrix-btn', function (e) {
        var mid = $(this).closest(".apd-form").find("[id=fkMainModuleId]").
                find(":selected").val();
        var said = $(this).closest(".apd-form").find("[id=fkSubmoduleAttibutesId]").
                find(":selected").val();
        var saname = $(this).closest(".apd-form").find("[id=fkSubmoduleAttibutesId]").
                find(":selected").text();
        var sname = $(this).closest(".apd-form").find("[id=shortName]").val();
        if (typeof said === 'undefined' || !said) {
            return;
        }
        var tbl = $(this).closest(".apd-form").find("[id=matrixTbl]");
        var rc = $('tr', $(tbl).find('tbody')).length;
        var tr = $('<tr></tr>')
                .append("<td ><span class=\"glyphicon glyphicon-move\"></span>" + (rc + 1) + "</td>")
                .append("<td cn='fkSubmoduleAttibutesId' style=\"display:none;\">" + said + "</td>")
                .append("<td >" + saname + "</td>")
                .append("<td cn='shortName'>" + sname + "</td>")
                .append("<td > <i class=\"fa fa-remove\" \n\
                        style=\"color:red\" onClick = \"$(this).closest('tr').remove();\"></i>  </td>");
        $(tbl).children('tbody').append(tr);
        $('.table-sortable tbody').sortable({
            handle: 'span'
        });
    });
}

function comboInspectionCodeListener() {
    $(document).on("click", '.apd-inspection-cmb-list', function (e) {
        var mid = $(this).find(":selected").val();
        if (mid === '-2') {
            $('#apd-submodule-button-list-id').hide();
        } else {
            $('#apd-submodule-button-list-id').show();
        }
        if (mid === '-1') {
            var cd = getNewInsCode();
            $('.apd-subm-attr-button').attr("ins_id", cd);
            $('.apd-subm-attr-button').attr("data-target", "#popup1");
        } else if (mid !== '-1' && mid !== '-2') {
            $('.apd-subm-attr-button').attr("ins_id", mid);
            $('.apd-subm-attr-button').attr("data-target", "#popup2");
        }
        toggleReportLine();
    });
}

function getNewInsCode() {
    var cd = "";
    var data = "";
    $.ajax({
        url: "api/post/srv/serviceCrGetNewSInspectionCode",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            if (res.kv.insCode) {
                cd = res.kv.insCode;
            }
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
    return cd;
}

function comboModuleListListener() {
    $(document).on("click", '.apd-module-cmb-list', function (e) {

        var mid = $(this).find(":selected").val();
        var json = {kv: {}};
        json.kv.fkModuleId = mid;
        fillCombobox($('#fkReportId'), json);
        fillSubmoduleButtonDiv(json);
    });
}

function fillSubmoduleButtonDiv(json) {
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGenSubmoduleButtonList",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = "";
            if (res.kv.body) {
                obj = res.kv.body;
            }
            $('#apd-submodule-button-list-id').html(obj);
            tablePatientClickExecutor();
            s_h_sm_attribute_buttons();
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function tablePatientClickListener() {

    $(document).on("click", '.apd-table-checkbox', function (e) {
        var len = $('.apd-table-checkbox:checked').length;
        if (len === 0)
            tablePatientClickExecutor();
    });
    
    $(document).on("click", '#tbl_appointment_list .apd-table-checkbox', function (e) {
        var len = $('.apd-table-checkbox:checked').length;
         s_h_sm_attribute_buttons();
        if (len === 1) {
            var fkSessionId = $(this).val();
            $('#fkReportId').removeAttr("disabled");
            var mid = $('#fkModuleId').val();
            var json = {kv: {}};
            json.kv.fkModuleId = mid;
            json.kv.fkSessionId = fkSessionId;
            fillSubmoduleButtonDiv(json);
        }else{
             $('#fkReportId').attr("disabled","disabled");
        }
    });
}

function s_h_sm_attribute_buttons() {
    var len = $('.apd-table-checkbox:checked').length;
    if (len !== 1) {
        $(".apd-subm-attr-button").each(function () {
            $(this).attr("disabled", "disabled");
        });
    } else {
        $(".apd-subm-attr-button").removeAttr("disabled");
    }
}




function tablePatientClickExecutor() {
//    var len = $('.apd-table-checkbox:checked').length;
//    if (len > 1) {
//        $('#inspectionCode').attr('disabled', 'disabled');
//        fillInspectionComboByTableClick("");
//    } else {
//        var pid = $('.apd-table-checkbox:checked').val();
//        $('#inspectionCode').removeAttr('disabled');
//        fillInspectionComboByTableClick(pid);
//    }
//    $('#inspectionCode').click();
}

function toggleReportLine() {
//    var insCode = $('#inspectionCode').val();
//    var len = $('.apd-table-checkbox:checked').length;
//
//    if (len === 0) {
//        $('#fkReportId').attr('disabled', 'disabled');
//    } else if (len > 1) {
//        $('#fkReportId').attr('disabled', 'disabled');
//    } if (len === 1) {
//        var insCode = $('#inspectionCode').val();
////        alert('outer' + insCode)
//        if (insCode !== '-1' && insCode !== '-2') {
////            alert('inside' + insCode)
////            alert($('#fkReportId').attr('disabled')+'--55');
//            $('#fkReportId').removeAttr('disabled');
////            alert($('#fkReportId').attr('disabled')+'--77');
//        } else {
//            $('#fkReportId').attr('disabled', 'disabled');
//        }
//    }
}

function fillInspectionComboByTableClick(fkPatientId) {
//    var pid = '-1';
//    if (fkPatientId) {
//        pid = fkPatientId;
//    }
//    var mdid = $('#fkModuleId').val();
//    var json = {kv: {}};
//    json.kv.fkPatientId = pid;
//    json.kv.fkModuleId = mdid;
//    var data = JSON.stringify(json);
//    $.ajax({
//        url: "api/post/srv/serviceCrGetInspectionCodeList",
//        type: "POST",
//        data: data,
//        contentType: "application/json",
//        crossDomain: true,
//        async: false,
//        success: function (res) {
//            isResultRedirect(JSON.stringify(res));
//            var obj = res.tbl[0].r;
//            var e = $('#inspectionCode');
//            e.empty();
//            for (var j = 0; j < obj.length; j++) {
//                var k = obj[j].inspectionCode;
//                var v = obj[j].inspectionValue;
//                $(e).append($("<option />").val(k).text(v));
//            }
//
//            $(e).find('option[value=-2]').attr("selected", "selected");
//            $(e).addClass('selectpicker');
//            $(e).attr("data-show-subtext", "true");
//            $(e).attr("data-live-search", "true");
//            $('.selectpicker').selectpicker('refresh');
//        },
//        error: function (res, status) {
//            alert(getMessage('somethingww'));
//        }
//    });
}

function fillInspectionMatrixList() {

    var json = {kv: {}};
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetInspectionMatrixMainList",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = (res.tbl[0]) ? res.tbl[0].r : 0;
            var e = $('#inspectionMatrixMain');
            e.empty();
            $(e).append(
                    $('<li>')
                    .append(
                            $('<a>')
                            .attr("id", "-1")
                            .attr("data-toggle", "modal")
                            .attr("data-target", "#insertNewInspectionMatrix")
                            .addClass("apd-task-create apd-task-form apd-patient-matrix-item")
                            .append("<b>New</b>")));
            for (var j = 0; j < obj.length; j++) {
                var k = obj[j].id;
                var v = obj[j].matrixName;
                $(e).append(
                        $('<li>')
                        .append(
                                $("<i>")
                                .attr("data-toggle", "modal")
                                .attr("data-target", "#insertNewInspectionMatrix")
                                .addClass("fa fa-edit apd-task-create apd-task-form apd-patient-matrix-item-edit")
                                .attr("type", "button")
                                .attr("id", k)
                                .attr("style", "padding-left:5px;border-style:none; cursor:pointer;font-size:12px;color:#00b289;height: 18px;")
                                )
                        .append(
                                $('<span style="margin-left: 10px;cursor:pointer">').attr("id", k)
                                .addClass("apd-patient-matrix-item")
                                .append(v)));
            }
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function onload() {
    var id = $('.apd-portal-loader').attr('page_id');
    //alert(id);
    if (!id) {
        return;
    }
    menuListenerActivies(id);
    $('.apd-page').hide();
    $('#' + id).show();
}

function buttonFillFormListener() {
    $(document).on("click", ".apd-task-form-fill", function (e) {
        var el = $(e.target);
        //apd-form-fill-kv="id=1,name=1" seklinde olur
        var url = el.attr('apd-form-fill-url');
        var kv = el.attr('apd-form-fill-kv');
        var target_form = el.attr('data-target');
        //remove # sign
        target_form = target_form.substring(1, target_form.length);
        clearForm(target_form, 'update');
        //fill form elements
        //fill select element
        $('#' + target_form).find(".apd-form-select").each(function () {
            fillCombobox(this);
            $(this).change();
        });
        $('#' + target_form).find(".apd-form-htmleditor").each(function () {
            $(this).Editor();
        });
        
        
        
        
        //create json
        json = {kv: {}};
        //fill json
        var arr = kv.split(',');
        for (var i in arr) {
            var t = arr[i].split('=');
            var key = t[0];
            var val = t[1];
            json.kv[key] = val;
        }
        
        $('#' + target_form).find(".apd-form-switch-list").each(function () {
            console.log("buttonFillFormListener: "+JSON.stringify(json));
            
            console.log("buttonFillFormListener: "+kv);
            fillSwitchList(this,json);
        });
        
        
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
                //birinci gelen table goturulur. susmaya gore cedvel 
                //adi Response olmalidir
                //get key of each element. Output is array of strings
                var keys = Object.keys(res.tbl[0].r[0]);
                //set form input values
                for (var i = 0; i < keys.length; i++) {
                    var v = (res.tbl[0].r[0][keys[i]]) ? res.tbl[0].r[0][keys[i]] : "";
                    try {
                        $('#' + target_form).find('input[id=' + keys[i] + ']').val(v);
                    } catch (err) {
                    }
                    try {
                        $('#' + target_form).find('input[type=file,id=' + keys[i] + ']').attr("file_value", v);
                    } catch (err) {

                    }
                    $('#' + target_form).find('textarea[id=' + keys[i] + ']').val(v);
                    if (v) {
                        $('#' + target_form).find('select[id=' + keys[i] + ']').
                                find('option[value=' + v + ']').attr("selected", "selected");
                    }
                    $('#' + target_form).find('.apd-form-htmleditor').each(function () {
                        var e1 = $(this).attr('id');
                        if (e1 == keys[i]) {
                            if (!$(this).data("source-mode"))
                                $(this).data("editor").html(v);
                            else
                                $(this).data("editor").children().first().text(v);
                        }
                    });
                }
                $('.selectpicker').selectpicker('refresh');
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}

function buttonTaskTriggerListener() {
    $(document).on("click", ".apd-task-trigger", function (e) {
        var r = confirm(getMessage("sureToProseed_q"));
        if (r === false) {
            return;
        }

        var el = $(e.target);
        //apd-form-fill-kv="id=1,name=1" seklinde olur
        var url = el.attr('apd-form-fill-url');
        var kv = el.attr('apd-form-fill-kv');
        var refreshBtnId = el.attr('apd-form-reload-button-id');
        //create json
        json = {kv: {}};
        //fill json
        var arr = kv.split(',');
        for (var i in arr) {
            var t = arr[i].split('=');
            var key = t[0];
            var val = t[1];
            json.kv[key] = val;
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
                //get onclick fuct name
                var func = $('#' + refreshBtnId).attr('onclick');
                //execute function
                eval(func);
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}

function formButtonListener() {
    $(document).on("click", '.apd-form-btn', function (e) {
        
        
        
        var el = $(e.target);
        
        /*el.closest("form[class='apd-form']").on("forminvalid.zf.abide", function(ev,frm) {
                    alert("Form is invalid, finally!");
                    return false;
                    // do something perhaps
                });*/
         
        //remove all error message from form
        el.closest("form[class='apd-form']").find(".apd-form-error-msg").each(function () {
            $(this).remove();
        });
        //get url
        var url = el.closest("form[class='apd-form']").attr("action");
        //control variable for emptying data after successful operation
        var emptyData = el.attr('apd-clear-form-data');
        //json data
        var json = {kv: {}, tbl: []};
        //add input values to json
        el.closest("form[class='apd-form']").find(".apd-form-input").each(function () {
            var k = $(this).attr("id");
            var v = $(this).val();
            json.kv[k] = v;
        });
        el.closest("form[class='apd-form']").find(".apd-form-input-file").each(function () {
            var k = $(this).attr("id");
            var v = $(this).attr("file_value");
            json.kv[k] = v;
        });
        el.closest("form[class='apd-form']").find(".apd-form-email").each(function () {
            var k = $(this).attr("id");
            var v = $(this).val();
            json.kv[k] = v;
        });
        el.closest("form[class='apd-form']").find(".apd-form-date").each(function () {
            var v = $(this).val();
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
            var k = $(this).attr("id");
            json.kv[k] = v;
        });
        //add select values to json
        el.closest("form[class='apd-form']").find(".apd-form-select").each(function () {
            var id = $(this).attr('id');
            var val = $(this).find(":selected").val();
            json.kv[id] = val;
        });
        //add select values to json
        el.closest("form[class='apd-form']").find(".apd-form-multiselect, .apd-form-multiselect-manual").each(function () {
            var id = $(this).attr('id');
            var v = $(this).val();
            var l = "";
            for (var i in v) {
                l += v[i] + "|";
            }
            l = l.substring(0, l.length - 1);
            json.kv[id] = l;
        });
        el.closest("form[class='apd-form']").find(".apd-form-select-manual").each(function () {
            var id = $(this).attr('id');
            var val = $(this).find(":selected").val();
            json.kv[id] = val;
        });
        //add switch-list
        el.closest("form[class='apd-form']").find(".apd-form-switch-list").each(function () {
            var id = $(this).attr('id');
            var tn  = $(this).attr('tn');
            
            var tn1 = {};
            tn1.tn = tn;
            var r = [];
            $(this).find('input:checked').each(function () {
                var tro = {};
                
                    var cn = $(this).val();
                    if (typeof cn === 'undefined' || !cn) {
                        cn = '';
                    }
                    if (cn.length > 0) {
                        tro[id] = cn;
                    }
                r.push(tro);
            })
            tn1.r = r;
            json.tbl.push(tn1);
            
            
        });
        
        //add input values to json
        el.closest("form[class='apd-form']").find(".apd-form-textarea").each(function () {
            var k = $(this).attr("id");
            var v = $(this).val();
            json.kv[k] = v;
        });
        //add input values to json
        el.closest("form[class='apd-form']").find(".apd-form-htmleditor").each(function () {
            var k = $(this).attr("id");
            var v = "";
            if (!$(this).data("source-mode"))
                v = $(this).data("editor").html();
            else
                v = $(this).data("editor").children().first().text();
            json.kv[k] = v;
        });
        //add table to json
        el.closest("form[class='apd-form']").find(".apd-form-table").each(function () {
            var tn = $(this).attr("id");
            var tn1 = {};
            tn1.tn = tn;
            var r = [];
            $(this).find('tbody>tr').each(function () {
                var tro = {};
                $(this).find('td').each(function () {
                    var cn = $(this).attr("cn");
                    if (typeof cn === 'undefined' || !cn) {
                        cn = '';
                    }
                    if (cn.length > 0) {
                        tro[cn] = $(this).html();
                    }
                })
                r.push(tro);
            })
            tn1.r = r;
            json.tbl.push(tn1);
        });
        //send request 
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
                if (res.err.length > 0) {
                    //there are/is errors
                    for (var i in res.err) {
                        el.closest("form[class='apd-form']").
                                find("[id=" + res.err[i].code + "]").
                                after('<p class=\'apd-form-error-msg\'>' + res.err[i].val + '</p>');
                    }
                } else {
                    var formId = el.closest("form[class='apd-form']").attr("id");
                    //clear data if apd-clear-form-data=true
                    if (emptyData === 'true') {
                        clearForm(formId);
                    }
                    //get id of reload div 
                    var i = 1;
                    var a = el.closest("div[class='row apd-page']").find(".table-filter-comp").each(function () {
                        if (i < 3) {
                            $(this).change();
                        }
                        i++;
                    });
//                    var reloadDiv = el.closest("[apd-form-reload-button-id]").attr('apd-form-reload-button-id');
//                   
                    //get onclick fuct name
//                    var func = $('#' + reloadDiv).attr('onclick');
                    //execute function
//                    eval(func);
                    //show successful message
                    alert(getMessage('successOperation'));
                    if (el.attr('id')==='insertNewPatientBtn'){
                        fillCombobox($('#fkPatientId'));
                    }
                }
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}

function clearForm(formId, actionType) {
    $('#' + formId).find(".apd-form-input").each(function () {
        $(this).val("");
    });
    $('#' + formId).find(".apd-form-textarea").each(function () {
        $(this).val("");
    });
    if (actionType === 'update') {
        $('#' + formId).find(".Editor-container").each(function () {
            $(this).remove();
        });
    }
}

function menuListeners(arg) {
    $(document).on("click", '.megamenu li', function (e) {
        var id = $(this).attr('page_id');
        //call manual functions
        menuListenerActivies(id);
        $('.apd-page').hide();
        $('#' + id).show();
    });
}

function menuListenerActivies(page_id) {
//execute page loader
    loadRemotePage(page_id);
    var func = $('#' + page_id).find('.apd-task-page-loader').attr('onclick');
    eval(func);
    switch (page_id) {
        case "page_dn_patient":
            fillCombobox($('#fkModuleId'));
//            fillCombobox($('#fkReportId'));
            fillInspectionComboByTableClick("");
            break;
        case "page_appointment":
            fillCombobox($('#fkModuleId'));
            $('#fkModuleId').click();
            fillCombobox($('#fkDoctorUserId'));
            fillCombobox($('#fkPatientId'));
            fillInspectionMatrixList();
            $("#serviceCrGetAppointmentList").click();
            fillCombobox($('#fkReportId'));
//            fillInspectionComboByTableClick("");
            break;
        case "page_statistic":
            fillCombobox($('#fkModuleId'));
            $('#fkModuleId').click();
            fillCombobox($('#fkDoctorUserId'));
            fillCombobox($('#fkPatientId'));  
            fillCombobox($('#fkReportId'));
            fillInspectionMatrixList();
            clickFirstElementOfMatritList();
//            fillInspectionComboByTableClick("");
            break;
        case "page_patient":
            fillCombobox($('#fkModuleId'));
//            fillCombobox($('#fkReportId'));
            fillInspectionComboByTableClick("");
            fillInspectionMatrixList();
            break;
        case "page_payment":
            break;
        default:
    }
}

function clickFirstElementOfMatritList(){
    $('.apd-patient-matrix-item:eq(1)').click();
}
 
function loadRemotePage(pageid) {
    var json = {kv: {}};
    json.kv["page"] = pageid;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetPage",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = res.kv.body;
            $('.page-content').html(obj);
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function panelListener() {
    $('.main_panel').hide(function (e) {
    });
    $('.main_panel').show(function (e) {
    });
}

function fillComboInTableFilter(json, url, comboThis, colName, checkedValues) {

    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/" + url,
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
//            isResultRedirect(JSON.stringify(res));
            if (res.tbl.length > 0) {
                var obj = res.tbl[0].r;
                $(comboThis).children().remove();
                for (var i in obj) {
                    $(comboThis).append($("<option></option>",
                            {'value': obj[i][colName]}).text(obj[i][colName]));
                }

                $(comboThis).multiselect('rebuild');
                var arr = checkedValues.split("|");
                $(comboThis).multiselect('select', arr);
            } else {
                $(comboThis).children().remove();
                $(comboThis).multiselect('rebuild');
            }
            //add checked values 


        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function tableFilterListener() {
    $(document).on("change", ".table-filter-comp, .table-filter-combo", (function (e) {
        var json = {kv: {}};
        var element = $(e.target);
        var matrixId = element.closest('table').attr("matrixid");
        if (typeof matrixId != 'undefined' && matrixId) {
            json.kv.matrixId = matrixId;
        }

        var div1 = element.closest('[srv_url]');
        var url = div1.attr("srv_url");
        var var_tablename = element.closest('[global_var]').attr('global_var');
        var tableId = var_tablename;
        var div = element.closest('div[class="custom-table"]');
//        var per_page = div.find(".table-filter-comp[name=per_page]").val();
//        var page_count = div.find(".table-filter-comp[name=page_count]").val();
//        page_count = (page_count) ? page_count : 1;
//        var start_ind = per_page * (page_count - 1);
//        var sLimit = per_page * (page_count - 1);
//        var eLimit = per_page * (page_count);
        var sLimit = g_tbl[var_tablename].start_limit;
        var eLimit = g_tbl[var_tablename].end_limit;
        json.kv["startLimit"] = sLimit;
        json.kv['endLimit'] = eLimit;
        div.find(".table-filter-comp").each(function () {
            var k = $(this).attr("name");
            var v = $(this).val();
            if (v) {
//                if (k != "per_page" && k != "page_count") {
                    json.kv[k] = v;
//                }
            }
        });
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
                clearSortIcons();
                var obj = res.tbl;
                if (obj.length === 0) {
                    var tr = '<tr role="row"><td class="et" colspan="220">';
                    tr += getMessage('nothingFound');
                    tr += '</td></tr>';
                    $('#' + tableId).find('tbody').html(tr);
                    //bu hisseni hazirlamaq lazimdir.
//                    addEmptyRow(tableId);    
//                    changePageNumberOfTable(per_page, 1, page_count, div);
                } else {
                    for (var i = 0; i < obj.length; i++) {
                        var tn = obj[i]['tn'];
                        if (tn === g_tbl[var_tablename].response_tn) {
                            var rc = obj[i]['rowCount'];
//                            changePageNumberOfTable(per_page, rc, page_count, div);
                            fillTableBodyByid(var_tablename, obj[i], sLimit);
                            break;
                        }
                    }
                }
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });


    }));
}

function formActivateListeners() {
    $(document).on("click", '.apd-task-form', function (e) {
        var target_id = $(this).attr('data-target');
        target_id = target_id.substring(1, target_id.length);
        clearForm(target_id, 'update');
        $('#' + target_id).find("form").find(".apd-form-select").each(function () {
            fillCombobox(this);
        });
        $('#' + target_id).find("form").find(".apd-form-switch-list").each(function () {
            console.log("formActivateListeners: <empty>");
            fillSwitchList(this);
        });
        $('#' + target_id).find("form").find(".apd-form-multiselect").each(function () {
            fillCombobox(this);
            $(this).multiselect(
                    {includeSelectAllOption: true,
                        enableFiltering: true,
                        selectAllJustVisible: true}
            );
        });
        $('#' + target_id).find("form").find(".apd-form-multiselect-manual").each(function () {
            $(this).multiselect(
                    {includeSelectAllOption: true,
                        enableFiltering: true,
                        selectAllJustVisible: true}
            );
        });
        $('#' + target_id).find("form").find(".apd-form-htmleditor").each(function () {
            $(this).Editor();
        });
    });
}

function formSelectListeners() {
    $(document).on("change", '.apd-form-select', function (e) {
        var element = $(e.target);
        var id = $(element).attr('id');
        var text = $(element).find(":selected").text();
        var val = $(element).find(":selected").val();
        var json = {kv: {}};
        json.kv[id] = val;
        
         element.closest('.apd-form').find('input[id="id"]').each(function () {
            console.log("userid="+$(this).val());
            json.kv["id"] = $(this).val();
           
        });
        
        
        element.closest('.apd-form').find('select[dependence_id=' + id + ']').each(function () {
            var e_id = $(this).attr('id');
            fillCombobox(this, json);
        });
        element.closest('.apd-form').find('ul[dependence_id=' + id + ']').each(function () {
            console.log("formSelectListeners: "+JSON.stringify(json));
            var kv = element.attr('apd-form-fill-kv');
            console.log("formSelectListeners: "+kv);
            fillSwitchList(this, json);
        });
    });
}


function reportComboListeners() {
    $(document).on("click", '.apd-report-cmb-list', function (e) {
        var rid = $('#fkReportId').val();
        var moduleId = $('#fkModuleId').val();
        var fkSessionId = $('.apd-table-checkbox:checked').val();
        fkSessionId = (fkSessionId) ? fkSessionId : "";
        if (fkSessionId === '') {
            alert(getMessage('chooseSession'));
            return;
        }  

        json = {kv: {}};
        json.kv.id = rid;
        json.kv.fkSessionId = fkSessionId;
        json.kv.fkModuleId = moduleId; 
        var data = JSON.stringify(json);
        $.ajax({
            url: "api/post/srv/serviceCrGetReportLineList4Print",
            type: "POST",
            data: data,
            contentType: "application/json",
            crossDomain: true,
            async: false,
            success: function (res) {
                isResultRedirect(JSON.stringify(res));
                var rhtml = res.tbl[0].r[0].reportHtml;
                var oPrntWin = window.open("", "_blank", "width=1100,height=1140,left=30,top=30,menubar=yes,toolbar=no,location=no,scrollbars=yes");
                oPrntWin.document.open();
                oPrntWin.document.write("<!doctype html><html><head><title>Print</title></head><body onload=\"print();\">" + rhtml + "</body></html>");
                oPrntWin.document.close();
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}


function subModuleFormShowListeners() {
    $(document).on("click", '.apd-subm-attr-button', function (e) {
        var insCmCode = '-1';
        var sesId = $('.apd-table-checkbox:checked').val();
        if (typeof sesId === 'undefined' || !sesId) {
            alert(getMessage("sessionIsNotSelected"));
            return;
        }

        var el = $(e.target);
        var smodule_id = el.attr("submodule_id");
        if (typeof smodule_id === 'undefined' || !smodule_id) {
            alert(getMessage("submoduleIsNotSelected"));
            return;
        }

        json = {kv: {}};
        json.kv.fkSubmoduleId = smodule_id;
        json.kv.fkSessionId = sesId;
        var data = JSON.stringify(json);
        $.ajax({
            url: "api/post/srv/serviceCrGetSubmoduleFormBody",
            type: "POST",
            data: data,
            contentType: "application/json",
            crossDomain: true,
            async: false,
            success: function (res) {
                isResultRedirect(JSON.stringify(res));
                var body = res.kv.body;
                var hd = res.kv.header;
                if (insCmCode === '-1') {
                    $('#modal-insert-title-name').text(hd);
                    $('#form-insert-element-body').html(body);
                    $('#form-insert-element-body').find(".apd-form-select").each(function () {
                        fillCombobox(this);
                    });
                    $('#form-insert-element-body').find(".apd-form-select-manual").each(function () {
                        var has_other = $(this).attr('has_other');
                        if (typeof has_other === 'undefined' || !has_other) {
                            has_other = '0';
                        }
                        if (has_other === '1') {
                            $(this).append($("<option />").val("__2__").text("Other"));
                        }
                        $(this).addClass('selectpicker');
                        $(this).attr("data-show-subtext", "true");
                        $(this).attr("data-live-search", "true");
                        $(this).selectpicker('refresh');
                    });
                    $('#form-insert-element-body').find(".apd-form-multiselect").each(function () {
                        $(this).multiselect('destroy');
                        fillCombobox(this);
                        $(this).multiselect(
                                {includeSelectAllOption: true,
                                    enableFiltering: true,
                                    selectAllJustVisible: true
                                }
                        );
                    });
                    $('#form-insert-element-body').find(".apd-form-multiselect-manual").each(function () {
                        $(this).multiselect('destroy');
                        $(this).multiselect(
                                {includeSelectAllOption: true,
                                    enableFiltering: true,
                                    selectAllJustVisible: true
                                }
                        );
                    });
                    var res = getInspectionInfo(sesId, smodule_id);
                    setSubmoduleUpdateFormValues(res);
                }
            },
            error: function (res, status) {
                alert(getMessage('somethingww'));
            }
        });
    });
}

function getInspectionInfo(fkSessionId, fkSubmoduleId) {
    if (!fkSessionId || !fkSubmoduleId) {
        return '';
    }
    var r = "";
    var json = {kv: {}};
    json.kv.fkSessionId = fkSessionId;
    json.kv.fkSubmoduleId = fkSubmoduleId;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/srv/serviceCrGetInspectionListBySession",
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            r = res;
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
    return r;
}

function setSubmoduleUpdateFormValues(res) {
//birinci gelen table goturulur. susmaya gore cedvel 
//adi Response olmalidir
//get key of each element. Output is array of strings
    var obj = res.tbl[0].r;
    //set form input values
    for (var i = 0; i < obj.length; i++) {
//        var sid = obj[i].saSubmoduleAttributeId;
        var sid = 'sa_' + obj[i].fkSubmoduleAttributeId;
        var haid = 'ha_' + obj[i].fkSubmoduleAttributeId;
        var hav = obj[i].haInspectionValue;
        var v = obj[i].inspectionValue;
        try {
            $('#popup1').find('input[id=' + sid + ']').val(v);
            $('#popup1').find('input[id=' + haid + ']').val(hav);
        } catch (err) {
//fileuploader ucun istifade edilir
            $('#popup1').find('input[id=' + sid + ']').attr("file_value", v);
        }
        $('#popup1').find('textarea[id=' + sid + ']').val(v);
        try {
            $('#popup1').find('select[id=' + sid + ']').
                    find('option[value=' + v + ']').attr("selected", "selected");
            $('#popup1').find('select[id=' + sid + ']').change();
            $('.selectpicker').selectpicker('refresh');
        } catch (err) {
//multiselect ucun
            var arr = v.split("|");
            $('#popup1').find('select[id=' + sid + ']').
                    multiselect('select', arr);
        }
    }
}

