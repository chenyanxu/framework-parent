/**
 * <moduleDescription>查看表单
 *
 * @author
 * @version 1.0.0
 */

Ext.define('<extjsPrefix>.view.<beanName>ViewWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    alias: 'widget.<pomName>ViewWindow',
    xtype: "<pomName>ViewWindow",
    width: 400,
    //todo 在此修改查看字段
    items: [
        {
            defaults: {readOnly: true},
            xtype: 'baseForm',
            items: [
                <ViewWindowItems>
            ]
        }
    ]
});