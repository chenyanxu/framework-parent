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