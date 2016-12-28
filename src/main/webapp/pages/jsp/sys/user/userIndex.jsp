<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head lang="en">
	<meta charset="UTF-8"/>
	<title>index jsp</title>
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
		<%-- sreach--%>
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
		<%-- add --%>
		<div id="jq_add_form" class="panel panel-default" style="display: none">
			<div class="panel-heading">新增账号</div>
			<div class="panel-body">
				<form id="add_form" action="" class="form-inline" role="form" onsubmit="return false;">
					<div class="col-sm-12" style="margin-bottom: 15px">
						<div class="form-group col-sm-6">
							<label for="account" class="col-sm-4">账号：</label>
							<div class="col-sm-8">
								<input id="jq_account" name="account" type="text" class="form-control input-sm" />
							</div>
						</div>
						<div class="fomr-group col-sm-6">
							<label for="jq_name" class="col-sm-4">姓名：</label>
							<div class="col-sm-8">
								<input id="jq_name" name="name" type="text" class="form-control input-sm" />
							</div>
						</div>
					</div>
					<div class="col-sm-12" style="margin-bottom: 15px">
						<div class="form-group col-sm-6">
							<label for="jq_password" class="col-sm-4">密码：</label>
							<div class="col-sm-8">
								<input id="jq_password" name="password" type="text" class="form-control input-sm" />
							</div>
						</div>
						<div class="form-group col-sm-6">
							<label for="jq_repassword" class="col-sm-4">确认密码：</label>
							<div class="col-sm-8">
								<input id="jq_repassword" type="text" class="form-control input-sm" />
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
		<button id="jq_add" type="button" class="btn btn-primary clearfix" style="margin-bottom: 10px;;">新增</button>
		<div class="table-responsive">
			<table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
				<thead>
					<tr>
						<th>序号</th>
						<th>账号</th>
						<th>真实姓名</th>
						<th>创建日期</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd gradeX">
						<td></td>
						<td></td>
						<td></td>
						<td class="center"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</body>

<script data-start="/static/js/sys/userIndex" data-main="/static/js/main" src="/static/plugin/require/require.js"></script>
</html>