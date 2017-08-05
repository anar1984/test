/* global per_page, g_tbl, global_var */

var heeden_table_coloumn = ["id", "status", "insertDate", "modificationDate"];

function changePageNumberOfTable(perPage, rowCount, currentPageNumber, parentDiv) {
    //add page count select
    var s = $('<select></select>');
    //add page number to select
    var a = Math.ceil(rowCount / perPage);
    for (var k = 1; k <= a; k++) {
        var isSelected = "";
        if (k == currentPageNumber) {
            isSelected = "selected";
        }
        var st = "<option value=" + k + " " + isSelected + ">" + k + "</option>";
        s.append(st);
    }
    parentDiv.find(".table-filter-comp[name=page_count]").html(s.html());
}

function sortTable(tablename, colnumber) {
    var n = colnumber;
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById(tablename);
    switching = true;
    //Set the sorting direction to ascending:
    dir = "asc";
    /*Make a loop that will continue until
     no switching has been done:*/
    while (switching) {
//start by saying: no switching is done:
        switching = false;
        rows = table.getElementsByTagName("TR");
        /*Loop through all table rows (except the
         first, which contains table headers):*/
        for (i = 3; i < (rows.length - 1); i++) {
//start by saying there should be no switching:
            shouldSwitch = false;
            /*Get the two elements you want to compare,
             one from current row and one from the next:*/
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            /*check if the two rows should switch place,
             based on the direction, asc or desc:*/
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
//if so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
//if so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            /*If a switch has been marked, make the switch
             and mark that a switch has been done:*/
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            //Each time a switch is done, increase this count by 1:
            switchcount++;
        } else {
            /*If no switching has been done AND the direction is "asc",
             set the direction to "desc" and run the while loop again.*/
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

function toggleTableFilter(tableId) {
    $('#' + tableId).find('.table-filter').toggle();
    $('.table-filter-combo').multiselect(
            {includeSelectAllOption: true,
                enableFiltering: true,
                selectAllJustVisible: true}
    );
    $('.table-filter-combo').first().change();
}

function clearTableFilter(tableId) {
    $('#' + tableId).find('.table-filter').find(":input").each(function () {
        $(this).val('');
    });
}

function createTableDivHTML(data, var_tablename) {


    var mainDiv = $('<div></div>');
    mainDiv.addClass("custom-table")
            .attr("srv_url", g_tbl[var_tablename].list_url)
            .attr("global_var", var_tablename);

    var pageDivPerPage = $('<div></div>');
    pageDivPerPage.addClass("col-md-1 text-left");

    //add per page select
    var select_per_page = $('<select></select>')
            .addClass("table-per-page")
            .addClass("table-filter-comp")
            .attr("name", "per_page")
            .attr("style", "height: 22px;padding: 1px 1px;font-size: 12px;")
            .addClass("form-control");

    for (var i = 0; i < per_page.length; i++) {
        select_per_page.append($("<option />").val(per_page[i]).text(per_page[i]));
    }
    pageDivPerPage.append(select_per_page);
    mainDiv.append(pageDivPerPage);

    var pageDivPageNumber = $('<div></div>');
    pageDivPageNumber.addClass("col-md-1 text-left");

    //add page count select
    var select_page_count = $('<select></select>')
            .addClass("table-page-number")
            .addClass("table-filter-comp")
            .addClass("form-control")
            .attr("style", "height: 22px;padding: 1px 1px;font-size: 12px;")
            .attr("name", "page_count");

    //add page number to select
    var a = Math.ceil(data.rowCount /
            global_var.default_per_page);
    for (var k = 1; k <= a; k++) {
        select_page_count.append($("<option />").
                val(k).text(k));
    }
    pageDivPageNumber.append(select_page_count);
    mainDiv.append(pageDivPageNumber);

    var divButton = getTableButtonHtml(var_tablename);
    mainDiv.append(divButton);

    var tblDiv = $('<div></div>');
    tblDiv.addClass('col-md-12');

    var st = createPureTableHtml(var_tablename, data);
    tblDiv.append(st);
    mainDiv.append(tblDiv);

    var tmpDiv = $('<div></div>');
    tmpDiv.append(mainDiv);
    return tmpDiv.html();
}

function loadTable(var_tablename, data) {
    var list_url = g_tbl[var_tablename].list_url;
    var sourceId = g_tbl[var_tablename].result_div_id;
    var tablename = g_tbl[var_tablename].response_tn;

    if (!list_url) {
        return;
    }

    if (!tablename) {
        tablename = "Response";
    }

    var json;
    if (data) {
        json = data;
        if (data.kv.matrixId) {
            g_tbl[var_tablename]['matrixId'] = data.kv.matrixId;
        }
    } else {
        json = {kv: {}};
    }


    json.kv['startLimit'] = 0;
    json.kv['endLimit'] = global_var.default_per_page;
    var data = JSON.stringify(json);
    $.ajax({
        url: "api/post/" + list_url,
        type: "POST",
        data: data,
        contentType: "application/json",
        crossDomain: true,
        async: false,
        success: function (res) {
            isResultRedirect(JSON.stringify(res));
            var obj = res.tbl;
            for (var i = 0; i < obj.length; i++) {
                var tn = obj[i]['tn'];
                if (tn === tablename) {
                    var dhtml = createTableDivHTML(obj[i], var_tablename)
                    $('#' + sourceId).html(dhtml);
                    $('#' + sourceId).show();
                    break;
                }
            }
//            if (var_tablename === 'tbl_inspection_list') {
            $(".youtube").YouTubeModal({autoplay: 0, width: 640, height: 480});
//            } 
        },
        error: function (res, status) {
            alert(getMessage('somethingww'));
        }
    });
}

function getTableButtonHtml(var_tablename) {
    var mainDiv = $('<div></div>');
    mainDiv.addClass("col-md-10 text-right")

    var btnFilter = $('<button></button>');
    btnFilter.addClass("btn btn-sm task-button");
    btnFilter.attr("type", "button");
    btnFilter.attr("onclick", "toggleTableFilter('" + var_tablename + "')");
    btnFilter.html("<i class=\"fa fa-filter\" aria-hidden=\"true\"></i>");
    mainDiv.append(btnFilter);
    mainDiv.append("\n");

    var btnFilterClr = $('<button></button>');
    btnFilterClr.addClass("btn btn-sm task-button");
    btnFilterClr.attr("type", "button");
    btnFilterClr.attr("onclick", "clearTableFilter('" + var_tablename + "')");
    btnFilterClr.html("<i class=\"fa fa-archive\" aria-hidden=\"true\"></i>");
    mainDiv.append(btnFilterClr);
    mainDiv.append("\n");

    var btnPrint = $('<button></button>');
    btnPrint.addClass("btn btn-sm task-button")
            .attr("type", "button")
            .attr("onclick", "printTableData('" + var_tablename + "')")
            .html("<i class=\"fa fa-print\" aria-hidden=\"true\"></i>");
    mainDiv.append(btnPrint).append("\n");

    var btnExcel = $('<button></button>')
            .addClass("btn btn-sm task-button")
            .attr("type", "button")
            .attr("onclick", "exportToExcel('" + var_tablename + "')")
            .html("<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\"></i>");
    mainDiv.append(btnExcel)
            .append("\n");
    var t = $('<div></div>');
    t.append(mainDiv);
    return t.html();
}

function createPureTableHtml(var_tablename, data) {
    var withTblBorder = g_tbl[var_tablename].table_block;
    var t_n = g_tbl[var_tablename].response_tn;
    var matrixId = (g_tbl[var_tablename].matrixId) ? g_tbl[var_tablename].matrixId : "";

    var tblBorder = "";
    if (withTblBorder === "1") {
        tblBorder = "display:block;";
    }

    var div = $('<div></div>');
    var table = $('<table></table>');
    table.attr("id", var_tablename)
            .attr("class", "table  table-striped  table-bordered")
            .attr("align", "center")
            .attr("style", "table-layout: auto; " + tblBorder + "overflow-x: auto;");
    if (matrixId.length > 0) {
        table.attr("matrixId", matrixId);
    }

    var theader = $('<thead></thead>');

    var tr4filter = $('<tr></tr>')
            .addClass("table-filter")
            .attr("hidden", "");
    //add column for trigger events

    tr4filter.append('<th class="apd-table-td"><input type="text"  class="smt form-control table-filter-comp hidden" id="trigger" name="trigger" ></th>');
    tr4filter.append('<th class="apd-table-td"><input type="text"  class="smt form-control table-filter-comp hidden" id="trigger" name="trigger" ></th>');
    tr4filter.append('<th class="apd-table-td"><input type="text"  class="smt form-control table-filter-comp hidden" id="trigger" name="trigger" ></th>');
    tr4filter.append('<th class="apd-table-td"><input type="text"  class="smt form-control table-filter-comp hidden" id="trigger" name="trigger" ></th>');
    tr4filter.append('<th class="apd-table-td"><input type="text"  class="smt form-control table-filter-comp hidden" id="trigger" name="trigger" ></th>');
    //get column names

    var tr4head = $('<tr></tr>');
    tr4head.addClass("table-header");
    //add column for trigger events
    var ckb = '<input type="checkbox" class="apd-table-checkbox-all" name="' + t_n + '">';
    tr4head.append('<th style="width:2px">' + ckb + '</th>')
            .append('<th style="width:2px"></th>')
            .append('<th style="width:2px"></th>')
            .append('<th style="width:2px"></th>')
            .append('<th style="width:2px">No</th>');

    var colPair = {};
    var colType = {};
    for (var n = 0; n < data.c.length; n++) {
        colPair[data.c[n].i] = data.c[n].n;
        colType[data.c[n].i] = data.c[n].t;
    }

    var sequence = data.seq;
    var seqArr = sequence.split(",");

    var sortno = 5;
    for (var j = 0; j < seqArr.length; j++) {
        var th = $('<th class="apd-table-td" nowrap></th>');
        var cid = seqArr[j].trim();
        var cname = colPair[cid];
        var ctype = colType[cid];

        //if colomn is in the heeden_table_coloumn the continue
        if (heeden_table_coloumn.indexOf(cid) > -1) {
            continue;
        }

        var input = "";

        if (ctype === 'DATE__') {
            input = $('<select></select>')
                    .addClass("form-control table-filter-combo")
                    .attr("id", cid)
                    .attr("name", cid)
                    .attr("multiple", "multiple");

            for (var k = 1; k <= 6; k++) {
                var st = "<option value=" + k + ">" + k + "</option>";
                input.append(st);
            }
        } else {
            input = "<input type=\"text\"  class=\"form-control " +
                    " fa fa-search table-filter-comp\" " +
                    "id=\"" + cid + "\"" +
                    "  style=\"height: 22px;padding: 1px 1px;font-size: 12px;\"" +
                    " name=\"" + cid + "\">";
        }
        th.append(input);
        tr4filter.append(th);

        var thh = $('<th class="apd-table-td"></th>');
        thh.append(cname);
        thh.attr("onclick", "sortTable('" + var_tablename + "'," + sortno + ")");
        thh.attr('nowrap', 'true');

        sortno++;
        tr4head.append(thh);
    }
    theader.append(tr4filter);
    theader.append(tr4head);


    // footer info;
    var tfooter = $('<tfoot></tfoot>');
    var trfooter = $('<tr></tr>');
    trfooter.append('<td></td>').append('<td></td>').append('<td></td>')
            .append('<td></td>').append('<td></td>');
    var f_val = {};
    var f_val_c = {};

    //get body values
    var body = $('<tbody></tbody>');
    var objChildBody = data['r'];



    for (var j = 0; j < objChildBody.length; j++) {
        //get key of each element. Output is array of strings
//        var keys = Object.keys(objChildBody[j]);
        var keys = sequence.split(",");

        //get value and craeate body.
        var tr = $('<tr></tr>');

        //create trigger events for update copy and delete
        var row_id = objChildBody[j]['id'];
        var ckbHTML = getTableCheckBoxTriggerEventButtonHtml(var_tablename, row_id);
        var updatHTML = getTableUpdateTriggerEventButtonHtml(var_tablename, row_id);
        var copyHTML = getTableCopyTriggerEventButtonHtml(var_tablename, row_id);
        var deleteHTML = getTableDeleteTriggerEventButtonHtml(var_tablename, row_id);

        var td1 = $('<td class="apd-table-td" nowrap></td>');
        var td2 = $('<td class="apd-table-td" nowrap></td>');
        var td3 = $('<td class="apd-table-td" nowrap></td>');
        var td4 = $('<td class="apd-table-td" nowrap></td>');
        var td5 = $('<td class="apd-table-td" nowrap></td>');

        var ln = createTriggerRowHTML(var_tablename, objChildBody[j]['id']);
        td1.html(ckbHTML);
        td2.html(deleteHTML);
        td3.html(updatHTML);
        td4.html(copyHTML);
        td5.html((j + 1));
        tr.append(td1).append(td2).append(td3).append(td4).append(td5);

        for (var i = 0; i < keys.length; i++) {
            if (heeden_table_coloumn.indexOf(keys[i]) > -1) {
                continue;
            }
            var td = $('<td class="apd-table-td" align="center" valign="center"></td>');
            td.addClass('apd-table-td');
            td.html(objChildBody[j][keys[i]]);

            //get sum by column
            var ctype = colType[keys[i]];
            var first2Digits = keys[i].substring(0, 2);
            var trdDigit = keys[i].substring(2, 3);
            var isInt = parseInt(trdDigit);

            if (ctype === 'INTEGER' || (first2Digits === 'sa' && !isNaN(isInt))) {
                var valFloat = parseFloat(objChildBody[j][keys[i]]);
                if (!isNaN(valFloat)) {
                    var oldVal = (f_val[keys[i]]) ? f_val[keys[i]] : '0';
                    var newVal = parseFloat(oldVal) + parseFloat(valFloat);
                    f_val[keys[i]] = newVal;
                    f_val_c[keys[i]] = (f_val_c[keys[i]]) ? parseInt(f_val_c[keys[i]]) + 1 : 1;
                }
            }
            tr.append(td);
        }
        body.append(tr);
    }

    for (var i = 0; i < keys.length; i++) {
        if (heeden_table_coloumn.indexOf(keys[i]) > -1) {
            continue;
        }
        var fval = (f_val[keys[i]]) ? f_val[keys[i]] : '';
        var ave = (f_val[keys[i]]) ? parseFloat(fval) / f_val_c[keys[i]] : '';
        ave = Math.round(ave * 10000) / 10000;
        ave = ave === 0 ? '' : ave;

        var ln = '<font color="red"><b>' + fval + '</b></font>';
        ln += '<br><font color="blue"><b>' + ave + '</b></font>';
        var td = $('<td class="apd-table-td" align="center" valign="center"></td>');
        td.addClass('apd-table-td');
        td.html(ln);
        trfooter.append(td);
    }
    tfooter.append(trfooter);

    table.append(tfooter);
    table.append(theader);
    table.append(body);
    div.append(table);
    return div.html();
}

function createTriggerRowHTML(var_tablename, row_id) {
//    
    var ckbHTML = getTableCheckBoxTriggerEventButtonHtml(var_tablename, row_id);
    var updatHTML = getTableUpdateTriggerEventButtonHtml(var_tablename, row_id);
    var copyHTML = getTableCopyTriggerEventButtonHtml(var_tablename, row_id);
    var deleteHTML = getTableDeleteTriggerEventButtonHtml(var_tablename, row_id);
    var ln = ckbHTML + " | " + deleteHTML + " | " + updatHTML + " | " + copyHTML;
    return ln;
}

function getTableCheckBoxTriggerEventButtonHtml(var_tablename, row_id) {
    var checkboxHTML = '';
    checkboxHTML = '<input type="checkbox" ' +
            ' class="apd-table-checkbox" ' +
            ' name=\' ' + g_tbl[var_tablename].response_tn + ' \' ' +
            ' value =\'' + row_id + ' \'>';
    return checkboxHTML;
}

function getTableUpdateTriggerEventButtonHtml(var_tablename, row_id) {
    var lnUpd = '';
    lnUpd += '<i class="apd-task-form-fill fa fa-edit" style="color:#00b289; cursor:pointer"';
    lnUpd += ' apd-form-fill-url="' + g_tbl[var_tablename].list_url
            + '" apd-form-fill-kv="id=' + row_id + '"';
    lnUpd += ' data-toggle="modal" data-target="#' +
            g_tbl[var_tablename].form_update_popup_id + '" aria-hidden="true"></i>';
    return lnUpd;
}

function getTableCopyTriggerEventButtonHtml(var_tablename, row_id) {
    var lnUpd = '';
    lnUpd += '<i class="apd-task-form-fill fa fa-copy" style="color:#41c4f4;cursor:pointer"';
    lnUpd += ' apd-form-fill-url="' + g_tbl[var_tablename].list_url +
            '" apd-form-fill-kv="id=' + row_id + '"';
    lnUpd += ' data-toggle="modal" data-target="#' +
            g_tbl[var_tablename].form_copy_popup_id + '" aria-hidden="true"></i>';
    return lnUpd;
}

function getTableDeleteTriggerEventButtonHtml(var_tablename, row_id) {
    var lnUpd = '';
    lnUpd += '<i class="apd-task-trigger fa fa-remove" style=" color:red;cursor:pointer"';
    lnUpd += ' apd-form-fill-url="' + g_tbl[var_tablename].delete_url + '" apd-form-fill-kv="id=' + row_id + '"';
    lnUpd += ' data-toggle="modal"  ' + ' aria-hidden="true"  ';
    lnUpd += ' apd-form-reload-button-id="' + g_tbl[var_tablename].reload_buttion_id + '"></i>';
    return lnUpd;
}




function getTableFilterData(tableId) {
    var json = {kv: {}};
    $('#' + tableId).find('.table-filter').find(":input").each(function () {
        if ($(this).val()) {
            json.kv[$(this).attr("name")] = $(this).val();
        }
    });
    return json;
}

function fillTableBodyByid(var_tablename, data, start_ind) {
    var colType = {};
    for (var n = 0; n < data.c.length; n++) {
        colType[data.c[n].i] = data.c[n].t;
    }

    // footer info;

    var f_val = {};
    var f_val_c = {};


    if (!start_ind) {
        start_ind = 0;
    }
    var colnames = [];
    var i = 0;
    $('#' + var_tablename).find('.table-filter').find('.table-filter-comp,.table-filter-combo').each(function () {
        colnames[i++] = $(this).attr('name');
    });

    var body = $('<tbody></tbody>');
    var objChildBody = data['r'];
    for (var j = 0; j < objChildBody.length; j++) {
        //get value and craeate body.
        var tr = $('<tr></tr>');

        //add trigger line
        var row_id = objChildBody[j]['id'];
        var ckbHTML = getTableCheckBoxTriggerEventButtonHtml(var_tablename, row_id);
        var updatHTML = getTableUpdateTriggerEventButtonHtml(var_tablename, row_id);
        var copyHTML = getTableCopyTriggerEventButtonHtml(var_tablename, row_id);
        var deleteHTML = getTableDeleteTriggerEventButtonHtml(var_tablename, row_id);

//        var ln = createTriggerRowHTML(var_tablename, objChildBody[j]['id']);
        var td1 = $('<td nowrap></td>').html(ckbHTML);
        var td2 = $('<td nowrap></td>').html(deleteHTML);
        var td3 = $('<td nowrap></td>').html(updatHTML);
        var td4 = $('<td nowrap></td>').html(copyHTML);
        tr.append(td1).append(td2).append(td3).append(td4);

        //add row number
        var td5 = $('<td nowrap></td>');
        td5.html((start_ind + j + 1));
        tr.append(td5);

        //add body cell
        for (var i = 0; i < colnames.length; i++) {
            if (colnames[i] === 'trigger') {
                continue;
            }
            var td = $('<td></td>');
            td.addClass("apd-table-td");
            td.html(objChildBody[j][colnames[i]]);
            tr.append(td);

            //get sum by column
            var ctype = colType[colnames[i]];
            var first2Digits = colnames[i].substring(0, 2);
            var trdDigit = colnames[i].substring(2, 3);
            var isInt = parseInt(trdDigit);


            if (ctype === 'INTEGER' || (first2Digits === 'sa' && !isNaN(isInt))) {
                var valFloat = parseFloat(objChildBody[j][colnames[i]]);

                if (!isNaN(valFloat)) {

                    var oldVal = (f_val[colnames[i]]) ? f_val[colnames[i]] : '0';
                    var newVal = parseFloat(oldVal) + parseFloat(valFloat);
                    f_val[colnames[i]] = newVal;
                    f_val_c[colnames[i]] = (f_val_c[colnames[i]]) ? parseInt(f_val_c[colnames[i]]) + 1 : 1;
                }
            }
        }
        body.append(tr);
    }


    var tfooter = $('<tfoot></tfoot>');
    var trfooter = $('<tr></tr>');
    for (var i = 0; i < colnames.length; i++) {

        var fval = (f_val[colnames[i]]) ? f_val[colnames[i]] : '';
        var ave = (f_val[colnames[i]]) ? parseFloat(fval) / f_val_c[colnames[i]] : '';
        ave = Math.round(ave * 10000) / 10000;
        ave = ave === 0 ? '' : ave;

        var ln = '<font color="red"><b>' + fval + '</b></font>';
        ln += '<br><font color="blue"><b>' + ave + '</b></font>';
        var td = $('<td class="apd-table-td" align="center" valign="center"></td>');
        td.addClass('apd-table-td');
        td.html(ln);
        trfooter.append(td);
    }
    tfooter.append(trfooter);

//set html value back to the table div
    $('#' + var_tablename).find('tfoot').html(tfooter.html());
    $('#' + var_tablename).find('tbody').html(body.html());
    $(".youtube").YouTubeModal({autoplay: 0, width: 640, height: 480}); 
}


function printTableData(tableId, colStartInd, rowStartInd) {
    if (!colStartInd)
        colStartInd = 4;
    if (!rowStartInd)
        rowStartInd = 0;

    var res = getTableHtmlForPrintAndExcel(tableId, colStartInd, rowStartInd);

    newWin = window.open("");
    newWin.document.write(res);
    newWin.print();
    newWin.close();
}

function getTableHtmlForPrintAndExcel(tableId, colStartInd, rowStartInd) {
    var div = $('<div></div>');

    var table = $('<table></table>');
    table.attr("style", "border-collapse:collapse; width:100%;");
    table.attr("border", "1px");

    var rhc = 0;
    $('#' + tableId + ' thead tr').each(function () {
        if (rhc < 1) {
            rhc++;
            return true;
        }
        var tr = $('<tr></tr>');
        var cc = 0;
        $(this).find('th').each(function () {
            if (cc < colStartInd) {
                cc++;
                return true;
            }

            var td = $('<th ></th>');
            td.attr("style", "word-wrap:break-word;\n\
                         max-width:160px; styleborder: 1px; text-align: center;");

            td.html($(this).html());
            tr.append(td);
        });
        table.append(tr);
    });

    var rc = 0;
    $('#' + tableId + ' tbody tr').each(function () {
        if (rc < rowStartInd) {
            rc++;
            return true;
        }

        var tr = $('<tr></tr>');
        var cc = 0;
        $(this).find('td').each(function () {
            if (cc < colStartInd) {
                cc++;
                return true;
            }

            var td = $('<td></td>');
            td.attr("style", "word-wrap:break-word;\n\
                     max-width:160px; styleborder: 1px; text-align: center;");

            td.html($(this).html());
            tr.append(td);
        });
        table.append(tr);
    });
    div.append(table);
    return div.html();
}

function getTableHtmlForExcel(tableId, colStartInd, rowStartInd) {
    var div = $('<div></div>');

    var table = $('<table></table>');


    var rhc = 0;
    $('#' + tableId + ' thead tr').each(function () {
        if (rhc < 1) {
            rhc++;
            return true;
        }
        var tr = $('<tr></tr>');
        var cc = 0;
        $(this).find('th').each(function () {
            if (cc < colStartInd) {
                cc++;
                return true;
            }

            var td = $('<td></td>');

            td.html($(this).html());
            tr.append(td);
        });
        table.append(tr);
    });

    var rc = 0;
    $('#' + tableId + ' tbody tr').each(function () {
        if (rc < rowStartInd) {
            rc++;
            return true;
        }

        var tr = $('<tr></tr>');
        var cc = 0;
        $(this).find('td').each(function () {
            if (cc < colStartInd) {
                cc++;
                return true;
            }

            var td = $('<td></td>');

            td.html($(this).html());
            tr.append(td);
        });
        table.append(tr);
    });
    div.append(table);
    return div.html();
}

function exportToExcel(tableId) {
    var data_type = 'data:application/vnd.ms-excel';
    var table_div = document.getElementById(tableId);
    var table_html = table_div.outerHTML.replace(/ /g, '%20');

    var html = getTableHtmlForExcel(tableId, 1, 0);

    var a = document.createElement('a');
    a.href = data_type + ', ' + html;
    a.download = 'exported_table_' + Math.floor((Math.random() * 9999999) + 1000000) + '.xls';
    a.click();
}

