<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Mosaddek">
    <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    <link rel="shortcut icon" href="img/favicon.png">

   <title>湖南挚新 EFrom-System</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/styles/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/styles/bootstrap-reset.css" rel="stylesheet">
    <!--external css-->
    <link href="${pageContext.request.contextPath}/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/styles/style-responsive.css" rel="stylesheet" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/lib/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/lib/respond.min.js"></script>
    <![endif]-->
</head>

  <body class="login-body login_index"> 

    <div class="container">

      <form class="form-signin" action="#">
        <h2 class="form-signin-heading">用户登录</h2>
        <div class="login-wrap">
            <input type="text" id="userName" name="userName" class="form-control" placeholder="用户名" autofocus  >
            <input type="password" id="password" name="password" class="form-control" placeholder="密码">
            <button class="btn btn-lg btn-login btn-block" type="button">登录</button>  
        </div> 
      </form>
          
    </div>



    <!-- js placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.cookie.js"></script> 
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/json3/lib/json3.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.serialAll.js"></script> 
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery.cookie.js"></script>  
    
	<script>
		$(".form-signin button").on("click", function() {
			var userName =$("#userName").val();
			var password =$("#password").val();
			var user = {
						userName:userName,
						password:password
					   };
			$.ajax({
				url : "${pageContext.request.contextPath}/api/auth/login",
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				type : "POST",
				data : JSON.stringify(user),
				success : function(data) {
					window.location.href="${pageContext.request.contextPath}/index.html";
				}, 
				error : function(data) {
					alert(data);
				}
			});
		});
	</script>
</body>
</html>
