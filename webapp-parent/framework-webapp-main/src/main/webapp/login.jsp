<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <style type="text/css">html{height:100%}</style>
    <link rel="icon" href="" type="image/x-icon"/>
    <script type="text/javascript" src="<%=path %>/resources/js/dynamic-loader.js"></script>
</head>
<body style='background: url(resources/images/loading.gif) 50% 50% no-repeat;'>
</body>
<script type="text/javascript">var rootPath = '<%=path %>';</script>
<script type="text/javascript" src="<%=path %>/resources/js/login.js"></script>
</html>