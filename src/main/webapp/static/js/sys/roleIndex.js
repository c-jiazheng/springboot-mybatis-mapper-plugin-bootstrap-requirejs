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
			$(".btn").click(function () {
				var roleId = $(this).children().attr("id");
				$.get("/role/query", {id: roleId}, function (data) {
					var json = JSON.parse(data);
					$("#jq_curRoleName").text(json.data.roleName);
					console.log(json);
					var param = {
						allList: json.allResources,
						roleList: json.roleResources
					};
					var source =
						'{{each allList as obj i}}'
						+ ' {{if obj.resLevel == 1}}'
						+ '     <div class="panel panel-default">'
						+ '         <div class="panel-heading">'
						+ '             <h4 class="panel-title">'
						+ '                 <a data-toggle="collapse" data-parent="#jq_resource_manager" href="#collapse{{i}}">'
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
						+ '                                         <input type="checkbox" id="sec_{{obj2.resNo}}" '
						+ '                                         {{if isInclude(obj2.id,roleList, "id") == 1}}checked=\"checked\"{{/if}}'
						+ '                                         data-parentResNo="fir_{{obj2.parentResNo}}">{{obj2.resName}}'
						+ '                                     </h3></label>'
						+ '                                 </div>'
						+ '                             </div>'
						+ '                             <ul class="form-group" style="margin-left: -37px;">'
						+ '                             {{each allList as obj3 i3}}'
						+ '                                 {{if obj3.resLevel == 3 && obj2.resNo == obj3.parentResNo}}'
						+ '                                         <li class="checkbox pull-left" style="margin: 0 0 0 15px;width: 70px;">'
						+ '                                             <label>'
						+ '                                                 <input type="checkbox" id="three_{{obj3.resNo}}" '
						+ '                                                 {{if isInclude(obj3.id,roleList, "id") == 1}}checked=\"checked\"{{/if}}'
						+ '                                                 data-parentResNo="sec_{{obj3.parentResNo}}">{{obj3.resName}}'
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
						var parentCheckLength = $("#" + parentResNo).length;
						var parentCheckState = $("#" + parentResNo).prop("checked");
						if (parentCheckLength > 0 && !parentCheckState) {
							$("#" + parentResNo).prop("checked", true);
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
							$("#" + parentResNo).prop("checked", false);
						}
						//下级
						if ($(this).prop("checked")) {
							$("input[data-parentResNo='" + this.id + "']").prop("checked", true);
						} else {
							$("input[data-parentResNo='" + this.id + "']").prop("checked", false);
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
		$.post("/user/add", formInput, function (data) {
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

