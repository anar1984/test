(function ($) {
    "use strict";
    var csa = {};
    "use strict";
    var CustomSelect = function (el, settings) {
        var settings = settings;
        var element = $(el);
        var cntr;
        var defaults = {
            method: 'post',
            witdh: '100%',
            inputHeight: '28px',
            scrollPagination: true,
            height: '200px',
            minChar: 0,
            onInput: function (input) {
                //User code
            }
        };
        var conf = $.extend({}, settings);
        var cfg = $.extend(true, {}, defaults, conf);
        this.setValue = function (value) {
            self._updateHelperBySingleValue(value);
            self._selectCurrentItem(cntr.find('ul li').first());
        };
        var self = {
            half: true,
            _updateHelperBySingleValue: function (value) {
                var json = {b: {}};
                json.b[cfg.value] = value;
                self._updateHelper(json);
            },
            _updateHelperByLike: function (like) {
                var json = {b: {}};
                json.b.startLimit = '1';
                json.b.endLimit = '15';
                json.b[cfg.texts[0]] = '%%' + like + '%%';
                self._updateHelper(json);
            },
            _updateHelper: function (json) {
                if (!json)
                    json = {b: {}};
                if (!json.b.startLimit)
                    json.b.startLimit = '1';
                if (!json.b.endLimit)
                    json.b.endLimit = '15';
                json.b.includedFields = cfg.value;
                for (var i in cfg.texts) {
                    json.b.includedFields += ',' + cfg.texts[i];
                }
                if (cfg.filter) {
                    var filters = cfg.filter.split(',');
                    for (var i in filters) {
                        json.b[filters[i].split('=')[0]] = filters[i].split('=')[1];
                    }
                }
                var helper = cntr.find('ul');
                helper.empty();
                $.ajax({
                    method: cfg.method,
                    url: urls[cfg.url],
                    async: false,
                    data: JSON.stringify(json),
                    contentType: "application/json",
                    success: function (data) {
                        var res = data.res;
                        console.log(res);
                        var rows = res.r;
                        for (var i in rows) {
                            var text = '', value;
                            var li = $('<li/>');
                            for (var j in cfg.texts) {
                                var field = cfg.texts[j];
                                if (rows[i][field])
                                    if (j === '0') {
                                        text = rows[i][field];
                                    } else
                                        text = text + ' - ' + rows[i][field];
                            }
                            value = rows[i][cfg.value];
                            li.attr('data-value', value);
                            li.html(text);
                            helper.append(li);
                            li.click(handlers._onItemSelect);
                        }

                    },
                    error: function (d, s, n) {
                        console.log("Something went wrong. Status: " + s);
                    }
                });
            },
            _selectCurrentItem: function (item) {
                console.log('_selectCurrentItem');
                var value = $(item).attr('data-value');
                console.log('value: '+value);
                var  text= $(item).html();
                cntr.find('.cs-input').val(text);
                var hiddenVal = cntr.find('.custom-select');
                hiddenVal.empty();
                var option = $('<option/>');

                option.attr('value', value);
                option.attr('selected', true);
                option.html(text);
                $(hiddenVal).append(option);
                handlers._basics._collapseHelper();
                self.half = false;
                cfg.onInput(hiddenVal);
            },
            _init: function () {
                element.hide();
                var hiddenInput = element.clone();
                var container = $('<div/>', {'class': 'cs-container'}).height(cfg.inputHeight);
                var input = $('<input/>', {'class': 'cs-input'});
                var trigger = $('<div/>', {'class': 'cs-trigger'});
                var icon = $('<div/>', {'class': 'cs-trigger-icon'});
                var ul = $('<ul/>').css('max-height', cfg.height).hide();
                container.append(input);
                container.append(trigger.append(icon));
                container.append(ul);
                container.append(hiddenInput);
                cntr = container;
                element.replaceWith(container);

                trigger.click(handlers._onTrigger);
                trigger.focus(handlers._onTrigger);
                input.on('input', handlers._onInput);
                input.focus(handlers._basics._expandHelper);
                self._updateHelper();
                $(document).click(function (e) {
                    event.stopPropagation();
                    if ($(e.target).closest('.cs-container').length === 0) {
                        handlers._basics._collapseHelper();
                        if (self.half) {
                            cntr.find('.cs-input').val('');
                        }
                    }
                });
                hiddenInput.on('input', cfg.onInput);
            }
        };
        var handlers = {
            _basics: {
                _toggleHelper: function (helper) {
                    event.stopPropagation();
                    $(helper).closest('.cs-container').find('ul').toggle();
                },
                _expandHelper: function () {
                    event.stopPropagation();
                    cntr.find('ul').show();
                },
                _collapseHelper: function () {
                    event.stopPropagation();
                    cntr.find('ul').hide();
                }
            },
            _onHiddenValueChange: function () {
                console.log('$(this).val()');
                console.log($(this).val());
            },
            _onTrigger: function () {
                handlers._basics._toggleHelper(this);
            },
            _onItemSelect: function () {
                self._selectCurrentItem(this);
            },
            _onInput: function () {
                console.log('triggered');
                self.half = true;
                var value = $(this).val();
                handlers._basics._expandHelper();
                if (value.length >= cfg.minChar)
                    self._updateHelperByLike(value);
            }
        };
        self._init();
    };

    $.fn.customselect = function (options) {
        console.log('custom select called');
        var l = Object.keys(csa).length;
        var obj = $(this);
        if (obj.length === 1) {
            if (obj.attr('cs-id')) {
                return csa[obj.attr('cs-id')];
            }
        }
        obj.each(function () {
            var def = {};
            if ($(this).data('text'))
                def.texts = $(this).data('text').split(',');
            if ($(this).data('change'))
                def.change = $(this).data('change').split(',');
            if ($(this).data('url'))
                def.url = $(this).data('url');
            if ($(this).data('value'))
                def.value = $(this).data('value');
            if ($(this).data('filter'))
                def.filter = $(this).data('filter');

            var settings = $.extend(def, options);
            var isg = new IDGenerator();
            var test = isg.generate();
            var id = 'cs_' + test;
            $(this).attr('cs-id', id);
            var cs = new CustomSelect($(this), settings);
            csa[id] = cs;
        });
    };

}(jQuery));