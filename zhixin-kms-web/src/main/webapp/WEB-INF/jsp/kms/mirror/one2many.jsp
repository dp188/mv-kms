<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Mosaddek">
    <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.png">
     <jsp:include page="../include/styles.jsp"></jsp:include>
    <title>一对多监控</title>
  </head>
  <body>
  <section id="container" class="">
      <jsp:include page="../include/header.jsp"></jsp:include>
       <jsp:include page="../include/menu.jsp"></jsp:include>
      <section id="main-content">
           <section class="wrapper site-min-height" module="role">
			<div class="row"> 
				<div class="col-sm-12">
					<section class="panel">
						<header class="panel-heading">
						<!-- 	<fieldset style="float: left; padding-left: 12px;">
		                         <a class="btn btn-warning btn-restart-notify"  href="javascript:void(0)">手动重试</a>  
		                         <a class="btn btn-danger  btn-delete"  href="javascript:void(0)">删除</a> 
							</fieldset> -->
							<fieldset style="float: right; padding-right: 10px;">
								<div  class="form-inline search-form">
                                     <label>状态：</label>
									<select id="status" name="status" style="height: 34px !important"  class="form-control" title="">
											<option value="1">已创建</option> 
											<option value="2" selected="selected">直播中</option>
											<option value="3">已关闭</option> 
										</select>
									<label>名称：</label> <input id="name" name="name" class="form-control"
										type="text" placeholder="房间名称" />
									<button type="button" class="btn btn-primary btn-search" onclick="javascript:void(0)">
										查询 <i class="icon-search icon-on-right bigger-110"></i>
									</button>
								</div>
							</fieldset>
						</header>
						<div class="panel-body">
							<div class="adv-table">
								<div id="dynamic-table_wrapper"
									class="dataTables_wrapper form-inline" role="grid"> 
									 <!-- 表格区域 -->
									<table id="grid-table"></table>
									<!-- 分页区域 -->
									<div id="grid-pager"></div>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div> 
			<!-- Modal --> 
 		 </section>
      </section> 
     <jsp:include page="../include/footer.jsp"></jsp:include>
      <!--footer end-->
  </section>
  <jsp:include page="../include/script.jsp"></jsp:include>
	<script type="text/javascript">
		var root = $("section[module=role]");
		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";
		var apiPath = "${pageContext.request.contextPath}/live/list";
		
		initEventBind(); 
		initTab();
		
		function initEventBind(){
			root.find(".btn-delete").on("click", function(event) {toDelete()});
			root.find(".btn-search").on("click", function(event) {searchForm()});
			root.find("#status").on("change",function(){
				searchForm();
			});
			root.find(".search-form :input").on("keyup", function(event) {
				if (event.keyCode == 13) {
					searchForm()
				}
			}); 
		}
		function initTab(){
			root.find(grid_selector).jqGrid({
				url : apiPath+"?status=2",
				datatype : "json",
				mtype : 'GET',
				height : 530,
				colNames : [ 'id',  '房间名称',  '人员数量','文件路径','状态','创建时间','关闭时间'],// 标题定义
				colModel : [ {
					name : 'id',
					index : 'id',
					width : 60,
					align : "left"
				}, {
					name : 'name',
					index : 'name',
					width : 60,
					align : "left"
				}, {
					name : 'userCount',
					index : 'userCount',
					width : 60,
					align : "left"
				}, {
					name : 'livePath',
					index : 'livePath',
					width : 60,
					align : "left"
				},  {
					name : 'status',
					index : 'status',
					width : 60,
					align : "left",
					formatter : function(cellvalue, options, row) {
						if(cellvalue == 0){
							return "已删除";
						}else if(cellvalue == 1){
							return "已创建";
						} else if(cellvalue == 2){
							return "直播中";
						} else if(cellvalue == 3){
							return "已关闭";
						} 
						return "未知";
					}
				}, {
					name : 'createdTime',
					index : 'createdTime',
					width : 60,
					align : "left"
				}, {
					name : 'updatedTime',
					index : 'updatedTime',
					width : 60,
					align : "left" 
				} ],
				autowidth : true,
				rownumbers : true,// 添加左侧行号
				viewrecords : false,
				multiselect : true,
				pagerpos : 'center',
				rowNum : 10,// 每页显示记录数
				rowList : [ 10, 20 ],// 用于改变显示行数的下拉列表框的元素数组。
				jsonReader : {
					root : "data.result", // json中代表实际模型数据的入口
					page : "result_info.page", // json中代表当前页码的数据
					total : "result_info.page_count", // json中代表页码总数的数据
					records : "result_info.total_count", // json中代表数据行总数的数据
					repeatitems : false
				// 如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
				},
				prmNames : {
					page : "page", // 表示请求页码的参数名称
					rows : "per_page", // 表示请求行数的参数名称
					order : "direction" // 表示采用的排序方式的参数名称
				},
				pager : root.find(pager_selector)
			});
		}
		function searchForm() {
			root.find(grid_selector).jqGrid('setGridParam', {
				url : apiPath,
				postData : {
					'name__constains' : root.find("#name").val(),
					'status':root.find("#status").val()
				}, // 发送数据
				page : 1
			}).trigger("reloadGrid"); // 重新载入
		}  
	</script> 
  </body>
</html>

