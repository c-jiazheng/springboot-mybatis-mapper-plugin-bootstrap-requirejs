define(function () {
	var userManage = {
		getQueryCondition: function (data, externalParam) {
			var param = {};
			//组装分页参数,可以被查询参数覆盖
			param.pn = data.start;
			param.ps = data.length;
			//组装排序参数
			if (data.order && data.order.length && data.order[0]) {
				param.orderColumn = externalParam.column[data.order[0].column - externalParam.isCheck - externalParam.isNumb].name;
				param.orderDir = data.order[0].dir;
			}
			//组装查询参数
			$.extend(param, externalParam.ajax.data);
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
				CUSTOM: {
					DEFAULT_OPTION: {
						"isCheck": false,//首列是否需要复选框，默认复选框在前
						"checkCallback": {
							checkAll: function (jqDoms) {
							},
							checkSingle: function (jqDom) {
							}
						},
						"isNumb": true,//是否需要序号
						"isOperate": false,
						"isShade": true,//发送请求是否需要遮罩
						"isTfoot": false,
						"ajax": {
							"type": "POST",
							"cache": false,
							"dataType": "json",
							"data": {
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
						"column": [],
						"buttons": [],
						rowClick: function (row, event) {
						}
					}
				},
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
				}

			};
			//////////////////////////////////////////////////////////////////////
			return {
				load: function (loadParam) {
					$.extend(CONSTANT.DATA_TABLES.DEFAULT_OPTION, $obj);
					$.extend(CONSTANT.CUSTOM.DEFAULT_OPTION.ajax, loadParam.ajax);
					$.extend(loadParam.ajax, CONSTANT.CUSTOM.DEFAULT_OPTION.ajax);
					$.extend(CONSTANT.CUSTOM.DEFAULT_OPTION, loadParam);
					var $table = $(CONSTANT.CUSTOM.DEFAULT_OPTION.el);
					var $table_thead = $("<thead></thead>");
					var $table_tr = $("<tr></tr>");
					$table_thead.append($table_tr);
					$table.append($table_thead);
					var $columns = [];
					if (CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck) {
						$table_tr.append('<th><input type="checkbox" name="cb-check-all"></th>');
						$columns.push(CONSTANT.DATA_TABLES.COLUMN.CHECKBOX);
					}
					if (CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb) {
						$columns.push(CONSTANT.DATA_TABLES.COLUMN.INDEX);
						if (CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck) {
							$table_tr.append('<th>序号</th>');
						} else {
							$table_tr.append('<th>序号</th>');
						}
					}
					var temp = CONSTANT.CUSTOM.DEFAULT_OPTION.column;
					for (var i in temp) {
						$table_tr.append('<th>'+temp[i]["show"]+'</th>');
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
					if (CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate) {
						$table_tr.append('<th>操作</th>');
					}
					if (CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate) {
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
							if (CONSTANT.CUSTOM.DEFAULT_OPTION.isShade != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isShade)
								layer.load(1, {shade: 0.6});//手动控制遮罩
							//封装请求参数
							var param = userManage.getQueryCondition(data, CONSTANT.CUSTOM.DEFAULT_OPTION);
							$.ajax({
								type: CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.type == null ? "GET" : CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.type,
								url: CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.url,
								cache: CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.cache,  //禁用缓存
								data: param,    //传入已封装的参数
								dataType: CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.dataType,
								success: function (result) {
									//setTimeout仅为测试延迟效果
									setTimeout(function () {
										//异常判断与处理
										if (result.errorCode) {
											layer.alert("查询失败。错误码：" + result.errorCode);
											return;
										}
										var returnData = CONSTANT.CUSTOM.DEFAULT_OPTION.ajax.success(result);
										returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
										//关闭遮罩
										if (CONSTANT.CUSTOM.DEFAULT_OPTION.isShade != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isShade)
											layer.closeAll('loading');
										//调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
										//此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
										callback(returnData);
									}, 200);
								},
								error: function (XMLHttpRequest, textStatus, errorThrown) {
									if (CONSTANT.CUSTOM.DEFAULT_OPTION.isShade != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isShade) {
										layer.alert("查询失败");
										layer.closeAll('loading');
									}
								}
							});
						},
						columns: $columns,
						fnDrawCallback: function () {
							if ((0 + CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck + CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb) != 0) {
								this.api().column(0 + CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck + CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb - 1).nodes().each(function (cell, i) {
									cell.innerHTML = i + 1;
								});
							} else if (CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb) {
								this.api().column(0 + CONSTANT.CUSTOM.DEFAULT_OPTION.isNumb - 1).nodes().each(function (cell, i) {
									cell.innerHTML = i + 1;
								});
							}
							if (CONSTANT.CUSTOM.DEFAULT_OPTION.isTfoot != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isTfoot) {
								var $table_tfoot = $("<tfoot></tfoot>");
								$table_tfoot.append($table_tr.clone());
								$table.append($table_tfoot)
							}
							//渲染完毕后的回调
							//清空全选状态
							if (CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck) {
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
							if (CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isOperate) {
								var lastTd = $('td:last', row);
								var $buttons = CONSTANT.CUSTOM.DEFAULT_OPTION.buttons;
								for (var i in $buttons) {
									(function (i) {
										var _button = $.e($buttons[i].dom);
										var _method = $buttons[i].func;
										_button.on('click', function () {
											$buttons[i].func(this, _table, $table);
										});
										lastTd.append(_button);
									})(i);
								}
							}
						}
					})).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象
					if (CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck != null && CONSTANT.CUSTOM.DEFAULT_OPTION.isCheck) {
						$table.on("change", ":checkbox", function (event) {
							if ($(this).is("[name='cb-check-all']")) {
								//全选
								$(":checkbox", $table).prop("checked", $(this).prop("checked"));
								CONSTANT.CUSTOM.DEFAULT_OPTION.checkCallback.checkAll($("td :checkbox", $table), event)
							} else {
								//一般复选
								var checkbox = $("tbody :checkbox", $table);
								$(":checkbox[name='cb-check-all']", $table).prop('checked', checkbox.length == checkbox.filter(':checked').length);
								CONSTANT.CUSTOM.DEFAULT_OPTION.checkCallback.checkSingle(checkbox.filter(':checked'), event)
							}
						}).on("click", ".td-checkbox", function (event) {
							//点击单元格即点击复选框
							!$(event.target).is(":checkbox") && $(":checkbox", this).trigger("click");
						});
					}
					if(CONSTANT.CUSTOM.DEFAULT_OPTION.rowClick !=null && CONSTANT.CUSTOM.DEFAULT_OPTION.rowClick instanceof Function){
						//行点击事件
						$("tbody", $table).on("click", "tr", function (event) {
							$(this).addClass("active").siblings().removeClass("active");
							//获取该行对应的数据
							var item = _table.row($(this).closest('tr')).data();
							CONSTANT.CUSTOM.DEFAULT_OPTION.rowClick(item, event);
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
		$.fn.table.reload = function (datatables){
			datatables.ajax.reload();
		};
	})(jQuery)
});