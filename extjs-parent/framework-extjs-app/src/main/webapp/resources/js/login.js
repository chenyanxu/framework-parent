function doVCOdeTest(appName){
  var rtn=false;

  Ext.Ajax.request({
    url: CONFIG.restRoot + '/camel/rest/system/vcode/'+appName,
    async:false,
    scope: this,
    method: "GET",
    callback: function (options, success, response) {
      var obj = Ext.JSON.decode(response.responseText);

      if (obj && obj.success) {
        rtn=(obj.msg=='true'?true:false);
      }
    }
  });

  return rtn;
}


function doSysServiceTest(autoLoad){
  var rtn;

  Ext.Ajax.request({
    url: CONFIG.restRoot + '/camel/rest/system',
    async:false,
    scope: this,
    method: 'GET',
    callback: function (options, success, response) {
      rtn = Ext.JSON.decode(response.responseText);

      if (rtn.success && autoLoad) {
        //window.location.href = rtn.tag;
          //alert('已经登录，窗口将关闭！');
          window.location.href='/message.html';
      }
    }
  });

  return rtn;
}

var Ext;
DynamicLoading.css(rootPath + '/resources/css/theme-triton-all.css');
DynamicLoading.js(rootPath + '/extjs/classic/ext-all.js');
CONFIG.restRoot=rootPath;
var intervalObj = setInterval(function () {
  if (Ext) {
    clearInterval(intervalObj);
    DynamicLoading.js(rootPath + '/extjs/classic/locale/locale-zh_CN.js');
    Ext.Loader.setConfig({ enabled: true, disableCaching: false });
    Ext.define("Ext.noclosebutton.window.MessageBox", {
      override: 'Ext.window.MessageBox',
      closable:false,
      confirm: function(cfg, message, fn, scope) {
        if (Ext.isString(cfg)) {
          cfg = {
            title: cfg,
            message: message,
            buttons: this.YESNO,
            callback: fn,
            scope: scope
          };
        }
        return this.show(cfg);
      }
    });

    DynamicLoading.js(rootPath + '/app/login.js');
  }
}, 50);