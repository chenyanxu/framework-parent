/**
 * ����������ģ��
 *
 * date:2015-10-26
 */

Ext.define('kalix.viewmodel.MainToolbarViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.maintoolbarViewModel',

    constructor: function () {
        this.callParent(arguments);

        var mainToolbarStore = Ext.getStore('mainToolbarStore');
        mainToolbarStore.on("load", function () {
            this.updateItems(mainToolbarStore.getData());
        }, this);
    },

    updateItems: function (modelItems) {
        var items = [];
        modelItems.each(function (item, index) {
            items.push(item.getData());
        });

        this.set('items', items);
    }
});
