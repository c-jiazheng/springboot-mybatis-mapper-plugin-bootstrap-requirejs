/**
 * 登录js
 * Created by zengyufei on 2016/11/20/020.
 * QQ: 312636208
 */
define(function () {
	var Form = (function () {
		var obj = function () {
		};
		obj.prototype.bind = function () {
			var _button = $("#b_submit");
			var obj = this;
			_button.bind("click", function () {
				bindCallBack(obj);
			});
			return this;
		};
		function collectParams() {
			var account = $("input[name='account']").val();
			var password = $("input[name='password']").val();
			return {account: account, password: password};
		}

		function bindCallBack(obj) {
			layer.load(1, {shade: 0.6});
			obj.ajaxBallBack("", collectParams());
		}
		obj.prototype.ajaxBallBack = function (url, params) {
			if(url == null || url == "")
				url = "/loginIn";
			$.post(url, params, function (data) {
				console.log(data);
				var json = JSON.parse(data);
				if (json.result) {
					if (json.redirect == null || json.redirect == "")
						json.redirect = "/common";
					window.location.href = json.redirect;
				} else {
					layer.alert(json.msg);
					layer.closeAll('loading');
				}
			});
		};
		return obj;
	})();

	var Test = (function () {
		var obj = function () {
		};
		obj.prototype.bind = function () {
			var _button = $("#b_oneKey");
			_button.bind("click", function () {
				bindCallBack(this);
			});
			function bindCallBack(_button) {
				new Form().ajaxBallBack("/onceLoginIn", {});
			}
		};
		return obj;
	})();

	new Form().bind();
	new Test().bind();

});