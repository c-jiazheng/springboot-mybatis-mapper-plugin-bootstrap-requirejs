requirejs(["dataTables", "initBindMethod"], function(){
});

define("initBindMethod", ["utils/TemplateUtils", "utils/FormUtils", "dataTables"], function (TemplateUtils, FormUtils, dataTables) {
	$("#jq_query_option").validationEngine({
		binded:false,
		onValidationComplete: function(form, valid){
			dataTables.reload();
		},
		addPromptClass:"formError-text formError-white formError-small"
	});
	
	$("#jq_add").on("click", function () {
		$(this).hide();
		TemplateUtils.buildForm({
			wrapper: {
				el: "#jq_add_form_div",
				dom: '.panel.panel-default' +
				'>.panel-heading>span{新增账号}' +
				'^.panel-body>form#add_form.form-inline[role=form][onsubmit="return false;"]' +
				'>#input-$.col-sm-12[style="margin-bottom: 15px;"]*5'+
				'+center>button#jq_submit[type=submit].btn.btn-primary[style="margin-right: 20px;"]{提交新增}' +
				'+button#jq_close[type=button].btn.btn-primary{关闭}'
			},
			form:{
				el: "#add_form",
				buttons: {
					submit: {
						el: "#jq_submit",
						event: "click",
						func: function(wrapper, form, btn){
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
									wrapper.hide();
									dataTables.reload();
								} else {
									layer.alert(json.msg);
								}
								layer.closeAll('loading');
							});
						}
					},
					close: {
						el: "#jq_close",
						event: "click",
						func: function(wrapper, form, btn){
							wrapper.hide();
							$("#jq_add").show();
						}
					}
				},
				items: [
					{
						el: "#input-1",
						dom: ".form-group.col-sm-6" +
							">label.col-sm-4[for=account]{账号}" +
							"+.col-sm-8" +
							">input#account[type=text][name=account].form-control.input-sm"
					},
					{
						el: "#input-1",
						dom: ".form-group.col-sm-6" +
						">label.col-sm-4[for=name]{姓名}" +
						"+.col-sm-8" +
						">input#name[type=text][name=name].form-control.input-sm"
					},
					{
						el: "#input-2",
						dom: ".form-group.col-sm-6" +
						">label.col-sm-4[for=password]{密码}" +
						"+.col-sm-8" +
						">input#password[type=text][name=password].form-control.input-sm"
					},
					{
						el: "#input-2",
						dom: ".form-group.col-sm-6" +
						">label.col-sm-4[for=repassword]{确认密码}" +
						"+.col-sm-8" +
						">input#repassword[type=text][name=repassword].form-control.input-sm"
					},
					{
						el: "#input-3",
						dom: ".form-group.col-sm-6" +
						">label.col-sm-4[for=roleId]{角色}" +
						"+.col-sm-8" +
						">select#roleId[name=roleId].form-control.input-sm",
					}
				]
			},
			render: function(wrapper, form, thisObj){
				$.get("/role/queryList", function (data) {
					var json = JSON.parse(data);
					var roleList = json.dataList;
					var selectKey = $("#roleId").attr("value");
					var options = ['<option value="">请选择角色</option>'];
					$.each(roleList, function (i, v) {
						var temp = '<option value="' + v.id + '" ';
						if(selectKey != null && selectKey != ''){
							temp += 'selected="selected"';
						}
						temp += ' >' + v.roleName + '</option>';
						options.push(temp);
					});
					$("#roleId").html(options);
				});
			}
		}).show();
	});
});

