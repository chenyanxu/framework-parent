/**
 * @author chenyanxu
 */
Ext.define('kalix.controller.BaseSearchFormController', {
  extend: 'Ext.app.ViewController',
  alias: 'controller.baseSearchFormController',
  onSearch: function (target, event) {
    var view = this.getView();

    if (view.gridStore) {
      view.gridStore.currentPage = 1;
      view.gridStore.load();
    }
  },
  onReset: function () {
    this.getView().getForm().reset();
  }
});