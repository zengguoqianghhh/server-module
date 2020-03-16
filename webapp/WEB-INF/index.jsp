<%@ page contentType="text/html; charset=utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
    <title>首页</title>
    <style>
        body{text-align: center;}
    </style>
</head>
<body>
<h2>欢迎来到首页！</h2><hr/>
<div>
    <h1>这是端口8080得index，jsp</h1>
</div>
</body>
</html>