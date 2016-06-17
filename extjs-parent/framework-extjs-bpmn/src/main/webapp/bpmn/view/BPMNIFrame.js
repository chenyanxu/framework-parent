/**
 * 自定义formPanel，项目中的form必须扩展该类
 */
Ext.define('kalix.bpmn.view.BPMNIFrame', {
    extend: 'Ext.ux.IFrame',
    alias: 'widget.bpmniframe',
    src:'app/bpmn/index.html',
    style:'background:white url("app/bpmn/grid.gif")'
});
