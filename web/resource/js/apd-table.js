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

function sortTable(table, col, reverse) {
    var tb = document.getElementById(table).tBodies[0], // use `<tbody>` to ignore `<thead>` and `<tfoot>` rows
            tr = Array.prototype.slice.call(tb.rows, 0), // put rows into array
            i;
    reverse = -((+reverse) || -1);
    tr = tr.sort(function (a, b) { // sort rows
        if (isDate(a.cells[col].textContent) && isDate(b.cells[col].textContent)) {
            var d1 = convertDate(a.cells[col].textContent);
            var d2 = convertDate(b.cells[col].textContent);
            var r = reverse // `-1 *` if want opposite order
                    * (d1.trim() // using `.textContent.trim()` for test
                            .localeCompare(d2.trim())
                            );
            return r;
        }
        if (!isNaN(a.cells[col].textContent) && !isNaN(b.cells[col].textContent))
            return reverse * ((+a.cells[col].textContent) - (+b.cells[col].textContent));
        return reverse // `-1 *` if want opposite order
                * (a.cells[col].textContent.trim() // using `.textContent.trim()` for test
                        .localeCompare(b.cells[col].textContent.trim())
                        );
    });
    for (i = 0; i < tr.length; ++i) {
        tr[i].cells[4].textContent = (i + 1);
        tb.appendChild(tr[i]); // append each row in order

    }
}

function isDate(d) {
    var r = (d.length === 10 && d.substring(2, 3) === '.' && d.substring(5, 6) === '.')
            ? true : false;
    return r;
}

function convertDate(date) {
    date = date.substring(6, 10) + date.substring(3, 5) + date.substring(0, 2);
//    var pattern = /(\d{2})\.(\d{2})\.(\d{4})/;
//    var dt = new Date(st.replace(pattern, '$3-$2-$1'));
    return date;
}

function sortTable1(tablename, colnumber) {
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
//            if (switchcount == 0 && dir == "asc") {
//                dir = "desc";
//                switching = true;
//            }
        }
    }
}

