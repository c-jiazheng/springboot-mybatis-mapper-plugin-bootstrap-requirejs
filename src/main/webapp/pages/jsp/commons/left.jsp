<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- /.navbar-top-links -->
<div class="navbar-default sidebar" role="navigation">
	<div class="sidebar-nav navbar-collapse">
		<ul class="nav" id="side-menu">
			<li class="sidebar-search">
				<div class="input-group custom-search-form">
					<input type="text" class="form-control" placeholder="搜索...">
					<span class="input-group-btn">
						<button class="btn btn-default" type="button">
							<i class="fa fa-search"></i>
						</button> </span>
				</div>
				<!-- /input-group -->
			</li>
			<li>
				<a href="/index"><i class="fa fa-dashboard fa-fw"></i> 首页</a>
			</li>
			<c:forEach items="${resources}" var="vo" varStatus="vs">
					<c:if test="${vo.resLevel == 1}">
						<li>
							<a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> ${vo.resName}<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<c:forEach items="${resources}" var="secVo" varStatus="secVs">
									<c:if test="${secVo.resLevel == 2 && vo.resNo == secVo.parentResNo}">
										<li>
											<a href="${secVo.resUrl}">${secVo.resName}</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							<!-- /.nav-second-level -->
						</li>
					</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- /.sidebar-collapse -->
</div>
<!-- /.navbar-static-side -->