define("dataTables", ["utils/TemplateUtils", "utils/FormUtils", "utils/TableUtils"], function (TemplateUtils, FormUtils) {
	function loadTable(){
		var test = {
			"el": "#table-user",//需要套用表格的元素
			"isOperate": true,//是否需要操作选项
			"ajax": {
				"type": "GET",
				"url": "/user/queryList",
				"data": {
					"account": $("#jq_query_option_account").val(),
					"mobile": $("#jq_query_option_mobile").val()
				}
			},
			"column": [
				{
					"show": "账号",
					"name": "account",
					"type": "string"
				},{
					"show": "姓名",
					"name": "name",
					"type": "string"
				},{
					"show": "角色",
					"name": "roleId",
					"type": "string"
				},{
					"show": "创建日期",
					"name": "createTime",
					"type": "date"
				}
			],
			"buttons": [
				{
					"dom": 'button[type=button][style="margin-right: 10px;"].btn.btn-small.btn-primary.btn-edit{修改}',
					"func": function (jqDom, datatables, table) {
						//点击编辑按钮
						var item = datatables.row($(jqDom).closest('tr')).data();
						TemplateUtils.buildForm({
							wrapper: {
								el: "#jq_add_form_div",
								dom: '.panel.panel-default' +
								'>.panel-heading>span{修改账号}' +
								'^.panel-body>form#add_form.form-inline[role=form][onsubmit="return false;"]' +
								'>#input-$.col-sm-12[style="margin-bottom: 15px;"]*5'+
								'+center>button#jq_submit[type=submit].btn.btn-primary[style="margin-right: 20px;"]{提交修改}' +
								'+button#jq_close[type=button].btn.btn-primary{关闭}'
							},
							form:{
								el: "#add_form",
								buttons: [
									{
										el: "#jq_submit",
										event: "click",
										func: function(wrapper, form, btn){
											layer.load(1, {shade: 0.6});
											var formToJson = FormUtils.formToJson("#add_form");
											for (var val in formToJson) {
												if (formToJson[val] == "") {
													layer.alert("所有的数据录入不能为空");
													layer.closeAll('loading');
													return false;
												}
											}
											$.post("/user/update", formToJson, function (data) {
												var json = JSON.parse(data);
												if (json.result) {
													layer.alert(json.msg);
													$("#jq_add").show();
													wrapper.hide();
													tableUtils.reload(datatables);
												} else {
													layer.alert(json.msg);
												}
												layer.closeAll('loading');
											});
										}
									},
									{
										el: "#jq_close",
										event: "click",
										func: function(wrapper, form, btn){
											wrapper.hide();
											$("#jq_add").show();
										}
									}
								],
								items: [
									{
										el: "#input-1",
										dom: "input#id[type=hidden][name=id][value="+item.id+"]"
									},
									{
										el: "#input-1",
										dom: ".form-group.col-sm-6" +
										">label.col-sm-4[for=account]{账号}" +
										"+.col-sm-8" +
										">input#account[type=text][name=account].form-control.input-sm[value="+item.account+"]"
									},
									{
										el: "#input-1",
										dom: ".form-group.col-sm-6" +
										">label.col-sm-4[for=name]{姓名}" +
										"+.col-sm-8" +
										">input#name[type=text][name=name].form-control.input-sm[value="+item.name+"]"
									},
									{
										el: "#input-2",
										dom: ".form-group.col-sm-6" +
										">label.col-sm-4[for=password]{密码}" +
										"+.col-sm-8" +
										">input#password[type=text][name=password].form-control.input-sm"
									},
									{
										el: "#input-2",
										dom: ".form-group.col-sm-6" +
										">label.col-sm-4[for=repassword]{确认密码}" +
										"+.col-sm-8" +
										">input#repassword[type=text][name=repassword].form-control.input-sm"
									},
									{
										el: "#input-3",
										dom: ".form-group.col-sm-6" +
										">label.col-sm-4[for=roleId]{角色}" +
										"+.col-sm-8" +
										">select#roleId[name=roleId].form-control.input-sm[value="+item.roleId+"]",
									}
								]
							},
							render: function(wrapper, form, thisObj){
								$.get("/role/queryList", function (data) {
									var json = JSON.parse(data);
									var roleList = json.dataList;
									var options = ['<option value="">请选择角色</option>'];
									$.each(roleList, function (i, v) {
										var temp = '<option value="' + v.id + '" ';
										if(v.id == item.roleId){
											temp += 'selected="selected"';
										}
										temp += ' >' + v.roleName + '</option>';
										options.push(temp);
									});
									$("#roleId").html(options);
								});
							}
						}).show(item.id);
					}
				}, {
					"dom": 'button[type=button].btn.btn-small.btn-danger.btn-del{删除}',
					"func": function (jqDom, datatables, table) {
						var item = datatables.row($(jqDom).closest('tr')).data();
						console.log(item);
					}
				}
			]
		};
		return $("#table-user").table({}).load(test);
	}
	var tableUtils = loadTable();
	return tableUtils;
});