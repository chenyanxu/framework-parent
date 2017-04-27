/**
 * 主控制器
 *
 * date:2015-10-26
 */

Ext.define('kalix.controller.UpdateUserInfoController', {
  extend: 'kalix.controller.BaseWindowController',
  alias: 'controller.updateUserInfoController',
  requires: ['kalix.Notify'],
  onChange: function (target, event, domValue) {
    var form = target.findParentByType('form');
    var me = target.findParentByType('window');
    var store = me.viewModel.get('store');
    var mainId = me.viewModel.get('rec').id
    scope = {mainId: mainId, store: store};

    form.submit({
      url: CONFIG.restRoot + '/camel/rest/upload',
      waitMsg: '正在上传...',
      scope: scope,
      success: function (fp, o) {
        if (o.result.success) {
          me.viewModel.get('rec').set('icon', o.result.attachmentPath);
          Ext.getCmp('updateUserInfoWindowImage').setSrc(o.result.attachmentPath);
          kalix.Notify.success('头像上传成功！！！', CONFIG.ALTER_TITLE_SUCCESS);
        }
      },
      failure: function (fp, o) {
        Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, o.result.msg);
      }
    });
  },
  /**
   * 保存/更新操作.
   *
   */
  onSave: function () {
    var viewModel = this.getViewModel();
    var model = viewModel.get('rec');
    var view = this.getView();
    var store = viewModel.get('store');

    store.proxy.extraParams = {};

    if (model.isValid()) {
      if (!model.dirty) {
        Ext.Msg.alert(CONFIG.ALTER_TITLE_INFO, '未修改数据');
        return;
      }

      model.modified = model.data;

      if (0 == model.id) {
        store.add(model);
      }

      store.sync(
        {
          success: function () {
            view.close();
            model.dirty = false;
          },
          callback: function (batch) {
            store.currentPage = 1;
            store.load();

            var res = Ext.JSON.decode(batch.operations[0].getResponse().responseText);

            if (batch.operations[0].success) {
              Ext.util.Cookies.set('currentUserIcon', store.getData().items[0].data.icon);
              var model = Ext.app.Application.instance._mainView.controller.getReferences().profilebar.lookupViewModel().get('user');
              model.set('icon', store.getData().items[0].data.icon);
              kalix.Notify.success(res.msg, CONFIG.ALTER_TITLE_SUCCESS);
            }
            else {
              Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res);
            }
          }
        }
      );
    } else {
      var validation = _.pick(model.getValidation().data, function (value, key, object) {
        return value !== true;
      });

      var formItems = arguments[0].findParentByType('window').items;

      for (var formIndex = 0; formIndex < formItems.length; ++formIndex) {
        var fieldItems = formItems.getAt(formIndex).items;

        for (var fieldIndex = 0; fieldIndex < fieldItems.length; ++fieldIndex) {
          var fieldItem = fieldItems.getAt(fieldIndex);
          if (fieldItem.bind != null) {
            var msg = validation[fieldItem.bind.value.stub.path.split('.')[1]];

            if (msg != undefined) {
              fieldItem.setActiveError(msg);
            }
          }
        }
      }
    }
  }
});
