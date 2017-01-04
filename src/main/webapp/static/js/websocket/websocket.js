require(["jquery", "sockjs"], function ($, ss) {
	var sock = new ss('/sockjs/message?siteId=webtrn&userId=lucy');
	sock.onopen = function () {
		console.log('open and send "test" ');
		sock.send('test');
	};
	sock.onmessage = function (e) {
		console.log('message', e.data);
		$("#text").text(e.data);
	};
	sock.onclose = function () {
		console.log('close');
		sock.close();
	};

});