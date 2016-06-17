/**
 * @author chenyanxu
 */
Ext.define('kalix.controller.BaseSearchFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseSearchFormController',
    onSearch: function (target, event) {
        var store = Ext.app.Application.instance.getApplication().getStore(this.getView().storeId);

        store.currentPage = 1;
        store.load();
    },
    onReset: function () {
        this.getView().getForm().reset();
    }
});