<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head lang="en">
	<meta charset="UTF-8" />
	<title>login jsp</title>
	<jsp:include page="/pages/jsp/commons/style.jsp"></jsp:include>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-md-4 col-md-offset-4">
			<div class="login-panel panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Please Sign In</h3>
				</div>
				<div class="panel-body">
					<form id="jq_form" role="form">
						<fieldset>
							<div class="form-group">
								<input class="form-control validate[required]" placeholder="account" name="account" type="text" />
							</div>
							<div class="form-group">
								<input class="form-control validate[required]" placeholder="password" name="password" type="password" value=""/>
							</div>
							<div class="checkbox">
								<label>
									<input name="remember" type="checkbox" value="Remember Me"/>Remember Me
								</label>
							</div>
							<!-- Change this to a button or input when using this as a form -->
							<button id="b_submit" class="btn btn-lg btn-success btn-block">Login</button>
						</fieldset>
					</form>
				</div>
				<div class="panel-footer">
					<span>一键登录</span> :
					<button id="b_oneKey" class="btn btn-default">登录</button>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script data-start="/static/js/login" data-main="/static/js/main" src="/static/plugin/require/require.js"></script>
</html>