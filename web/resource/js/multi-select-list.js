/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function ($) {
    "use strict";
    var MultiSelect = function (element, settings) {
        var settings = settings;
        var element = element;
        var mainContainer;
        var msInput;
        var availableList;
        var selectedList;
        var sl, el;
        var defaults = {
            method: 'post',
            width: '100%',
            height: '200px',
            backgroundColor: '#F7F7F7',
            evenRowColor: '#F7F7F7',
            oddRowColor: 'white',
            selectedRowColor: '#5DC3A7',
            header: false,
            headerColor: '#F7F7F7',
            inputSeperator: '|'
        };
        var conf = $.extend({}, settings);
        var cfg = $.extend(true, {}, defaults, conf);
        this.refresh = function () {};
        var self = {
            _initialize: function () {
                mainContainer = $('<fieldset/>', {'class': 'multi-select-list'});
                if (cfg.label) {
                    mainContainer.append($('<legend/>').html(cfg.label));
                }
                msInput = element.clone();
                msInput.hide();
                var container = $('<div/>', {'class': '_cont_'}).css('height', cfg.height).css('width', cfg.width);
                var avalCont = $('<div/>', {'class': '_alc_'});
                var buttonCont = $('<div/>', {'class': '_bc_'});
                var selCont = $('<div/>', {'class': '_slc_'});
                $(container).append(avalCont).append(buttonCont).append(selCont);
                mainContainer.append(container);
                $(element).replaceWith(mainContainer);

                var searchAvailable = $('<input/>', {'type': 'text'});
                searchAvailable.attr('placeholder', 'Search In Available List');
                var searchSelected = $('<input/>', {'type': 'text'});
                searchSelected.attr('placeholder', 'Search In Selected List');
                $(searchSelected).keyup(self._searchFromList);
                $(searchAvailable).keyup(self._searchFromServer);

                var ah = $('<div/>', {'class': 'ah'});
                var avalList = $('<div/>', {'class': '_alist_'});
                ah.append(searchAvailable);
                avalCont.append(ah).append(avalList);
                var avalUl = $('<ul></ul>');
                avalList.append(avalUl);
                availableList = avalList;

                var sh = $('<div/>', {'class': 'sh'});
                var selList = $('<div/>', {'class': '_slist_'});
                sh.append(searchSelected);
                selCont.append(sh).append(selList);
                var selUl = $('<ul></ul>');
                selList.append(selUl);
                selectedList = selList;

                var buttonPart = $('<div/>', {'class': '_bp_'});
                var left = self._createButton('caret left icon');
                $(left).addClass('removeFromSelect');
                $(left).click(self._removeFromSelectedList);
                var right = self._createButton('caret right icon');
                $(right).addClass('addToSelect');
                $(right).click(self._addToSelectedList);
                var all = self._createButton('select all right icon');
                $(all).addClass('selectAll');
                $(all).click(self._addAllToSelect);
                buttonPart.append(left).append(right).append(all);
                buttonCont.append(buttonPart);
                container.append(msInput);
                if (cfg.inputSeperator === 'json')
                    msInput.val(JSON.stringify([]));
                else
                    msInput.val('');
                msInput.on('input', self._getData);
                if (cfg.change) {
                    mainContainer.closest('form').find('*[name=' + cfg.change + ']').on('input', function () {
                        self._getData();
                    });
                }
                self._getData();
            },
            _customize: function () {
//                if (cfg.backgroundColor) {
//                    var color = cfg.backgroundColor;
//                    mainContainer.find('._cont_ ._alc_').css('background', color);
//                    mainContainer.find('._cont_ ._slc_').css('background', color);
//                }
//                if (cfg.oddRowColor) {
//                    var color = cfg.oddRowColor;
//                    var al = mainContainer.find('._alist_ li:nth-child(odd)');
//                    var sl = mainContainer.find('._slist_ li:nth-child(odd)');
//                    $(al).css('background', color);
//                    $(sl).css('background', color);
//                }
//                if (cfg.evenRowColor) {
//                    var color = cfg.evenRowColor;
//                    var al = mainContainer.find('._alist_ li:nth-child(even)');
//                    var sl = mainContainer.find('._slist_ li:nth-child(even)');
//                    $(al).css('background', color);
//                    $(sl).css('background', color);
//                }
//                if (cfg.headerColor) {
//                    var color = cfg.headerColor;
//                    mainContainer.find('._cont_ .msl-header').css('background', color);
//                }
////                if (cfg.selectedRowColor) {
////                    var color = cfg.selectedRowColor;
////                    var outColor = mainContainer.find('._bp_ button').css('background');
////                    var s = mainContainer.find('.selected');
////                    $(s).attr('style', 'background:' + color + ' !important');
//////                    mainContainer.find('._bp_ button').hover(function () {
//////                        $(this).css('background', color);
//////                    }, function () {
//////                        $(this).css('background', outColor);
//////                    });
////                }
            },
            _createButton: function (c) {
                var button = $('<button></button>', {'type': 'button'});
                var i = $('<i/>', {'class': c});
                button.append(i);
                return button;
            },
            _getData: function (e) {
                var inp = $(this);
                var url = urls[cfg.url], method = cfg.method;
                sl = 1, el = 25;
                var data = {"b": {
                        "startLimit": "'" + sl + "'",
                        "endLimit": "'" + el + "'",
                        "includedFields": cfg.texts + ',' + cfg.value
                    }
                };
                if (cfg.change) {
                    var key = cfg.change;
                    var partner = msInput.closest('form').find('select[name=' + key + ']');
                    var va = $(partner).first().val();
                    if (!va) {
                        va = partner.find('option').first().attr('value');
                    }
                    data.b[key] = va;
                }
                $.ajax({
                    url: url,
                    method: method,
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (d) {
                        var v = msInput.val();
                        msInput.attr('value', v);
                        if (cfg.inputSeperator === 'json')
                            v = JSON.parse(v);
                        else
                            v = v.split(cfg.inputSeperator);
                        self._createList(d.res);
                        if (v.length > 0) {
                            self._createSelectedListOnUpdate(v, d.res);
                        }
                    },
                    error: function () {
                        console.log('Something Went Wrong');
                    }
                });
            },
            _createList: function (table) {
                var avalUl = availableList.find('ul');
                avalUl.empty();
                var dataNames = cfg.texts.split(',');
                var columns = table.c;
                if (cfg.header && columns.length > 0) {
                    var d = mainContainer.find('.msl-header');
                    if (d) {
                        $(d).remove();
                    }
                    d = $('<div/>', {'class': 'msl-header'});
                    for (var i in columns) {
                        for (var j in dataNames) {
                            if (columns[i].i === dataNames[j]) {
                                var div = $('<div/>', {'class': 'c_' + j});
                                div.css('margin-right', '10px');
                                div.html(columns[i].n);
                                d.append(div);
                            }
                        }
                    }
                    availableList.prepend(d);
                    selectedList.prepend(d.clone());
                }
                if (!cfg.header && columns.length > 0) {
                    if (dataNames.length > 1) {
                        var d = mainContainer.find('.msl-header');
                        if (d) {
                            $(d).remove();
                        }
                        d = $('<div/>', {'class': 'msl-header'});
                        for (var i in columns) {
                            for (var j in dataNames) {
                                if (columns[i].i === dataNames[j]) {
                                    var div = $('<div/>', {'class': 'c_' + j});
                                    div.css('margin-right', '10px');
                                    div.html(columns[i].n);
                                    d.append(div);
                                }
                            }
                        }
                        availableList.prepend(d);
                        selectedList.prepend(d.clone());
                    }
                }

                var rows = table.r;
                for (var i in rows) {
                    var li = $('<li></li>');
                    li.attr('data-value', rows[i][cfg.value]);
                    for (var j in dataNames) {
                        var d = rows[i][dataNames[j]];
                        var div = $('<div/>', {'class': 'c_' + j});
                        div.html(d);
                        li.append(div);
                        li.click(function () {
                            if (!$(this).hasClass('selected')) {
                                $(this).addClass('selected');
                            } else {
                                $(this).removeClass('selected');
                            }
                        });
                        li.dblclick(self._exchangeMe);
                    }
                    avalUl.append(li);
                }
                self._removeIfSelected();
            },
            _createSelectedListOnUpdate: function (valueArr, table) {
                var dataName = cfg.texts.split(',');
                var selUl = selectedList.find('ul');
                var rows = table.r;
                for (var i in rows) {
                    for (var j in valueArr) {
                        var v;
                        if (cfg.inputSeperator === 'json')
                            v = valueArr[j][cfg.value];
                        else
                            v = valueArr[j];

                        if (rows[i][cfg.value] === v) {
                            var li = $('<li></li>');
                            li.attr('data-value', rows[i][cfg.value]);
                            for (var k in dataName) {
                                var d = rows[i][dataName[k]];
                                var div = $('<div/>', {'class': 'c_' + k});
                                div.html(d);
                                li.append(div);
                            }
                            selUl.append(li);
                            $(li).find('div').each(function () {
                                $(this).addClass('data-cont');
                            });
                        }
                    }
                }
                var li = mainContainer.find('._slist_ ul').find('li');
                li.click(function (e) {
                    if (!$(this).hasClass('selected')) {
                        $(this).addClass('selected');
                    } else {
                        $(this).removeClass('selected');
                    }
                    self._customize();
                });
                li.dblclick(function (e) {
                    $(this).addClass('selected');
                    self._removeFromSelectedList(e);
                });
                self._removeIfSelected();
                self._equalize();
                self._customize();
            },
            _removeIfSelected: function () {
                var sli = selectedList.find('li');
                var ali = availableList.find('li');
                sli.each(function () {
                    var a = $(this).attr('data-value');
                    ali.each(function () {
                        var b = $(this).attr('data-value');
                        if (a === b) {
                            mainContainer.find('._alist_ ul').find('[data-value="' + b + '"]').remove();
                        }
                    });
                });
            },
            _equalize: function () {
                var dataName = cfg.texts.split(',');
                var div = mainContainer.find('._alist_ li').find('div');
                var ulWidth = 0;
                for (var j = 0; j < div.length; j++) {
                    var l = $('._alist_').find('.c_' + j);
                    var s = $('._slist_').find('.c_' + j);
                    var max = 0;
                    for (var i = 0; i < l.length; i++) {
                        if ($(l[i]).width() > max) {
                            max = $(l[i]).width();
                        }
                    }
                    $(l).width(max);
                    $(s).width(max);
                    ulWidth = ulWidth + max;
                }
                var d = mainContainer.find('._cont_ li').find('div');
                $(d).css('margin-right', '10px');
//                mainContainer.find('._cont_').find('ul').width(ulWidth + (dataName.length + 1) * 10);

                var aw = mainContainer.find('._alist_').width();
                var auw = mainContainer.find('._alist_').find('ul').width();

//                if (aw > auw) {
//                    console.log(aw);
//                    console.log(auw);
//                    mainContainer.find('._alist_ ul').width('100%');
//                    mainContainer.find('._slist_ ul').width(aw);
//                }
                if (cfg.header) {
                    var aw = mainContainer.find('._alist_').find('ul').width();
//                    mainContainer.find('.msl-header').width(aw - 10);
                }

            },
            _addToSelectedList: function (e) {
                var selectedItem = availableList.find('.selected');
                self._exchange(selectedItem);
            },
            _removeFromSelectedList: function (e) {
                var selectedItems = selectedList.find('.selected');
                self._exchange(selectedItems);
            },
            _addAllToSelect: function () {
                var selectedItem = availableList.find('li');
                self._exchange(selectedItem);
            },
            _exchange: function (selectedItems) {
                var $items = $(selectedItems);
                $items.removeClass('selected');
                var type = $items.first().closest('ul').parent().hasClass('_alist_');
                var mirrorList = type ? selectedList.find('ul') : availableList.find('ul');
                var value = msInput.val();

                var valueArr = [];
                $items.each(function () {
                    valueArr.push($(this).attr('data-value'));
                    $(this).find('div').each(function () {
                        $(this).addClass('data-cont');
                    });
                    $(this).detach();
                    mirrorList.append(this);
                });
                var valArr;
                if (cfg.inputSeperator === 'json')
                    valArr = JSON.parse(value);
                else
                    valArr = value.split(cfg.inputSeperator);
                if (type)
                    for (var k in valueArr)
                        if (cfg.inputSeperator === 'json') {
                            var obj = {};
                            obj[cfg.value] = valueArr[k];
                            valArr.push(obj);
                        } else {
                            valArr.push(valueArr[k]);
                        }
                else
                {
                    valArr = valArr.filter(function (item) {
                        return valueArr.indexOf(item) === -1;
                    });
                }
                if (cfg.inputSeperator === 'json') {
                    var json = JSON.stringify(valArr);
                    msInput.val(json);
                    msInput.attr('value', json);
                } else {
                    var st = '';
                    for (var x in valArr) {
                        if (valArr[x].length)
                            if (!st.length)
                                st = valArr[x];
                            else
                                st += cfg.inputSeperator + valArr[x];
                    }
                    msInput.val(st);
                    msInput.attr('value', st);
                }
            },
            _exchangeMe: function () {
                $(this).removeClass('selected');
                self._exchange(this);
            },
            _searchFromServer: function (e) {
                var element = e.target;
                var value = $(this).val().toLowerCase();
                $(element).closest('._cont_').find('._alist_ li').remove();
                var field = cfg.texts.split(',')[0];
                var url = urls[cfg.url], method = cfg.method;
                var data = {b: {
                        "includedFields": cfg.texts + ',' + cfg.value,
                        "startLimit": "1",
                        "endLimit": "25"
                    }
                };
                data.b[field] = '%%' + value + '%%';
                if (cfg.change) {
                    var key = cfg.change;
                    var partner = msInput.closest('form').find('select[name=' + key + ']');
                    var va = $(partner).first().val();
                    data.b[key] = va;
                }
                $.ajax({
                    method: method,
                    url: url,
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (d) {
                        self._createList(d.res);
                    },
                    error: function () {
                        console.log('something went wrong');
                    }
                });
            },
            _searchFromList: function (e) {
                var element = e.target;
                var value = $(this).val().toUpperCase();
                selectedList.find('li').hide();
                $(element).closest('._cont_').find('.data-cont').each(function () {
                    var text = $(this).text().toUpperCase();
                    if (text.indexOf(value) !== -1) {
                        $(this).closest('li').show();
                    }
                });
            },
            _scroll: function () {
                $('._alist_').bind('scroll', function () {
                    if ($(this).scrollTop() >= $(this)[0].scrollHeight * 0.7)
                    {
                        self._scrollData();
                    }
                });
            },
            _scrollData: function () {
                var url = urls[cfg.url], method = cfg.method;
                sl = sl + 25, el = el + 25;
                var data = {"b": {
                        "startLimit": sl,
                        "endLimit": el,
                        "includedFields": cfg.texts + ',' + cfg.value
                    }
                };
                $.ajax({
                    url: url,
                    method: method,
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (d) {
                        self._createList(d.res);
                    },
                    error: function () {
                        console.log('Something Went Wrong');
                    }
                });
            }

        };
        self._initialize();
//        self._scroll();
//        self._customize();
    };
    $.fn.multiSelectList = function (option) {
        var obj = $(this);
        obj.each(function () {
            var attrs = {};
            if ($(this).data('text'))
                attrs.texts = $(this).data('text');
            if ($(this).data('label'))
                attrs.label = $(this).data('label');
            if ($(this).data('url'))
                attrs.url = $(this).data('url');
            if ($(this).data('value'))
                attrs.value = $(this).data('value');
            if ($(this).data('change'))
                attrs.change = $(this).data('change');

            var settings = $.extend(true, {}, option, attrs);
            new MultiSelect($(this), settings);
        });
    };
}(jQuery));
