Ext.define('kalix.controller.BaseItemSelectorWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseItemSelectorWindowController',
    onSearch: function (target, event) {
      var searchText = this.getView().items.getAt(0).value;
      var dataStore = this.getView().findParentByType('window').Config.store;
      var itemSelector = this.getView().findParentByType('window').items.getAt(1);
      var oldValue = itemSelector.value;

      dataStore.clearFilter();

      if (searchText.trim() != '') {
        var likeFilter = new Ext.util.Filter({
          filterFn: function (item) {
            return item.get('name').search(searchText) > -1 || this.indexOf(item.get('id')) > -1;
          },
          scope: oldValue
        });

        dataStore.filter(likeFilter);
      }

      this.getView().findParentByType('window').items.getAt(1).reset()
      itemSelector.setValue(oldValue);
    },
    onReset: function () {
      this.getView().getForm().reset();
    },
    onSave: function () {
      var win = this.getView();
      var itemSelector = win.items.getAt(1);
      var selectIds = itemSelector.getValue();
      var recordId = win.Config.recoredId;
      var saveUrl = win.Config.baseUrl+'/'+recordId+'/users';

      if(selectIds.toString()==win.Config.selectItems.toString()){
        Ext.Msg.alert(CONFIG.ALTER_TITLE_INFO, "未修改");
      }
      else{
        Ext.Ajax.request({
          url: saveUrl,
          defaultPostHeader : 'application/json;charset=utf-8',
          params:Ext.encode([recordId.toString(),selectIds.join(',')]),
          method: 'POST',
          callback: function (options, success, response) {
            var resp = Ext.JSON.decode(response.responseText);
            if (resp != null && resp.success) {
              kalix.Notify.success(resp.msg, CONFIG.ALTER_TITLE_SUCCESS);
            } else {
              Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, resp.msg);
            }

            win.close();
          }
        });
      }

    },
    onResetItemSelector: function () {}
  }
);