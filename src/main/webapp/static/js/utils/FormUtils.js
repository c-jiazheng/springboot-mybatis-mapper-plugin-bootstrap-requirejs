define(function () {
	return {
		formToJson: function(val){
			var jqDom;
			if(typeof val == 'string'){
				jqDom = $(val)
			}else{
				jqDom =val;
			}
			var serializeObj={};
			var array = jqDom.serializeArray();
			var str = jqDom.serialize();
			$(array).each(function(){
				if(serializeObj[this.name]){
					if($.isArray(serializeObj[this.name])){
						serializeObj[this.name].push(this.value);
					}else{
						serializeObj[this.name]=[serializeObj[this.name],this.value];
					}
				}else{
					serializeObj[this.name]=this.value;
				}
			});
			return serializeObj;
		}
	}
});