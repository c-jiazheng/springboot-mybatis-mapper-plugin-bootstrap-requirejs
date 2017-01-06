/**
 * 登录js
 * Created by zengyufei on 2016/11/20/020.
 * QQ: 312636208
 */

require(["validControl"], function (validControl) {
	var ValidUtils = validControl("#jq_form", "/loginIn");

	function oneLogin(){
		$.post("/onceLoginIn", function (data) {
			var json = JSON.parse(data);
			if (json.result) {
				if (json.redirect == null || json.redirect == "")
					json.redirect = "/index";
				window.location.href = json.redirect;
			} else {
				layer.alert(json.msg);
				layer.closeAll('loading');
			}
		});
	}
	$("#b_oneKey").bind("click", function () {
		oneLogin();
	});
});


define("validControl", ["utils/ValidUtils"], function (ValidUtils) {
	return function (el, url) {
		var action = url;
		function onAjaxAfterCallback(status, form, json, options){
			if (json.result) {
				if (json.redirect == null || json.redirect == "")
					json.redirect = "/common";
				window.location.href = json.redirect;
			} else {
				layer.alert(json.msg);
				layer.closeAll('loading');
			}

		}
		function onSubmitBeforeCallback(form, options){
			layer.load(1, {shade: 0.6});
		}
		var jqFormDom = ValidUtils.init.call($(el), action, onSubmitBeforeCallback, onAjaxAfterCallback);
		return ValidUtils;
	}
});



