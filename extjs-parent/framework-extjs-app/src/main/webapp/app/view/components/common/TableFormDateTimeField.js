/**
 * 自定义formPanel，项目中的form必须扩展该类
 *         date:2015-10-16
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.TableFormDateTimeField', {
    extend: 'Ext.ux.DateTimeField',
    xtype: 'tableFormDateTimeField',
    alias: 'widget.tableFormDateTimeField',
    hideLabel: true,
    fieldStyle: 'font-size:15px;text-align:center;background:transparent;',
    editable: false,
    format: 'Y年m月d日 H时i分',
    listeners: {
        render: function (target) {
            if (target.bodyEl) {
                target.bodyEl.dom.firstChild.style.border = '0px';
                target.bodyEl.dom.firstChild.firstChild.style.borderColor = 'transparent'
            }
        }
    }
});
