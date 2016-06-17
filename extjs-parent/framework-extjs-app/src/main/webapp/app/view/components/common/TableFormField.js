/**
 * 自定义formPanel，项目中的form必须扩展该类
 *         date:2015-10-16
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.TableFormField', {
    extend: 'Ext.form.Field',
    xtype: 'tableFormField',
    hideLabel: true,
    fieldStyle: 'font-size:15px;background:transparent;'
});
