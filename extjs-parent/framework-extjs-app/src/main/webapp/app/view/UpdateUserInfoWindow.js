/**
 * 用户添加表单
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
        'kalix.view.components.common.BaseImage',
        'kalix.controller.UpdateUserInfoController',
        'kalix.viewmodel.UpdateUserViewModel',
        'kalix.view.UploadIcon'
    ],
    controller: {
        type: 'updateUserInfoController'
    },
    viewModel: 'updateUserViewModel',
    width: 400,
    title: '修改个人信息',
    items: [
        {
            xtype: 'panel',
            layout: {
                type: 'vbox',
                align: 'center'
            },
            border: false,
            width: '100%',
            items: [
                {
                    xtype: 'panel',
                    layout: {
                        type: 'hbox',
                    },
                    width: '100%',
                    border: false,
                    margin: '10 0 0 0',
                    items: [
                        {
                            xtype: 'label',
                            html: '头像:',
                            margin: '30 0 0 20',
                        },
                        {
                            xtype: 'baseImage',
                            title: 'resources/images/default_user.png',
                            width: 72,
                            height: 72,
                            margin: '0 0 0 30'
                        },
                        {
                            xtype: 'uploadIcon',
                            id: 'meUploadIcon',
                            margin: '30 20 0 20'
                        }
                    ]
                },
                {
                    xtype: 'baseForm',
                    width: '100%',
                    border: false,
                    items: [
                        {
                            fieldLabel: '工号',
                            bind: {
                                value: '{rec.code}'
                            }
                        },
                        {
                            fieldLabel: '登录名',
                            readOnly: true,
                            bind: {
                                value: '{rec.loginName}'
                            }
                        },
                        {
                            fieldLabel: '姓名',
                            beforeLabelTextTpl: '<span class="field-required" data-qtip="必填选项">*</span>',
                            bind: {
                                value: '{rec.name}'
                            }
                        },
                        {
                            fieldLabel: '邮箱',
                            beforeLabelTextTpl: '<span class="field-required" data-qtip="必填选项">*</span>',
                            bind: {
                                value: '{rec.email}'
                            }
                        },
                        {
                            fieldLabel: '头像',
                            bind: {
                                value: '{rec.icon}'
                            },
                            hidden: true
                        },
                        {
                            fieldLabel: '电话号',
                            bind: {
                                value: '{rec.phone}'
                            }
                        },
                        {
                            fieldLabel: '手机号',
                            beforeLabelTextTpl: '<span class="field-required" data-qtip="必填选项">*</span>',
                            bind: {
                                value: '{rec.mobile}'
                            }
                        }
                    ]
                }
            ],
        }
    ]
});