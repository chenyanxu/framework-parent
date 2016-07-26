/**
 * 字典新增和修改表单
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.dict.view.DictWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    requires: [
        'kalix.controller.BaseWindowController'
    ],
    alias: 'widget.dictWindow',
    controller: {
        type: 'baseWindowController'
    },
    xtype: 'dictWindow',
    width: 400,
    items: [{
        xtype: 'baseForm',
        items: [
            {
                fieldLabel: '类型',
                xtype:'combo',
                store: Ext.create('Ext.data.Store',{proxy: {
                    type: 'rest',
                    autoLoad:true,
                    pageSize:0,
                    url: CONFIG.restRoot + '/camel/rest/dicts/types/list'
                }}),
                displayField: 'name',
                valueField: 'name',
                minChars: 2,
                typeAhead:true,
                allowBlank: false,
                bind: {
                    value: '{rec.type}'
                }
            },
            {
                fieldLabel: '标签名',
                allowBlank: false,
                bind: {
                    value: '{rec.label}'
                }
            },
            {
                fieldLabel: '备注',
                xtype: 'textarea',
                bind: {
                    activeError: '{validation.content}',
                    value: '{rec.description}'
                }
            }
        ]
    }]
});