/**
 * <moduleDescription>首页
 *
 * @author
 * @version 1.0.0
 */
Ext.define('<extjsPrefix>.Main', {
    extend: 'kalix.container.BaseContainer',
    requires: [
        '<extjsPrefix>.view.<beanName>Grid',
        '<extjsPrefix>.view.<beanName>SearchForm'
    ],
    items: [
        {
            title: '<moduleDescription>查询',
            xtype: '<pomName>SearchForm'
        }, {
            xtype: '<pomName>GridPanel',
            id: '<pomName>GridPanel',
            title: '<moduleDescription>列表',
            margin: 10
        }
    ]
});