function toggleTableFilter(tableId) {
    $('#' + tableId).find('.table-filter').toggle();
//    $('.table-filter-combo').multiselect(
//            {includeSelectAllOption: true,
//                enableFiltering: true,
//                selectAllJustVisible: true}
//    );
//    $('.table-filter-combo').first().change();
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
    var pageDivPerPage = $('<div></div>')
            .addClass("apd-tbl-nav")
            .attr("style", "border: 1px solid #ccc;background-color:#ddd;");
    var lblFirst = $('<label></label>')
            .addClass("apd-span-nav apd-tbl-nav-frst hidden ")
            .html('<i class="fa fa-angle-double-left" \n\
                    are-hidden="true" style="font-size: 15px"></i>');
    var lblPrvs = $('<label></label>')
            .addClass("apd-span-nav apd-tbl-nav-prvs")
            .html('<i class="fa fa-angle-left" \n\
                    are-hidden="true" style="font-size: 15px"></i>');
    var lblNxt = $('<label></label>')
            .addClass("apd-span-nav apd-tbl-nav-nxt")
            .html('<i class="fa fa-angle-right" \n\
                    are-hidden="true" style="font-size: 15px"></i>');
    var lblLst = $('<label></label>')
            .addClass("apd-span-nav apd-tbl-nav-lst hidden")
            .html('<i class="fa fa-angle-double-right" \n\
                    are-hidden="true" style="font-size: 15px"></i>');
    //add per page select
    var select_per_page = $('<select></select>')
            .addClass("table-per-page apd-table-combo")
            .attr("name", "per_page");
    for (var i = 0; i < per_page.length; i++) {
        select_per_page.append($("<option />").val(per_page[i]).text(per_page[i]));
    }

//add columns names
    var colUl = $('<ul></ul>').addClass("apd-table-ul");
    var colLi = $('<li></li>');
    var childUl = $('<ul></ul>').addClass("apd-table-ul");
    var colPair = {};
    var colType = {};
    for (var n = 0; n < data.c.length; n++) {
        colPair[data.c[n].i] = data.c[n].n;
        colType[data.c[n].i] = data.c[n].i;
    }

    var sequence = data.seq;
    var seqArr = sequence.split(",");
    for (var n = 0; n < seqArr.length; n++) {
        var childLi = $('<li></li>').addClass("apd-table-li");
        var ln = '<input type="checkbox" class="apd-table-cols" checked';
        ln += ' cn="_c' + n + '" cnm="' + colType[seqArr[n]] +
                '" cntitle="' + colPair[seqArr[n]] + '"> ';
        ln += colPair[seqArr[n]];
        childLi.html(ln);
        childUl.append(childLi);
    }

    var sp = $('<span></ul>').html("&nbsp;" + getMessage("columnName"));
    colLi.append(sp);
    colLi.append(childUl);
    colUl.append(colLi);
    var lblSpc = $('<label></label>').addClass("apd-span-space").html('|');
    var lblClrTbl = $('<label></label>').addClass("apd-span-nav")
            .attr("onclick", "toggleTableFilter('" + var_tablename + "')")
            .html('<i class="fa fa-filter" aria-hidden="true" ></i>');
    var lblFltrTbl = $('<label></label>').addClass("apd-span-nav")
            .attr("onclick", "clearTableFilter('" + var_tablename + "')")
            .html('<i class="fa fa-eraser" aria-hidden="true" ></i>');
    var lblSrchTbl = $('<label></label>')
            .addClass("apd-span-nav apd-table-general-filter")
            .html('<i class="fa fa-search" aria-hidden="true" ></i>');
    var lblPrntTbl = $('<label></label>').addClass("apd-span-nav")
            .attr("onclick", "printTableData('" + var_tablename + "')")
            .html('<i class="fa fa-print" aria-hidden="true" ></i>');
    var lblExcelTbl = $('<label></label>').addClass("apd-span-nav")
            .attr("onclick", "exportToExcel('" + var_tablename + "')")
            .html('<i class="fa fa-file-excel-o" aria-hidden="true" ></i>');
    pageDivPerPage.append(lblFirst);
    pageDivPerPage.append(lblPrvs);
    pageDivPerPage.append(select_per_page);
    pageDivPerPage.append(lblNxt);
    pageDivPerPage.append(lblLst);
    pageDivPerPage.append(colUl);
    pageDivPerPage.append(lblSpc);
    pageDivPerPage.append(lblClrTbl);
    pageDivPerPage.append(lblFltrTbl);
    pageDivPerPage.append(lblSrchTbl);
    pageDivPerPage.append(lblPrntTbl);
    pageDivPerPage.append(lblExcelTbl);
    mainDiv.append(pageDivPerPage);
    var tblDiv = $('<div></div>');
    tblDiv.addClass('col-md-12');
    tblDiv.attr("style", "background-color:#e5e5e5;padding:0px");
    var st = createPureTableHtml(var_tablename, data);
    tblDiv.append(st);
    mainDiv.append(tblDiv);
    var tmpDiv = $('<div></div>');
    tmpDiv.append(mainDiv);
    return tmpDiv.html();
}


//function createTableDivHTML(data, var_tablename) {
//
//
//    var mainDiv = $('<div></div>');
//    mainDiv.addClass("custom-table")
//            .attr("srv_url", g_tbl[var_tablename].list_url)
//            .attr("global_var", var_tablename);
//
//    var pageDivPerPage = $('<div></div>');
//    pageDivPerPage.addClass("col-md-1 text-left");
//
//    //add per page select
//    var select_per_page = $('<select></select>')
//            .addClass("table-per-page")
//            .addClass("table-filter-comp")
//            .attr("name", "per_page")
//            .attr("style", "height: 22px;padding: 1px 1px;font-size: 12px;")
//            .addClass("form-control");
//
//    for (var i = 0; i < per_page.length; i++) {
//        select_per_page.append($("<option />").val(per_page[i]).text(per_page[i]));
//    }
//    pageDivPerPage.append(select_per_page);
//    mainDiv.append(pageDivPerPage);
//
//    var pageDivPageNumber = $('<div></div>');
//    pageDivPageNumber.addClass("col-md-1 text-left");
//
//    //add page count select
//    var select_page_count = $('<select></select>')
//            .addClass("table-page-number")
//            .addClass("table-filter-comp")
//            .addClass("form-control")
//            .attr("style", "height: 22px;padding: 1px 1px;font-size: 12px;")
//            .attr("name", "page_count");
//
//    //add page number to select
//    var a = Math.ceil(data.rowCount /
//            global_var.default_per_page);
//    for (var k = 1; k <= a; k++) {
//        select_page_count.append($("<option />").
//                val(k).text(k));
//    }
//    pageDivPageNumber.append(select_page_count);
//    mainDiv.append(pageDivPageNumber);
//
//    var divButton = getTableButtonHtml(var_tablename);
//    mainDiv.append(divButton);
//
//    var tblDiv = $('<div></div>');
//    tblDiv.addClass('col-md-12');
//    tblDiv.attr("style", "background-color:#e5e5e5;padding:0px");
//
//    var st = createPureTableHtml(var_tablename, data);
//    tblDiv.append(st);
//    mainDiv.append(tblDiv);
//
//    var tmpDiv = $('<div></div>');
//    tmpDiv.append(mainDiv);
//    return tmpDiv.html();
//}

