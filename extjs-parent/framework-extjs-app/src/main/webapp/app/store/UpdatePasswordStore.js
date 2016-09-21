/**
 * 用户修改个人信息、密码数据仓库
 *
 */
Ext.define('kalix.store.UpdatePasswordStore', {
    extend: 'kalix.store.BaseStore',
    model: 'kalix.model.UpdatePasswordModel',
    alias: 'store.updateUserStore',
    xtype: 'updateUserStore',
    storeId: "updateUserStore",
    proxyUrl: CONFIG.restRoot + '/camel/rest/users'
});

