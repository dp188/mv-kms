<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2015/8/19
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<aside>
	<div id="sidebar" class="nav-collapse ">
		<ul class="sidebar-menu" id="nav-accordion">
			<li><a href="${pageContext.request.contextPath}/index.html"> <i class="fa fa-dashboard"></i> <span>Dashboard</span>
			</a></li>
			<li class="sub-menu dcjq-parent-li">
				<a href="javascript:;" class="dcjq-parent active"> 
					<i class="fa fa-laptop"></i> <span>监控</span>
				</a>
				<ul class="sub">
					<li><a href="${pageContext.request.contextPath}/moveCMD/mirror.html">多对多监控</a></li>   
					<li><a href="${pageContext.request.contextPath}/live/mirror.html">一对多监控</a></li>   
				</ul>
			</li>
		</ul>
	</div>
</aside>