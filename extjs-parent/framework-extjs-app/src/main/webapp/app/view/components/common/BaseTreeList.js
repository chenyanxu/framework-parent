/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseTreeList', {
  extend: 'kalix.view.components.common.BaseTree',
  alias: 'widget.baseTreeList',
  xtype: 'baseTreeList',
  tbar: [
    {
      tooltip: '刷新',
      iconCls: 'iconfont icon-refresh',
      handler: 'onRefresh',
      text: '刷新'
    }
  ]
});