(function ($) {
    String.prototype.toInt = function () {
        return parseInt(this);
    };
    Date.prototype.humanFormat = function () {
        var yyyy = this.getFullYear().toString();
        var mm = (this.getMonth() + 1).toString();
        var dd = this.getDate().toString();
        var mmChars = mm.split('');
        var ddChars = dd.split('');
        return yyyy + '-' + (mmChars[1] ? mm : "0" + mmChars[0]) + '-' + (ddChars[1] ? dd : "0" + ddChars[0]);
    };
    Date.prototype.toDateInputValue = (function () {
        var local = new Date(this);
        local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
        return local.toJSON().slice(0, 10);
    });
    var dla = {};
    var CT = {
        I: "ID",
        S: "STATUS",
        M: "MODIFYTIMESTAMP",
        O: "ORDINARY",
        IMAGE: "IMAGE",
        STRING: "STRING",
        TEXT: "TEXT"
    };
    "use strict";
    var Table = function () {
        this.f = new Filter();
        this.c = [];
        this.r = [];
        this.tn;
        this.grc;
        this.getRowCount = function () {
            return this.grc;
        };
        this.setRowCount = function (rc) {
            this.grc = rc;
        };
        this.insertData = function (data) {
            this.tn = data.tn;
            this.c = data.c;
            this.r = data.r;
        };
        this.insertRows = function (rows) {
            this.r = rows;
        };
        this.isEmpty = function () {
            if (this.r.length === 0) {
                return true;
            } else if (this.r.length > 0) {
                return false;
            }
        };
        this.getSums = function () {
            var sums = {};
            for (var j in this.r)
                for (var i in this.c) {
                    if (this.c[i].t === 'INTEGER') {
                        if (!sums[this.c[i].i])
                            sums[this.c[i].i] = parseFloat(0);

                        sums[this.c[i].i] = sums[this.c[i].i] + parseFloat(this.r[j][this.c[i].i]);
                    }
                }
            return sums;
        };
        this.setColumn = function (columnName, id, type) {
            var column = {};
            column.n = columnName;
            if (!id)
                column.i = 'c_' + (this.c.length + 1);
            else {
                column.i = id;
            }
            if (type === null) {
                column.t = type;
            } else {
                column.t = CT.O;
            }
            this.c.push(column);
        };
        this.setSequence = function (seq) {
            var tmpC = [];
            for (var i in seq) {
                for (var j in this.c) {
                    if (seq[i] === this.c[j].i) {
                        tmpC.push(this.c[j]);
                    }
                }
            }
            for (var j in this.c) {
                if ('id' === this.c[j].i) {
                    tmpC.push(this.c[j]);
                }
            }
            this.c = tmpC;
        };
        this.getColumns = function () {
            return this.c;
        };
        this.getColumnNames = function () {
            var arr = [];
            for (var i in this.c) {
                arr.push(this.c[i].n);
            }
            return arr;
        };
        this.getColumnByName = function (h) {
            for (var i in this.c) {
                if (this.c[i].n === h) {
                    return this.c[i];
                }
            }
        };
        this.getColumnById = function (id) {
            for (var i in this.c) {
                if (this.c[i].i === id) {
                    return this.c[i];
                }
            }
        };
        this.insertRow = function (values) {
            var row = {};
            for (var i = 0; i < this.c.length; i++) {
                row[this.c[i].i] = values[i];
            }
            this.r.push(row);
        };
        this.setRow = function (row) {
            this.r.push(row);
        };
        this.setName = function (name) {
            this.tn = name;
        };
        this.getName = function () {
            return this.tn;
        };
        this.getRows = function () {
            return this.r;
        };
        this.getRow = function (i) {
            return this.r[i];
        };
        this.removeAllRows = function () {
            this.r = [];
        };
    };
    "use strict";
    var Filter = function () {
        this.startLimit = '1';
        this.endLimit = '25';
        this.includedFields;
        this.groupBy;
        this.unGroupBy;
        this.sumBy;
        this.intervalField;
        this.intervalStartDate;
        this.intervalEndDate;
        this.intervalSeperator;
        this.asc;
        this.desc;
        this.excludedFields;
        this.insertFilterData = function (data) {
            var keys = Object.keys(data);
            for (var i in keys) {
                if (data[keys[i]]) {
                    this[keys[i]] = data[keys[i]];
                }
            }
        };
        this.getStartLimit = function () {
            return this.startLimit;
        };
        this.setAsc = function (columnName) {
            this.removeSort();
            this.asc = columnName;
        };
        this.setGroupBy = function (s) {
            this.groupBy = s;
        };
        this.removeGroupBy = function () {
            delete this.groupBy;
        };
        this.setSumBy = function (field) {
            this.sumBy = field;
        };
        this.removeSumBy = function () {
            delete this.sumBy;
        };
        this.setIntervalField = function (field) {
            this.intervalField = field;
        };
        this.removeIntervalField = function () {
            delete this.intervalField;
        };
        this.setIntervalStartDate = function (startDate) {
            this.intervalStartDate = startDate;
        };
        this.removeIntervalStartDate = function () {
            delete this.intervalStartDate;
        };
        this.setIntervalEndDate = function (endDate) {
            this.intervalEndDate = endDate;
        };
        this.removeIntervalEndDate = function () {
            delete this.intervalEndDate;
        };
        this.setIntervalSeperator = function (e) {
            this.intervalSeperator = e;
        };
        this.removeIntervalSeperator = function () {
            delete this.intervalSeperator;
        };
        this.getAsc = function () {
            return this.asc;
        };
        this.setDesc = function (columnName) {
            this.removeSort();
            this.desc = columnName;
        };
        this.getDesc = function () {
            return this.desc;
        };
        this.removeSort = function () {
            delete this.desc;
            delete this.asc;
        };
        this.getEndLimit = function () {
            return this.endLimit;
        };
        this.getIncludedFields = function () {
            return this.includedFields;
        };
        this.getExcludedFields = function () {
            return this.excludedFields;
        };
        this.setStartLimit = function (sl) {
            this.startLimit = sl.toString();
        };
        this.getStartLimit = function () {
            return this.startLimit;
        };
        this.setEndLimit = function (el) {
            this.endLimit = el.toString();
        };
        this.setIncludedFields = function (ifi) {
            this.includedFields = ifi;
        };
        this.setExcludedFields = function (efi) {
            this.excludedFields = efi;
        };
        this.removeFilter = function (field) {
            delete this[field];
        };
        this.setGratherThan = function (field, arg) {
            this[field] = 'GT%' + arg;
        };
        this.setLessEqual = function (field, arg) {
            this[field] = 'LE%' + arg;
        };
        this.setGratherEqual = function (field, arg) {
            this[field] = 'GE%' + arg;
        };
        this.setNotEqual = function (field, arg) {
            this[field] = 'NE%' + arg;
        };
        this.setLessThan = function (field, arg) {
            this[field] = 'LT%' + arg;
        };
        this.setBetween = function (field, arg0, arg1) {
            this[field] = arg0 + '%BN%' + arg1;
        };
        this.setIn = function (field, arg0) {
            this[field] = arg0;
        };
        this.setEqual = function (field, arg0) {
            this[field] = arg0;
        };
    };
    "use strict";
    var DataList = function (element, settings) {
        var settings = settings;
        var element = element;
        var currentTable;
        var defaults = {
            method: 'post',
            witdh: '100%',
            hide: ['ID'],
            pagination: true,
            height: '300px',
            checkable: true,
            scrollable: false,
            scrollWidth: '17px',
            sortable: true,
            filters: true,
            header: true,
            excel: true,
            print: true,
            fileUrl: null,
            columnSelect: false,
            nubmers: true,
            footer: true,
            refresh: false,
            deletable: true,
            editable: true,
            groupBy: false,
            copiable: false,
            timeInterval: false,
            edit: function () {},
            show: function () {},
            copy: function () {}
        };
        var conf = $.extend({}, settings);
        var cfg = $.extend(true, {}, defaults, conf);
        this.update = function () {
            self._reload();
        };
        this.updateWithData = function (data) {
            $('.filters-container').empty();
            currentTable.f.insertFilterData(data.f);
            var columns = currentTable.getColumns();
            var c = data.c;
            for (var i in columns) {
                columns[i].checked = false;
                for (var j in c) {
                    if (c[j].i === columns[i].i) {
                        if (c[j].checked) {
                            columns[i].checked = true;
                        }
                    }
                }
            }
            if (data.f.groupBy) {
                cfg.groupBy = false;
                cfg.unGroupBy = true;
            }
            self._changeCSwhenLoad(columns);
            handlers._getFilter(data);
            self._reload();
        };
        this.getTableData = function () {
            return currentTable;
        };
        this.pivot = function (s) {
            var table = new Table();
            var rows = currentTable.getRows();
            var x = [], y = [], totalSum = 0;
            table.setColumn('');
            for (var i in rows) {

                if (rows[i][s.xaxis.id] || rows[i][s.xaxis.id] === '') {
                    var k = rows[i][s.xaxis.id];
                    if (!x.includes(k)) {
                        table.setColumn(k);
                        x.push(k);
                    }
                }
            }
            table.setColumn('');
            for (var i in rows) {
                if (rows[i][s.zaxis.id] || rows[i][s.zaxis.id] === '') {
                    var k = rows[i][s.zaxis.id];
                    if (!y.includes(k)) {
                        y.push(k);
                    }
                }
            }
            for (var i in y) {
                var row = {};
                row['c_' + 1] = y[i];
                var sum = 0;
                for (var j in x) {
                    for (var k in rows) {
                        if ((rows[k][s.zaxis.id] === y[i]) && (rows[k][s.xaxis.id] === x[j])) {
                            row['c_' + (parseFloat(j) + 2)] = rows[k][s.yaxis.id];
                            sum = sum + parseFloat(rows[k][s.yaxis.id]);
                        } else if ((rows[k][s.zaxis.id] === y[i]) && (rows[k][s.xaxis.id] !== x[j])) {
                            if (!row['c_' + (parseFloat(j) + 2)] && row['c_' + (parseFloat(j) + 2)] !== 0)
                                row['c_' + (parseFloat(j) + 2)] = '0';
                        }
                    }

                }
                sum = Math.round(sum * 100) / 100;
                row['c_' + (x.length + 2)] = sum;
                totalSum = totalSum + sum;
                table.setRow(row);
            }
            var row = {};
            row['c_' + 1] = '';
            for (var i in x) {
                var sum = 0;
                for (var k in rows) {
                    if (rows[k][s.xaxis.id] === x[i]) {
                        sum = sum + parseFloat(rows[k][s.yaxis.id]);
                    }
                }
                sum = Math.round(sum * 100) / 100;
                row['c_' + (parseFloat(i) + 2)] = sum;
            }
            totalSum = Math.round(totalSum * 100) / 100;
            row['c_' + (x.length + 2)] = totalSum;
            table.setRow(row);
            return table;
        };
        this.selectedColumns = function () {
            return self._getSelectedColumns();
        };
        this.getSingleSelectedRow = function () {
            var ids = this.getCheckedIds();
            if (ids.length !== 1) {
                throw new UserException("Invalid Selection");
            }
            return handlers._sh._singleSelect(ids[0]);
        };
        this.getCheckedIds = function () {
            var arr = [];
            $(element).find('.tbc tbody .chk input:checked').each(function () {
                var id = $(this).closest('tr').find('td[col-id=id]').html();
                arr.push(id);
            });
            return arr;
        };
        this.getFilterData = function () {
            return currentTable.f;
        };
        var self = {
            _widths: {},
            _getSelectedColumns: function () {
                var arr = [];
                var cs = currentTable.getColumns();
                for (var i in cs) {
                    if (cs[i].checked)
                        arr.push(cs[i]);
                }
                if (!arr.length)
                    return cs;
                return arr;
            },
            _equalize: function () {
                var columns = currentTable.getColumns();
                var sum = $(element).find('.tbc tbody').outerWidth();
                for (var i in columns) {
                    var arr = [], o = {};
                    o.q = '.thc th[col-id=' + columns[i].i + ']';
                    o.w = $(element).find(o.q).first().outerWidth();
                    arr.push(o);
                    o = {};
                    o.q = '.tbc thead th[col-id=' + columns[i].i + ']';
                    o.w = $(element).find(o.q).first().outerWidth();
                    arr.push(o);
                    o = {};
                    o.q = '.tbc tbody td[col-id=' + columns[i].i + ']';
                    o.w = $(element).find(o.q).first().outerWidth();
                    arr.push(o);
                    o = {};
                    o.q = '.tbc tfoot th[col-id=' + columns[i].i + ']';
                    o.w = $(element).find(o.q).first().outerWidth();
                    arr.push(o);
                    o = {};
                    o.q = '.tfc th[col-id=' + columns[i].i + ']';
                    o.w = $(element).find(o.q).first().outerWidth();
                    arr.push(o);
                    var m = Math.max(arr[0].w, arr[1].w, arr[2].w, arr[3].w, arr[4].w);
                    for (var i in arr) {
                        $(element).find(arr[i].q).css('min-width', m);
                        $(element).find(arr[i].q).css('max-width', m);
                    }
                }
                if (cfg.numbers) {
                    var d = $(element).find('tbody .numbers').first().outerWidth();
                    var c = $(element).find('.thc .numbers').first().outerWidth();
                    var m2 = Math.max(c, d);
                    element.find('.numbers').css('min-width', '20px');
                }
                if (cfg.checkable) {
                    element.find('.chk').css('min-width', '20px');
                }

                if (!cfg.footer)
                    $(element).find('.tbc').on('scroll', function () {
                        $(element).find('.thc').scrollLeft($(this).scrollLeft());
                    });
                else {
                    $(element).find('.tfc').on('scroll', function () {
                        $(element).find('.thc').scrollLeft($(this).scrollLeft());
                        $(element).find('.tbc').scrollLeft($(this).scrollLeft());
                    });
                    var columns = currentTable.getColumns();
                    var sums = currentTable.getSums();
                    for (var k in columns)
                        if (sums[columns[k].i]) {
                            var qu2 = '.tfc th[col-id=' + columns[k].i + ']';
                            $(element).find(qu2).first().html(Number((sums[columns[k].i]).toFixed(1)));
                        }
                }
                var body = $(element).find('.tbc');
                var table = body.find('table');

                var pag = $(element).find('.lpc');
                var pagw = pag.width();

                var bodyw = table.width();
                var bodyh = table.height();
                var elw = $(element).width();
                if (elw > bodyw) {
                    element.find('table').width('100%');
                }
                if (bodyh > cfg.height.toInt()) {
                    $(element).css('padding-right', cfg.scrollWidth);
                    body.css('margin-right', ('-' + cfg.scrollWidth));
                } else {
                    $(element).css('padding-right', 0);
                    body.css('margin-right', 0);
                }

            },
            _autoconfig: function () {
                var del = cfg.deleteService;
                var upt = cfg.updateService;
                if (!del) {
                    cfg.deletable = false;
                }
                if (!upt) {
                    cfg.editable = false;
                }
//                function isValidURL(url) {
//                    var isValid = false;
//                    $.ajax({
//                        url: url,
//                        type: "post",
//                        async: false,
//                        dataType: "json",
//                        success: function () {
//                            isValid = true;
//                        },
//                        error: function () {
//                            isValid = false;
//                        }
//                    });
//
//                    return isValid;
//                }
            },
            _init: function () {
                self._autoconfig();
                var url = cfg.listService, method = cfg.method, table = new Table();
                var data = {b: table.f};
                $.ajax({
                    url: url,
                    type: method,
                    async: false,
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (d, s, r) {
                        table.insertData(d.res);
                        table.setRowCount(d.b.rowCount);
                        if (d.b.sequence)
                            table.setSequence(d.b.sequence.split(','));
                        currentTable = table;
                        self._print(table);
                    },
                    error: function (r, s, e) {

                    }
                });
            },
            _reload: function () {
                var url = cfg.listService;
                var method = cfg.method;
                var data = {};
                data.b = currentTable.f;
                self._showLoading();
                $.ajax({
                    url: url,
                    type: method,
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (d, s, r) {
                        var oldrow = currentTable.getRowCount();
                        currentTable.setRowCount(d.b.rowCount);
                        var newrow = currentTable.getRowCount();
                        currentTable.insertRows(d.res.r);
                        self._printTable();

                        if (oldrow !== newrow && newrow)
                            self._createP($(element).find('.lpc'));
                        self._equalize();
                    },
                    error: function (r, s, e) {

                    }
                });
            },
            _showLoading: function () {
                var div = document.createElement('div');
                $(div).addClass('loading');
                var icon = document.createElement('i');
                $(icon).addClass('notched circle loading icon huge');
                $(div).append(icon);
                $(element).find('.tbc').append(div);
            },
            _hideLoading: function () {
                $(element).find('.loading').remove();
            },
            _print: function () {
                if (cfg.pagination)
                    self._printPagination();
                self._printTable();
                self._equalize();
            },
            _printTable: function () {
                $(element).find('.container').remove();
                var container = $('<div></div>');
                container.addClass('container');
                var body = $('<div></div>');
                body.addClass('tbc');
                if (!cfg.footer)
                    body.css('overflow', 'auto');
                else {
                    body.css('overflow-x', 'hidden');
                    body.css('overflow-y', 'auto');
                }
                body.css('position', 'relative');
                body.css('border', '0px');
                body.css('max-height', 450);
                $(element).append(container);
                if (cfg.header) {
                    var header = $('<div></div>');
                    header.addClass('thc');
                    header.css('overflow', 'hidden');
                    header.css('border', '0px');
                    header.css('width', '100%');
                    self._printHeader(header);
                    container.append(header);
                }

                self._printBody(body);
                container.append(body);
                if (cfg.footer) {
                    var footer = $('<div></div>');
                    footer.addClass('tfc');
                    footer.css('overflow', 'auto');
                    footer.css('width', '100%');
                    self._printFooter(footer);
                    container.append(footer);
                }

            },
            _printBody: function (body) {
                body.empty();
                var table = $('<table></table>');
                table.addClass('datalist');
                table.attr('cellspacing', '0');
                table.attr('role', 'grid');
                if (cfg.header) {
                    var thead = $('<thead></thead>');
                    var tr = $('<tr></tr>');
                    tr.attr('role', 'row');
                    tr.css('height', '0px');
                    if (cfg.checkable) {
                        var c = $('<th></th>');
                        $(c).addClass('chk');
                        c.css('padding-top', '0px');
                        c.css('padding-bottom', '0px');
                        tr.append(c);
                    }
                    if (cfg.deletable) {
                        var dtd = $('<th></th>');
                        dtd.attr('tabindex', '0');
                        dtd.attr('rowspan', '1');
                        dtd.attr('colspan', '1');
                        $(dtd).addClass('_db _ih');
                        tr.append(dtd);
                    }
                    if (cfg.editable) {
                        var etd = $('<th></th>');
                        etd.attr('tabindex', '0');
                        etd.attr('rowspan', '1');
                        etd.attr('colspan', '1');
                        $(etd).addClass('_eb _ih');
                        tr.append(etd);
                    }
                    if (cfg.copiable) {
                        var etd = $('<th></th>');
                        etd.attr('tabindex', '0');
                        etd.attr('rowspan', '1');
                        etd.attr('colspan', '1');
                        $(etd).addClass('_cb _ih');
                        tr.append(etd);
                    }
                    if (cfg.numbers) {
                        var c = $('<th></th>');
                        c.addClass('numbers _ih');
                        c.attr('tabindex', '0');
                        c.attr('rowspan', '1');
                        c.attr('colspan', '1');
                        tr.append(c);
                    }

                    var columns = currentTable.getColumns();
                    for (var i in columns) {
                        var th = $('<th></th>');
                        th.attr('tabindex', '0');
                        th.attr('rowspan', '1');
                        th.attr('colspan', '1');
                        th.attr('col-id', columns[i].i);
                        var div = $('<div></div>');
                        div.css('height', '0px');
                        div.css('overflow', 'hidden');
                        div.addClass('zero-size');
                        div.html(columns[i].n);
                        th.append(div);
                        th.addClass('_ih');
                        if ($.inArray(columns[i].n, cfg.hide) > -1 || (!columns[i].checked && cfg.columnSelect)) {
                            th.addClass('_hc');
                        }
                        tr.append(th);
                    }


                    thead.append(tr);
                    table.append(thead);
                }

                var tbody = $('<tbody></tbody>');
                var rows = currentTable.getRows();
                if (!rows.length) {
                    var tr = $('<tr></tr>');
                    tr.attr('role', 'row');
                    var c = $('<td></td>');
                    c.addClass('_et');
                    c.html('Nothing Found!');
                    var colspan = $(element).find('.thc th:not(._hc)').length;
                    c.attr('colspan', colspan);
                    tr.append(c);
                    tbody.append(tr);
                }
                if (cfg.uniqueColumn && rows[0][cfg.uniqueColumn]) {
                    var currentUniqueColumnData = rows[0][cfg.uniqueColumn];
                    var change = false;
                }
                for (var i in rows) {
                    var tr = $('<tr></tr>');
                    tr.attr('role', 'row');
                    if (cfg.uniqueColumn && rows[i][cfg.uniqueColumn])
                    {
                        if (currentUniqueColumnData !== rows[i][cfg.uniqueColumn]) {
                            change = !change;
                            currentUniqueColumnData = rows[i][cfg.uniqueColumn];
                        }
                        change ? tr.addClass('odd') : tr.addClass('even');
                    }
                    if (cfg.checkable) {
                        var c = $('<td></td>');
                        $(c).addClass('chk');
                        var inp = $('<input>');
                        inp.click(function (e) {
                            e.stopPropagation();
                        });
                        inp.attr('type', 'checkbox');
                        c.append(inp);
                        tr.append(c);
                    }
                    if (cfg.deletable) {
                        var deleteCell = $('<td></td>');
                        $(deleteCell).addClass('_db');
                        var del = createDeleteButton();
                        deleteCell.append(del);
                        tr.append(deleteCell);
                    }
                    if (cfg.editable) {
                        var editCell = $('<td></td>');
                        $(editCell).addClass('_eb');
                        var edit = createEditButton();
                        editCell.append(edit);
                        tr.append(editCell);
                    }
                    if (cfg.copiable) {
                        var editCell = $('<td></td>');
                        $(editCell).addClass('_cb');
                        var edit = createCopyButton();
                        editCell.append(edit);
                        tr.append(editCell);
                    }
                    if (cfg.numbers) {
                        var num = $('<td></td>');
                        num.addClass('numbers');
                        $(num).css('text-align', 'center');
                        $(num).css('width', '20px');
                        num.html(currentTable.f.startLimit.toInt() + i.toInt());
                        tr.append(num);
                    }

                    for (var j in columns) {
                        var td = $('<td></td>');
                        if (columns[j].t === CT.IMAGE) {
                            var link = $('<span/>', {'image-url': rows[i][columns[j].i]});
                            link.css('color', 'blue');
                            link.html('image');
                            td.append(link);
                            link.click(function () {
                                handlers._showImage($(this).attr('image-url'));
                            });
                        } else
                            td.html(rows[i][columns[j].i]);
                        td.attr('col-id', columns[j].i);
                        if ($.inArray(columns[j].n, cfg.hide) > -1 || (!columns[j].checked && cfg.columnSelect)) {
                            td.addClass('_hc');
                        }
                        if (cfg.dataEnumColumns)
                            if (cfg.dataEnumColumns.includes(columns[j].i)) {
                                var keys = Object.keys(cfg.dataEnumColors);
                                for (var n in keys)
                                    if (keys[n].toLowerCase() === rows[i][columns[j].i].toLowerCase()) {
                                        td.css('color', cfg.dataEnumColors[keys[n]]);
                                    }
                            }
                        tr.append(td);
                    }
                    tbody.append(tr);
                }

                table.append(tbody);
                if (cfg.footer) {
                    var tfoot = $('<tfoot></tfoot>');
                    var tr = $('<tr></tr>');
                    tr.attr('role', 'row');
                    tr.css('height', '0px');
                    if (cfg.checkable) {
                        var c = $('<th></th>');
                        c.attr('tabindex', '0');
                        c.attr('rowspan', '1');
                        c.attr('colspan', '1');
                        $(c).addClass('chk');
                        c.css('padding-top', '0px');
                        c.css('padding-bottom', '0px');
                        c.css('border-top-width', '0px');
                        c.css('border-bottom-width', '0px');
                        c.css('height', '0px');
                        var div = $('<div></div>');
                        div.addClass('zero-size');
                        c.append(div);
                        tr.append(c);
                    }
                    if (cfg.deletable) {
                        var dtd = $('<th></th>');
                        dtd.attr('tabindex', '0');
                        dtd.attr('rowspan', '1');
                        dtd.attr('colspan', '1');
                        dtd.css('padding-top', '0px');
                        dtd.css('padding-bottom', '0px');
                        dtd.css('border-top-width', '0px');
                        dtd.css('border-bottom-width', '0px');
                        dtd.css('height', '0px');
                        var div = $('<div></div>');
                        div.addClass('zero-size');
                        dtd.append(div);
                        $(dtd).addClass('_db');
                        tr.append(dtd);
                    }
                    if (cfg.editable) {
                        var etd = $('<th></th>');
                        etd.attr('tabindex', '0');
                        etd.attr('rowspan', '1');
                        etd.attr('colspan', '1');
                        etd.css('padding-top', '0px');
                        etd.css('padding-bottom', '0px');
                        etd.css('border-top-width', '0px');
                        etd.css('border-bottom-width', '0px');
                        etd.css('height', '0px');
                        var div = $('<div></div>');
                        div.addClass('zero-size');
                        etd.append(div);
                        $(etd).addClass('_eb');
                        tr.append(etd);
                    }
                    if (cfg.copiable) {
                        var etd = $('<th></th>');
                        etd.attr('tabindex', '0');
                        etd.attr('rowspan', '1');
                        etd.attr('colspan', '1');
                        etd.css('padding-top', '0px');
                        etd.css('padding-bottom', '0px');
                        etd.css('border-top-width', '0px');
                        etd.css('border-bottom-width', '0px');
                        etd.css('height', '0px');
                        var div = $('<div></div>');
                        div.addClass('zero-size');
                        etd.append(div);
                        $(etd).addClass('_cb');
                        tr.append(etd);
                    }
                    if (cfg.numbers) {
                        var c = $('<th></th>');
                        c.attr('tabindex', '0');
                        c.attr('rowspan', '1');
                        c.attr('colspan', '1');
                        c.addClass('numbers');
                        c.css('padding-top', '0px');
                        c.css('padding-bottom', '0px');
                        c.css('border-top-width', '0px');
                        c.css('border-bottom-width', '0px');
                        c.css('height', '0px');
                        var div = $('<div></div>');
                        div.addClass('zero-size');
                        c.append(div);
                        tr.append(c);
                    }

                    var columns = currentTable.getColumns();
                    var sums = currentTable.getSums();
                    for (var i in columns) {
                        var th = $('<th></th>');
                        th.attr('tabindex', '0');
                        th.attr('rowspan', '1');
                        th.attr('colspan', '1');
                        th.attr('col-id', columns[i].i);
                        var div = $('<div></div>');
                        div.css('height', '0px');
                        div.css('overflow', 'hidden');
                        div.addClass('zero-size');
                        if (sums[columns[i].i])
                            div.html('S:' + sums[columns[i].i]);
                        th.append(div);
                        th.css('padding-top', '0px');
                        th.css('padding-bottom', '0px');
                        th.css('border-top-width', '0px');
                        th.css('border-bottom-width', '0px');
                        th.css('height', '0px');
                        if ($.inArray(columns[i].n, cfg.hide) > -1 || (!columns[i].checked && cfg.columnSelect)) {
                            th.addClass('_hc');
                        }
                        tr.append(th);
                    }



                    tfoot.append(tr);
                    table.append(tfoot);
                }

                body.append(table);
                function createDeleteButton() {
                    var b = createIconButton('remove icon', '_dbtn');
                    b.click(handlers._onDeleteAction);
                    return b;
                }
                function createCopyButton() {
                    var b = createIconButton('copy icon', '_cbtn');
                    b.click(handlers._onCopyAction);
                    return b;
                }
                function createEditButton() {
                    var b = createIconButton('edit icon', '_ebtn');
                    b.click(handlers._onEditAction);
                    return b;
                }
                function createIconButton(iconclasses, buttonclasses) {
                    var b = $('<button/>', {'class': buttonclasses});
                    var i = $('<i/>', {'class': iconclasses});
                    return b.append(i);
                }
                body.find('tbody tr').dblclick(handlers._showRow);
            },
            _printHeader: function (header) {
                var inner = $('<div></div>');
                inner.addClass('header-inner');
                inner.css('box-sizing', 'content-box');

                var table = $('<table></table>');
                table.addClass('datalist');
                table.attr('cellspacing', '0');
                table.attr('role', 'grid');
                table.css('margin-left', '0px');
                var thead = $('<thead></thead>');
                var tr = $('<tr></tr>');
                tr.attr('role', 'row');
                if (cfg.checkable) {
                    var c = $('<th></th>');
                    var checkall = $('<input>');
                    checkall.attr('type', 'checkbox');
                    checkall.change(handlers._onCheckAll);
                    c.append(checkall);
                    $(c).addClass('chk');
                    tr.append(c);
                }
                if (cfg.deletable) {
                    var dtd = $('<th></th>');
                    dtd.attr('tabindex', '0');
                    dtd.attr('rowspan', '1');
                    dtd.attr('colspan', '1');
                    $(dtd).addClass('_db');
                    tr.append(dtd);
                }
                if (cfg.editable) {
                    var etd = $('<th></th>');
                    $(etd).addClass('_eb');
                    tr.append(etd);
                }
                if (cfg.copiable) {
                    var etd = $('<th></th>');
                    $(etd).addClass('_cb');
                    tr.append(etd);
                }
                if (cfg.numbers) {
                    var c = $('<th></th>');
                    c.addClass('numbers');
                    var span = document.createElement('span');
                    $(span).html("№");
                    c.append(span);
                    tr.append(c);
                }
                var columns = currentTable.getColumns();
                for (var i in columns) {
                    var th = $('<th></th>');
                    th.attr('tabindex', '0');
                    th.attr('rowspan', '1');
                    th.attr('colspan', '1');
                    th.attr('col-id', columns[i].i);
                    if ($.inArray(columns[i].n, cfg.hide) > -1 || (!columns[i].checked && cfg.columnSelect)) {
                        th.addClass('_hc');
                    }
                    var div = $('<div></div>');
                    div.addClass('thc-hd');
                    if (cfg.sortable) {
                        var icon = document.createElement('span');
                        $(icon).addClass('sicon');
                        var ico = document.createElement('i');
                        $(ico).addClass('sort icon');
                        $(ico).attr('data-t', 'n');
                        icon.appendChild(ico);
                        th.append(icon);
                        $(icon).click(handlers._onSortClick);
                        if (currentTable.f.getAsc() === columns[i].i) {
                            $(ico).addClass('descending');
                        }
                        if (currentTable.f.getDesc() === columns[i].i) {
                            $(ico).addClass('ascending');
                        }
                    }
                    if (cfg.timeInterval && columns[i].t === 'DATE') {
                        var interval = $('<span></span>');
                        interval.addClass('interval-i');
                        var intIc = $('<i></i>');
                        intIc.addClass('interval icon');
                        interval.append(intIc);
                        th.append(interval);
                        var intData = $('<div></div>');
                        intData.addClass('interval-container');
                        var intUl = $('<ul></ul>');
                        var d = $('<li></li>');
                        $(d).click(handlers._addInterval);
                        d.html('Günlük');
                        $(d).addClass('1 sltd');
                        var w = $('<li></li>');
                        w.html('Həftəlik');
                        $(w).addClass('2');
                        $(w).click(handlers._addInterval);
                        var m = $('<li></li>');
                        m.html('Aylıq');
                        $(m).addClass('3');
                        $(m).click(handlers._addInterval);
                        var rub = $('<li></li>');
                        rub.html('Rüblük');
                        $(rub).addClass('4');
                        $(rub).click(handlers._addInterval);
                        var y = $('<li></li>');
                        y.html('İllik');
                        $(y).addClass('5');
                        $(y).click(handlers._addInterval);
                        intUl.append(d);
                        intUl.append(w);
                        intUl.append(m);
                        intUl.append(rub);
                        intUl.append(y);
                        intData.append(intUl);
                        interval.append(intData);
                        $(interval).click(handlers._onTimeIntervalClick);
                    }
                    if (cfg.groupBy && columns[i].t === 'INTEGER') {
                        var grp = $('<span></span>');
                        $(grp).addClass('grp ' + columns[i].i);
                        var grpI = $('<i></i>');
                        grpI.addClass('object group icon');
                        grp.append(grpI);
                        grp.click(handlers._onGroupByAction);
                        th.append(grp);
                    }
                    if (cfg.unGroupBy && columns[i].t === 'INTEGER') {
                        var unGrp = $('<span></span>');
                        $(unGrp).addClass('unGrp ' + columns[i].i);
                        var unGrpI = $('<i></i>');
                        unGrpI.addClass('object ungroup icon');
                        unGrp.append(unGrpI);
                        unGrp.click(handlers._onUngroupByAction);
                        th.append(unGrp);
                        cfg.unGroupBy = false;
                        cfg.groupBy = true;
                    }
                    var t = document.createElement('span');
                    $(t).addClass('h-content');
                    t.innerHTML = columns[i].n;
                    th.append(t);


                    if (cfg.filters) {
                        var ficon = document.createElement('span');
                        $(ficon).addClass('ficon');
                        var fico = document.createElement('i');
                        $(fico).addClass('filter icon');
                        ficon.appendChild(fico);
                        th.append(ficon);
                        $(ficon).click(handlers._onFilterClick);
                    }
                    th.find('span').wrapAll("<div class='thc-hd'></div>");
                    tr.append(th);
                }


                thead.append(tr);
                table.append(thead);
                inner.append(table);
                header.append(inner);
            },
            _printFooter: function (footer) {
                var inner = $('<div></div>');
                inner.addClass('footer-inner');
                inner.css('box-sizing', 'content-box');
                var table = $('<table></table>');
                table.addClass('datalist');
                table.attr('cellspacing', '0');
                table.attr('role', 'grid');
                table.css('margin-left', '0px');
                var thead = $('<tfoot></tfoot>');
                var tr = $('<tr></tr>');
                tr.attr('role', 'row');
                if (cfg.checkable) {
                    var c = $('<th></th>');
                    c.attr('tabindex', '0');
                    c.attr('rowspan', '1');
                    c.attr('colspan', '1');
                    c.css('min-width', '20px');
                    c.addClass('chk');
                    tr.append(c);
                }
                if (cfg.deletable) {
                    var dtd = $('<th></th>');
                    dtd.attr('tabindex', '0');
                    dtd.attr('rowspan', '1');
                    dtd.attr('colspan', '1');
                    $(dtd).addClass('_db');
                    tr.append(dtd);
                }
                if (cfg.editable) {
                    var etd = $('<th></th>');
                    $(etd).addClass('_eb');
                    tr.append(etd);
                }
                if (cfg.copiable) {
                    var etd = $('<th></th>');
                    $(etd).addClass('_cb');
                    tr.append(etd);
                }
                if (cfg.numbers) {
                    var c = $('<th></th>');
                    c.attr('tabindex', '0');
                    c.attr('rowspan', '1');
                    c.attr('colspan', '1');
                    c.addClass('numbers');
                    tr.append(c);
                }

                var columns = currentTable.getColumns();
                for (var i in columns) {
                    var td = $('<th></th>');
                    td.attr('tabindex', '0');
                    td.attr('rowspan', '1');
                    td.attr('colspan', '1');
                    td.attr('col-id', columns[i].i);
                    if ($.inArray(columns[i].n, cfg.hide) > -1 || (!columns[i].checked && cfg.columnSelect)) {
                        td.addClass('_hc');
                    }
                    tr.append(td);
                }


                thead.append(tr);
                table.append(thead);
                inner.append(table);
                footer.append(inner);
            },
            _createOthers: function (pagination) {
                if (cfg.excel) {
                    var excel = createButtonWithIcon('file excel outline');
                    $(pagination).append(excel);
                    excel.addEventListener('click', handlers._onExportAction);
                }
                if (cfg.print) {
                    var print = createButtonWithIcon('print');
                    pagination.appendChild(print);
                    print.addEventListener('click', handlers._onPrintAction);
                }

                if (cfg.refresh) {
                    var refresh = createButtonWithIcon('refresh green');
                    pagination.appendChild(refresh);
                    refresh.addEventListener('click', refreshTable);
                }
                function createButtonWithIcon(ic) {
                    var b = document.createElement('button');
                    $(b).addClass('icon-button');
                    var i = document.createElement('i');
                    $(i).addClass('icon');
                    $(i).addClass('icon-size');
                    $(i).addClass(ic);
                    b.appendChild(i);
                    return b;
                }
                function refreshTable(e) {
                    self._reload();
                }
            },
            _createPC: function (pagination) {
                var select = document.createElement('select');
                $(select).addClass('lpcombo');
                $(select).css('display', 'inline-block');
                var arr = [5, 25, 50, 100, 500, 1000, 3000, 5000];
                for (var i in arr) {
                    var option = document.createElement('option');
                    option.value = arr[i];
                    option.innerHTML = arr[i];
                    $(select).append(option);
                    if (arr[i] === 25)
                        option.selected = true;
                }
                $(pagination).append(select);
                $(select).change(function () {
                    var n = $(this).val();
                    var k = 1;
                    self._changePaginationData(n, k);
                    self._createP(pagination);
                });
            },
            _createCS: function (pagination) {
                var columns = currentTable.getColumns();
                var btn = $('<button></button>');
                $(pagination).append(btn);
                btn.addClass('cs-multi');
                btn.html('Columns');
                var ul = $('<ul></ul>');
                ul.addClass('multi-container');
                var i = $('<i></i>');
                $(i).addClass('down triangle basic icon');
                btn.append(i);
                btn.append(ul);
                for (var i in columns) {
                    columns[i].checked = true;
                    var li = $('<li></li>'), hed = $('<span></span>');
                    hed.html(columns[i].n);
                    var cb = $('<input>');
                    cb.attr('type', 'checkbox');
                    cb.attr('checked', true);
                    cb.addClass('multi-cb');
                    cb.attr('f-id', columns[i].i);
                    if ($.inArray(columns[i].n, cfg.hide) > -1 || (!columns[i].checked && cfg.columnSelect)) {
                        li.addClass('_hc');
                    }
                    cb.change(function (e) {
                        var id = $(e.target).attr('f-id');
                        currentTable.getColumnById(id).checked = e.target.checked;
                        self._printTable();
                        self._equalize();
//                        var q = '*[col-id=' + id + ']';
//                        if (!e.target.checked) {
////                            $(q).addClass('_hc');
//                            $(q).addClass('hideCol');
////                            $(q).hide();
//                            self._equalize();
//                        } else {
////                            $(q).removeClass('_hc');
//                            $(q).removeClass('hideCol');
////                            $(q).show();
//                            self._equalize();
//                        }
                    });
                    ul.append(li.append(cb).append(hed));
                }

                btn.click(function (e) {
                    e.stopPropagation();
                    if (!$(this).find('ul').is(':visible'))
                        $(this).find('ul').show();
                    else if ($(e.target).hasClass('cs-multi'))
                        $(this).find('ul').hide();
                });
                $(document).click(function (e) {
                    if (!$(e.target).closest('.multi-container').length) {
                        $('.multi-container').hide();
                    }
                });

            },
            _createP: function (pagination) {
                $(pagination).find('.lpu').detach();
                var ul = document.createElement('ul');
                $(ul).addClass('lpu');
                $(pagination).append(ul);
                var f = document.createElement('li');
                f.innerHTML = "&#10218;";
                $(f).addClass('lpl');
                var p = document.createElement('li');
                p.innerHTML = "&#10216;";
                $(p).addClass('lpl');
                var ldc = document.createElement('li');
                $(ldc).addClass('lpl');
                var n = document.createElement('li');
                n.innerHTML = "&#10217;";
                $(n).addClass('lpl');
                var l = document.createElement('li');
                l.innerHTML = "&#10219;";
                $(l).addClass('lpl');
                ul.appendChild(f);
                ul.appendChild(p);
                ul.appendChild(ldc);
                ul.appendChild(n);
                ul.appendChild(l);
                var rsize = $(pagination).find('.lpcombo').val();
                var rc = currentTable.getRowCount().toInt();
                var pn = Math.ceil(rc / rsize);
                $(ldc).append($('<span>1</span><span>of</span><span>' + pn + '</span>'));
                var ud = document.createElement('ul');
                $(ud).addClass('lplpc');
                var i = document.createElement('i');
                $(i).addClass('down triangle basic icon');
                $(ldc).append(i);
                for (var i = 1; i <= pn; i++) {
                    var li = document.createElement('li');
                    li.innerHTML = i;
                    $(ul).append(li);
                    $(li).addClass('lplp');
                    if (i === 1) {
                        $(li).addClass('active');
                    }
                    ud.appendChild(li);
                }
                ldc.appendChild(ud);
                $('.lplp').click(function (e) {
                    $(this).parent().find('.lplp').removeClass('active');
                    $(this).addClass('active');
                    var n = $(pagination).find('.lpcombo').val();
                    var k = $(this).text();
                    self._changePaginationData(n, k);
                    $(ldc).find('span').first().html($(this).text());
                    $('#data').fadeOut();
                });
                $(p).click(function () {
                    var pbox = $(this).closest('.lpu').find('.lplpc');
                    var active = pbox.find('.active');
                    var value = active.text().toInt();
                    var count = 1;
                    if (value === count) {
                        return false;
                    }
                    active.removeClass('active');
                    pbox.find('li:nth-child(' + (value - 1) + ')').addClass('active');
                    var n = $(pagination).find('.lpcombo').val();
                    var k = value - 1;
                    self._changePaginationData(n, k);
                    $(ldc).find('span').first().html(value - 1);
                });
                $(n).click(function () {
                    var pbox = $(this).closest('.lpu').find('.lplpc');
                    var active = pbox.find('.active');
                    var value = active.text().toInt();
                    var count = pbox.children().length;
                    if (value === count)
                        return;
                    active.removeClass('active');
                    pbox.find('li:nth-child(' + (value + 1) + ')').addClass('active');
                    var n = $(pagination).find('.lpcombo').val();
                    var k = value + 1;
                    self._changePaginationData(n, k);
                    $(ldc).find('span').first().html(value + 1);
                });
                $(f).click(function () {
                    var pbox = $(this).closest('.lpu').find('.lplpc');
                    var active = pbox.find('.active');
                    active.removeClass('active');
                    pbox.find('li:nth-child(' + 1 + ')').addClass('active');
                    $(ldc).find('span').first().html(1);
                    var n = $(pagination).find('.lpcombo').val();
                    var k = 1;
                    self._changePaginationData(n, k);
                });
                $(l).click(function () {
                    var pbox = $(this).closest('.lpu').find('.lplpc');
                    var active = pbox.find('.active');
                    var lastChild = pbox.find('li:last-child');
                    active.removeClass('active');
                    lastChild.addClass('active');
                    var value = lastChild.text().toInt();
                    var n = $(pagination).find('.lpcombo').val();
                    var k = value;
                    self._changePaginationData(n, k);
                    $(ldc).find('span').first().html($(lastChild).text());
                });
                $(ldc).click(function () {
                    $(this).find('.lplpc').toggle();
                });
            },
            _changePaginationData: function (n, k) {
                k = parseInt(k);
                n = parseInt(n);
                var startLimit = (k - 1) * n + 1;
                var endLimit = k * n;
                currentTable.f.setStartLimit(startLimit);
                currentTable.f.setEndLimit(endLimit);
                self._reload();
            },
            _printPagination: function () {
                var pagination = document.createElement('div');
                $(pagination).addClass('lpc');
                $(element).append(pagination);
                self._createPC(pagination);
                if (cfg.columnSelect) {
                    self._createCS(pagination);
                }
                self._createP(pagination);
                self._createOthers(pagination);
            },
            _changeCSwhenLoad: function (columns) {
                for (var i in columns) {
                    var cb = $('.multi-cb');
                    if (columns[i].checked) {
                        cb[i].checked = columns[i].checked;
                    } else {
                        cb[i].checked = false;
                    }
                }
            }
        };
        var handlers = {
            _export: {
                _getExcelBlob: function () {
                    var data = [];
                    var wscols = [];
                    var columns = self._getSelectedColumns();
                    var row = [];
                    for (var i in columns) {
                        if (columns[i].i === 'id')
                            continue;
                        row.push(columns[i].n);
                        var o = {};
                        o.wch = columns[i].n.length + 2;
                        wscols.push(o);
                    }
                    data.push(row);
                    var rows = currentTable.getRows();
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
            },
            _sh: {
                _currentId: function (e) {
                    return $(e.target).closest('tr').find('td[col-id=id]').html();
                },
                _singleSelect: function (id) {
                    var resultData;
                    var json = {b: {}};
                    json.b.id = id;
                    $.ajax({
                        url: cfg.listService,
                        type: cfg.method,
                        async: false,
                        data: JSON.stringify(json),
                        contentType: "application/json",
                        success: function (data) {
                            var res = data.res;
                            var table = new Table();
                            table.insertData(res);
                            table.setRowCount(data.b.rowCount);
                            if (data.b.sequence)
                                table.setSequence(data.b.sequence.split(','));

                            resultData = table;
                        },
                        error: function (d, s, n) {
                            console.log("Something went wrong. Status: " + s);
                        }
                    });
                    return resultData;
                }
            },
            _fh: {
                _filterChangeListener: function (filcon, t, id) {
                    var arg0 = $(filcon).find('.filter-first').val();
                    var arg1 = $(filcon).find('.filter-second').val();
                    var type = $(filcon).find('select').val();
                    switch (t) {
                        case 'INTEGER':
                            handlers._fh._doIntegerFilter(id, type, arg0, arg1);
                            break;
                        case 'DATE':
                            handlers._fh._doDateFilter(id, type, arg0, arg1);
                            break;
                        case 'TIME':
                            handlers._fh._doTimeFilter(id, type, arg0, arg1);
                            break;
                    }
                },
                _doStringFilter: function (field, arr) {
                    var f = arr[0].name;
                    for (var i in arr) {
                        if (i !== '0') {
                            f = f + '%IN%' + arr[i].name;
                        }
                    }
                    currentTable.f.setIn(field, f);
                    self._reload();
                },
                _removeStringFilter: function (field) {
                    currentTable.f.removeFilter(field);
                    self._reload();
                },
                _doIntegerFilter: function (field, type, arg0, arg1) {
                    handlers._fh._doFilter(field, type, arg0, arg1);
                    self._reload();
                },
                _doFilter: function (field, type, arg0, arg1) {
                    if (type === 'eq') {
                        currentTable.f.setEqual(field, arg0);
                    }
                    if (type === 'gt') {
                        currentTable.f.setGratherThan(field, arg0);
                    }
                    if (type === 'lt') {
                        currentTable.f.setLessThan(field, arg0);
                    }
                    if (type === 'be') {
                        currentTable.f.setBetween(field, arg0, arg1);
                    }
                    if (type === 'ne') {
                        currentTable.f.setNotEqual(field, arg0);
                    }
                    if (type === 'le') {
                        currentTable.f.setLessEqual(field, arg0);
                    }
                    if (type === 'ge') {
                        currentTable.f.setGratherEqual(field, arg0);
                    }
                },
                _doDateFilter: function (field, type, arg0, arg1) {
                    arg0 = arg0.replace(/-/g, '');
                    if (arg1) {
                        arg1 = arg1.replace(/-/g, '');
                    }
                    handlers._fh._doFilter(field, type, arg0, arg1);
                    self._reload();
                },
                _doTimeFilter: function (field, type, arg0, arg1) {
                    arg0 = arg0.replace(/:/g, '') + '00';
                    if (arg1) {
                        arg1 = arg1.replace(/:/g, '') + '00';
                    }
                    handlers._fh._doFilter(field, type, arg0, arg1);
                    self._reload();
                },
                _distinct: function (a, param) {
                    return a.filter(function (item, pos, array) {
                        return array.map(function (mapItem) {
                            return mapItem[param];
                        }).indexOf(item[param]) === pos;
                    });
                },
                _filterByType: function (id, filcon, fd) {
                    var column = currentTable.getColumnById(id);
                    var ty = '';
                    if (column.t === 'STRING') {
                        var input = document.createElement('input');
                        $(input).attr('name', id);
                        $(input).attr('class', 'autoCompleteInput');
                        $(filcon).find('.lai').append(input);
//                        filcon.appendChild(input);
                        var list = $(filcon).closest('.lt');
                        var tg = $(input).tagSuggest({
                            data: cfg.listService,
                            dataUrlParams: {
                                field: id
                            },
                            selectionStacked: true,
                            resultAsString: true,
                            minChars: 0,
                            width: "auto",
                            name: id,
                            onSelect: function (selected) {
                                if (selected.length)
                                    handlers._fh._doStringFilter(id, selected, list);
                                else
                                    handlers._fh._removeStringFilter(id, list);
                            },
                            onRemove: function (selected) {
                                if (selected.length)
                                    handlers._fh._doStringFilter(id, selected, list);
                                else
                                    handlers._fh._removeStringFilter(id, list);
                            }
                        });
                        if (fd) {
                            var arr = fd.split('%IN%');
                            var data = [];
                            for (var i in arr) {
                                var obj = {};
                                obj.id = i;
                                obj.name = arr[i];
                                data.push(obj);
                            }
                            tg.setValue(data);
                        }
                    } else if (column.t === 'INTEGER') {
                        ty = 'number';
                    } else if (column.t === 'DATE') {
                        ty = 'date';
                    } else if (column.t === 'TIME') {
                        ty = 'time';
                    }
                    if (column.t !== '' && column.t !== 'STRING' && column.t !== 'ID') {
                        var input = document.createElement('input');
                        $(input).attr('name', id);
                        $(input).attr('type', ty);
                        $(input).attr('placeholder', 'Type or click here');
                        $(input).attr('class', 'filter-first');
                        $(input).on('input', function (e) {
                            handlers._fh._filterChangeListener(filcon, column.t, id);
                        });

                        var select = document.createElement('select');
                        $(select).attr('class', 'filter-combo');
                        var gt = document.createElement('option');
                        gt.innerHTML = '<';
                        gt.value = 'gt';
                        var lt = document.createElement('option');
                        lt.innerHTML = '>';
                        lt.value = 'lt';
                        var le = document.createElement('option');
                        le.innerHTML = '>=';
                        le.value = 'le';
                        var ge = document.createElement('option');
                        ge.innerHTML = '<=';
                        ge.value = 'ge';
                        var be = document.createElement('option');
                        be.innerHTML = '> and <';
                        be.value = 'be';
                        var ne = document.createElement('option');
                        ne.innerHTML = '><';
                        ne.value = 'ne';
                        var eq = document.createElement('option');
                        eq.innerHTML = '=';
                        eq.value = 'eq';
                        select.appendChild(eq);
                        select.appendChild(gt);
                        select.appendChild(lt);
                        select.appendChild(le);
                        select.appendChild(ge);
                        select.appendChild(ne);
                        select.appendChild(be);
                        select.addEventListener('change', function (e) {
                            var type = e.target.value;
                            if (type === 'be') {
                                var s = document.createElement('input');
                                $(s).attr('name', id);
                                $(s).attr('type', ty);
                                $(s).attr('placeholder', 'Type or click here');
                                $(s).attr('class', 'filter-second');
                                $(s).on('input', function () {
                                    handlers._fh._filterChangeListener(filcon, column.t, id);
                                });
                                $(e.target.parentNode).find('select').after(s);
                            } else {
                                var removable = $(e.target.parentNode).find('.filter-second');
                                if ($(removable).length) {
                                    removable.remove();
                                }
                            }
                            handlers._fh._filterChangeListener(filcon, column.t, id);
                        });
//                        filcon.appendChild(input);
                        $(filcon).find('.lai').append(input);
                        filcon.appendChild(select);
                    }

                    var b = document.createElement('button');
                    b.setAttribute('class', '_dbtn fdb');
                    $(b).click(function (e) {
                        var id = $(this).closest('.filter-container').attr('data-id');
                        currentTable.f.removeFilter(id);
                        $(this).closest('.filter-container').remove();
                        self._equalize();
                        self._reload();

                    });
                    var i = document.createElement('i');
                    i.setAttribute('class', 'remove icon');
                    b.appendChild(i);
                    $(filcon).append(b);
                    if (ty === 'date') {
                        $(input).val(new Date().toDateInputValue());
                        handlers._fh._filterChangeListener(filcon, column.t, id);
                    }
                    if (fd) {
                        $(input).val(fd);
                    }
                },
                _addFilter: function (e) {
                    var fc = $(element).find('.filters-container');
                    var id = $(e.target).closest('th').attr('col-id');
                    if (!(fc).length) {
                        fc = document.createElement('div');
                        $(fc).addClass('filters-container');
                        $(element).prepend(fc);
                    }
                    var column = currentTable.getColumnById(id);
                    if (column.t === 'ID') {
                        return false;
                    }
                    var filcon = document.createElement('div');
                    $(filcon).addClass('filter-container');
                    $(filcon).attr('data-id', id);
                    $(fc).append(filcon);
                    var div = document.createElement('div');
                    $(div).addClass('lai');
                    var p = document.createElement('label');
                    p.innerHTML = column.n + ":";
                    div.appendChild(p);
                    filcon.appendChild(div);
                    this._filterByType(id, filcon);
                    $(e.target).closest('.container').parent().parent().css('overflow', 'auto');
                },
                _makeAutocompleteData: function (field, url, data) {
                    var json = {b: {}};
                    json.b.includedFields = field;
                    $.ajax({
                        url: url,
                        type: 'post',
                        async: false,
                        data: JSON.stringify(json),
                        contentType: "application/json",
                        success: function (d, s, r) {
                            var res = d.res;
                            var tbl = new Table();
                            tbl.insertData(res);
                            var rows = tbl.getRows();
                            for (var i = 0; i < rows.length; i++) {
                                var value = rows[i][field];
                                var obj = {};
                                obj.id = i;
                                obj.name = value;
                                if (value.length > 1) {
                                    data.push(obj);
                                }
                            }
                        },
                        error: function (r, s, e) {

                        }
                    });
                }
            },
            _onCheckAll: function (e) {
                $(element).find('tbody .chk input').prop('checked', e.target.checked);
            },
            _onFilterClick: function (e) {
                handlers._fh._addFilter(e);
            },
            _onTimeIntervalClick: function (e) {
                var field = $(e.target).closest('th').attr('col-id');
                handlers._createInterval(field);
            },
            _createInterval: function (field) {
                var optionSelectedValue;
                var fc = $('#xyz').find('.filters-container');
                if (!(fc).length) {
                    fc = $('<div></div>');
                    $(fc).addClass('filters-container');
                    $('#xyz').prepend(fc);
                }
                var ic = $('<div></div>');
                $(ic).addClass('ic');
                $(ic).attr('data-id', field);

                var fromDiv = $('<div></div>');
                $(fromDiv).addClass('_sd_');
                var from = $('<label></label>');
                from.html('From');
                var si = $('<input type="date"/>');
                $(fromDiv).append(from).append(si);

                var toDiv = $('<div></div>');
                $(toDiv).addClass('_ed_');
                si.addClass('start-date');
                var to = $('<label></label>');
                to.html('To');
                var ei = $('<input type="date"/>');
                ei.addClass('end-date');
                $(toDiv).append(to).append(ei);

                var s = $('<select></select>');
                $(s).addClass('interval-combo');
                var d = $('<option></option>');
                d.html('Günlük');
                d.val('1');
                var w = $('<option></option>');
                w.html('Həftəlik');
                w.val('2');
                var m = $('<option></option>');
                m.html('Aylıq');
                m.val('3');
                var rub = $('<option></option>');
                rub.html('Rüblük');
                rub.val('4');
                var y = $('<option></option>');
                y.html('İllik');
                y.val('5');

                $(m).select();
                $(s).append(d).append(w).append(m).append(rub).append(y);
                var db = $('<button></button>');
                db.addClass('_dbtn fdb');
                var i = $('<i></i>');
                i.addClass('remove icon');
                $(db).append(i);
                $(db).click(function (e) {
                    var id = $(this).closest('.ic').attr('data-id');
                    currentTable.f.removeFilter(id);
                    $(this).closest('.ic').remove();
                    currentTable.f.removeIntervalField();
                    currentTable.f.removeIntervalStartDate();
                    currentTable.f.removeIntervalEndDate();
                    currentTable.f.removeIntervalSeperator();
                    self._equalize();
                    self._reload();
                });
                $(ic).append(fromDiv).append(s).append(toDiv).append(db);
                $(fc).append(ic);

                $(s).change(function (e) {
                    var optionSelected = $("option:selected", this);
                    optionSelectedValue = this.value;
                    handlers._onIntervalAction(e, field, optionSelectedValue);
                });
                $(ei).change(function (e) {
                    handlers._onIntervalAction(e, field);
                });
            },
            _showImage: function (url) {
                var img = $('<img/>', {'class': 'fscontent'});
                img.attr('src', cfg.fileUrl + url);
                var fullscreen = $('<div/>', {'class': 'fullscreen'});
                var close = $('<span/>', {'class': 'clos'}).html('x');
                fullscreen.append(close);
                fullscreen.append(img);
                $(document.body).append(fullscreen);
                close.click(function () {
                    $(this).closest('.fullscreen').remove();
                });
            },
            _onSortClick: function (e) {
                var ic = $(e.target).find('i');
                var key = $(e.target).closest('th').attr('col-id');
                if (ic.hasClass('ascending')) {
                    currentTable.f.removeSort();
                } else if (ic.hasClass('descending')) {
                    currentTable.f.setDesc(key);
                } else {
                    currentTable.f.setAsc(key);
                }
                self._reload();
            },
            _onDeleteAction: function (e) {
                e.stopPropagation();
                if (confirm('Are you sure?')) {
                    var url = cfg.deleteService;
                    var json = {b: {}};
                    json.b.id = handlers._sh._currentId(e);
                    $.ajax({
                        url: url,
                        type: cfg.method,
                        data: JSON.stringify(json),
                        contentType: "application/json",
                        success: function (d, s, r) {
                            self._reload();
                        },
                        error: function (r, s, e) {
                            console.log('cannot delete');
                        }
                    });
                }
            },
            _onCopyAction: function (e) {
                e.stopPropagation();
                var data = handlers._sh._singleSelect(handlers._sh._currentId(e));
                cfg.copy(data);
            },
            _onEditAction: function (e) {
                e.stopPropagation();
                var data = handlers._sh._singleSelect(handlers._sh._currentId(e));
                cfg.edit(data);
            },
            _onPrintAction: function () {
                var table = $('<table/>', {'style': 'page-break-inside:auto;border-collapse: collapse;white-space: nowrap;'});
//                table.css('style','-ms-zoom: 0.75;-moz-transform: scale(0.75);-moz-transform-origin: 0 0;-o-transform: scale(0.75);-o-transform-origin: 0 0;-webkit-transform: scale(0.75);-webkit-transform-origin: 0 0;');
                var thead = $('<thead/>', {'style': 'display:table-header-group'});
                var tbody = $('<tbody/>');
                var columns = self._getSelectedColumns();
                var tr = $('<tr/>', {'style': 'page-break-inside:avoid; page-break-after:auto'});
                thead.append(tr);
                for (var i in columns) {
                    if (columns[i].i === 'id')
                        continue;
                    var th = $('<th/>', {'style': 'border: 1px solid #ccc;'}).html(columns[i].n);
                    tr.append(th);
                }
                var rows = currentTable.getRows();
                for (var i in rows) {
                    var tr = $('<tr/>', {'style': 'page-break-inside:avoid; page-break-after:auto'});
                    for (var j in columns) {
                        if (columns[j].i === 'id')
                            continue;
                        tr.append($('<td/>', {'style': 'border: 1px solid #ccc;'}).html(rows[i][columns[j].i]));
                    }
                    tbody.append(tr);
                }
                table.append(thead);
                table.append(tbody);
                var iframe = document.createElement('iframe');
                document.body.appendChild(iframe);
                iframe.contentWindow.document.write(table[0].outerHTML);
                iframe.contentWindow.print();
                $(iframe).remove();
            },
            _onExportAction: function () {
                var blob = handlers._export._getExcelBlob();
                var date = new Date();
                var filename = currentTable.tn + '_' + date.humanFormat() + '.xlsx';

                var a = document.createElement('a');
                a.href = window.URL.createObjectURL(blob);
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                $(a).remove();

            },
            _onGroupByAction: function (e) {
                var a = $(e.target).closest('.thc-hd').find('.h-content').text();
                var column = currentTable.getColumnByName(a);
                if ($(e.target).hasClass(column.i)) {
//                    $(e.target).closest('.thc-hd').find('.grp').hide();
//                    cfg.groupBy = false;
                    cfg.unGroupBy = true;
                }
                var icon = $(e.target).find('i');
                var id = $(e.target).closest('th').attr('col-id');
                var sc = self._getSelectedColumns();
                var s = '';
                var a = function () {
                    for (var i in sc) {
                        if (sc[i].t !== 'INTEGER') {
                            s = sc[i].i;
                            return s;
                        }
                    }
                };
                a();
                for (var i in sc) {
                    if (sc[i].t !== 'INTEGER' && sc[i].t !== 'ID' && s !== sc[i].i) {
                        s = s + '%IN%' + sc[i].i;
                    }
                }
                currentTable.f.setSumBy(id);
                currentTable.f.setGroupBy(s);
                pivotCombobox();
                printPivot();
                self._reload();
            },
            _onUngroupByAction: function (e) {
//                this.cfg.unGroupBy = false;
                cfg.unGroupBy = false;
                cfg.groupBy = true;
                currentTable.f.removeSumBy();
                currentTable.f.removeGroupBy();
                self._reload();
            },
            _onIntervalAction: function (e, field, ov) {
                var id;
                var sc = self._getSelectedColumns();
                var s = '';
                for (var i in sc) {
                    if (sc[i].t === 'INTEGER') {
                        id = sc[i].i;
                    }
                    if (sc[i].t !== 'INTEGER') {
                        if (i === '0') {
                            s = sc[i].i;
                        } else
                            s = s + '%IN%' + sc[i].i;
                    }
                }
                if (!ov) {
                    var ov = $('.filters-container').find('.interval-combo').val();
                }
                var startDate = $('.start-date').val().replace(/-/g, "");
                var endDate = $('.end-date').val().replace(/-/g, "");
                currentTable.f.setSumBy(id);
                currentTable.f.setGroupBy(s);
                currentTable.f.setIntervalSeperator(ov);
                currentTable.f.setIntervalStartDate(startDate);
                currentTable.f.setIntervalEndDate(endDate);
                currentTable.f.setIntervalField(field);
                self._reload();
            },
            _getFilter: function (d) {
                var fc = $(element).find('.filters-container');
                var keys = Object.keys(d.f);
                var columns = currentTable.getColumns();
                if (!(fc).length) {
                    fc = document.createElement('div');
                    $(fc).addClass('filters-container');
                    $(element).prepend(fc);
                }
                for (var i in keys) {
                    for (var j in columns) {
                        if (keys[i] === columns[j].i) {
                            var column = currentTable.getColumnById(keys[i]);
                            if (column.t === 'ID') {
                                return false;
                            }
                            var filcon = document.createElement('div');
                            $(filcon).addClass('filter-container');
                            $(filcon).attr('data-id', keys[i]);
                            $(fc).append(filcon);
                            var div = document.createElement('div');
                            $(div).addClass('lai');
                            var p = document.createElement('label');
                            p.innerHTML = column.n + ":";
                            div.appendChild(p);
                            filcon.appendChild(div);
                            handlers._fh._filterByType(keys[i], filcon, d.f[keys[i]]);
                            handlers._insertDataToFilter(d.f, keys[i]);
                        }
                    }
                    if (keys[i] === 'intervalField') {
                        var intervalField = d.f['intervalField'];
                        handlers._createInterval(intervalField);
                        handlers._getDataToInterval(d.f, keys[i]);
                    }
                }
            },
            _insertDataToFilter: function (f, key) {
                var column = currentTable.getColumnById(key);
                var value = f[key];
                if (column.t === 'INTEGER') {
                    var v, ty;
                    if (value.indexOf('%') > -1) {
                        if (value.split('%')[1] === 'BN') {
                            v = handlers._dataToFilterSecond(value, key, column.t);
                            ty = 'be';
                        } else {
                            v = value.split('%')[1];
                            ty = value.split('%')[0].toLowerCase();
                        }
                    } else {
                        v = value;
                    }
                    var input = $('.filters-container').find('input[name="' + key + '"].filter-first');
                    $(input).val(v);
                    $('[data-id="' + key + '"]').find('select option[value="' + ty + '"]').attr('selected', 'true');
                } else if (column.t === 'DATE') {
                    var v, ty;
                    currentTable.f[key] = value;
                    if (value.indexOf('%') > -1) {
                        if (value.split('%')[1] === 'BN') {
                            v = handlers._dataToFilterSecond(value, key, column.t);
                            ty = 'be';
                        } else {
                            v = value.split('%')[1];
                            ty = value.split('%')[0].toLowerCase();
                        }
                    } else {
                        v = value;
                    }
                    value = [v.slice(0, 4), '-', v.slice(4)].join('');
                    v = [value.slice(0, 7), '-', value.slice(7)].join('');
                    var input = $('.filters-container').find('input[name="' + key + '"].filter-first');
                    $(input).val(v);
                    $('[data-id="' + key + '"]').find('select option[value="' + ty + '"]').attr('selected', 'true');
                } else if (column.t === 'TIME') {
                    var v, ty;
                    if (value.indexOf('%') > -1) {
                        if (value.split('%')[1] === 'BN') {
                            v = handlers._dataToFilterSecond(value, key, column.t);
                            ty = 'be';
                        } else {
                            v = value.split('%')[1];
                            ty = value.split('%')[0].toLowerCase();
                        }
                    } else {
                        v = value;
                    }
                    value = [v.slice(0, 2), ':', v.slice(2)].join('');
                    v = [value.slice(0, 5), ':', value.slice(5)].join('');
                    var input = $('.filters-container').find('input[name="' + key + '"]');
                    $(input).val(v);
                    $('[data-id="' + key + '"]').find('select option[value="' + ty + '"]').attr('selected', 'true');
                }
            },
            _dataToFilterSecond: function (value, key, t) {
                var filcon = $('[data-id="' + key + '"]');
                var v, v2;
                v = value.split('%')[0];
                v2 = value.split('%')[2];

                var s = document.createElement('input');
                $(s).attr('name', key);
                $(s).attr('type', t);
                $(s).attr('class', 'filter-second');
                $(s).on('input', function () {
                    handlers._fh._filterChangeListener(filcon, t, key);
                });
                if (t === 'DATE') {
                    var value = [v2.slice(0, 4), '-', v2.slice(4)].join('');
                    v2 = [value.slice(0, 7), '-', value.slice(7)].join('');
                } else if (t === 'TIME') {
                    value = [v2.slice(0, 2), ':', v2.slice(2)].join('');
                    v2 = [value.slice(0, 5), ':', value.slice(5)].join('');
                }
                $(s).val(v2);
                $('[data-id="' + key + '"').find('select').after(s);
                return v;
            },
            _getDataToInterval: function (f, key) {
                var column = currentTable.getColumnById(f[key]);
                var startv, endv, is, sv, ev;
                startv = f['intervalStartDate'];
                endv = f['intervalEndDate'];
                is = f['intervalSeperator'];
                sv = [startv.slice(0, 4), '-', startv.slice(4)].join('');
                startv = [sv.slice(0, 7), '-', sv.slice(7)].join('');
                ev = [endv.slice(0, 4), '-', endv.slice(4)].join('');
                endv = [ev.slice(0, 7), '-', ev.slice(7)].join('');
                var si = $('.filters-container').find('.start-date');
                var ei = $('.filters-container').find('.end-date');
                $(si).val(startv);
                $(ei).val(endv);
                $('.filters-container').find('select option[value="' + is + '"]').attr('selected', 'true');
            },
            _showRow: function (e) {
                var data = handlers._sh._singleSelect(handlers._sh._currentId(e));
                cfg.show(data);
            }
        };
        self._init();
    };

    $.fn.datalist = function (options) {
        var dlasize = Object.keys(dla).length;
        var def = {};
        var $dl = this;
        if (!$dl.attr('data-list-id')) {
            $dl.attr('data-list-id', dlasize);
            $.each(this.attributes, function (i, att) {
                def[att.name] = att.value;
            });
            var settings = $.extend(def, options);
            if ($dl.data('unique')) {
                settings.uniqueColumn = $dl.data('unique');
            }
            var dlist = new DataList($(this), settings);
            dla[$dl.attr('data-list-id')] = dlist;
        }
        return dla[$dl.attr('data-list-id')];
    };
    $.fn.hasXScrollBar = function () {
        return this.get(0) ? this.get(0).scrollWidth > this.innerWidth() : false;
    };
}(jQuery));