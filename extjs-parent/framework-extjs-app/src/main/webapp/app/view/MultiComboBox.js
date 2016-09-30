/**
 * Created by Administrator on 2016/9/28.
 */
Ext.define('kalix.view.MultiComboBox', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.multiComboBox',
    xtype: 'multiComboBox',
    requires: [
        'kalix.controller.MultiComboBoxController'
    ],
    controller: {
        type: 'multiComboBoxController'
    },

    //==custom property
    valueFieldName: 'id',
    displayText: '',
    storeUrl: '',
    menuItemValue: 'id',
    menuItemText: 'name',
    actions: true,
    //custom property

    callback: function() {},
    listeners: {
        'render': 'onRender',
        'afterrender': 'onAfterRender',
    },
    items: [
        {
            xtype: 'textfield',
            name: 'myTextField',
            hidden: true
        },
        {
            xtype: 'button',
            text: 'zzz',
            showText: true,
            menu: {
                xtype: 'menu',
                width: 120,
                items: [
                    {
                        xtype: 'menucheckitem',
                        text: '全部',
                        checked: true,
                        'checkHandler': 'onItemSelectAllClick'
                    }, '-'
                ]
            }
        }
    ]
});