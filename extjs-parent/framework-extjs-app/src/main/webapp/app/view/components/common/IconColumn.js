Ext.define('kalix.view.components.common.IconColumn', {
  extend: 'Ext.grid.column.Template',
  xtype: 'iconcolumn',
  text:'头像',
  constructor:function(){
    this.tpl='<img src="{'+arguments[0].dataIndex+'}" height="48px" width="48px" onerror="this.src=\'resources/images/default_user.png\'"/>';
    arguments[0].renderer=null;
    this.callParent(arguments);
  }
});
