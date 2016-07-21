/**
 * <moduleDescription>表格
 * @author
 * @version 1.0.0
 */
Ext.define('<extjsPrefix>.view.<beanName>Grid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        '<extjsPrefix>.controller.<beanName>GridController',
        '<extjsPrefix>.store.<beanName>Store'
    ],
    alias: 'widget.<pomName>Grid',
    xtype: '<pomName>GridPanel',
    controller: {
        type: '<pomName>GridController',
        cfgForm: '<extjsPrefix>.view.<beanName>Window',
        cfgViewForm: '<extjsPrefix>.view.<beanName>ViewWindow',
        cfgModel: '<extjsPrefix>.model.<beanName>Model'
    },
    store: {
        type: '<pomName>Store'
    },

    //todo 在此修改grid显示列
    columns: {
        defaults: {flex: 1,renderer: 'addTooltip'},
        items: [
            {
                xtype: "rownumberer",
                text: "行号",
                width: 50,
                flex: 0,
                align: 'center',
                renderer:this.update
            },
            {
                text: '编号',
                dataIndex: 'id',
                hidden: true
            },
            <GridItems>
            {
                xtype: 'securityGridColumnCommon',
                //todo change permission
                items: [
                    {
                        iconCls: "iconfont icon-view-column",
                        permission: '',
                        tooltip: '查看',
                        handler: 'onView'
                    },
                    {
                        //bind: {icon: ''},
                        iconCls: "iconfont icon-edit-column",
                        permission: '',
                        tooltip: '编辑',
                        handler: 'onEdit'
                    },
                    {
                        iconCls: "iconfont icon-delete",
                        permission: '',
                        tooltip: '删除',
                        handler: 'onDelete'
                    }
                ]
            }
        ]
    },
    tbar: {
        xtype: 'securityToolbar',
        verifyItems: [
            {
                text: '添加',
                xtype: 'button',
                iconCls:'iconfont icon-add',
                permission: '',
                handler: 'onAdd'
            }
        ]
    }
});
