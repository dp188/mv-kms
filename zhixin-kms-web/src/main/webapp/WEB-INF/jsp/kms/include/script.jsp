<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2015/8/19
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- js placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/json3/lib/json3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.serialAll.js"></script>  
<script class="include" type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.dcjqaccordion.2.7.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.scrollTo.min.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/slidebars.min.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.nicescroll.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/respond.min.js" ></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap.min.js"></script>  
<script  type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap3-dialog/js/bootstrap-dialog.js"></script>  
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/fuelux/js/spinner.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-fileupload/bootstrap-fileupload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-wysihtml5/wysihtml5-0.3.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-wysihtml5/bootstrap-wysihtml5.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-daterangepicker/moment.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-daterangepicker/daterangepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/jquery-multi-select/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/jquery-multi-select/js/jquery.quicksearch.js"></script>


<!--summernote--> 
<!-- <script src="${pageContext.request.contextPath}/assets/summernote/dist/summernote.min.js"></script> --> 
<script src="${pageContext.request.contextPath}/assets/summernote/summernote.min.js"></script> 

<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jqGrid/js/jquery.jqGrid.src.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jqGrid/js/i18n/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/count.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.sparkline.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/sparkline-chart.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.customSelect.min.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.cookie.js"></script> 
 
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/zTree_v3/js/jquery.ztree.all-3.5.js"></script> 

<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.validator.messages_cn.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plupload/moxie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plupload/plupload.dev.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.restful.js"></script>

<!--common script for all pages-->
<script src="${pageContext.request.contextPath}/lib/common-scripts.js"></script>

<script>
$(".fa-key").parent().on("click",function(){
	$.REST.GET({
		url : "${pageContext.request.contextPath}/api/auth/logout",
		success : function(data) {
			 winow.location.href="${pageContext.request.contextPath}/login.html";
		},
		error : function(data) {
			alert(data.message);
		}
	});
	$.cookie("JSESSIONID");
});

</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/app/caption.js"></script>
