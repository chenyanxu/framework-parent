<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>Code Generation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="shortcut icon" type="image/x-icon" href=""/>
    <link rel="stylesheet" href="/kalix/extjs/classic/theme-triton/theme-triton-all.css" type="text/css" />
    <script type="text/javascript" src="/kalix/extjs/classic/ext-all.js"></script>
    <script type="text/javascript" src="/kalix/extjs/classic/locale/locale-zh_CN.js"></script>
    <script type="text/javascript">
        Ext.onReady(function () {
            Ext.create('Ext.container.Viewport', {
                layout: 'border',
                items:[{
                    region: 'center',
                    xtype: 'form',
                    title:'代码生成',
                    buttonAlign:'left',
                    defaultType: 'textfield',
                    jsonSubmit: true,
                    url:'/cg/camel/rest/',
                    items: [
                        {
                            fieldLabel: '机构代码',
                            name: 'inputDir'
                        }
                    ],
                    buttons:[
                        {
                            text:"生成",
                            handler:function(){
                                this.findParentByType('form').submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert("1", action.result.msg);
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert("2", action.result.msg);
                                    }
                                });
                            }
                        }
                    ]
                }],
                renderTo: Ext.getBody()
            });
        });
    </script>
</head>
<body>
</body>
</html>