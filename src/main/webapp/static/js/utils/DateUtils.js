define(function () {
	return {
		getNowTime: function(){
			var date = new Date();
			return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		},
		getTimeYMD: function (dateMills){
			var date = new Date(dateMills);
			return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		},
		getTime: function(dateStr){
			dateStr = dateStr.replace(/-/g,"/");
			var date = new Date(dateStr);
			return date;
		}
	}
});