/**
 * @author chenyanxu
 */
Ext.define('kalix.plugin.AutoHeightPlugin', {
  extend: 'Ext.AbstractPlugin',
  alias: 'plugin.autoheightplugin',
  init: function (container) {
    this.callParent(arguments);
    this.cmp.on({
      afterrender: function (target) {
        var baseContainer = this.findParentByType('container');

        if (baseContainer) {
          var mainContainer = baseContainer.findParentByType('container');
          var clientHeight = height = Ext.Element.getViewportHeight() - 65;

          if (baseContainer.layout.type=='autocontainer') {
            if(baseContainer.items.length == 2){
              this.setHeight(clientHeight - 115);
            }
            else if(baseContainer.items.length == 1){
              this.setHeight(clientHeight - 14);
            }
          }
          else if (baseContainer.layout.type=='hbox') {
            this.setHeight(clientHeight - 14);
          }
          else {
          }
        }
      }
    });
  },
  destroy: function () {
  }
});
