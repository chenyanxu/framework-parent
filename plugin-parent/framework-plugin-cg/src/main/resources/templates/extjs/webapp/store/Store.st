/**
 * <moduleDescription>数据仓库
 *
 * @author
 * @version 1.0.0
 */
Ext.define('<extjsPrefix>.store.<beanName>Store', {
    extend: 'kalix.store.BaseStore',
    model: '<extjsPrefix>.model.<beanName>Model',
    alias: 'store.<pomName>Store',
    xtype: '<pomName>Store',
    storeId: "<pomName>Store",
    proxyUrl: '/kalix/camel/rest/<pomName>s'
});