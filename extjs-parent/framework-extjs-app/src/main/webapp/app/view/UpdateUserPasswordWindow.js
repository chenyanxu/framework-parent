/**
 * 用户添加表单
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */

Ext.define('kalix.view.UpdateUserPasswordWindow', {
  extend: 'kalix.view.components.common.BaseWindow',
  alias: 'widget.updateUserPasswordWindow',
  xtype: 'updateUserPasswordWindow',
  requires: [
    'kalix.controller.UpdateUserController',
    'kalix.viewmodel.UpdateUserViewModel'
  ],
  controller: {
    type: 'updateUserController'
  },
  viewModel: 'updateUserViewModel',
  width: 400,
  title: '修改密码',
  items: [
    {
      xtype: 'baseForm',
      items: [
        {
          inputType: 'password',
          fieldLabel: '原密码',
          beforeLabelTextTpl: '<span class="field-required" data-qtip="必填选项">*</span>',
          listeners: {
            blur: 'oldPasswordBlur'
          }
        },
        {
          inputType: 'password',
          fieldLabel: '新密码',
          beforeLabelTextTpl: '<span class="field-required" data-qtip="必填选项">*</span>',
          name: 'password',
          allowBlank: false,
          bind: {
            value: '{rec.password}'
          },
          listeners: {
            change: 'newPasswordChange'
          }
        },
        {
          inputType: 'password',
          fieldLabel: '确认密码',
          allowBlank: false,
          bind: {
            value: '{rec.confirmPassword}'
          },
          listeners: {
            change: 'confirmPasswordChange'
          }
        }
      ]
    }
  ]
});