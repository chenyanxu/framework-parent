Ext.define('kalix.view.components.common.AutoHPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'autoHPanel',
    layout: {
        type: 'hbox',
    },
    beforeLayout : function () {
        var me = this,
            height = Ext.Element.getViewportHeight() - 64,
            items=me.items;

        for(var itemIndex=0;itemIndex<items.length;++itemIndex)
        {
            items.getAt(itemIndex).setHeight(height-2);
        }

        me.callParent(arguments);
    }
});
