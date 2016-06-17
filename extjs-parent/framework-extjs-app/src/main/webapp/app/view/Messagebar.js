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
        //href: '#workflow/receiver',
        //hrefTarget: '_self',
        routeId:'workflow/receiver',
        margin: '0 5 0 10',
        handler:'onNavigationSpecial'
    }
    ]
});
