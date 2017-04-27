/**
 * 用户修改个人信息、密码数据仓库
 *
 */
Ext.define('kalix.store.UpdateUserStore', {
  extend: 'kalix.store.BaseStore',
  model: 'kalix.model.UpdateUserModel',
  alias: 'store.updateUserStore',
  xtype: 'updateUserStore',
  storeId: 'updateUserStore',
  proxyUrl: CONFIG.restRoot + '/camel/rest/users'
});