//function loadTable(var_tablename, data) {
//    var list_url = g_tbl[var_tablename].list_url;
//    var sourceId = g_tbl[var_tablename].result_div_id;
//    var tablename = g_tbl[var_tablename].respableonse_tn;
//
//    if (!list_url) {
//        return;
//    }
//
//    if (!tablename) {
//        tablename = "Response";
//    }
//
//    var json;
//    if (data) {
//        json = data;
//        if (data.kv.matrixId) {
//            g_tbl[var_tablename]['matrixId'] = data.kv.matrixId;
//        }
//    } else {
//        json = {kv: {}};
//    }
//
//
//    json.kv['startLimit'] = 0;
//    json.kv['endLimit'] = global_var.default_per_page;
//    var data = JSON.stringify(json);
//    $.ajax({
//        url: "api/post/" + list_url,
//        type: "POST",
//        data: data,
//        contentType: "application/json",
//        crossDomain: true,
//        async: false,
//        success: function (res) {
//            isResultRedirect(JSON.stringify(res));
//            var obj = res.tbl;
//            for (var i = 0; i < obj.length; i++) {
//                var tn = obj[i]['tn'];
//                if (tn === tablename) {
//                    var dhtml = createTableDivHTML(obj[i], var_tablename)
//                    $('#' + sourceId).html(dhtml);
//                    $('#' + sourceId).show();
//                    break;
//                }
//            }
////            if (var_tablename === 'tbl_inspection_list') {
//            $(".youtube").YouTubeModal({autoplay: 0, width: 640, height: 480});
////            } 
//        },
//        error: function (res, status) {
//            alert(getMessage('somethingww'));
//        }
//    });
//}


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
    
    var kv = g_tbl[var_tablename].kv;
    if (typeof kv !== 'undefined' && kv) {
        var arr = kv.split(',');
        for (var i in arr) {
            var t = arr[i].split('=');
            var key = t[0];
            var val = t[1];
            json.kv[key] = val;
        }
    }
    
    g_tbl[var_tablename].startLimit = 0;
    g_tbl[var_tablename].endLimit = global_var.default_per_page;

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
            $(".youtube").YouTubeModal({autoplay: 0, width: 640, height: 480});
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
            .attr("style", "table-layout: auto; " + tblBorder + "overflow-x: auto;margin-bottom:0px");
    if (matrixId.length > 0) {
        table.attr("matrixId", matrixId);
    }

    var theader = $('<thead></thead>');
    var tr4filter = $('<tr></tr>')
            .addClass("table-filter")
            .attr("hidden", "");
    //add column for trigger events
    var thh = '<th class="apd-table-td"><input type="text"  \n\
                class="smt form-control table-filter-comp hidden" id="trigger" \n\
                name="trigger" style="margin-bottom:0px"></th>';
    tr4filter.append(thh).append(thh).append(thh).append(thh).append(thh);
    //get column names

    var tr4head = $('<tr></tr>');
    tr4head.addClass("table-header");
    //add column for trigger events
    var ckb = '<input type="checkbox" class="apd-table-checkbox-all" \n\
                name="' + t_n + '">';
    tr4head.append('<th style="width:2px"></th>')
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
        var th = $('<th class="apd-table-td _c' + j + '" ></th>');
        var cid = seqArr[j].trim();
        var cname = colPair[cid];
        var ctype = colType[cid];
        //if colomn is in the heeden_table_coloumn the continue
