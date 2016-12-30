define(function () {
	var userManage = {
		getQueryCondition: function (data, externalParam) {
			var param = {};
			//组装排序参数
			if (data.order && data.order.length && data.order[0]) {
				param.orderColumn = externalParam.column[data.order[0].column - externalParam.isCheck - externalParam.isNumb].name;
				param.orderDir = data.order[0].dir;
			}
			//组装查询参数
			$.extend(param, externalParam.ajax.data);
			//组装分页参数
			param.pn = data.start;
			param.ps = data.length;
			return param;
		}
	};

	return (function () {
		$.fn.table = function (obj) {
			//var $self = this;
			var $obj = obj;
//////////////////////////////////////////////////////////////////////////////////
			/*常量*/
			var CONSTANT = {
				DATA_TABLES: {
					DEFAULT_OPTION: { //DataTables初始化选项
						language: {
							"sProcessing": "处理中...",
							"sLengthMenu": "每页 _MENU_ 项",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
							"sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索:",
							"sUrl": "",
							"sEmptyTable": "表中数据为空",
							"sLoadingRecords": "载入中...",
							"sInfoThousands": ",",
							"oPaginate": {
								"sFirst": "首页",
								"sPrevious": "上页",
								"sNext": "下页",
								"sLast": "末页",
								"sJump": "跳转"
							},
							"oAria": {
								"sSortAscending": ": 以升序排列此列",
								"sSortDescending": ": 以降序排列此列"
							}
						},
						autoWidth: false,   //禁用自动调整列宽
						stripeClasses: ["odd", "even"],//为奇偶行加上样式，兼容不支持CSS伪类的场合
						order: [],          //取消默认排序查询,否则复选框一列会出现小箭头
						processing: false,  //隐藏加载提示,自行处理
						serverSide: true,   //启用服务器端分页
						searching: false    //禁用原生搜索
					},
					COLUMN: {
						CHECKBOX: { //复选框单元格
							className: "td-checkbox text-center",
							orderable: false,
							width: "25px",
							data: null,
							render: function (data, type, row, meta) {
								return '<input type="checkbox" class="iCheck">';
							}
						},
						INDEX: {//首列序号
							className: "text-center",
							orderable: false,
							width: "35px",
							data: null,
							render: function (data, type, row, meta) {
								return '<input type="checkbox" class="iCheck">';
							}
						}
					},
					RENDER: {   //常用render可以抽取出来，如日期时间、头像等
						ELLIPSIS: function (data, type, row, meta) {
							data = data || "";
							return '<span title="' + data + '">' + data + '</span>';
						},
						DATE_FORMAT: function (data, type, row, meta) {
							data = data || "";
							var date = new Date(data);
							var fullYear = date.getFullYear();
							var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
							var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
							return '<span title="' + data + '">' + fullYear + "-" + month + "-" + day + '</span>';
						}
					}
				},
				CUSTOM: {
					DEFAULT_OPTION: {
						"isCheck": false,//首列是否需要复选框，默认复选框在前
						"isNumb": true,//是否需要序号
						"isOperate": false,
						"isShade": true,//发送请求是否需要遮罩
					}
				}
			};
			$.extend(CONSTANT.DATA_TABLES.DEFAULT_OPTION, $obj);
			//////////////////////////////////////////////////////////////////////
			return {
				load: function (loadParam) {
					var $table = $(loadParam.el);
					var $columns = [];
					if (loadParam.isCheck != null && loadParam.isCheck) {
						$table.find("tr").find("th:first").before('<th><input type="checkbox" name="cb-check-all"></th>');
						$columns.push(CONSTANT.DATA_TABLES.COLUMN.CHECKBOX);
					}
					if (loadParam.isNumb != null && loadParam.isNumb) {
						$columns.push(CONSTANT.DATA_TABLES.COLUMN.INDEX);
						if (loadParam.isCheck != null && loadParam.isCheck) {
							$table.find("tr").find("th:first").after('<th>序号</th>');
						} else {
							$table.find("tr").find("th:first").before('<th>序号</th>');
						}
					}
					if (loadParam.isOperate != null && loadParam.isOperate) {
						$table.find("tr").find("th:last").after('<th>操作</th>');
					}
					var temp = loadParam.column;
					for (var i in temp) {
						//console.log(Object.keys(temp[i])[i])
						//console.log(temp[i][Object.keys(temp[i])[i]])
						//temp[i]["name"]
						if (temp[i]["type"] == "string") {
							$columns.push({
								className: "ellipsis", //文字过长时用省略号显示，CSS实现
								data: temp[i]["name"],
								render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,
							});
						} else if (temp[i]["type"] == "date") {
							$columns.push({
								data: temp[i]["name"],
								width: "80px",
								render: CONSTANT.DATA_TABLES.RENDER.DATE_FORMAT
							});
						} else if (temp[i]["type"] == null) {
							$columns.push({
								className: "td-operation",
								data: null,
								defaultContent: "",
								orderable: false,
								width: "140px"
							});
						} else {
							$columns.push({
								data: temp[i]["name"],
								render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,
								width: "80px"
							});
						}
					}
					if (loadParam.isOperate != null && loadParam.isOperate) {
						$columns.push({
							className: "td-operation",
							data: null,
							defaultContent: "",
							orderable: false,
							width: "140px"
						});
					}
					var _table = $table.dataTable($.extend(true, {}, CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
						ajax: function (data, callback, settings) {//ajax配置为function,手动调用异步查询
							if (loadParam.isShade != null && loadParam.isShade)
								layer.load(1, {shade: 0.6});//手动控制遮罩
							//封装请求参数
							var param = userManage.getQueryCondition(data, loadParam);
							$.ajax({
								type: loadParam.ajax.type == null ? "GET" : loadParam.ajax.type,
								url: loadParam.ajax.url,
								cache: loadParam.ajax.cache,  //禁用缓存
								data: param,    //传入已封装的参数
								dataType: loadParam.ajax.dataType,
								success: function (result) {
									//setTimeout仅为测试延迟效果
									setTimeout(function () {
										//异常判断与处理
										if (result.errorCode) {
											layer.alert("查询失败。错误码：" + result.errorCode);
											return;
										}
										var returnData = loadParam.ajax.success(result);
										returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
										//关闭遮罩
										if (loadParam.isShade != null && loadParam.isShade)
											layer.closeAll('loading');
										//调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
										//此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
										callback(returnData);
									}, 200);
								},
								error: function (XMLHttpRequest, textStatus, errorThrown) {
									if (loadParam.isShade != null && loadParam.isShade) {
										layer.alert("查询失败");
										layer.closeAll('loading');
									}
								}
							});
						},
						columns: $columns,
						fnDrawCallback: function () {
							if ((0 + loadParam.isCheck + loadParam.isNumb) != 0) {
								this.api().column(0 + loadParam.isCheck + loadParam.isNumb - 1).nodes().each(function (cell, i) {
									cell.innerHTML = i + 1;
								});
							} else if (loadParam.isNumb != null && loadParam.isNumb) {
								this.api().column(0 + loadParam.isNumb - 1).nodes().each(function (cell, i) {
									cell.innerHTML = i + 1;
								});
							}

							//渲染完毕后的回调
							//清空全选状态
							if (loadParam.isCheck != null && loadParam.isCheck) {
								$(":checkbox[name='cb-check-all']", $table).prop('checked', false);
							}
							//默认选中第一行
							//$("tbody tr", $table).eq(0).click();
						},
						"createdRow": function (row, data, index) {
							//行渲染回调,在这里可以对该行dom元素进行任何操作
							//给当前行加样式
							//给当前行某列加样式
							//$('td', row).eq(3).addClass(data.status ? "text-success" : "text-error");
							//不使用render，改用jquery文档操作呈现单元格
							/*var $btnEdit = $('<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>');
							 var $btnDel = $('<button type="button" class="btn btn-small btn-danger btn-del">删除</button>');
							 $('td:last', row).append($btnEdit).append($btnDel);*/
							if (loadParam.isOperate != null && loadParam.isOperate) {
								var lastTd = $('td:last', row);
								var $buttons = loadParam.buttons;
								for (var i in $buttons) {
									(function (i) {
										var _button = $($buttons[i].style);
										var _method = $buttons[i].btnCallback;
										_button.on('click', function () {
											$buttons[i].btnCallback(this, _table, $table);
										});
										lastTd.append(_button);
									})(i);
								}
							}
						}
					})).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象
					if (loadParam.isCheck != null && loadParam.isCheck) {
						$table.on("change", ":checkbox", function () {
							if ($(this).is("[name='cb-check-all']")) {
								//全选
								$(":checkbox", $table).prop("checked", $(this).prop("checked"));
								loadParam.checkCallback.checkAll($("td :checkbox", $table))
							} else {
								//一般复选
								var checkbox = $("tbody :checkbox", $table);
								$(":checkbox[name='cb-check-all']", $table).prop('checked', checkbox.length == checkbox.filter(':checked').length);
								loadParam.checkCallback.checkSingle(checkbox.filter(':checked'))
							}
						}).on("click", ".td-checkbox", function (event) {
							//点击单元格即点击复选框
							!$(event.target).is(":checkbox") && $(":checkbox", this).trigger("click");
						});
					}
					if(loadParam.rowClick !=null && loadParam.rowClick instanceof Function){
						//行点击事件
						$("tbody", $table).on("click", "tr", function (event) {
							$(this).addClass("active").siblings().removeClass("active");
							//获取该行对应的数据
							var item = _table.row($(this).closest('tr')).data();
							loadParam.rowClick(item, event);
						});
					}

					return {
						reload: function () {
							_table.ajax.reload();
						}
					};
				}
			}
		};
	})(jQuery)
});