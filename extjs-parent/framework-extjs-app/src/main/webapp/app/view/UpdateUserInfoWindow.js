/**
 * 用户新增表单
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */

Ext.define('kalix.view.UpdateUserInfoWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    alias: 'widget.updateUserInfoWindow',
    xtype: 'updateUserInfoWindow',
    requires: [
        'kalix.viewmodel.UpdateUserViewModel'
    ],
    viewModel: 'updateUserViewModel',
    width: 400,
    title: '修改个人信息',
    items: [
        {
            xtype: 'baseForm',
            items: [
                {
                    fieldLabel: '登录名',
                    readOnly: true,
                    bind: {
                        value: '{rec.loginName}'
                    }
                },
                {
                    fieldLabel: '姓名',
                    bind: {
                        value: '{rec.name}'
                    }
                },
                {
                    fieldLabel: '邮箱',
                    bind: {
                        value: '{rec.email}'
                    }
                },
                {
                    fieldLabel: '电话号',
                    bind: {
                        value: '{rec.phone}'
                    }
                },
                {
                    fieldLabel: '手机号',
                    bind: {
                        value: '{rec.mobile}'
                    }
                }
            ]
        }
    ]
});