/**
 * @author chenyanxu
 */
Ext.define('kalix.container.BaseContainer', {
  extend: 'Ext.container.Container',
  constructor:function(){
    this.callParent(arguments);

    if(this.items.length>1){
      var comp1=this.items.getAt(0);
      var comp2= this.items.getAt(1);

      if(comp1.xtype.indexOf('dictSearchForm')>-1 && comp2.xtype.indexOf('GridPanel')>-1){
        comp1.storeId=comp2.getStore().type;
      }
    }

  },
  listeners: {
    render: function (target, eOpts) {
      if(!this.storeId){
        this.storeId=this.$className.split('.').reverse()[1]+'Store';
      }

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
    },
    beforeshow:function(){
      var navTree = kalix.getApplication()._mainView.controller.getReferences().navigationTreeList;

      if (navTree.getSelection()) {
        this.lookupViewModel().set('grid_iconCls',navTree.getSelection().data.iconCls);
      }
    }
  }
});
