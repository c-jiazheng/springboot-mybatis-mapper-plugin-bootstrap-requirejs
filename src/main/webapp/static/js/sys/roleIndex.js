requirejs(['initStyle', 'initMethod']);

define("initStyle", ["utils/art"], function (art) {
	$(function () {
		$.get("/role/queryList", function (data) {
			var json = JSON.parse(data);
			var source = '{{each list as obj i}}'
				+ '<label class="btn btn-primary">'
				+ '<input id="{{obj.id}}" class="selectBtn" name="{{obj.roleName}}" type="radio" >{{obj.roleName}}'
				+ '</label>'
				+ '{{/each}}';
			var param = {
				list: json.dataList
			};
			var html = art.render(source, param);
			$("#jq_role_list").html(html);
			//bind interaction method
			$("#jq_role_list .btn").click(function () {
				var roleId = $(this).children().attr("id");
				$.get("/role/query", {id: roleId}, function (data) {
					var json = JSON.parse(data);
					$("#jq_curRoleName").text(json.data.roleName);
					var param = {
						allList: json.allResources,
						roleList: json.roleResources
					};
					var source =
						'<input type="hidden" id="'+json.data.id+'" name="roleId"/>'
						+ '{{each allList as obj i}}'
						+ ' {{if obj.resLevel == 1}}'
						+ '     <div class="panel panel-default">'
						+ '         <div class="panel-heading">'
						+ '             <h4 class="panel-title">'
						+ '                 <a data-toggle="collapse" data-resNo="fir_{{obj.resNo}}" data-parent="#jq_resource_manager" href="#collapse{{i}}">'
						+ '                     {{obj.resName}}'
						+ '                 </a>'
						+ '             </h4>'
						+ '         </div>'
						+ '         <div id="collapse{{i}}" class="panel-collapse collapse {{if i == 0}}in{{/if}}">'
						+ '             <div class="panel-body">'
						+ '                 {{each allList as obj2 i2}}'
						+ '                     {{if obj.resNo == obj2.parentResNo && obj2.resLevel == 2}}'
						+ '                         <div class="pull-left">'
						+ '                             <div class="form-group">'
						+ '                                 <div class="checkbox">'
						+ '                                     <label><h3>'
						+ '                                         <input id="{{obj2.id}}" data-parent="{{obj.id}}" data-type="2" type="checkbox" '
						+ '                                         data-resNo="sec_{{obj2.resNo}}" data-parentResNo="fir_{{obj2.parentResNo}}"'
						+ '                                         {{if isInclude(obj2.id,roleList, "id") == 1}}checked=\"checked\"{{/if}}>{{obj2.resName}}'
						+ '                                     </h3></label>'
						+ '                                 </div>'
						+ '                             </div>'
						+ '                             <ul class="form-group" style="margin-left: -37px;">'
						+ '                             {{each allList as obj3 i3}}'
						+ '                                 {{if obj3.resLevel == 3 && obj2.resNo == obj3.parentResNo}}'
						+ '                                         <li class="checkbox pull-left" style="margin: 0 0 0 15px;width: 70px;">'
						+ '                                             <label>'
						+ '                                                 <input id="{{obj3.id}}" data-parent="{{obj2.id}}" data-type="3" type="checkbox" '
						+ '                                                 data-resNo="three_{{obj3.resNo}}" data-parentResNo="sec_{{obj3.parentResNo}}"'
						+ '                                                 {{if isInclude(obj3.id,roleList, "id") == 1}}checked="checked"{{/if}}>{{obj3.resName}}'
						+ '                                             </label>'
						+ '                                         </li>'
						+ '                                 {{/if}}'
						+ '                             {{/each}}'
						+ '                             </ul>'
						+ '                         </div>'
						+ '                     {{/if}}'
						+ '                 {{/each}}'
						+ '             </div>'
						+ '         </div>'
						+ '     </div>'
						+ ' {{/if}}'
						+ '{{/each}}';
					var html = art.render(source, param);
					$("#jq_resource_manager").html(html);
					//bind interaction method
					$("input[data-parentResNo]").on("click", function () {
						//上级
						var parentResNo = $(this).attr("data-parentResNo");
						var parentCheckLength = $("input[data-resNo='" + parentResNo + "']").length;
						var parentCheckState = $("input[data-resNo='" + parentResNo + "']").prop("checked");
						if (parentCheckLength > 0 && !parentCheckState) {
							$("input[data-resNo='" + parentResNo + "']").prop("checked", true);
						}
						//同级
						var silbingsCheckState = false;
						$("input[data-parentResNo='" + parentResNo + "']").each(function (i, v) {
							if ($(v).prop("checked")) {
								silbingsCheckState = true;
								return false;
							}
						});
						if (parentCheckLength > 0 && !silbingsCheckState) {
							$("input[data-resNo='" + parentResNo + "']").prop("checked", false);
						}
						//下级
						if ($(this).prop("checked")) {
							$("input[data-parentResNo='" + $(this).attr("data-resNo") + "']").prop("checked", true);
						} else {
							$("input[data-parentResNo='" + $(this).attr("data-resNo") + "']").prop("checked", false);
						}
					})
				});
			});
		});
	});
});
define("initMethod", function () {
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
		$.post("/role/insert", formInput, function (data) {
			var json = JSON.parse(data);
			if (json.result) {
				layer.alert(json.msg);
			} else {
				layer.alert(json.msg);
			}
			layer.closeAll('loading');
		});
	});

	$("#jq_submit_role_resource").on("click", function () {
		//layer.load(1, {shade: 0.6});
		var form = $("#jq_resource_manager");
		var submitMenuList = [];
		var secMenuList = form.find("[data-type='2']:checked");
		var threeMenuList = form.find("[data-type='3']:checked");
		secMenuList.each(function(i, v){
			var oneId = $(v).attr("data-parent");
			if((","+submitMenuList.join(",")+",").indexOf(","+oneId+","))
				submitMenuList.push(oneId);
		});
		secMenuList.each(function(i, v){
			var secId = $(v).attr("id");
			submitMenuList.push(secId);
		});
		threeMenuList.each(function (i, v) {
			var threeId = $(v).attr("id");
			submitMenuList.push(threeId)
		});
		var resourceId = submitMenuList.join(",");
		var roleId = $("input[name='roleId']").attr("id");
		$.post("/role/updateRoleResource", {id: roleId,resourceId: resourceId}, function (data) {
			var json = JSON.parse(data);
			if (json.result) {
				layer.alert(json.msg);
			} else {
				layer.alert(json.msg);
			}
			layer.closeAll('loading');
		});
	});
});

