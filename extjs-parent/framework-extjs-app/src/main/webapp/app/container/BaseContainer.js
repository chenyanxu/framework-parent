/**
 * @author chenyanxu
 */
Ext.define('kalix.container.BaseContainer', {
  extend: 'Ext.container.Container',
  storeId: '',
  constructor:function(){
    this.callParent(arguments);
    var navTree = kalix.getApplication()._mainView.controller.getReferences().navigationTreeList;

    if (navTree.getSelection()) {
      this.lookupViewModel().set('grid_iconCls',navTree.getSelection().data.iconCls);
    }
  },
  listeners: {
    render: function (target, eOpts) {
      var store = Ext.app.Application.instance.getApplication().getStore(this.storeId);

      store.on('beforeload', function (store, opts, target) {
        if (target.items.length > 0 && target.items.getAt(0).xtype.search('Form') != -1) {
          var jsonObj = target.items.getAt(0).getForm().getFieldValues();
          var jsonObjNew = {};

          for (var jsonKey in jsonObj) {
            if (jsonObj[jsonKey] != '-') {
              jsonObjNew[jsonKey] = jsonObj[jsonKey];
            }
          }

          var jsonStr = Ext.JSON.encode(jsonObjNew);

          store.proxy.extraParams = {'jsonStr': jsonStr};
        }
      }, this, target);
    }
  }
});
