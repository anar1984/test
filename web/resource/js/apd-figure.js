/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function drawSeries() {
    drawLine('f4figure', 'f4figureSingle', 'linechart_material');
}

function drawBar() {
    drawBarLine('f4figure', 'f4figureSingle', 'linechart_material');
}

function drawPie() {
    drawPieLine('f4figure', 'f4figureSingle', 'linechart_material');
}

function drawPieLine(multiselect_element_y_id, multiselect_element_x_id, result_div_id) {
    var cols = $('#' + multiselect_element_y_id).val();
    var x_col = $('#' + multiselect_element_x_id).val();

    if (!cols) {
        alert(getMessage("chooseField4Y"));
        return;
    }

    if (!x_col) {
        alert(getMessage("chooseField4X"));
        return;
    }

    var x_title = $('#' + multiselect_element_x_id).find('option[value='
            + x_col + ']').text();

    var colnames = [];
    colnames.push(x_title);
    var cl = cols.length >= 1 ? 1 : 0;
    for (var i = 0; i < cl; i++) {
        var colname = $('#' + multiselect_element_y_id).find('option[value='
                + cols[i] + ']').text();
        colnames.push(colname);
    }

    var d = [];
    d.push(colnames);
    $('#tbl_inspectiomatrix_list tbody tr').each(function () {
        var row_t = [];
        $(this).find('.' + x_col).each(function () {
            var vl = $(this).html();
            row_t.push(vl);
        });


        for (var i = 0; i < cl; i++) {
            $(this).find('.' + cols[i]).each(function () {
                var vl = parseFloat($(this).html());
                if (isNaN(vl)) {
                    vl = 0;
                }
                row_t.push(vl);
            });
        }
        d.push(row_t);
    });


    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart( ) {

        var data = new google.visualization.arrayToDataTable(d);

        var title = $('#matrixListName').html();
        var options = {
            chart: {
                title: title,  
            }
        };

        var chart = new google.visualization.PieChart(document.getElementById(result_div_id));

        chart.draw(data, options);



    }
}


function drawBarLine(multiselect_element_y_id, multiselect_element_x_id, result_div_id) {
    var cols = $('#' + multiselect_element_y_id).val();
    var x_col = $('#' + multiselect_element_x_id).val();

    if (!cols) {
        alert(getMessage("chooseField4Y"));
        return;
    }

    if (!x_col) {
        alert(getMessage("chooseField4X"));
        return;
    }

    var x_title = $('#' + multiselect_element_x_id).find('option[value='
            + x_col + ']').text();

    var colnames = [];
    colnames.push(x_title);
    for (var i = 0; i < cols.length; i++) {
        var colname = $('#' + multiselect_element_y_id).find('option[value='
                + cols[i] + ']').text();
        colnames.push(colname);
    }

    var d = [];
    d.push(colnames);
    $('#tbl_inspectiomatrix_list tbody tr').each(function () {
        var row_t = [];
        $(this).find('.' + x_col).each(function () {
            var vl = $(this).html();
            row_t.push(vl);
        });


        for (var i = 0; i < cols.length; i++) {
            $(this).find('.' + cols[i]).each(function () {
                var vl = parseFloat($(this).html());
                if (isNaN(vl)) {
                    vl = 0;
                }
                row_t.push(vl);
            });
        }
        d.push(row_t);
    });


    google.charts.load('current', {'packages': ['bar']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart( ) {

        var data = new google.visualization.arrayToDataTable(d);
        var title = $('#matrixListName').html();
        var options = {
            chart: {
                title: title
            }
        };

        var chart = new google.charts.Bar(document.getElementById(result_div_id));
        chart.draw(data, google.charts.Bar.convertOptions(options));

    }
}

function drawLine(multiselect_element_y_id, multiselect_element_x_id, result_div_id) {
    var cols = $('#' + multiselect_element_y_id).val();
    var x_col = $('#' + multiselect_element_x_id).val();

    if (!cols) {
        alert(getMessage("chooseField4Y"));
        return;
    }

    if (!x_col) {
        alert(getMessage("chooseField4X"));
        return;
    }

    var colnames = [];
    for (var i = 0; i < cols.length; i++) {
        var colname = $('#' + multiselect_element_y_id).find('option[value='
                + cols[i] + ']').text();
        colnames.push(colname);
    }

    var x_title = $('#' + multiselect_element_x_id).find('option[value='
            + x_col + ']').text();

    var d = [];
    $('#tbl_inspectiomatrix_list tbody tr').each(function () {

        var row_t = [];
        $(this).find('.' + x_col).each(function () {
            var vl = $(this).html();
            row_t.push(vl);
        });


        for (var i = 0; i < cols.length; i++) {
            $(this).find('.' + cols[i]).each(function () {
                var vl = parseFloat($(this).html());
                if (isNaN(vl)) {
                    vl = 0;
                }
                row_t.push(vl);
            });
        }
        d.push(row_t);
    });


    google.charts.load('current', {'packages': ['line']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart( ) {

        var data = new google.visualization.DataTable();
        data.addColumn('string', x_title);
        for (var i = 0; i < colnames.length; i++) {
            data.addColumn('number', colnames[i]);
        }

        data.addRows(d);

        var title = $('#matrixListName').html();
        var options = {
            chart: {
                title: title
            },
            width: '100%',
            height: 400
        };

        var chart = new google.charts.Line(document.getElementById(result_div_id));
        chart.draw(data, google.charts.Line.convertOptions(options));
    }
}
