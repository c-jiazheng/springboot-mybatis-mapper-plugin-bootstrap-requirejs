define(["emmet"], function (emmet) {
	//var emmet = require("emmet");
	return {
		buildForm: function(obj){
			var wrapper = obj.wrapper;
			var form = obj.form;
			var	render = obj.render;
			return {
				show: function (key) {
					//设置结构
					var wrapperDom = $.e(wrapper.dom);
					var formDom = wrapperDom.find(form.el);
					$(wrapper.el).html(wrapperDom);
					var items = form.items;
					for(var i in items){
						var item = items[i];
						var formControlGroup = $.e(item.dom);
						wrapperDom.find(item.el).append(formControlGroup);
					}
					//渲染
					render(wrapperDom, formDom, this);
					//绑定按钮方法
					var buttons = form.buttons;
					for(var j in buttons){
						(function(i){
							var button = buttons[i];
							$(button.el).on(button.event, function () {
								button.func(wrapperDom, formDom, this);
							});
						})(j)
					}
				}
			}
		}
	}
});
