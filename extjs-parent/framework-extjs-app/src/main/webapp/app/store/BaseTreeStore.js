/**
 * @author chenyanxu
 *
 */

Ext.define('kalix.store.BaseTreeStore', {
    extend: 'Ext.data.TreeStore',
    listeners: {
        //listener function for shiro session timeout
        load: function (target, records, successful, eOpts) {
            if ('login' == Ext.JSON.decode(eOpts.getResponse().responseText).message) {
                location.reload();
            }
        }
    }
});
