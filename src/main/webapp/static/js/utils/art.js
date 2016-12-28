define(function () {
	var art = require("artTemplate");
	art.helper("isInclude", function(id, list, key){
		var b = 0;
		$(list).each(function(){
			if(id == this[key]){
				b = 1;
				return false;
			}
		});
		return b;
	});
	return {
		render: function(source, data){
			var render = art.compile(source);
			return render(data);
		}
	}
});