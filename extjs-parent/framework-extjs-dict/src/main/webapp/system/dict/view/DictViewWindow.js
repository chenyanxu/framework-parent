/**
 * 字典查看表单
 *
 * @author
 * @version 1.0.0
 */

Ext.define('kalix.dict.view.DictViewWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    alias: 'widget.dictViewWindow',
    xtype: "dictViewWindow",
    width: 400,
    items: [{
        defaults: {readOnly: true},
        xtype: 'baseForm',
        items: [
            {
                fieldLabel: '类型',
                bind: {
                    value: '{rec.type}'
                }
            },
            {
                fieldLabel: '标签名',
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
            }]
    }]
});