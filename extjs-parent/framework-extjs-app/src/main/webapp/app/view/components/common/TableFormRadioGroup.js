/**
 * 自定义formPanel，项目中的form必须扩展该类
 *         date:2015-10-16
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.TableFormRadioGroup', {
    extend: 'Ext.form.RadioGroup',
    xtype: 'tableFormRadioGroup',
    hideLabel: true,
    vertical: false,
    defaults: {
        name: 'rn'
    },
    listeners: {
        change: function (target, newValue, oldValue, eOpts) {
            this.lookupViewModel().getData().rec.set(this.fieldName, newValue.rn);
        },
        afterrender: function () {
            this.setValue({rn: this.lookupViewModel().getData().rec.get(this.fieldName)});
        }
    }
});
