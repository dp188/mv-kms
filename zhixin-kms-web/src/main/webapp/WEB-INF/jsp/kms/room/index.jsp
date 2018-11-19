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
    <title>湖南挚新 EFrom-System</title>
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
							<fieldset style="float: left; padding-left: 12px;">
		                         <a class="btn btn-warning btn-to-create"  href="javascript:void(0)">直播</a>  
		                          <a class="btn btn-warning btn-to-viewer  href="javascript:void(0)">观看</a>  
		                         <a class="btn btn-danger  btn-delete"  href="javascript:void(0)">强制关闭</a> 
							</fieldset>
							<fieldset style="float: right; padding-right: 10px;">
								<div  class="form-inline search-form">
                                     <label>状态：</label>
									<select id="status" name="status" style="height: 34px !important"  class="form-control" title="">
											<option value="1">直播中</option> 
											<option value="0">已关闭</option>
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
		var apiPath = "${pageContext.request.contextPath}/api/kms/room/";
		var kmsDefinitionList = null;
		initTab();
		initEventBind();  
		
		function initEventBind(){
		 
			root.find(".btn-to-create").on("click", function(event) {toCreate()});
			root.find(".btn-to-viewer").on("click", function(event) {toViewer()});
			root.find(".btn-delete").on("click", function(event) {toDelete()});
			root.find(".btn-search").on("click", function(event) {searchForm()});
 
			root.find("#status").on("change", function(event) {
				searchForm()
			}); 
			root.find(".btn-search").on("click", function(event) {searchForm()});
		}
		function initTab(){
			root.find(grid_selector).jqGrid({
				url : apiPath+"?status=1",
				datatype : "json",
				mtype : 'GET',
				height : 530,
				colNames : [ 'id',  '房间名称', '密码','创建时间','关闭时间','状态'],// 标题定义
				colModel : [ {
					name : 'id',
					index : 'id',
					hidden : true
				}, {
					name : 'name',
					index : 'name',
					width : 60,
					align : "left"
				}, {
					name : 'key',
					index : 'key',
					width : 60,
					align : "left"
				}, {
					name : 'createdTime',
					index : 'createdTime',
					width : 60,
					align : "left"
				}, {
					name : 'updatedTime',
					index : 'updatedTime',
					width : 60,
					align : "left",
					formatter : function(cellvalue, options, row) {
						if(row["status"] == 0){
							return cellvalue;
						}else if(row["status"] == 1){
							return "直播中";
						} 
						return "未知";
					}
				} ,  {
					name : 'status',
					index : 'status',
					width : 60,
					align : "left",
					formatter : function(cellvalue, options, row) {
						if(cellvalue == 0){
							return "已关闭";
						}else if(cellvalue == 1){
							return "直播中";
						} 
						return "未知";
					}
				}],
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
					'status' : root.find("#status").val()
				}, // 发送数据
				page : 1
			}).trigger("reloadGrid"); // 重新载入
		}  

		function toCreate() {
			 window.location.href="${pageContext.request.contextPath}/room/present.html";
		}
		function toViewer() {
			var selectedIds = root.find(grid_selector).jqGrid("getGridParam","selarrrow");
			var toDeleteIdList = new Array();
			if (!selectedIds || selectedIds.length == 0) {
				alert("请至少选择一条数据！");
				return false;
			}
			if(selectedIds.length>1){
				alert("只能选择一个房间")
				return false;
			}
			//to viewer
			var roomId = root.find(grid_selector).jqGrid("getRowData",selectedIds[0]).id;
			 window.location.href="${pageContext.request.contextPath}/room/viewer.html?roomId="+roomId;
		}
		function toStop() {
			var selectedIds = root.find(grid_selector).jqGrid("getGridParam","selarrrow");
			var toDeleteIdList = new Array();
			if (!selectedIds || selectedIds.length == 0) {
				alert("请至少选择一条数据！");
				return false;
			}
			for (var i = 0; i < selectedIds.length; i++) {
				toDeleteIdList.push(root.find(grid_selector).jqGrid("getRowData",selectedIds[i]).id);
			}
			$.REST.DELETE({
				url : apiPath,
				data : JSON.stringify(toDeleteIdList),
				success : function(data) {
					if (data.success) { 
						searchForm();
					} else {
						alert(data.message);
					}
				},
				error : function(data) {
					alert(data.message);
				}
			});
		}
	</script> 
  </body>
</html>

