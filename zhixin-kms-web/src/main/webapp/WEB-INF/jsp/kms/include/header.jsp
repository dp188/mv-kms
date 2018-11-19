<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2015/8/19
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <!--header start-->
      <header class="header white-bg">
          <div class="sidebar-toggle-box">
              <div data-original-title="显示、隐藏系统菜单" data-placement="right" class="fa fa-bars tooltips"></div>
          </div>
          <!--logo start-->
          <!--logo end--> 
          <div class="top-nav ">
              <ul class="nav pull-right top-menu"> 
                  <!-- user login dropdown start-->
                  <li class="dropdown">
                      <a data-toggle="dropdown" class="dropdown-toggle" href="${pageContext.request.contextPath}/#"> 
                          <span class="username">湖南挚新-System</span>
                          <b class="caret"></b>
                      </a>
                      <ul class="dropdown-menu extended logout">
                          <div class="log-arrow-up"></div>
                          <li><a href="#" onclick="javascript:void(0)"><i class=" fa fa-suitcase"></i>个人信息</a></li>
                          <li><a href="#" onclick="javascript:void(0)"><i class="fa fa-cog"></i>修改密码</a></li>
                          <li><a href="#" onclick="javascript:void(0)"><i class="fa fa-bell-o"></i> 通知</a></li>
                          <li><a href="${pageContext.request.contextPath}/login.html"><i class="fa fa-key"></i> 退出</a></li>
                      </ul>
                  </li> 
              </ul>
          </div>
      </header>