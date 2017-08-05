String.prototype.replaceAll = function (str1, str2, ignore) {
    return this.replace(new RegExp(str1.replace(/([\/\,\!\\\^\$\{\}\[\]\(\)\.\*\+\?\|\<\>\-\&])/g, "\\$&"), (ignore ? "gi" : "g")), (typeof (str2) === "string") ? str2.replace(/\$/g, "$$$$") : str2);
};
var currentTable;
var currentReportUrl;
var tabs = {};
var tableConfig = {};
function selectTab() {
    var id = $(this).attr('tab-id');
    selectTabWithoutEvent(id);
}
function selectTabWithoutEvent(id) {
    $('.tab').hide();
    $('.tablink').removeClass('select');
    $('#' + id).show();
    $('li[tab-id=' + id + ']').addClass('select');
    $(':checkbox:not(:checked)').prop('disabled', false);
    $('.selected-arrow').removeClass('selected-arrow');

    printComboboxes(id);
    switch (id) {
        case 'table':
            $('.table-right-arrow').addClass('selected-arrow');
            printTable();
            break;
        case 'pie':
            printPie();
            $('.pie-right-arrow').addClass('selected-arrow');
            break;
        case 'diagram':
            printDiagram();
            $('.diagram-right-arrow').addClass('selected-arrow');
            break;
        case 'series':
            $('.series-right-arrow').addClass('selected-arrow');
            printSeries();
            break;
        case 'pivot':
//            pivotCombobox();
//            printPivot();
            $('.pivot-right-arrow').addClass('selected-arrow');
            break;
    }
}

