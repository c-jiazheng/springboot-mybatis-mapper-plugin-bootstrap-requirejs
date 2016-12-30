<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en">
	<meta charset="UTF-8"/>
	<title>role index page</title>
	<jsp:include page="/pages/jsp/commons/style.jsp"></jsp:include>
</head>
<body>
<div id="wrapper">
	<jsp:include page="/pages/jsp/commons/head.jsp"></jsp:include>
	<jsp:include page="/pages/jsp/commons/left.jsp"></jsp:include>
	<div id="page-wrapper">
		<%--title--%>
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">角色管理</h1>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<%-- add --%>
		<div id="jq_add_form" class="panel panel-default" style="display: none">
			<div class="panel-heading">新增角色</div>
			<div class="panel-body">
				<form id="add_form" action="" class="form-inline" role="form" onsubmit="return false;">
					<div class="col-sm-12" style="margin-bottom: 15px">
						<div class="form-group col-sm-6">
							<label for="jq_roleName" class="col-sm-4">角色名称:</label>
							<div class="col-sm-8">
								<input id="jq_roleName" name="roleName" type="text" class="form-control input-sm"/>
							</div>
						</div>
						<div class="fomr-group col-sm-6">
							<label for="jq_name" class="col-sm-4">角色描述：</label>
							<div class="col-sm-8">
								<input id="jq_name" name="description" type="text" class="form-control input-sm"/>
							</div>
						</div>
					</div>
					<center>
						<button id="jq_submit" type="submit" class="btn btn-primary">提交</button>
						<button id="jq_close" type="button" class="btn btn-primary">关闭</button>
					</center>
				</form>
			</div>
		</div>
		<%-- content--%>
		<div>
			<button id="jq_add" type="button" class="btn btn-primary clearfix" style="margin-bottom: 10px;;">新增</button>
		</div>
		<%--角色列表组--%>
		<div style="margin-bottom: 10px;">
			<div id="jq_role_list" class="btn-group" data-toggle="buttons"></div>
		</div>
		<div>
			<div class="panel-heading">角色：<b><span id="jq_curRoleName"></span></b></div>
			<hr/>
			<form id="jq_resource_manager" action="" role="form" class="panel-group" ></form>
			<div>
				<button id="jq_submit_role_resource" type="button" class="btn btn-primary clearfix">提交</button>
			</div>
		</div>
	</div>
</div>
</body>

<script data-start="/static/js/sys/roleIndex" data-main="/static/js/main" src="/static/plugin/require/require.js"></script>
</html>