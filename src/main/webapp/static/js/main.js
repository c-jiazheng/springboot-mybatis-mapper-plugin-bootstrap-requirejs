/******require-global-config.js******/
require.config({
	//"baseUrl" : "/static/plugin",
	paths: {
		"bootstrap": "/static/plugin/bootstrap/js/bootstrap.min",
		"jquery": "/static/plugin/jquery.min",
		/* 模板技术 */
		"artTemplate": "/static/plugin/artTemplate",
		/* 弹窗技术 */
		"layer": "/static/plugin/layer/layer",
		/* 该框架的index风格 */
		"sbAdmin": "/static/plugin/sbAdmin/js/sb-admin-2.min",
		"metisMenu": "/static/plugin/metisMenu/metisMenu.min",
		/* websocket */
		"sockjs": "/static/plugin/sockjs/sockjs.min",
		"stomp": "/static/plugin/sockjs/stomp.min",
		/* emmet */
		"emmet": "/static/plugin/emmet/jquery.emmet.min",
		//"emmet": "/static/plugin/emmet/emmet",
		/* table技术 */
		"datatables": "/static/plugin/datatables/js/jquery.dataTables.min",
		"dataTables.bootstrap": "/static/plugin/datatables-plugins/dataTables.bootstrap.min",
		"datatables-responsive": "/static/plugin/datatables-responsive/dataTables.responsive",
		/* jq-validation-Engine */
		"validationEngine": "/static/plugin/jQuery-Validation-Engine/js/jquery.validationEngine.min",
		"validationEngineZH": "/static/plugin/jQuery-Validation-Engine/js/jquery.validationEngine-zh_CN",
		//"css": "/static/plugin/require/css",
		"text": "/static/plugin/require/text"
	},
	shim: {
		"jquery": {exports: "jquery"},
		"bootstrap": {deps: ["jquery"]},
		"metisMenu": {deps: ["jquery", "bootstrap"]},
		"sbAdmin": {deps: ["jquery", "datatables-responsive"]},
		"artTemplate": {deps: ["jquery"]},
		"datatables": {deps: ["jquery", "metisMenu"]},
		"dataTables.bootstrap": {deps: ["jquery", "datatables"]},
		"datatables-responsive": {deps: ["jquery", "dataTables.bootstrap"]},
		"layer": {deps: ["jquery"]},
		"validationEngine": {deps: ["jquery"]},
		"validationEngineZH": {deps: ["validationEngine"]},
		//"emmet": {deps: ["jquery"], exports: "emmet"},
		"emmet": {deps: ["jquery"], exports: "emmet"}
	}
});

require(["jquery", "artTemplate", "bootstrap", "metisMenu", "sbAdmin", "layer",
	"datatables", "dataTables.bootstrap", "datatables-responsive", "emmet",
	"validationEngine","validationEngineZH"], function ($) {
	var startModuleName = $("script[data-main][data-start]").attr("data-start");
	startModuleName = startModuleName + ".js";

	if (startModuleName) {
		require([startModuleName], function (startModule) {
			$(function () {
				var fn = $.isFunction(startModule) ? startModule : startModule;
				if (fn) {
					fn();
				}
			});
		});
	}
});