function printTable() {
    var config = {listService: currentReportUrl,
        numbers: true,
        checkable: false,
        deletable: false,
        editable: false,
        filters: true,
        groupBy: true,
        sortable: true,
        pagination: true,
        columnSelect: true,
        timeInterval: true};
    $.extend(config,tableConfig);
    console.log(JSON.stringify(config));
    console.log(JSON.stringify(tableConfig));
    $('#xyz').datalist(config);
    tabs.table.checkedColumns = $('#xyz').datalist().selectedColumns();
    currentTable = $('#xyz').datalist().getTableData();
}
function printPie() {
    var pieData = tableDataToPieChartData();
    $('#pie-container').highcharts({
        chart: {
            plotBackgroundColor: '#fff',
            backgroundColor: '#fff',
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: false,
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
            }
        },
        legend: {
            layout: 'vertical',
            backgroundColor: '#FFFFFF',
            align: 'left',
            verticalAlign: 'middle',
            borderWidth: 1,
            borderRadius: 2
        },
        series: [{
                name: 'Brands',
                colorByPoint: true,
                data: pieData
            }]
    });
}
function printSeries() {
    var data = $('#xyz').datalist().pivot(tabs.series.selectedColumns);
    var columns = data.getColumns();
    var rows = data.getRows();
    var rwT = currentTable.getRows();
    var x = [], y = [], sd = [];
    for (var i in columns) {
        if (columns[i].i !== 'c_1') {
            x.push(columns[i].n);
        }
    }
    x.pop();
    for (var i in rwT) {
        if (rwT[i][tabs.series.selectedColumns.yaxis.id]) {
            var k = rwT[i][tabs.series.selectedColumns.yaxis.id];
            if (!y.includes(k)) {
                y.push(k);
            }
        }
    }
    for (var i in rows) {
        var series = {};
        series.name = rows[i]['c_1'];
        series.data = [];
        for (var j in columns) {
            if (columns[j].i !== 'c_1') {
                series.data.push(parseInt(rows[i][Object.keys(rows[i])[j]]));
            }
        }
        series.data.pop();
        sd.push(series);
    }
    sd.pop();
    $('#series-container').highcharts({
        chart: {
            backgroundColor: '#fff',
            type: 'line',
            renderTo: 'series-container'
        },
        title: false,
        xAxis: {
            title: {
                text: tabs.series.selectedColumns.xaxis.n
            },
            categories: x
        },
        yAxis: {
            title: {
                text: tabs.series.selectedColumns.yaxis.n
            }
        },
        legend: {
            layout: 'vertical',
            backgroundColor: '#FFFFFF',
            align: 'left',
            verticalAlign: 'middle',
            borderWidth: 1,
            borderRadius: 2
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.x + '</b><br/>' +
                        this.series.name + ': ' + this.y;
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        series: sd
    });
}
function printDiagram() {
    var data = $('#xyz').datalist().pivot(tabs.diagram.selectedColumns);
    var columns = data.getColumns();
    var rows = data.getRows();
    var rwT = currentTable.getRows();
    var x = [], y = [], sd = [];
    for (var i in columns) {
        if (columns[i].i !== 'c_1') {
            x.push(columns[i].n);
        }
    }
    x.pop();
    for (var i in rwT) {
        if (rwT[i][tabs.diagram.selectedColumns.yaxis.id]) {
            var k = rwT[i][tabs.diagram.selectedColumns.yaxis.id];
            if (!y.includes(k)) {
                y.push(k);
            }
        }
    }
    for (var i in rows) {
        var series = {};
        series.name = rows[i]['c_1'];
        series.data = [];
        for (var j in columns) {
            if (columns[j].i !== 'c_1') {
                series.data.push(parseInt(rows[i][Object.keys(rows[i])[j]]));
            }
        }
        series.data.pop();
        sd.push(series);
    }
    sd.pop();
    $('#diagram-container').highcharts({
        chart: {
            type: 'column',
            plotBackgroundColor: '#fff',
            backgroundColor: '#fff',
            renderTo: 'diagram-container'
        },
        title: false,
        xAxis: {
            categories: x,
            title: {
                text: tabs.diagram.selectedColumns.xaxis.n
            },
            crosshair: true
        },
        yAxis: {
            title: {
                text: tabs.diagram.selectedColumns.yaxis.n
            }
        },
        legend: {
            layout: 'vertical',
            backgroundColor: '#FFFFFF',
            align: 'left',
            verticalAlign: 'middle',
            borderWidth: 1,
            borderRadius: 2
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                dataLabels: {
                    enabled: true
                },
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: sd
    });
}

function tableDataToPieChartData() {
    var rw = currentTable.getRows();
    var total = 0, x = [];
    for (var i in rw) {
        if (rw[i][tabs.pie.selectedColumns.xaxis.id]) {
            var k = rw[i][tabs.pie.selectedColumns.xaxis.id];
            if (!x.includes(k)) {
                x.push(k);
            }
        }
    }
    for (var i in x)
        var pd = [];
    for (var i in rw) {
        total = total + parseInt(rw[i][tabs.pie.selectedColumns.yaxis.id]);
    }
    for (var i in x) {
        var sum = 0;
        var obj = {};
        obj.name = x[i];
        for (var j in rw) {
            if (rw[j][tabs.pie.selectedColumns.xaxis.id] === x[i]) {
                sum = sum + parseInt(rw[j][tabs.pie.selectedColumns.yaxis.id]);
            }
        }
        obj.y = sum / total;
        pd.push(obj);
    }
    return pd;
}
function printComboboxes(comp) {
    if (comp !== 'table' && comp !== 'pivot') {
        var s = tabs[comp].selectedColumns;
        var headers = tabs.table.checkedColumns;
        var div = $('#' + comp).find('.combobox');
        div.empty();
        var xAxis = $('<div/>', {'class': 'xAxisSeries axis'});
        var labelX = $('<label/>'), selectX = $('<select/>');
        labelX.html('X axis');
        xAxis.append(labelX).append(selectX);
        div.append(xAxis);

        var yAxis = xAxis.clone();
        yAxis.removeClass('xAxisSeries').addClass('yAxisSeries');
        yAxis.find('label').html('Y axis');
        div.append(yAxis);
        if (comp !== 'pie') {
            var zAxis = xAxis.clone();
            zAxis.removeClass('xAxisSeries').addClass('zAxisSeries');
            zAxis.find('label').html('Z axis');
            div.append(zAxis);
        }

        div.find('select').change(function () {
            changeCombobox(this, comp);
        });
        for (var i in headers) {
            if (headers[i].checked && headers[i].t !== 'ID') {
                var o = $('<option/>').html(headers[i].n).val(headers[i].i);
                if (headers[i].t !== 'INTEGER') {
                    if (comp !== 'pie') {
                        zAxis.find('select').append(o.clone());
                    }
                    xAxis.find('select').append(o.clone());
                }
                if (headers[i].t !== 'STRING') {
                    yAxis.find('select').append(o);
                }
            }
        }
        if (s.xaxis.id)
            xAxis.find('select').val(s.xaxis.id);
        if (s.yaxis.id)
            yAxis.find('select').val(s.yaxis.id);
        if (comp !== 'pie')
            if (s.zaxis.id)
                zAxis.find('select').val(s.zaxis.id);

        s.xaxis.id = xAxis.find('select').val();
        s.xaxis.n = xAxis.find('select option:selected').text();
        s.yaxis.id = yAxis.find('select').val();
        s.yaxis.n = yAxis.find('select option:selected').text();
        if (comp !== 'pie') {
            s.zaxis.id = zAxis.find('select').val();
            s.zaxis.n = zAxis.find('select option:selected').text();
        }
    }
}
function changeCombobox(el, comp) {
    var sc = tabs[comp].selectedColumns;
    var val = $(el).val();
    var col = currentTable.getColumnById(val);
    if ($(el).parent().hasClass('xAxisSeries')) {
        sc.xaxis.n = col.n;
        sc.xaxis.id = col.i;
    } else if ($(el).parent().hasClass('yAxisSeries')) {
        sc.yaxis.n = col.n;
        sc.yaxis.id = col.i;
    } else if (comp !== 'pie' && $(el).parent().hasClass('zAxisSeries')) {
        sc.zaxis.n = col.n;
        sc.zaxis.id = col.i;
    }
    switch (comp) {
        case 'series':
            printSeries();
            break;
        case 'diagram':
            printDiagram();
            break;
        case 'pie':
            printPie();
            break;
    }
}
function pivotCombobox() {
    var s = tabs.pivot.selectedColumns;
    var headers = tabs.table.checkedColumns;
    var div = $('#pivot-combobox');
    div.empty();

    var xAxis = $('<div/>', {'class': 'xAxis axis'});
    div.append(xAxis);
    var labelX = $('<label/>'), selectX = $('<select/>');
    labelX.html('Sətir');
    xAxis.append(labelX).append(selectX);
    div.append(xAxis);

    var yAxis = xAxis.clone();
    yAxis.removeClass('xAxis').addClass('yAxis');
    yAxis.find('label').html('Sütun');
    div.append(yAxis);

    var zAxis = xAxis.clone();
    zAxis.removeClass('xAxis').addClass('zAxis');
    zAxis.find('label').html('Dəyər');
    div.append(zAxis);

    div.find('select').change(function () {
        changePivotCombobox(this);
    });

    for (var i in headers) {
        if (headers[i].checked) {
            var o = $('<option/>').html(headers[i].n).val(headers[i].i);
            if (headers[i].t !== 'INTEGER') {
                yAxis.find('select').append(o);
                xAxis.find('select').append(o.clone());
            }
            if (headers[i].t === 'INTEGER') {
                zAxis.find('select').append(o);

            }
        }
    }
    if (s.xaxis.id)
        xAxis.find('select').val(s.xaxis.id);
    if (s.zaxis.id)
        yAxis.find('select').val(s.zaxis.id);
    if (s.yaxis.id)
        zAxis.find('select').val(s.yaxis.id);

    s.xaxis.id = xAxis.find('select').val();
    s.xaxis.n = xAxis.find('select option:selected').text();
    s.yaxis.id = zAxis.find('select').val();
    s.yaxis.n = zAxis.find('select option:selected').text();
    s.zaxis.id = yAxis.find('select').val();
    s.zaxis.n = yAxis.find('select option:selected').text();
}

function changePivotCombobox(element) {
    var sc = tabs.pivot.selectedColumns;
    var val = $(element).val();
    var col = currentTable.getColumnById(val);
    if ($(element).parent().hasClass('xAxis')) {
        sc.xaxis.n = col.n;
        sc.xaxis.id = col.i;
    } else if ($(element).parent().hasClass('yAxis')) {
        sc.zaxis.n = col.n;
        sc.zaxis.id = col.i;
    } else if ($(element).parent().hasClass('zAxis')) {
        sc.yaxis.n = col.n;
        sc.yaxis.id = col.i;
    }
    printPivot();
}
function selectAllCheckbox(e) {
    $(e.target).closest('table').find('.chk input').prop('checked', e.target.checked);
}
function showReport(u) {
    console.log($(u).data('groupby'));
    tableConfig['timeInterval'] = $(u).data('time-interval');
    tableConfig['groupBy'] = $(u).data('group-by');
    currentReportUrl = $(u).data('url');
    initialize();
}
function initialize() {
    var t = {};
    t.table = {};
    t.series = {};
    t.pie = {};
    t.diagram = {};
    t.pivot = {};
    t.table.checkedColumns = [];
    t.series.selectedColumns = {};
    t.series.selectedColumns.xaxis = {};
    t.series.selectedColumns.yaxis = {};
    t.series.selectedColumns.zaxis = {};
    t.pie.selectedColumns = {};
    t.pie.selectedColumns.xaxis = {};
    t.pie.selectedColumns.yaxis = {};
    t.diagram.selectedColumns = {};
    t.diagram.selectedColumns.xaxis = {};
    t.diagram.selectedColumns.yaxis = {};
    t.diagram.selectedColumns.zaxis = {};
    t.pivot.selectedColumns = {};
    t.pivot.selectedColumns.xaxis = {};
    t.pivot.selectedColumns.yaxis = {};
    t.pivot.selectedColumns.zaxis = {};
    tabs = t;
    $('.tablink').click(selectTab);
    $('#xyz').removeAttr('data-list-id');
    $('#xyz').empty();
    $('.combobox').empty();
    $('.chart-container').empty();
    selectTabWithoutEvent('table');
}
function printPivot() {
    var div = document.getElementById('pivot-container');
    div.innerHTML = '';
    var paginationDiv = $('<div/>', {class: 'lpc'});
    var printb = $('<button/>', {class: 'print-button'});
    var pi = $('<i/>', {class: 'print icon'});
    $(printb).append(pi);
    $(printb).click(prnt);
    var excelb = $('<button/>', {class: 'excel-button'});
    var ei = $('<i/>', {class: 'file excel outline icon'});
    $(excelb).append(ei);
    $(excelb).click(exportPivotToExcel);
    $(paginationDiv).append(printb).append(excelb);
    var data = $('#xyz').datalist().pivot(tabs.pivot.selectedColumns);
    var table = $('<table/>', {class: 'list'}), thead = $('<thead/>'), tbody = $('<tbody/>');
    table.append(thead).append(tbody);
    $(div).append(paginationDiv).append(table);
    var columns = data.getColumns();
    var tr = $('<tr/>');
    for (var i in columns) {
        var th = $('<th/>');
        th.attr('col-id', columns[i].i);
        th.html(columns[i].n);
        tr.append(th);
    }
    thead.append(tr);
    var rows = data.getRows();
    for (var j in rows) {
        var tr = $('<tr/>');
        for (var i in columns) {
            var td = $('<td/>');
            td.attr('col-id', columns[i].i);
            td.html(rows[j][columns[i].i]);
            tr.append(td);
        }
        tbody.append(tr);
    }
}
function savePopUp() {
    var container = $('#sv-tmp-popUp');
    container.empty();
    var div = $('<div/>', {'class': 'save-pu cntr'});
    var heading = $('<div/>', {'class': 'save-h'});
    $(heading).attr('id', 'save-h');
    var span = $('<span/>');
    span.html('New Template');
    var x = $('<button/>');
    var i = $('<i/>', {'class': 'remove icon'});
    x.append(i);
    x.click(closePopUp);
    heading.append(span).append(x);
    div.append(heading);
    var form = $('<form/>');
    var input = $('<input>');
    input.attr('type', 'text');
    input.attr('placeholder', 'Enter name...');
    var label = $('<label/>');
    label.html('Name: ');

    var ed = $('<div/>', {'class': 'err-cont'});
    form.append(label).append(input);
    div.append(form).append(ed);

    var btn = $('<button/>', {'class': 'save'});
    btn.html('Save');
    $(btn).attr('id', 'save');
    btn.click(save);
    btn.click(addNewTemplate);
    btn.click(closePopUp);
    div.append(btn);
    container.append(div);

    $('.save-pu input').keypress(function (e) {
        if (e.which === 13) {
            return false;
        }
    });
    $('#tab-container').css({"-webkit-filter": "blur(2px)", "-moz-filter": "blur(2px)", "-o-filter": "blur(2px)", "-ms-filter": "blur(2px)", "filter": "blur(2px)"});
    $('.header').css({"-webkit-filter": "blur(2px)", "-moz-filter": "blur(2px)", "-o-filter": "blur(2px)", "-ms-filter": "blur(2px)", "filter": "blur(2px)"});

}
function save() {
    var inputValue = $('.save-pu').find('input').val();
    var template = {};
    template.c = $('#xyz').datalist().selectedColumns();
    template.f = $('#xyz').datalist().getFilterData();
    template.pie = tabs.pie.selectedColumns;
    template.series = tabs.series.selectedColumns;
    template.diagram = tabs.diagram.selectedColumns;
    template.pivot = tabs.pivot.selectedColumns;
    var templateKey = currentReportUrl.split('/')[currentReportUrl.split('/').length - 1];
    var json = {"b": {
            "templateKey": templateKey
        }};
    json.b.templateDescription = inputValue;
    json.b.templateValue = template;
    $.ajax({
        url: urls['insertNewTemplate'],
        type: 'post',
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (e) {
            if (inputValue === '') {
                printError(e);
            } else if (e.d.r.length > 0 && e.d.r[0].error_key === 'templateKey,templateDescription') {
                printError(e);
            }
        },
        error: function (jqXHR, textstatus, errorThrown) {
            console.log('Something went wrong!');
        }
    });
}
function printError(e) {
    var errCont = $('.save-pu').find('.err-cont');
    errCont.empty();
    var div = $('<div></div>');
    $(div).addClass('err');
    var span = $('<span></span>');
    span.html(e.d.r[0].error_code);
    div.append(span);
    errCont.append(div);
    $('.save-pu').show();
}
function prnt(e) {
    var table = $(e.target).closest('.reports-content').find('.list').clone();
    var win = window.open('print.html', '', 'toolbar=0,location=0,menubar=0');
    win.onload = function () {
        win.document.body.innerHTML = table[0].outerHTML;
    };
    win.print();
}
function loadTeamplate(e) {
    var fileName = $(e.target).parent().find('span').text();
    var json = {};
    json.b = {};
    var templateKey = currentReportUrl.split('/')[currentReportUrl.split('/').length - 1];
    json.b.templateKey = templateKey;
    json.b.templateDescription = fileName;
    $.ajax({
        url: urls.getTemplateByKey,
        type: 'post',
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (data) {
            var d = data.res.r[0].templateValue;
            var nd = d.replaceAll('\"', '"');
            nd = JSON.parse(nd);
            tabs.table.checkedColumns = nd["c"];
            tabs.pie.selectedColumns = nd["pie"];
            tabs.series.selectedColumns = nd["series"];
            tabs.diagram.selectedColumns = nd["diagram"];
            tabs.pivot.selectedColumns = nd["pivot"];
            $('#xyz').datalist().updateWithData(nd);
            printSeries();
            printDiagram();
            printPie();
            printPivot();
            pivotCombobox();


        },
        error: function (s, d, e) {
            console.log('Something Went Wrong.!.!.!...');
        }
    });
}
function closePopUp(e) {
    $(e.target).closest('.cntr').hide();
    $('#tab-container').css({"filter": "none"});
    $('.header').css({"filter": "none"});
}
function createTemplate(data) {
    var container = $('#tmp');
    container.empty();
    var div = $('<div/>', {'class': 'template-container cntr'});
    var headCont = $('<div/>', {'class': 'hd-c'});
    var span = $('<span/>').html('Templates');
    var x = $('<button/>');
    var i = $('<i/>', {'class': 'remove icon'});
    x.append(i);
    x.click(closePopUp);
    headCont.append(span).append(x);
    var bodyCont = $('<div/>', {'class': 'bd-c'});
//    var ftc = $('<div/>', {'class': 'ft-c'});
//    var ob = $('<button/>');
//    var cb = $('<button/>');
//    ob.html('Open');
//    cb.html('Cancel');

//    ftc.append(ob).append(cb);
    container.append(div);
    div.append(headCont);
    div.append(bodyCont);
//    div.append(ftc);
    for (var i in data) {
        var tmpDiv = $('<div/>', {'class': 'tmp-cont'});
        var c = $('<div/>', {'class': 'tc'});
        var d = createIcon('huge file icon');
        d = $(d);
        d.addClass('file-icon');
//        d.click(function(){
//            loadTeamplate();
//            closePopUp();
//        });
        d.click(loadTeamplate);
        d.click(closePopUp);


        var removeIcon = createIcon('remove icon');
        removeIcon = $(removeIcon);
        removeIcon.click(removeTemplate);
        removeIcon.click(areYouSure);
        removeIcon.addClass('rmv');
        var span = $('<span/>').html(data[i].templateDescription);
        span.click(loadTeamplate);
        span.click(closePopUp);
        c.append(removeIcon);
        c.append(d);
        c.append(span);
        tmpDiv.append(c);
        bodyCont.append(tmpDiv);
    }
}
function areYouSure() {
    var container = $('#tmp');
    alert('sure?');
}
function getDataForTemplates() {
    var templateKey = currentReportUrl.split('/')[currentReportUrl.split('/').length - 1];
    var json = {"b": {
            "templateKey": templateKey
        }};
    var url = urls.getTemplateByKey;
    $.ajax({
        url: url,
        data: JSON.stringify(json),
        type: 'post',
        contentType: "application/json",
        success: function (data) {
            createTemplate(data.res.r);
        },
        error: function () {
            console.log('somehing went wrong');
        }
    });
}
function addNewTemplate() {
    var inputValue = $('.save-pu').find('input').val();
    var tempsCont = $('.bd-c');
    var length = $('.template-container').find('.bd-c div').length;
    var arr = [];
    for (var i = 1; i <= length; i++) {
        var txt = $('.template-container').find('.bd-c div:nth-child(' + i + ') span').text();
        if (!arr.includes(txt)) {
            arr.push(txt);
        }
    }
    if (!arr.includes(inputValue)) {
        var tmpDiv = document.createElement('div');
        $(tmpDiv).addClass('tmp-cont');
        var d = createIcon('huge file icon');
        $(d).addClass('file-icon');
        d.addEventListener('click', loadTeamplate);
        var removeIcon = createIcon('remove icon');
        removeIcon.addEventListener('click', removeTemplate);
        $(removeIcon).addClass('rmv');
        var span = document.createElement('span');
        span.addEventListener('click', loadTeamplate);
        span.innerHTML = inputValue;
        tmpDiv.appendChild(removeIcon);
        tmpDiv.appendChild(d);
        tmpDiv.appendChild(span);
        tempsCont.append(tmpDiv);
    }
}
function openTemplateWindow(e) {
    getDataForTemplates();
    $('.template-container').show();
    $('#tab-container').css({"-webkit-filter": "blur(2px)", "-moz-filter": "blur(2px)", "-o-filter": "blur(2px)", "-ms-filter": "blur(2px)", "filter": "blur(2px)"});
    $('.header').css({"-webkit-filter": "blur(2px)", "-moz-filter": "blur(2px)", "-o-filter": "blur(2px)", "-ms-filter": "blur(2px)", "filter": "blur(2px)"});
}
function createIcon(e) {
    var d = document.createElement('div');
    var i = document.createElement('i');
    $(i).addClass(e);
    d.appendChild(i);
    return d;
}
function removeTemplate(e) {
    var fileName = $(e.target).parent().find('span').text();
    var url = urls.deleteTemplateByKey;
    var json = {};
    json.b = {};
    var templateKey = currentReportUrl.split('/')[currentReportUrl.split('/').length - 1];
    json.b.templateKey = templateKey;
    json.b.templateDescription = fileName;
    $.ajax({
        url: url,
        type: 'post',
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (data) {
            getDataForTemplates();
        },
        error: function () {
        }
    });
}
function exportPivotToExcel(e) {
    var blob = excel();
    var date = new Date();
    var filename = 'Pivot' + '_' + date.humanFormat() + '.xlsx';

    var a = document.createElement('a');
    a.href = window.URL.createObjectURL(blob);
    a.download = filename;
    document.body.appendChild(a);
    a.click();
}
function excel() {
    var data = [];
    var wscols = [];
    var d = $('#xyz').datalist().pivot(tabs.pivot.selectedColumns);
    var columns = d.getColumns();
    var rows = d.getRows();
    var row = [];
//    
//    var keys = Object.keys(rows[0]);
//    var last_key = keys[keys.length - 1];
//    var value = rows[0][last_key].toString();

    for (var i in columns) {
        row.push(columns[i].n);
        var o = {};
        if (columns[i].n.length) {
            o.wch = columns[i].n.length + 2;
        } else {
            o.wch = 15;
        }
        wscols.push(o);
    }
    data.push(row);


    for (var j in rows) {
        var row = [];
        for (var i in columns) {
            if (columns[i].i === 'id')
                continue;
            row.push(rows[j][columns[i].i]);
        }
        data.push(row);
    }
    var ws_name = "SheetJS";
    function Workbook() {
        if (!(this instanceof Workbook))
            return new Workbook();
        this.SheetNames = [];
        this.Sheets = {};
    }
    var wb = new Workbook();


    /* TODO: date1904 logic */
    function datenum(v, date1904) {
        if (date1904)
            v += 1462;
        var epoch = Date.parse(v);
        return (epoch - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1000);
    }

    /* convert an array of arrays in JS to a CSF spreadsheet */
    function sheet_from_array_of_arrays(data, opts) {
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
                if (wscols[C].wch < data[R][C].length) {
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
    var ws = sheet_from_array_of_arrays(data);

    /* TEST: add worksheet to workbook */
    wb.SheetNames.push(ws_name);
    wb.Sheets[ws_name] = ws;

    /* TEST: column widths */
    ws['!cols'] = wscols;

    /* write file */
    var wopts = {bookType: 'xlsx', bookSST: false, type: 'binary'};

    var wbout = XLSX.write(wb, wopts);
    function s2ab(s) {
        var buf = new ArrayBuffer(s.length);
        var view = new Uint8Array(buf);
        for (var i = 0; i !== s.length; ++i)
            view[i] = s.charCodeAt(i) & 0xFF;
        return buf;
    }

    /* the saveAs call downloads a file on the local machine */
    var filedata = new Blob([s2ab(wbout)], {type: ""});
    return filedata;
}