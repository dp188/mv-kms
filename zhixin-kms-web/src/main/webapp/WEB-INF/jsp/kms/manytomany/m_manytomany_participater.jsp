<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html >
<html lang="en">
	<head>
		<meta charset="utf-8">
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
   		<meta name="description" content="">
    	<meta name="author" content="Mosaddek">
    	<meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.png">
     	<jsp:include page="../include/styles.jsp"></jsp:include>
     	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/lib/kurento/kurento.css" />
     	<style type="text/css">
	     	#participants {
				width: 100%;
				margin: 5px;
			}
			
			#participants .main{
				width: 30%;
				margin: 5px;
				display: block;
				float: left;
			}
			
			#participants .participant{
				width: 30%;
				margin: 5px;
				display: block;
				float: left;
			}
			
			.participant video{
				width: 100%;
			}
			     	
		</style>
    	<title>视频会议</title>
	</head>	
<body>	
	<div id="container">
		<input type="button" value="退出房间" onclick="anchor.leaveRoom()">
		<div id="wrapper">		
			<div id="room" style="display: block;">
				<h2 id="room-header"></h2>
				<div id="participants"></div>				
			</div>
		</div>
	</div>
	<jsp:include page="../include/script.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/console.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/kurento-utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/adapter.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/app/m_videophone-anchor.js"></script>
	<script type="text/javascript">
	   var config={
			"ws_url" :  (location.href.startsWith("https")? "wss://":"ws://" )+ location.host + "${pageContext.request.contextPath}/many2Many",
			"name":	"${param.CallId}_" +CodeRand(),
			"roomId":"${param.CallId}",
			"roomName":'测试房间',
			"maxWidth" : "${maxWidth}",
			"minWidth" : "${minWidth}",
			"maxFrameRate" : "${maxframeRate}",
			"minFrameRate" : "${minframeRate}"
		};	
		const anchor = new  initAnchor(config);//初始化;		
	</script>

</body>
</html>