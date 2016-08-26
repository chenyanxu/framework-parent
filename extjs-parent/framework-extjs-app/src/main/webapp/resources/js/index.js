Ext.JSON.toArray = function (obj) {
    var rtn = [];

    for (var key in obj) {
        rtn[rtn.length] = {key: key, value: obj[key]};
    }

    return rtn;
};

Ext.JSON.encodeDate = function (d) {
    return Ext.Date.format(d, '"Y-m-d H:i:s"');
};
//override the render function: edit the zIndex of the class extend from panel.
//Fix navigation bar covered by the dynamic generate component bug.
Ext.override(Ext.panel.Panel,{render:function(){
    this.callParent(arguments);
    this.ariaEl.dom.style.zIndex=0;
}});

Ext.onReady(function () {
    Ext.Ajax.request({
        async:false,
        url: 'camel/rest/system/preferences',
        success: function (response, opts) {
            var obj = Ext.decode(response.responseText);

            if (obj != null && obj.theme) {
                DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/' + obj.theme + '/' + obj.theme + '-all.css');
                CONFIG.theme = obj.theme;
            }
            else {
                DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/theme-triton/theme-triton-all.css');
            }
        },
        failure: function (response, opts) {
            console.log('server-side failure with status code ' + response.status);
            DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/theme-triton/theme-triton-all.css');
        },
        callback: function () {
            DynamicLoading.js(CONFIG.restRoot + '/app/app.js');
        }
    });

    DynamicLoading.css(CONFIG.restRoot + '/resources/css/index.css');
    DynamicLoading.favicon(CONFIG.restRoot+Ext.util.Cookies.get('favicon'));
    document.title=Ext.util.Cookies.get('title');
});