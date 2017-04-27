/**
 * 主控制器
 *
 * date:2015-10-26
 */

Ext.define('kalix.controller.UpdateUserController', {
  extend: 'kalix.controller.BaseWindowController',
  alias: 'controller.updateUserController',
  requires: ['kalix.Notify'],
  // 旧密码 校验
  oldPasswordBlur: function (target) {
    if (target.value != null && target.value != '') {
      var store = Ext.create('kalix.store.UpdateUserStore');
      store.proxy.url = store.proxy.url + '/' + Ext.util.Cookies.get('currentUserId') + '/password/' + target.value;
      store.load({
        scope: this,
        callback: function (records, operation, success) {
          if (success) {
            if (operation._response.responseText == 'true') {
              var view = target.ownerCt;
              var vm = view.lookupViewModel();
              vm.set('oldPassword', true);
            }
            else {
              var view = target.ownerCt;
              var vm = view.lookupViewModel();
              vm.set('oldPassword', false);
              kalix.Notify.error('原密码错误！！！', CONFIG.ALTER_TITLE_ERROR);
            }
          }
          else {
            var view = target.ownerCt;
            var vm = view.lookupViewModel();
            vm.set('oldPassword', false);
            kalix.Notify.error('原密码错误！！！', CONFIG.ALTER_TITLE_ERROR);
          }
        }
      });

    }
  },
  // 新密码 校验
  newPasswordChange: function (target, newValue) {
    var view = target.ownerCt;
    var vm = view.lookupViewModel();
    var saveBtn = view.ownerCt.dockedItems.getAt(1).items.getAt(0);

    if (newValue.trim() == '' ||
      vm.get('rec').get('confirmPassword') == undefined ||
      vm.get('rec').get('confirmPassword').trim() == '') {
      saveBtn.disable();
      vm.set('passwordRight', false);
    }
    else {
      if (newValue.trim() == vm.get('rec').get('confirmPassword').trim()) {
        saveBtn.enable();
        vm.set('passwordRight', true);
      }
      else {
        saveBtn.disable();
        vm.set('passwordRight', false);
      }
    }
  },
  // 确认密码 校验
  confirmPasswordChange: function (target, newValue) {
    var view = target.ownerCt;
    var vm = view.lookupViewModel();
    var saveBtn = view.ownerCt.dockedItems.getAt(1).items.getAt(0);

    if (newValue.trim() == '' ||
      vm.get('rec').get('password') == undefined ||
      vm.get('rec').get('password').trim() == '') {
      saveBtn.disable();
      vm.set('passwordRight', false);
    }
    else {
      if (newValue.trim() == vm.get('rec').get('password').trim()) {
        saveBtn.enable();
        vm.set('passwordRight', true);
      }
      else {
        saveBtn.disable();
        vm.set('passwordRight', false);
      }
    }
  },
  onSave: function () {
    var vm = this.getView().lookupViewModel();
    if (vm.get('oldPassword') != null && vm.get('oldPassword')) {
      if (vm.get('rec').get('password') != null && vm.get('rec').get('password') != '') {
        if (vm.get('passwordRight') != null && vm.get('passwordRight')) {
          this.callParent(arguments);
        }
        else {
          kalix.Notify.error('密码不一致！！！', CONFIG.ALTER_TITLE_ERROR);
        }
      }
      else {
        kalix.Notify.error('新密码不能为空！！！', CONFIG.ALTER_TITLE_ERROR);
      }
    }
    else {
      kalix.Notify.error('原密码错误！！！', CONFIG.ALTER_TITLE_ERROR);
    }
  },
  onClose: function () {

  }
});
