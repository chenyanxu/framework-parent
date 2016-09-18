/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseTree', {
  extend: 'Ext.tree.Panel',
  requires: [
    'kalix.view.components.common.SecurityToolbar',
    'kalix.view.components.common.SecurityGridColumnCommon',
    'kalix.view.components.common.SecurityGridColumnRUD',
    'kalix.controller.BaseTreeController',
    'kalix.plugin.AutoHeightPlugin',
    'kalix.plugin.ZOrderPlugin'
  ],
  alias: 'widget.baseTree',
  xtype: 'baseTree',
  controller: 'baseTreeController',
  autoLoad: false,
  singleExpand: true,
  rootVisible: false,
  margin: 5,
  plugins:['autoheightplugin','zorderPlugin'],
  viewConfig: {
    enableTextSelection: true
  },
  //==custom property
  config: {
    expandId: -1 //config the id in child class to expand a tree node
  },
  //custom property
  listeners: {
    load: function (root) {
      if (this.config.expandId != -1) {
        var nodeExpand = root.getNodeById(this.config.expandId);

        if (nodeExpand) {
          if (nodeExpand.data.leaf) {
            nodeExpand.parentNode.expand();
          }
          else {
            nodeExpand.expand();
          }
        }
      }
      else {
        //auto expand the first child of the root when expandId not assigned
        var childNodes = this.getRootNode().childNodes;

        if (childNodes.length == 1) {
          childNodes[0].expand();
        }
      }

      var parentContainer = this.findParentByType('baseTreeContainer');

      if (parentContainer) {
        var items = parentContainer.items;

        if (items.length == 2) {
          var storeData = [];

          this.store.filter(function (record) {
            storeData.push({id: record.get('id'), name: record.get('name')});
            return true;
          });
          this.store.clearFilter();

          var tempStore = Ext.create('Ext.data.Store',
            {
              fields: ['id', 'name'],
              data: storeData
            }
          );
          items.getAt(0).items.getAt(0).setStore(tempStore);
        }
      }
    },
    beforeitemmouseup: function (target, record) {
      if (record.data.expanded) {
        if (this.config.expandId != record.id) {
          this.config.expandId = record.parentNode.id
        }
      }
      else {
        if (!record.data.leaf) {
          this.config.expandId = record.data.id
        }
      }
    }
  }
});