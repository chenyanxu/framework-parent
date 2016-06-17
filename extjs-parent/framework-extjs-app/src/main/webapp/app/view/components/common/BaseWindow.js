/**
 * @aurchor chenyanxu
 */

Ext.define('kalix.view.components.common.BaseWindow', {
    extend: 'Ext.window.Window',
    requires: [
        'kalix.view.components.common.FormPanel',
        'kalix.controller.BaseWindowController',
        'kalix.validator.Presence',
        'kalix.validator.Length',
        'kalix.validator.Email',
        'kalix.validator.Mobile'
    ],
    alias: 'widget.baseWindow',
    xtype: "baseWindow",
    controller: 'baseWindowController',
    width: 800,
    border: false,
    modal: true,
    resizable: false,
    buttonAlign: 'center',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    bind: {
        title: '{title}',
        iconCls: '{iconCls}'
    },
    defaults: {
        layout: 'form',
        xtype: 'baseForm',
        flex: 1,
        defaults: {
            labelAlign: 'right'
        }
    },
    buttons: [{
        text: '保存',
        iconCls:'iconfont icon-save iconfont-btn-small',
        handler: 'onSave',
        bind: {
            hidden: '{view_operation}'
        }
    }, {
        text: '重置',
        iconCls:'iconfont icon-reset iconfont-btn-small',
        handler: 'onReset',
        bind: {
            hidden: '{view_operation}'
        }
    },
        {
            text: '关闭',
            glyph: 'xf00d@FontAwesome',
            handler: function () {
                this.up('window').close();
            },
            bind: {
                hidden: '{!view_operation}'
            }
        }
    ]
    ,
    listeners: {
        close: 'onClose',
        beforerender: 'onBeforerender'
    }
});