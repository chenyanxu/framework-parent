Ext.define('kalix.view.components.common.AutoHPanel', {
  extend: 'Ext.panel.Panel',
  xtype: 'autoHPanel',
  layout: {
    type: 'hbox',
  },
  constructor: function () {
    var items = this.items;

    for (var index = 0; index < items.length; ++index) {
      switch (index) {
        case 0:
          items[index].margin = '5 2 5 5';
          break;
        case items.length-1:
          items[index].margin = '5 5 5 2';
          break;
        default:
          items[index].margin = '5 2 5 2';
      }
    }

    this.callParent(arguments);
  },
  beforeLayout: function () {
    // var me = this,
    //   height = Ext.Element.getViewportHeight() - 75,
    //   items = me.items;
    //
    // for (var itemIndex = 0; itemIndex < items.length; ++itemIndex) {
    //   items.getAt(itemIndex).setHeight(height - 2);
    // }
    //
    // me.callParent(arguments);
  }
});
