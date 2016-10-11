/**
 * @author chenyanxu
 */
Ext.define('kalix.container.BaseTreeContainer', {
  //extend: 'Ext.container.Container',
  extend:'Ext.panel.Panel',
  requires:['kalix.plugin.ZOrderPlugin'],
  xtype:'baseTreeContainer',
  collapsible: true,
  collapseDirection: Ext.Component.DIRECTION_LEFT,
  border:false,
  //==custom property
  tree: null,
  childItemMargin:-1,
  //custom property==
  constructor: function () {
    if(arguments[0].childItemMargin!=undefined){
      this.items[0].margin='0 0 5 0';
    }

    this.callParent(arguments);
    if (this.tree) {
      if(arguments[0].childItemMargin!=undefined){
        this.tree.margin=this.childItemMargin;
      }

      this.add(this.tree);
    }
  },
  items: [
    {
      xtype: 'form',
      bodyPadding: 10,
      margin: 5,
      defaults: {border: 0},
      layout: 'column',
      plugins:['zorderPlugin'],
      items: [
        {
          xtype: 'combo',
          labelWidth: 40,
          fieldLabel: '名称',
          queryModel: 'local',
          valueField: 'id',
          displayField: 'name',
          minChars: 1,
          width: 350,
          listeners: {
            change: function (target, newValue, oldValue, eOpts) {
              this.store.clearFilter();
              if(newValue && isNaN(newValue)){
                var isEqual=false;
                this.store.filter(function (record) {
                  if(record.get('name').indexOf(newValue)>-1)
                    {
                      return true;
                    }

                    return false;
                  }
                );
              }
            },
            select:function (target, record, eOpts) {
              var tree = this.findParentByType('baseTreeContainer').items.getAt(1);
              var root = tree.store;
              var nodeExpand = root.getNodeById(record.get('id'));

              if (nodeExpand) {
                if(!nodeExpand.get('expanded')){
                  if (nodeExpand.data.leaf) {
                    nodeExpand.parentNode.expand();
                  }
                  else {
                    nodeExpand.expand();
                  }
                }

                tree.setSelection(nodeExpand)
              }
            }
          }
        }
      ]
    }
  ]
});
