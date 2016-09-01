/**
 * the base form for the operation on tree panel
 *  @author chenyanxu
 *
 */
Ext.define('kalix.view.components.common.BaseTreeForm', {
    extend: 'Ext.FormPanel',
    requires: [
        'kalix.controller.BaseTreeFormController'
    ],
    alias: 'widget.baseTreeForm',
    controller: 'baseTreeFormController',
    xtype: 'baseTreeForm',
    labelAlign: 'center',
    labelWidth: 75,
    autoWidth: true,
    autoHeight: true,
    jsonSubmit: true,
    bodyStyle: 'padding:15px',
    buttonAlign: 'center',
    defaultType: 'textfield',
    buttons: [
        {
            text: '保存',
            type: 'submit',
            iconCls:'iconfont icon-save iconfont-btn-small',
            handler: 'onSave'
        },
        {
            text: '重置',
            iconCls:'iconfont icon-reset iconfont-btn-small',
            handler: 'onReset'
        }
    ],
    //给必录项前面添加红星
    initComponent: function () {
        this.on('beforeadd', function (me, field) {
            if (!field.allowBlank)
                field.beforeLabelTextTpl = '<span class="field-required" data-qtip="必填选项">*</span>';
                field.allowBlank=true;
        });
        this.callParent(arguments);
    }
});