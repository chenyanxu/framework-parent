Ext.define('kalix.view.Messagebar', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'messagebar',

    requires: [
        'kalix.viewmodel.MessagebarViewModel'
    ],

    viewModel: 'messagebarViewModel',

    id: 'messagebarId',

    layout: {
        type: 'hbox'
    },

    border: false,

    items: [{
        xtype: 'button',
        bind: {
            text: '{message.count}',
            iconCls: '{message.iconCls}'
        },
        //config the routeId right
        //because we route the url from the top toolbar,we are not sure the current app we select contain
        //the menu,so we need config the appName explicit
        routeId:'message/receiver',
        appName:'common',
        margin: '0 5 0 10',
        handler:'onNavigationSpecial'
    }
    ]
});
