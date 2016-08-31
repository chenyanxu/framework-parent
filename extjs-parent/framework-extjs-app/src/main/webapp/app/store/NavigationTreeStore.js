/**
 * 左侧导航 store
 *
 * date:2015-10-26

主工具条只初始化一次
 */

Ext.define('kalix.store.NavigationTreeStore', {
  extend : 'kalix.store.BaseTreeStore',

  storeId : 'navigationTreeStore',

  state : {
    hashToken : null
  },

  baseUrl: CONFIG.restRoot + '/camel/rest/system/applications/',
  treeSelInfo:{
    tree:null,
    selected:false,
    level1:'',
    level2:''
  },
  proxy : {
    type : 'ajax',
    url : '',
    reader : {
      type : 'json',
      rootProperty : ''
    }
  },

  load : function (options) {
    if (this.state.hashToken != options.hashToken) {
      this.proxy.url = this.baseUrl + options.hashToken;
      this.state.hashToken = options.hashToken;
      this.callParent(arguments);
    }
  },

  root : {
    expanded : true,
    children : []
  },
  fields : [{
      name : 'text'
    }
  ],
  listeners:{
    load:function( target, records, successful, operation, node, eOpts ){
      //fire the event when the navigation tree store load
      Ext.app.Application.instance.getApplication()._mainView.fireEvent('navTreeLoad',this);
    }
  }
});
