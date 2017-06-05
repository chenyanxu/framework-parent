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
    <!--add this logic for the org chart display-->
    <!--[if lt ie9]>
    <script type="text/javascript" src="<%=path %>/resources/js/excanvas.js"></script>
    <![endif]-->
    <script type="text/javascript" src="<%=path %>/resources/js/dynamic-loader.js"></script>
    <script type="text/javascript" src="<%=path %>/extjs/classic/ext-all-debug.js"></script>
    <script type="text/javascript" src="<%=path %>/extjs/classic/locale/locale-zh_CN.js"></script>
    <script type="text/javascript" src="<%=path %>/resources/js/globle-custom.js"></script>
    <script type="text/javascript" src="<%=path %>/resources/js/underscore-min.js"></script>
    <script type="text/javascript">
        CONFIG.restRoot = '<%=path %>';
        Ext.Loader.setConfig({ enabled: true, disableCaching: false });
    </script>
    <script type="text/javascript" src="<%=path %>/resources/js/index.js"></script>
</head>
<body>
</body>
</html>