//        if (heeden_table_coloumn.indexOf(cid) > -1) {
//            continue;
//        }

        var input = "";
        input = "<input type=\"text\"  class=\"form-control " +
                " fa fa-search table-filter-comp\" " +
                "id=\"" + cid + "\"" +
                "  style=\"height: 22px;padding: 1px 1px;font-size: 12px;\n\
                    margin-bottom:0px\"" +
                " name=\"" + cid + "\">";
        th.append(input);
        tr4filter.append(th);

        var divth = $('<div></div>').addClass("thc-hd");
        var span1 = $('<span></span>').addClass("apd-sicon");
        var ln1 = '<i class="fa fa-long-arrow-down apd-table-sasc" cn="' + (sortno) + '" aria-hidden="true"></i>';
        ln1 += '<i class="fa fa-long-arrow-up apd-table-sdesc" cn="' + (sortno) + '" aria-hidden="true"></i>';
        span1.html(ln1);

        var span2 = $('<span></span>').addClass("apd-h-content");
        span2.html(cname);

        var span3 = $('<span></span>').addClass("apd-ficon");
        var ln2 = '<i class="fa fa-filter apd-table-col-filter" cn="' + (sortno) + '" cnm="' + cid + '" aria-hidden="true"></i>';
        span3.html(ln2);

        divth.append(span1).append(span3).append(span2);

        var thh = $('<th class="apd-table-td _c' + j + '"></th>');
        thh.append(divth);
//        thh.attr("onclick", "sortTable('" + var_tablename + "'," + sortno + ")");
//        thh.attr('nowrap', 'true');
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
            var td = $('<td class="apd-table-td _c' + i + '" align="center" valign="center"></td>');
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
        var td = $('<td class="apd-table-td _c' + i + '" align="center" valign="center"></td>');
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
    checkboxHTML = '<input type="checkbox" ';
    checkboxHTML += ' class="apd-table-checkbox" ';
    checkboxHTML += ' name=\' ' + g_tbl[var_tablename].response_tn + ' \' ';
    checkboxHTML += ' value =\'' + row_id;
    var id = (row_id);
    if (jQuery.inArray(id, g_tbl[var_tablename].arrChecked) > -1) {
        checkboxHTML += ' checked="checked"';
    }
    checkboxHTML += ' \'>';

