/**
 * 权限控制工具条
 *
 * @author majian <br/>
 *         date:2015-8-14
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.SecurityToolbar', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.securityToolbar',
    xtype: 'securityToolbar',

    initComponent: function () {
        this.callParent(arguments);
        this.on('afterrender', this.initVerifyButton);
    },

    initVerifyButton: function () {
        var verifyItems = this.verifyItems;

        if (!this.verifyItems) {
            return;
        }

        var params='';
        var securityToolbar = this;

        verifyItems.forEach(function(item){
            if(item.permission!=''){
                if(params==''){
                    params=item.permission;
                }
                else{
                    params=params+'_'+item.permission;
                }
            }
        });

        if (params != '') {
            //查询授权
            Ext.Ajax.request({
                url: CONFIG.restRoot + '/camel/rest/system/applications/modules/children/buttons/' + params,
                method: "GET",
                async: false,
                callback: function (options, success, response) {
                    var resp = Ext.JSON.decode(response.responseText);
                    var respButtons = resp.buttons;

                    respButtons.forEach(function (btn) {
                        if (btn.status) {
                            verifyItems.forEach(function (item) {
                                if (btn.permission == item.permission) {
                                    securityToolbar.add(item);
                                }
                            });
                        }
                    });
                },
                failure: function (xhr, params) {
                    console.log('Permission call failure!');
                }
            });
        }
        verifyItems.forEach(function (item) {
            if (item.permission == '') {
                securityToolbar.add(item);
            }
        });
    }
});