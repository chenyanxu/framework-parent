/**
 * 主工具条 store
 *
 * date:2015-10-26

 主工具条只初始化一次
 */

Ext.define('kalix.store.MainToolbarStore', {
  extend: 'Ext.data.Store',
  storeId: 'mainToolbarStore',
  state: {
    hashInit: false
  },
  proxy: {
    type: 'ajax',
    url: CONFIG.restRoot + '/camel/rest/system/applications',
    reader: {
      type: 'json',
      rootProperty: ''
    }
  },
  load: function () {
    if (!this.state.hasInit) {
      this.callParent(arguments);
      this.state.hasInit = true;
    }
  }
});
