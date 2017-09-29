function  getDefaultLang() {
    return 'ENG';
}

function setLoginLabels(lang) {
    $('#lblPersonal').html(LNG[lang].login.personal);
    $('#lblCompany').html(LNG[lang].login.company);
    $('#lblSignin').html(LNG[lang].login.signin);
    $('#lblSignin1').html(LNG[lang].login.signin);
    $('#lblUsername').html(LNG[lang].login.username);
    $('#lblUsername1').html(LNG[lang].login.username);
    $('#pUsername').attr("placeholder", LNG[lang].login.username);
    $('#cUsername').attr("placeholder", LNG[lang].login.username);
    $('#lblPassowrd').html(LNG[lang].login.password);
    $('#lblPassowrd1').html(LNG[lang].login.password);
    $('#pPassword').attr("placeholder", LNG[lang].login.password);
    $('#cPassword').attr("placeholder", LNG[lang].login.password);
    $('#btnSignInPersonal').html(LNG[lang].login.signin);
    $('#btnSignInPersonal').val(LNG[lang].login.signin);
    $('#btnSignInCompany').html(LNG[lang].login.signin);
    $('#btnSignInCompany').val(LNG[lang].login.signin);
    $('#lblHaveAccount').html(LNG[lang].login.haveAccount);
    $('#lblCreateAccount').html(LNG[lang].login.crAccount);
    $('#lblHaveAccount1').html(LNG[lang].login.haveAccount);
    $('#lblCreateAccount1').html(LNG[lang].login.crAccount);
    $('#incorrentPwd').html(LNG[lang].login.incorrectPwd);
}

function setSignupLabels(lang) {
    $('#lblPersonal').html(LNG[lang].login.personal);
    $('#lblCompany').html(LNG[lang].login.company);
    $('#lblSignin').html(LNG[lang].login.signin);
    $('#lblSignin1').html(LNG[lang].login.signin);
    $('#lblUsername').html(LNG[lang].login.username);
    $('#lblUsername1').html(LNG[lang].login.username);
    $('#pUsername').attr("placeholder", LNG[lang].login.username);
    $('#cUsername').attr("placeholder", LNG[lang].login.username);
    $('#lblPassowrd').html(LNG[lang].login.password);
    $('#lblPassowrd1').html(LNG[lang].login.password);
    $('#pPassword').attr("placeholder", LNG[lang].login.password);
    $('#cPassword').attr("placeholder", LNG[lang].login.password);
    $('#btnSignInPersonal').html(LNG[lang].login.signin);
    $('#btnSignInPersonal').val(LNG[lang].login.signin);
    $('#btnSignInCompany').html(LNG[lang].login.signin);
    $('#btnSignInCompany').val(LNG[lang].login.signin);
    $('#lblHaveAccount').html(LNG[lang].login.haveAccount);
    $('#lblCreateAccount').html(LNG[lang].login.crAccount);
    $('#lblHaveAccount1').html(LNG[lang].login.haveAccount);
    $('#lblCreateAccount1').html(LNG[lang].login.crAccount);
    $('#incorrentPwd').html(LNG[lang].login.incorrectPwd);
}


function setLangCombo4(e, arg) {
    var keys = Object.keys(LNG.LANGS);
    for (var i = 0; i < keys.length; i++) {
        var v = (LNG.LANGS[keys[i]]) ? LNG.LANGS[keys[i]] : "";

        var o = $("<option></option>");
        o.val(keys[i]).text(v);
        if (keys[i] === arg) {
            o.attr("selected",'true');
        }
        $(e).append(o);
    }
}



function _onchangeLangLogin(e) {
    var l = $(e).val();
    setLoginLabels(l);
    $('#lblCreateAccount1').attr('href','signup.html?lang='+l+'#company');
    $('#lblCreateAccount').attr('href','signup.html?lang='+l+'#personal');
}


var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};


function _onchangeLangSignup(e) {
    var l = $(e).val();
    setSignupLabels(l);
}
 