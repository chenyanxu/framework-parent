Ext.define('kalix.view.MutiIconButton', {
    extend: 'Ext.button.Button',
    xtype: 'mutiiconbutton',
    setIcon: function (icon) {
        icon = icon || '';
        var me = this,
            btnIconEl = me.btnIconEl,
        //oldIcon = me.icon || '';
            oldIcon = '';
        me.icon = icon;
        if (icon !== oldIcon) {
            if (btnIconEl) {
                if (icon && icon instanceof Array) {
                    var urls = new Array();

                    for (var uIndex = 0; uIndex < icon.length; ++uIndex) {
                        if (icon[uIndex] != 'null') {
                            urls.push('url(' + icon[uIndex] + ')')
                        }
                    }

                    btnIconEl.setStyle('background-image', urls.join(','));
                }

                me._syncHasIconCls();
                if (me.didIconStateChange(oldIcon, icon)) {
                    me.updateLayout();
                }
            }
            me.fireEvent('iconchange', me, oldIcon, icon);
        }

        return me;
    }
});
