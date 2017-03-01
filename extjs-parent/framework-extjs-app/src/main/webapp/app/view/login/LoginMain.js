/**
 * 登陆表单
 *
 * date:2015-10-23
 *
 没有添加 enter 键提交支持的原因
 可以监听文本框输入的 keyup 事件来添加 enter 键提交表单操作
 但是，文本框没有 blur 方法，无法移除焦点
 即使在出现错误提示对话框时，enter 键也会激活提交行为

 */

Ext.define('kalix.view.login.LoginMain', {
  extend: 'Ext.form.Panel',
  requires: [
    'kalix.controller.LoginController'
  ],
  xtype: 'loginMain',
  controller: 'loginController',
  icon: 'resources/images/lock.png',
  title: '系统登录',
  id: 'loginForm',
  method: "POST",
  defaultType: 'textfield',
  frame: true,
  url: 'login.jsp',

  bodyBorder: false,
  bodyPadding: 20,
  border: false,
  buttonAlign: 'center',
  width: 300,
  layout: {
    type: 'vbox',
    align: 'stretch'
  },

  items: [{
    inputType: 'textfield',
    fieldLabel: '账号',
    name: 'username',
    cls: 'auth-textbox',
    allowBlank: false,
    height: 55,
    blankText: '账号不能为空!',
    hideLabel: true,
    emptyText: '账号',
    triggers: {
      glyphed: {
        cls: 'trigger-glyph-noop auth-email-trigger'
      }
    },
    bind: {
      value: '{username}'
    }
  }, {
    inputType: 'password',
    fieldLabel: '密码',
    name: 'password',
    cls: 'auth-textbox',
    height: 55,
    allowBlank: false,
    blankText: '密码不能为空!',
    hideLabel: true,
    emptyText: '密码',
    triggers: {
      glyphed: {
        cls: 'trigger-glyph-noop auth-password-trigger'
      }
    },
    listeners: {
      keyup: {
        element: 'el',
        fn: 'onKeyup'
      }
    },
    bind: {
      value: '{password}'
    }
  }, {
      inputType:'textField',
      name:'loginType',
      value: 'admin1'
    }
  ],
  buttons: [{
    text: '登录',
    handler: 'onLogin',
    iconAlign: 'right',
    glyph: 'xf090@FontAwesome'
  }, {
    text: '重置',
    handler: 'onReset',
    iconAlign: 'right',
    glyph: 'xf0e2@FontAwesome',
  }
  ],
  initComponent: function () {
    var me = this, listen;
    me.addCls('auth-dialog');
    me.callParent();
  }
});
