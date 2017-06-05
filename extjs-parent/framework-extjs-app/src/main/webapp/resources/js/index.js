Ext.onReady(function () {
  CONFIG.currentUserLoginName = Ext.util.Cookies.get('currentUserLoginName');

  if(window.sessionStorage) {
    CONFIG.JSESSIONID=window.sessionStorage.getItem('JSESSIONID');
    CONFIG.access_token=window.sessionStorage.getItem('access_token');
  }
  else{
    CONFIG.JSESSIONID=Ext.urlDecode(window.location.search.substring(1)).JSESSIONID;
    CONFIG.access_token=Ext.urlDecode(window.location.search.substring(1)).access_token;
  }

  Ext.Ajax.setDefaultHeaders({
    'access_token':CONFIG.access_token,
    'JSESSIONID':CONFIG.JSESSIONID
  });

  Ext.Ajax.addListener('requestcomplete', function (conn, response, options, eOpts) {
    if(Ext.decode(response.responseText).code == 401){
      window.location.href='/logout';
    }
    else{
      var timePast=Date.now()-CONFIG.timestamp;

      if(timePast<50000){
        CONFIG.timestamp=Date.now();
      }
      else{
        //TODO:No Operation TimeOut
      }
    }
  }, this)

  if (CONFIG.currentUserLoginName) {
    Ext.Ajax.request({
      async: false,
      url: 'camel/rest/system/preferences/'+CONFIG.currentUserLoginName,
      success: function (response, opts) {
        var obj = Ext.decode(response.responseText);
        var surffix = (this.location.href.indexOf('debug') > -1) ? '' : '.min';

        if (obj != null && obj.theme) {
          DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/' + obj.theme + '/' + obj.theme + '-all' + surffix + '.css');
          CONFIG.theme = obj.theme;
        }
        else {
          DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/theme-triton/theme-triton-all' + surffix + '.css');
        }
      },
      failure: function (response, opts) {
        console.log('server-side failure with status code ' + response.status);
        DynamicLoading.css(CONFIG.restRoot + '/extjs/classic/theme-triton/theme-triton-all' + surffix + '.css');
      },
      callback: function () {
        DynamicLoading.js(CONFIG.restRoot + '/app/app.js');
      }
    });
  }

  DynamicLoading.css(CONFIG.restRoot + '/resources/css/index.css');
  DynamicLoading.favicon(CONFIG.restRoot + Ext.util.Cookies.get('favicon'));
  document.title = Ext.util.Cookies.get('title');
});