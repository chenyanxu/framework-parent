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

Ext.define('kalix.view.login.LoginGYLC', {
  extend: 'Ext.container.Container',
  requires: [
    'kalix.view.login.LoginMain'
  ],
  controller: 'loginController',
  width: 700,
  defaults: {
    margin: '0 0 30 150',
    width: 380,
  },
  align: 'center',
  items: [
    {
      xtype: 'image',
      width: 680,
      margin: '0 0 30 0',
      src: 'resources/images/login_' + Ext.util.Cookies.get('loginImageTag') + '_top.png'
    },
    {
      xtype: 'textfield',
      height: 50,
      fieldStyle: 'font-size:15px;height:50px;',
      emptyText: '账号',
      margin: '0 0 10 150',
      bind: {
        value: '{username}'
      },
      listeners: {
        afterrender: function () {
          this.focus();
        }
      }
    },
    {
      xtype: 'textfield',
      inputType: 'password',
      fieldStyle: 'font-size:15px;height:50px;',
      emptyText: '密码',
      height: 50,
      bind: {
        value: '{password}'
      },
      listeners: {
        keyup: {
          element: 'el',
          fn: 'onKeyup'
        }
      }
    },
    {
      xtype: 'button',
      style: {
        background: 'url(resources/images/login_' + Ext.util.Cookies.get('loginImageTag') + '_btn.png) right bottom no-repeat'
      },
      height: 50,
      border: false,
      handler: 'onLogin'
    },
    {
      xtype: 'image',
      src: 'resources/images/login_' + Ext.util.Cookies.get('loginImageTag') + '_bottom.png'
    },
    {
      xtype: 'loginMain',
      hidden: true
    }
  ]
});
