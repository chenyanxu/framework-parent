/**
 * <moduleDescription>新增和修改表单
 *
 * @author
 * @version 1.0.0
 */
Ext.define('<extjsPrefix>.view.<beanName>Window', {
    extend: 'kalix.view.components.common.BaseWindow',
    requires: [
        'kalix.controller.BaseWindowController'
    ],
    alias: 'widget.<pomName>Window',
    controller: {
        type: 'baseWindowController'
    },
    xtype: "<pomName>Window",
    width: 400,
    //todo 在此修改表单
    items: [
        {
            xtype: 'baseForm',
            items: [
                <WindowItems>
            ]
        }
    ]
});