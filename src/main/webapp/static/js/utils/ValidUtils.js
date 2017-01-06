define(function () {
	return {
		init: function (action, onSubmitBeforeCallback, onAjaxAfterCallback) {
			_self = this;
			$(_self).validationEngine({
				promptPosition:"topRight",//提示信息的位置
				maxErrorsPerField:1,//单个元素显示错误提示的最大数量，值设为数值。默认 false 表示不限制。
				showOneMessage:true,//是否只显示一个提示信息
				ajaxFormValidationURL: action,
				scroll: false, //自动滚动到第一个错误元素
				addPromptClass: 'formError-white formError-small',
				ajaxFormValidation: true,//表单以AJAX方式提交
				validateNonVisibleFields:true,//是否验证不可见的元素
				ajaxFormValidationMethod:"post",//AJAX提交类型
				onBeforeAjaxFormValidation:onSubmitBeforeCallback, //AJAX验证之前
				onAjaxFormComplete: onAjaxAfterCallback//AJAX提交之后回调
			});
			return _self;
		}
	}
});