<%@ page import="com.kalix.framework.webapp.cg.internal.InitActivator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<%String path = InitActivator.mainPath;%>
<html manifest="">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>Code Generation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="shortcut icon" type="image/x-icon" href=""/>
    <link rel="stylesheet" href="<%=path %>/extjs/classic/theme-triton/theme-triton-all.css" type="text/css"/>
    <script type="text/javascript" src="<%=path %>/extjs/classic/ext-all.js"></script>
    <script type="text/javascript" src="<%=path %>/extjs/classic/locale/locale-zh_CN.js"></script>
    <script type="text/javascript">
        Ext.onReady(function () {
            Ext.create('Ext.container.Viewport', {
                layout: 'border',
                items: [
                    {
                        style:{'align':'center','vertical-align':'middle'},
                        region: 'center',
                        xtype: 'form',
                        title: '代码生成',
                        buttonAlign: 'left',
                        defaultType: 'textfield',
                        defaultAlign: 'center',
                        jsonSubmit: true,
                        url: '/cg/camel/rest/',
                        items: [
                            {
                                fieldLabel: 'java实体Bean文件目录',
                                xtype: 'filefield',
                                name: 'beanName',
                                labelWidth: 300,
                                msgTarget: 'side',
                                allowBlank: false,
                                anchor: '30%',
                                buttonText: '浏览...'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的中文描述信息',
                                name: 'moduleDescription',
                                allowBlank: false,
                                emptyText: '如：学生管理',
                                value: '学生管理'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的父工程的artifactId',
                                name: 'parentArtifactId',
                                allowBlank: false,
                                emptyText: '如：system-parent',
                                value: 'system-parent'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的父工程的groupId',
                                name: 'parentGroupId',
                                allowBlank: false,
                                emptyText: '如：com.kalix.oa.system',
                                value: 'com.kalix.oa.system'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的artifactId前缀',
                                name: 'artifactIdPrefix',
                                allowBlank: false,
                                emptyText: '如：oa-system-student',
                                value: 'oa-system-student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的名称前缀',
                                name: 'namePrefix',
                                allowBlank: false,
                                emptyText: '如：Kalix OA System Student',
                                value: 'Kalix OA System Student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的项目名称',
                                name: 'projectName',
                                allowBlank: false,
                                emptyText: '如：Student',
                                value: 'Student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的包结构',
                                name: 'packageName',
                                allowBlank: false,
                                emptyText: '如：com.kalix.oa.system.student',
                                value: 'com.kalix.oa.system.student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的pom名称',
                                name: 'pomName',
                                allowBlank: false,
                                emptyText: '如：student',
                                value: 'student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的实体Bean对应的后台表名称',
                                name: 'tableName',
                                allowBlank: false,
                                emptyText: '如：sys_student',
                                value: 'sys_student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的extjs前缀',
                                name: 'extjsPrefix',
                                allowBlank: false,
                                emptyText: '如：kalix.system.student',
                                value: 'kalix.system.student'
                            },
                            {
                                labelWidth: 300,
                                fieldLabel: '新建工程的contextPath',
                                name: 'contextPath',
                                allowBlank: false,
                                emptyText: '如：system/student',
                                value: 'system/student'
                            }
                        ],
                        buttons: [
                            {
                                text: "代码生成",
                                handler: function () {
                                    var scope=this.findParentByType('form');

                                    this.findParentByType('form').submit({
                                        url: '/cg/camel/rest/upload',
                                        waitMsg: '正在生成代码...',
                                        scope:scope,
                                        success: function (fp, o) {
                                            if (o.result.success) {
                                                this.dockedItems.getAt(1).items.getAt(1).enable();
                                                this.dockedItems.getAt(1).items.getAt(1).ariaEl.dom.href=o.result.sourcePath;
                                            }
                                        },
                                        failure: function(fp, o) {
                                            Ext.Msg.alert('错误', o.result.msg);
                                        }
                                    });
                                }
                            },
                            {
                                text: "代码下载",
                                disabled : true,
                                href: 'http://www.baidu.com'
                            }
                        ]
                    }
                ],
                renderTo: Ext.getBody()
            });
        });
    </script>
</head>
<body>
</body>
</html>