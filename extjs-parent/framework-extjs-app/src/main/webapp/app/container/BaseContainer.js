/**
 * @author chenyanxu
 */
Ext.define('kalix.container.BaseContainer', {
  extend: 'Ext.container.Container',
  listeners: {
    render: function (target, eOpts) {
      if(this.items.length>1) {
        var store;

        if(this.items.getAt(1) instanceof Ext.grid.GridPanel){
          store=this.items.getAt(1).store;
        }

        if(this.items.getAt(0) instanceof Ext.form.Panel){
          this.items.getAt(0).gridStore=store;
        }

        if(store) {
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
    },
    beforeshow:function(){
      var navTree = kalix.getApplication()._mainView.controller.getReferences().navigationTreeList;

      if (navTree.getSelection()) {
        this.lookupViewModel().set('grid_iconCls',navTree.getSelection().data.iconCls);
      }
    }
  }
});
