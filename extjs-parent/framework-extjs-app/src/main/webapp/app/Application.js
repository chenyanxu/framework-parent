/**
 * 主应用程序
 *
 * date:2015-10-26
 */

Ext.define('kalix.Application', {
    extend: 'Ext.app.Application',
    stores: [
        'kalix.store.NavigationTreeStore',
        'kalix.store.MainToolbarStore'
    ],
    models: [],
    defaultToken: ''
});
