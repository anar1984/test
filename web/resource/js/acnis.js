String.prototype.toDate = function () {
    return [this.slice(0, 4), '-', this.slice(4, 6), '-', this.slice(6, 8)].join('');
};


function IDGenerator() {
    this.length = 6;
    this.timestamp = +new Date;
    var _getRandomInt = function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };
    this.generate = function () {
        var ts = this.timestamp.toString();
        var parts = ts.split("").reverse();
        var id = "";
        for (var i = 0; i < this.length; ++i) {
            var index = _getRandomInt(0, parts.length - 1);
            id += parts[index];
        }
        return id;
    };
}

function isInteger(s) {
    var reg = new RegExp('^\\d+$');
    if (reg.test(s)) {
        return true;
    } else
        return false;
}

function makeAutocompleteDataForInput(id, name, url, key, val) {
    var json = {b: {}};
    var data = [];
    json.b.includedFields = id + "," + name;
    if (key)
        json.b[key] = val;
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
            for (var i in rows) {
                var n = rows[i][name];
                var d = rows[i][id];
                var obj = {};
                obj.id = d;
                obj.name = n;
                data.push(obj);
            }
        },
        error: function (r, s, e) {
        }
    });
    return data;
}
function distinct(a, param) {
    return a.filter(function (item, pos, array) {
        return array.map(function (mapItem) {
            return mapItem[param];
        }).indexOf(item[param]) === pos;
    });
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
function showList(id) {
    var list = $('#' + id);
    $('.task').removeClass('cl');
    $('.lt').empty().removeAttr('data-list-id').hide();
    list.show();
    list.addClass('cl');
    var ukey = list.attr('data-url');
    var dkey = list.closest('div[id^=tg_]').find('.trash').parent().attr('formaction');
    var ekey = $(list).closest('.tasks').find('.ut form').attr('action');
    var url = getUrl(ukey);
    var deleteUrl = getUrl(dkey);
    var editUrl = getUrl(ekey);
    $(list).datalist({
        listService: url,
        deleteService: deleteUrl,
        updateService: editUrl,
        fileUrl: getUrl('upload'),
        numbers: true,
        copiable: true,
        refresh: true,
        groupBy: false,
        columnSelect: true,
        height: '450px',
        dataEnumColumns: ['paymentStatusName', 'productAmbalajStatusName',
            'liReceiptStatusName', 'liChildProductStatus', 'operationPoolStatusName'],
        dataEnumColors: {
            'Təsdiqlənib': 'green',
            'Qəbul edilib': 'blue',
            'Qəbul Edilib': 'blue',
            'Bitib': 'green',
            'Passiv': 'red',
            'Sifariş Göndərilib': 'yellow'
        },
        copy: function (data) {
            var createTask = $(list).closest('.tasks').find('.ct');
            showUpdateTask(createTask, data);
        },
        edit: function (data) {
            var updateTask = $(list).closest('.tasks').find('.ut');
            showUpdateTask(updateTask, data);
        },
        show: function (data) {
            var rows = data.getRows();
            var columns = data.getColumns();
            var view = $('<div/>', {'class': 'popup'});
            var header = $('<div/>', {'class': 'lvh'});
            var button = $('<button/>', {'class': 'close'});
            var icon = $('<i/>', {'class': 'delete icon'});
            button.append(icon);
            header.append(button);
            view.append(header);
            var table = $('<table/>');
            for (var i in rows) {
                for (var j in columns) {
                    var tr = $('<tr/>');
                    var th = $('<th/>').html(columns[j].n);
                    var td = $('<td/>').html(rows[i][columns[j].i]);
                    tr.append(th);
                    tr.append(td);
                    table.append(tr);
                }
            }
            view.append(table);
            $('.cl').append(view);

            var w = view.outerWidth();
            var ww = $('body').outerWidth();
            view.css('margin-left', (ww - w) / 2);
            button.click(function () {
                $(this).closest('.popup').remove();
            });
        }
    });
}
function doNoViewTask(list, ukey, single) {
    var ids = $(list).datalist().getCheckedIds();
    var url = getUrl(ukey);
    if (single) {
        if (ids.length !== 1)
            alert('Zəhmət olmasa birini seçin');
        else {
            var json = {b: {}};
            json.b.id = ids[0];
            $.ajax({
                url: url,
                type: 'post',
                data: JSON.stringify(json),
                contentType: "application/json",
                success: function (x) {
                    var url = getUrl('upload') + x.b.filename;
                    var a = document.createElement('a');
                    a.href = url;
                    a.download = x.b.filename;
                    document.body.appendChild(a);
                    a.click();
                    $(a).remove();
                },
                error: function () {
                    console.log("Something went wrong.");
                }
            });
        }
        return;
    }
    if (!confirm('Are you sure?')) {
        return;
    }

    var f = ids[0];
    for (var i in ids) {
        if (i !== '0') {
            f = f + '%IN%' + ids[i];
        }
    }
    var json = {b: {}};
    json.b.id = f;
    $.ajax({
        url: url,
        type: 'post',
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function () {
            $(list).datalist().update();
        },
        error: function () {
            console.log("Something went wrong.");
        }
    });
}
function showUpdateTask(ut, data) {
    var form = $(ut).find('form');
    showTask($(ut).attr('id'), data);
//    console.log(JSON.stringify(data));
    if (data) {
        form.find('*[name]').each(function () {
            var key = $(this).attr('name');
            var value = data.r[0][key];
            console.log(key + ' - ' + value);
            if ($(this).hasClass('custom-select')) {
                $(this).customselect().setValue(value);
            } else {
                if ($(this).attr('type') === 'date')
                    $(this).val(value.toDate()).attr('value', value.toDate()).trigger('input');
                else
                    $(this).val(value).attr('value', value).trigger('input');
            }
        });
        var multi = $(form).find('.multi');
        multi.each(function () {
            var tname = $(this).find('.multi-list').attr('table-name');
            if (data.getRow(0)[tname]) {
                var table = JSON.parse(data.getRow(0)[tname]);
                console.log(table);
                var rows = table.r;
                var input = [];
                var select = [];
                $(this).find('*[multi-part]').each(function () {
                    if ($(this)[0].tagName === 'INPUT') {
                        input.push($(this).attr('multi-part'));
                    }
                    if ($(this)[0].tagName === 'SELECT') {
                        select.push($(this).attr('multi-part'));
                    }
                });

                var tablecontainer = $(this).find('.multi-list');
                var table = tablecontainer.find('table');
                if (!table.length) {
                    var table = $('<table/>');
                    tablecontainer.append(table);
                }
                table.empty();
                for (var i in rows) {
                    var tr = $('<tr/>');
                    for (var n in select) {
                        var td = $('<td/>', {'style': 'display:none'});
                        td.html(rows[i][select[n]]);
                        td.attr('data-id', select[n]);
                        tr.append(td);
                        var td = $('<td/>');
                        var slc = multi.find('select[multi-part=' + select[n] + ']');
                        var dataText = slc.data('text');
                        var id = rows[i][select[n]];
                        var dataUrl = slc.data('url');
                        var json = {b: {}};
                        if (slc.data('value'))
                            json.b[slc.data('value')] = id;
                        console.log(getUrl(dataUrl));
                        console.log(JSON.stringify(json));
                        var text;
                        $.ajax({
                            url: getUrl(dataUrl),
                            type: 'post',
                            async: false,
                            data: JSON.stringify(json),
                            contentType: "application/json",
                            success: function (data) {
                                console.log(JSON.stringify(data));
                                if (dataText) {
                                    var arr = dataText.split(',');
                                    text = data.res.r[0][arr[0]];
                                    for (var i = 1; i < arr.length; i++) {
                                        text = text + ' - ' + data.res.r[0][arr[i]];
                                    }
                                }
                            },
                            error: function () {
                                console.log('error');
                            }
                        });
                        td.html(text);
                        tr.append(td);
                    }
                    for (var k in input) {
                        var td = $('<td/>');
                        td.html(rows[i][input[k]]);
                        td.attr('data-id', input[k]);
                        tr.append(td);
                    }
                    var td = $('<td/>', {'class': 'mdc'});
                    var button = $('<button class="mdb"><i class="delete icon"></i></button>');
                    button.click(function () {
                        $(this).closest('tr').remove();
                    });
                    td.append(button);
                    tr.append(td);
                    tr.click(function () {
                        multi.find('tr').removeClass('selected');
                        $(this).addClass('selected');
                        $(this).find('td[data-id]').each(function () {
                            multi.find('*[multi-part=' + $(this).attr('data-id') + ']').val($(this).html());
                            var test = multi.find('.custom-select[multi-part=' + $(this).attr('data-id') + ']').customselect();
                            if (test) {
                                test.setValue($(this).html());
                            }

                        });
                    });
                    table.append(tr);
                }
            }
        });

    }
}
function fieldListener(field) {

    $(field).closest('.field').find('label').css('opacity', '1');
    var n = $(field).attr('name');

    var s = $(field).closest('.popup-form').find('.form-select[data-change*=\'' + n + '\']');
//        var cs = $(this).closest('.popup-form').find('.custom-select[data-change*=\'' + n + '\']');
    var d = $(field).closest('.popup-form').find('.text[data-change*=\'' + n + '\']');
    var m = $(field).closest('.popup-form').find('.autoCompleteInput[data-change*=\'' + n + '\']');
    var mt = $(field).closest('.popup-form').find('.multi[data-change*=\'' + n + '\']');
    s.each(function () {
        fillSelect(this);
    });
    d.each(function () {
        fillInput(this);
    });
//        cs.each(function () {
//            fillInput(this);
//        });
    m.each(function () {
        fillMulti(this);
    });
    mt.each(function () {
        fillMultiList(this);
    });
}
function clearForm(form) {
    form.trigger('reset');
    form.find('.multi-select-list').each(function () {
        var el = $(this).find('input[name]').clone();
        el.val('');
        el.attr('value', '');
        if (el.length)
            $(this).replaceWith(el);
    });
    form.find('.custom-select').each(function () {
        var s = $(this).clone(true);
        s.removeAttr('cs-id');
        $(this).closest('.cs-container').replaceWith(s);
    });
    form.find('.multi').find('table').empty();
}
function showTask(id, data) {

    var $form = $('#' + id).find('form');
    clearForm($form);
    $('#' + id).fadeIn(0);
    $form.find('.form-select').each(function () {
        fillSelect(this, data);
    });
    $form.find('input').each(function () {
        if (!$(this).hasClass('autoCompleteInput') && !$(this).hasClass('multi-select') && $(this).is('[data-change]')) {
            fillInput(this);
        }
    });
    $form.find('.custom-select').customselect({
        onInput: function (input) {
            fieldListener(input);
        }
    });
    $form.find('.multi-select-list').multiSelectList({header: true});
    var w = $('#' + id).find('.popup-form').outerWidth();
    var ww = $('body').outerWidth();
    $('#' + id).find('.popup-form').css('margin-left', (ww - w) / 2);
}

function fillInput(element) {
    var el = $(element);
    var json = {b: {}};
    var keys = el.attr('data-change').split(',');
    var valueKey = el.data('value');
    var ukey = el.attr('data-url');
    var url = getUrl(ukey);
    for (var i in keys) {
        var value = el.closest('.popup-form').find('*[name=\'' + keys[i] + '\']').val();
        json.b[keys[i]] = value;
    }
    $.ajax({
        url: url,
        type: 'post',
        async: false,
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (data) {
            var rows = data.res.r;
            if (rows[0])
                el.val(rows[0][valueKey]);
        },
        error: function () {
            console.log("Something went wrong.");
        }
    });
}

function fillSelect(s, d) {
    var ukey = $(s).attr('data-url');
    if (!ukey)
        return;
    var select = $(s);
    var nonable = select.attr('none-value');
    select.empty();
    var url = getUrl(ukey);
    var json = {b: {}};
    var key = $(s).attr('data-change');
    if (key) {
        var arr = key.split(',');
        for (var i in arr) {
            var value = $(s).closest('.popup-form').find('*[name=\'' + arr[i] + '\']').val();
            json.b[arr[i]] = value;
        }
    }
    if ($(s).attr('data-filter')) {
        var filters = $(s).attr('data-filter').split(',');
        for (var i in filters) {
            json.b[filters[i].split('=')[0]] = filters[i].split('=')[1];
        }
    }
    $.ajax({
        url: url,
        type: 'post',
        async: false,
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (data) {
            var rows = data.res.r;
            if (nonable) {
                var none = $('<option/>').val('').html('None');
                select.append(none);
                none.attr('selected', true);
            }
            for (var i in rows) {
                var option = $('<option/>');
                option.val(rows[i][$(s).attr('data-value')]);
                option.html(rows[i][$(s).attr('data-text')]);
                select.append(option);
            }
            if (rows[0])
                select.val(rows[0][$(s).attr('data-value')]);
            if (select.is('[data-group]')) {
                var key = select.attr('data-group');
                var q = 'input[data-group=' + key + ']';
                select.closest('form').find(q).each(function () {
                    $(this).val(rows[0][$(this).attr('data-value')]);
                });
            }
        },
        error: function () {
            console.log("Something went wrong.");
        }
    });
    if (d)
        select.val(d.getRow(0)[select.attr('name')]);
}
function fillMulti(s) {
    var id = $(s).attr('data-value');
    var name = $(s).attr('data-text');
    var n = $(s).attr('name');
    var ukey = $(s).attr('data-url');
    var url = getUrl(ukey);
    var key = $(s).attr('data-change');
    var value = $(s).closest('.popup-form').find('*[name=\'' + key + '\']').val();
    data = makeAutocompleteDataForInput(id, name, url, key, value);
    var fieldset = $(s).closest('fieldset');
    fieldset.find('.tag-ctn').remove();
    var icfg = {'name': n, 'data-url': ukey, 'data-change': key, 'data-text': name, 'data-value': id};
    var input = $('<input>', icfg);
    fieldset.append(input);
    var conf = {
        data: data,
        width: "auto",
        collapseOnSelect: false,
        allowFreeEntries: false,
        selectionStacked: true,
        resultAsString: false,
        name: n
    };
    var attrs = {
        'class': 'autoCompleteInput'
    };
    $.extend(attrs, icfg);
    conf.inputCfg = attrs;
    $(input).tagSuggest(conf);

}
function fillMultiList(s) {
    var parents = $(s).attr('data-change').split(',');
    var ukey = $(s).attr('data-url');
    var form = $(s).closest('form');
    var data = {};
    for (var i in parents) {
        data[parents[i]] = form.find('*[name=' + parents[i] + ']').val();
    }
    var json = {b: {}};
    $.extend(json.b, data);
    $.ajax({
        url: getUrl(ukey),
        type: 'post',
        async: false,
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function (data) {
            var tab = new Table();
            tab.insertData(data.res);
            var tname = $(s).find('.multi-list').attr('table-name');
            if (tab.getRow(0)[tname]) {
                var table = JSON.parse(tab.getRow(0)[tname]);
                var rows = table.r;
                var input = [];
                var select = [];
                $(s).find('*[multi-part]').each(function () {
                    if ($(this)[0].tagName === 'INPUT') {
                        input.push($(this).attr('multi-part'));
                    }
                    if ($(this)[0].tagName === 'SELECT') {
                        select.push($(this).attr('multi-part'));
                    }
                });
                var tablecontainer = $(s).find('.multi-list');
                var table = tablecontainer.find('table');
                if (!table.length) {
                    var table = $('<table/>');
                    tablecontainer.append(table);
                }
                table.empty();
                for (var i in rows) {
                    var tr = $('<tr/>');
                    for (var n in select) {
                        var td = $('<td/>', {'style': 'display:none'});
                        td.html(rows[i][select[n]]);
                        td.attr('data-id', select[n]);
                        tr.append(td);
                        var td = $('<td/>');
                        var slc = $(s).find('select[multi-part=' + select[n] + ']');
                        var dataText = slc.data('text');
                        var id = rows[i][select[n]];
                        var dataUrl = slc.data('url');
                        var json = {b: {}};
                        if (slc.data('value'))
                            json.b[slc.data('value')] = id;
                        console.log(getUrl(dataUrl));
                        console.log(JSON.stringify(json));
                        var text;
                        $.ajax({
                            url: getUrl(dataUrl),
                            type: 'post',
                            async: false,
                            data: JSON.stringify(json),
                            contentType: "application/json",
                            success: function (data) {
                                console.log(JSON.stringify(data));
                                var arr = dataText.split(',');
                                text = data.res.r[0][arr[0]];
                                for (var i = 1; i < arr.length; i++) {
                                    text = text + ' - ' + data.res.r[0][arr[i]];
                                }
                            },
                            error: function () {
                                console.log('error');
                            }
                        });
                        td.html(text);
                        tr.append(td);
                    }
                    for (var k in input) {
                        var td = $('<td/>');
                        td.html(rows[i][input[k]]);
                        td.attr('data-id', input[k]);
                        tr.append(td);
                    }
                    var td = $('<td/>', {'class': 'mdc'});
                    var button = $('<button class="mdb"><i class="delete icon"></i></button>');
                    button.click(function () {
                        $(this).closest('tr').remove();
                    });
                    td.append(button);
                    tr.append(td);
                    tr.click(function () {
                        $(s).find('tr').removeClass('selected');
                        $(this).addClass('selected');
                        $(this).find('td[data-id]').each(function () {
                            $(s).find('*[multi-part=' + $(this).attr('data-id') + ']').val($(this).html());
                        });
                    });
                    table.append(tr);
                }
            }
        },
        error: function () {
            console.log("Something went wrong.");
        }
    });
}
function printErrorMessages(pform, et) {
    var ek = et.getColumnByName('error_key');
    var ec = et.getColumnByName('error_code');
    var rows = et.getRows();
    $(pform).find('.error-message').remove();
    $(pform).find('.error').removeClass('error');

    for (var i in rows) {
        var key = rows[i][ek.i];
        var code = rows[i][ec.i];

        if (key.toLowerCase() === 'system') {
            var span = $('<span/>', {'class': 'error-message'});
            span.html(code);
            $(pform).prepend(span);
        } else {
            var p = $('<p/>', {'class': 'error-message'});
            p.html(code);
            var field = $(pform).find('*[name=\'' + key + '\']').closest('.field');
            $(pform).find('*[name=\'' + key + '\']').addClass('error');
            field.append(p);
        }
    }
}
function logout() {
    document.cookie = "apdtok=; expires=Thu, 01 Jan 1970 00:00:01 UTC; path=/";
    location.reload();
}
function getUrl(key) {
    return urls[key];
}