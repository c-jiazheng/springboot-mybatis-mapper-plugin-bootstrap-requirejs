define(["utils/DateUtils"], function (DateUtils) {

	$("#jq_add").on("click", function () {
		$(this).hide();
		$("#jq_add_form").show();
	});

	$("#jq_close").on("click", function () {
		$("#jq_add").show();
		$("#jq_add_form").hide();
	});

	$("#jq_submit").on("click", function () {
		layer.load(1, {shade: 0.6});
		var formInput = $("#add_form").serialize();
		$.post("/user/add", formInput, function (data) {
			var json = JSON.parse(data);
			if(json.result){
				layer.alert(json.msg);
				$("#jq_add").show();
				$("#jq_add_form").hide();
				$("#jq_submit:input").val("");
			}else{
				layer.alert(json.msg);
			}
			layer.closeAll('loading');
		});
	});


	$(document).ready(function() {
		$.get("/user/queryList", function(data){
			var json = JSON.parse(data);
			$('#dataTables-example').DataTable({
				"dom": "<'row'<'col-sm-12'tr>>" +
				"<'row'<'col-sm-10'l><'col-sm-2'i><'col-sm-12'p>>",
				"data": json.dataList,
				"columns": [
					{ "data": "id" },
					{ "data": "account" },
					{ "data": "name" },
					{ "data": "createTime" }
				],
				"responsive": true,
				"bPaginate": true, //翻页功能
				"bLengthChange": true, //改变每页显示数据数量
				"bFilter": false, //过滤功能
				"bSort": true, //排序功能
				"bInfo": true,//页脚信息
				"bAutoWidth": true,//自动宽度
				"bProcessing": false,          //当datatable获取数据时候是否显示正在处理提示信息。
				"oLanguage": {
					"sProcessing": "正在加载中......",
					"sLengthMenu": "每页显示 _MENU_ 条记录",
					"sZeroRecords": "对不起，查询不到相关数据！",
					"sEmptyTable": "表中无数据存在！",
					"sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
					"sInfoFiltered": "数据表中共为 _MAX_ 条记录",
					"sSearch": "搜索",
					"oPaginate": {
						"sFirst": "首页",
						"sPrevious": "上一页",
						"sNext": "下一页",
						"sLast": "末页"
					}
				},
				"rowCallback": function( row, data, index ) {
					$('td:eq(3)', row).html(DateUtils.getTimeYMD(data.createTime));
				}
			});
		});
	});
});