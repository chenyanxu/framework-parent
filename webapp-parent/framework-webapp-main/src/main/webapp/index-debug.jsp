<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <title></title>
    <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"/>
    <script type="text/javascript" src="<%=path %>/resources/js/dynamic-loader.js"></script>
    <script type="text/javascript" src="<%=path %>/extjs/classic/ext-all-debug.js"></script>
    <script type="text/javascript" src="<%=path %>/extjs/classic/locale/locale-zh_CN.js"></script>
    <script type="text/javascript" src="<%=path %>/resources/js/globle-custom.js"></script>
    <script type="text/javascript" src="<%=path %>/resources/js/underscore-min.js"></script>
    <script type="text/javascript">CONFIG.restRoot = '<%=path %>';</script>
    <script type="text/javascript" src="<%=path %>/resources/js/index.js"></script>
    <script type="text/javascript" src="<%=path %>/resources/js/tinymce/tinymce.min.js"></script>
</head>
<body>
</body>
</html>