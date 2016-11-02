
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
        window.location.href = rtn.tag;
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