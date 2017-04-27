/**
 * @author chenyanxu
 */
Ext.define('kalix.plugin.ToolTipPlugin', {
  extend: 'Ext.AbstractPlugin',
  requires: [
    'kalix.view.components.common.ImageToolTip'
  ],
  alias: 'plugin.tooltipplugin',
  //==custom property
  tooltip: 'CONFIG_THE_TOOLTIP_IN_PLUGIN', //the tooltip for the component
  serviceUrl: '',  //the url to get the tooltip
  //custom property==
  init: function (container) {
    this.callParent(arguments);
    this.cmp.on({
      afterrender: function (target) {
        if (target instanceof Ext.panel.Panel) {
          if (target.dockedItems && target.dockedItems.length > 0) {
            for (var i = 0; i < target.dockedItems.length; ++i) {
              var item = target.dockedItems.getAt(i);

              if (item.dock == 'top') {
                if (item.items.length > 0) {
                  item.items.getAt(0).flex = 0

                  item.items.insert(1, Ext.create('kalix.view.components.common.ImageToolTip', {
                    tooltip: this.tooltip, margin: '0 0 0 5'
                  }));

                  item.items.insert(2, Ext.create('Ext.toolbar.Spacer', {flex: 1}));
                }
                break;
              }
            }
          }
        }
      },
      scope: this
    });
  },
  destroy: function () {
  }
});
