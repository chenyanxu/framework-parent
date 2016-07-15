/**
 * @author chenyanxu
 */
Ext.define('kalix.controller.BaseSearchFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseSearchFormController',
    onSearch: function (target, event) {
        var view=this.getView();

        if(!view.storeId){
            view.storeId=this.getView().xtype.split('Search')[0]+'Store';
        }

        var store = Ext.app.Application.instance.getApplication().getStore(this.getView().storeId);

        store.currentPage = 1;
        store.load();
    },
    onReset: function () {
        this.getView().getForm().reset();
    }
});