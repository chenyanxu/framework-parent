/**
 * 顶部工具条
 *
 * date:2015-10-26
 */

Ext.define('kalix.view.MainToolbar', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'maintoolbar',

    requires: [
        'kalix.viewmodel.MainToolbarViewModel'
    ],

    viewModel: 'maintoolbarViewModel',

    id: 'mainToolbar',

    bind: {
        items: '{items}'
    },

    border: false,

    setItems: function (items) {
        var toolbar = this;
        toolbar.removeAll()
        items.forEach(function (item, index) {
            toolbar.add(Ext.create('Ext.button.Button', {
                id: item.id,
                href: '#' + item.id,
                text: item.text,
                iconCls: item.iconCls,
                hrefTarget: '_self',
                border: 1,
                margin: '0 5 0 10',
                listeners: {
                    click: function (target, e, eOpts) {
                        target.findParentByType('toolbar').items.items.forEach(function (child, index) {
                            child.ariaEl.dom.style.background = '';
                        });

                        target.ariaEl.dom.style.background = '#e1e1e1';
                    },
                    afterrender: function (target) {
                        var itemId = document.URL.split('#')[1].split('/')[0];

                        if (itemId == target.id)
                            target.ariaEl.dom.style.background = '#e1e1e1';
                    }
                }
            }));
        });
    }
});
