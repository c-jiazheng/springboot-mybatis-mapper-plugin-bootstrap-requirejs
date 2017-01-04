<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head lang="en">
	<meta charset="UTF-8"/>
	<title>user index jsp</title>
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
				<h1 class="page-header">用户管理</h1>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<%--sreach--%>
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bar-chart-o fa-fw"></i> 条件查询
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form class="form-inline" role="form">
					<div class="form-group">
						<label for="account">账号：</label>
						<input type="text" class="form-control" id="account" placeholder="输入查询账号"/>
					</div>
					<div class="form-group">
						<label for="mobile">手机号：</label>
						<input type="text" class="form-control" id="mobile" placeholder="输入查询手机号"/>
					</div>
					<button type="submit" class="btn btn-default form-control pull-right">查询</button>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<%--add--%>
		<div id="jq_add_form_div"></div>
		<%--content--%>
		<button id="jq_add" type="button" class="btn btn-primary clearfix" style="margin-bottom: 10px;;">新增</button>
		<div id="div-table-container" class="table-responsive">
			<table width="100%" class="table table-bordered table-hover" id="table-user" cellspacing="0">
			</table>
		</div>
	</div>
</div>
</body>

<script data-start="/static/js/sys/userIndex" data-main="/static/js/main" src="/static/plugin/require/require.js"></script>
</html>