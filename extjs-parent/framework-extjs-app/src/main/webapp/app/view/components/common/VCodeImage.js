Ext.define("kalix.view.components.common.VCodeImage", {
    extend : "Ext.Img",
    alias: 'widget.vcodeimage',
    xtype: 'vcodeimage',
    title: "点击更换图片",
    style:'cursor:pointer',
    listeners: {
        afterrender: function (cmp) {
            cmp.getEl().dom.onclick=function(){
                cmp.setSrc(cmp.src.split('?')[0]+'?dc='+Date.now());
            }
        }
    }
});
