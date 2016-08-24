/**
 * 字典表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.dict.view.DictGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.dict.controller.DictGridController'
    ],
    alias: 'widget.dictGrid',
    xtype: 'dictGridPanel',
    controller: {
        type: 'dictGridController',
        cfgForm: 'kalix.dict.view.DictWindow',
        cfgViewForm: 'kalix.dict.view.DictViewWindow',
        cfgModel: 'kalix.dict.model.DictModel'
    },
    columns: [
        {
            xtype: 'rownumberer',
        },
        {text: '编号', dataIndex: 'id', hidden: true},
        {text: '类型', dataIndex: 'type'},
        {text: '标签名', dataIndex: 'label'},
        {text: '数值', dataIndex: 'value'},
        {text: '创建人', dataIndex: 'createBy'},
        {
            text: '创建日期', dataIndex: 'creationDate'
        },
        {
            xtype: 'securityGridColumnRUD',
            permissions: ['view', 'edit', 'delete']
        }],
    tbar: {
        xtype: 'securityToolbar',
        verifyItems: [
            {
                text: '添加',
                tooltip: '添加字典',
                xtype: 'button',
                permission: 'add',
                iconCls: 'iconfont icon-add',
                handler: 'onAdd'
            }
        ]
    }

});