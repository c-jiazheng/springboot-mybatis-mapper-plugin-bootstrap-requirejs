requirejs(["initBindMethod", "initStyle"]);

define("initStyle", ["utils/art"], function (art) {
	$(function () {
		$.get("/role/queryList", function (data) {
			var json = JSON.parse(data);
			var roleList = json.dataList;
			var options = ['<option value="">请选择角色</option>'];
			$.each(roleList, function (i, v) {
				options.push('<option value="' + v.id + '">' + v.roleName + '</option>');
			});
			$("#jq_roles_select").html(options);
		});
	})
});

define("initBindMethod", ["utils/DateUtils", "utils/FormUtils", "utils/TableUtils"], function (DateUtils, FormUtils) {

	var datatables = loadTable();

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
		var formToJson = FormUtils.formToJson("#add_form");
		for (var val in formToJson) {
			if (formToJson[val] == "") {
				layer.alert("所有的数据录入不能为空");
				layer.closeAll('loading');
				return false;
			}
		}
		$.post("/user/add", formToJson, function (data) {
			var json = JSON.parse(data);
			if (json.result) {
				layer.alert(json.msg);
				$("#jq_add").show();
				$("#jq_add_form").hide();
				$("#jq_submit:input").val("");
				datatables.reload();
			} else {
				layer.alert(json.msg);
			}
			layer.closeAll('loading');
		});
	});


	function loadTable(){
		var test = {
			"el": "#table-user",//需要套用表格的元素
			"isCheck": false,//首列是否需要复选框，默认复选框在前
			"checkCallback": {
				checkAll: function (jqDoms) {
				},
				checkSingle: function (jqDom) {
				}
			},
			"isNumb": true,//是否需要序号
			"isOperate": true,
			"isShade": true,//发送请求是否需要遮罩
			"dataName": ["pages", "list"],
			"ajax": {
				"type": "GET",
				"url": "/user/queryList",
				"cache": false,
				"dataType": "json",
				//"sort": "",//排序字段
				"data": {
					id: 1,
					name: "zengyufei"
				},//发送服务器参数
				"success": function (data) {
					//封装返回数据，这里仅演示了修改属性名
					var returnData = {};
					returnData.recordsTotal = data.pages.total;//总记录数
					returnData.recordsFiltered = data.pages.total;//后台不实现过滤功能，每次查询均视作全部结果
					returnData.data = data.pages.list;//集合
					return returnData;
				}
			},
			"column": [
				{
					"name": "account",
					"type": "string"
				},{
					"name": "name",
					"type": "string"
				},{
					"name": "roleId",
					"type": "string"
				},{
					"name": "createTime",
					"type": "date"
				}
			],
			"buttons": [
				{
					"style": '<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>',
					"btnCallback": function (jqDom, datatables, table) {
						//点击编辑按钮
						var item = datatables.row($(jqDom).closest('tr')).data();
						console.log(item)
					}
				}, {
					"style": '<button type="button" class="btn btn-small btn-danger btn-del">删除</button>',
					"btnCallback": function (jqDom, datatables, table) {
						var item = datatables.row($(jqDom).closest('tr')).data();
						console.log(item);
					}
				}
			],
			rowClick: function (row, event) {
				//console.log(row);
			}
		};
		return $("#table-user").table({}).load(test);
	}

	function reloadTable(datatables) {
		datatables.reload();
	}

});