//    console.log(id+ " - "+JSON.stringify(g_tbl[var_tablename].arrChecked));;
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
    
    var kv = g_tbl[tableId].kv;
    if (typeof kv !== 'undefined' && kv) {
        var arr = kv.split(',');
        for (var i in arr) {
            var t = arr[i].split('=');
            var key = t[0];
            var val = t[1];
            json.kv[key] = val;
        }
    }
    
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
    var unChckdClm = [];
    var colnumber = [];
    var i = 0;
    var jj = 0;
    $('#' + var_tablename).closest('div[class=custom-table]').find('.apd-table-cols').each(function () {
        var el = $(this);
        var cn = el.attr("cn");
        var cnm = el.attr("cnm");
        colnames[i] = cnm;
        colnumber[i] = cn;
        i++;
        if (!el.is(":checked")) {
            unChckdClm[jj++] = cnm;
        }
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
            td.addClass("apd-table-td _c" + i);

            if (unChckdClm.indexOf(colnames[i]) >= 0) {
                td.attr("style", "display: none;");
            }

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
    var tdtmp1 = $('<td></td>');
    var tdtmp2 = $('<td></td>');
    var tdtmp3 = $('<td></td>');
    var tdtmp4 = $('<td></td>');
    var tdtmp5 = $('<td></td>');
    trfooter.append(tdtmp1).append(tdtmp2).append(tdtmp3).append(tdtmp4).append(tdtmp5);

    for (var i = 0; i < colnames.length; i++) {

        var fval = (f_val[colnames[i]]) ? f_val[colnames[i]] : '';
        var ave = (f_val[colnames[i]]) ? parseFloat(fval) / f_val_c[colnames[i]] : '';
        ave = Math.round(ave * 10000) / 10000;
        ave = ave === 0 ? '' : ave;
        var ln = '<font color="red"><b>' + fval + '</b></font>';
        ln += '<br><font color="blue"><b>' + ave + '</b></font>';
        var td = $('<td></td>').addClass('apd-table-td _c' + i).
                attr('align', "center").attr("valign", "center");
        if (unChckdClm.indexOf(colnames[i]) >= 0) {
            td.attr("style", "display: none;");
        }
        td.html(ln);
        trfooter.append(td);
    }
    tfooter.append(trfooter);
//set html value back to the table div
    $('#' + var_tablename).find('tfoot').html(tfooter.html());
    $('#' + var_tablename).find('tbody').html(body.html());
    $(".youtube").YouTubeModal({autoplay: 0, width: 640, height: 480});

    //set checkers
    var arr = g_tbl[var_tablename].arrChecked;
    for (var i = 0; i < arr.length; i++) {
//        console.log('i='+arr[i]);
        $('.apd-table-checkbox[value=\'' + arr[i] + '\'').attr("checked", true);
    }
}

function addEmptyRow(tableid) {
    var tb = document.getElementById(tableid).tBodies[0], // use `<tbody>` to ignore `<thead>` and `<tfoot>` rows
            tr = Array.prototype.slice.call(tb.rows, 0), // put rows into array
            i;
    reverse = -((+reverse) || -1);
    tr = tr.sort(function (a, b) { // sort rows
        if (isDate(a.cells[col].textContent) && isDate(b.cells[col].textContent)) {
            var d1 = convertDate(a.cells[col].textContent);
            var d2 = convertDate(b.cells[col].textContent);
            var r = reverse // `-1 *` if want opposite order
                    * (d1.trim() // using `.textContent.trim()` for test
                            .localeCompare(d2.trim())
                            );
            return r;
        }
        if (!isNaN(a.cells[col].textContent) && !isNaN(b.cells[col].textContent))
            return reverse * ((+a.cells[col].textContent) - (+b.cells[col].textContent));
        return reverse // `-1 *` if want opposite order
                * (a.cells[col].textContent.trim() // using `.textContent.trim()` for test
                        .localeCompare(b.cells[col].textContent.trim())
                        );
    });
    for (i = 0; i < tr.length; ++i) {
        tr[i].cells[4].textContent = (i + 1);
        tb.appendChild(tr[i]); // append each row in order

    }
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
            if ($(this).css('display') != 'none') {
                if (cc < colStartInd) {
                    cc++;
                    return true;
                }

                var td = $('<th ></th>');
                td.attr("style", "word-wrap:break-word;\n\
                         max-width:160px; styleborder: 1px; text-align: center;");
                td.html($(this).html());
                tr.append(td);
            }

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
            if ($(this).css('display') != 'none') {
                var td = $('<td></td>');
                td.attr("style", "word-wrap:break-word;\n\
                     max-width:160px; styleborder: 1px; text-align: center;");
                td.html($(this).html());
                tr.append(td);
            }
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

function exportToExcel_(tableId) {
    var data_type = 'data:application/vnd.ms-excel';
    var table_div = document.getElementById(tableId);
    var table_html = table_div.outerHTML.replace(/ /g, '%20');
    var html = getTableHtmlForExcel(tableId, 1, 0);
    var a = document.createElement('a');
    a.href = data_type + ', ' + html;
    a.download = 'exported_table_' + Math.floor((Math.random() * 9999999) + 1000000) + '.xls';
    a.click();
}

function exportToExcel(tableId) {
    var colnames = [];
    colnames.push('No');
    $('#' + tableId).closest('div[class=custom-table]')
            .find('.apd-table-cols').each(function () {
        var cnm = $(this).attr("cnm");
        if ($(this).is(":checked")) {
            colnames.push(getLabel(cnm));
        }
    });


    var data = [];
    var wscols = [];
    var columns = colnames;
    var row = [];
    for (var i in columns) {
        row.push(columns[i]);
        var o = {};
        o.wch = columns[i].length + 2;
        wscols.push(o);
    }
//    console.log("wscols-" + JSON.stringify(wscols));
//    console.log("row-" + JSON.stringify(row));
    data.push(row);


    var colStartInd = 4;
    $('#' + tableId + ' tbody tr').each(function () {
        var cc = 0;
        var row_t = [];
        $(this).find('td').each(function () {
            if (cc < colStartInd) {
                cc++;
                return true;
            }
            if ($(this).css('display') != 'none') {
                row_t.push($(this).html());

            }
        });
        data.push(row_t);
    });

//    console.log("data-" + JSON.stringify(data));


    var ws_name = "SheetJS";

    var wb = new Workbook();


    /* convert an array of arrays in JS to a CSF spreadsheet */

    var ws = sheet_from_array_of_arrays(data, "", wscols);

    /* TEST: add worksheet to workbook */
    wb.SheetNames.push(ws_name);
    wb.Sheets[ws_name] = ws;

    /* TEST: column widths */
    ws['!cols'] = wscols;

    /* write file */
    var wopts = {bookType: 'xlsx', bookSST: false, type: 'binary'};

    var wbout = XLSX.write(wb, wopts);


    /* the saveAs call downloads a file on the local machine */
    var filedata = new Blob([s2ab(wbout)], {type: ""});

    var date = new Date();
    var filename = tableId + '_' + humanFormat(date) + '.xlsx';

    var a = document.createElement('a');
    a.href = window.URL.createObjectURL(filedata);
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    $(a).remove();
}

/* TODO: date1904 logic */
function datenum(v, date1904) {
    if (date1904)
        v += 1462;
    var epoch = Date.parse(v);
    return (epoch - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1000);
}



function s2ab(s) {
    var buf = new ArrayBuffer(s.length);
    var view = new Uint8Array(buf);
    for (var i = 0; i !== s.length; ++i)
        view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}

function Workbook() {
    if (!(this instanceof Workbook))
        return new Workbook();
    this.SheetNames = [];
    this.Sheets = {};
}


function sheet_from_array_of_arrays(data, opt, wscols) {
    var ws = {};
    var range = {s: {c: 10000000, r: 10000000}, e: {c: 0, r: 0}};
    for (var R = 0; R !== data.length; ++R) {
        for (var C = 0; C !== data[R].length; ++C) {
            if (range.s.r > R)
                range.s.r = R;
            if (range.s.c > C)
                range.s.c = C;
            if (range.e.r < R)
                range.e.r = R;
            if (range.e.c < C)
                range.e.c = C;
            var cell = {v: data[R][C]};
            if (R === 0) {
                cell.r = '<b>' + data[R][C] + '</b>';
                cell.s =
                        {patternType: 'solid',
                            fgColor: {theme: 8, tint: 0.3999755851924192, rgb: '9ED2E0'},
                            bgColor: {indexed: 64}};
                cell.color =
                        {name: 'accent5', rgb: '4BACC6'};

            }
//            console.log("C=" + C)
            if (data[R][C] && wscols[C].wch < data[R][C].length) {
                wscols[C].wch = data[R][C].length + 2;
            }
            if (cell.v === null)
                continue;
            var cell_ref = XLSX.utils.encode_cell({c: C, r: R});

            /* TEST: proper cell types and value handling */

            if (typeof cell.v === 'number')
                cell.t = 'n';
            else if (typeof cell.v === 'boolean')
                cell.t = 'b';
            else if (cell.v instanceof Date) {
                cell.t = 'n';
                cell.z = XLSX.SSF._table[14];
                cell.v = datenum(cell.v);
            } else
                cell.t = 's';
            ws[cell_ref] = cell;
        }
    }

    /* TEST: proper range */
    if (range.s.c < 10000000)
        ws['!ref'] = XLSX.utils.encode_range(range);
    return ws;
}

function humanFormat(date) {
    var yyyy = date.getFullYear().toString();
    var mm = (date.getMonth() + 1).toString();
    var dd = date.getDate().toString();
    var mmChars = mm.split('');
    var ddChars = dd.split('');
    return yyyy + '-' + (mmChars[1] ? mm : "0" + mmChars[0]) + '-' + (ddChars[1] ? dd : "0" + ddChars[0]);
}
;