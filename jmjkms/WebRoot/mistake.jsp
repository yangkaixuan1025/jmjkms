<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
    <base href="<%=basePath%>">
	<meta charset="UTF-8">
    <title>error</title>
	<link rel="stylesheet" type="text/css" href="/jmjkms/css/main.css">
	
  </head>
  <body style="background-color: #ccc;
		text-align: center; text-decoration:none;">
    <div id="dino">
		<span style="font-size:30px;">Page Not Found</span>
		<h2 id="hno">页面错误！</h2>
		<p id="pno">抱歉！您的操作出错了！！！</p>
		<input type="button" value="返回首页" class="btn" onclick="<%=basePath %>index.jsp">
	</div>
  </body>
</html>
