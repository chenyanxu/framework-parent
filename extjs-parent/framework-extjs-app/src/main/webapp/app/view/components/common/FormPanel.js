/**
 * 自定义formPanel，项目中的form必须扩展该类
 *         date:2015-10-16
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.FormPanel', {
    extend: 'Ext.form.Panel',
    xtype: "baseForm",

    layout: 'form',
    defaultType: 'textfield',
    bodyPadding: 10,
    buttonAlign: "center",

    ////给必录项前面添加红星
    //initComponent: function () {
    //    this.on('beforeadd', function (me, field) {
    //        if (!field.allowBlank)
    //            field.beforeLabelTextTpl = '<span class="field-required" data-qtip="必填选项">*</span>';
    //            field.allowBlank=true;
    //    });
    //    this.callParent(arguments);
    //}
});
