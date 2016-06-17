//app 主窗口容器

Ext.define('kalix.view.Login', {
  extend: 'Ext.container.Viewport',
  requires: [
    'kalix.viewmodel.LoginViewModel'
  ],
  layout: 'center',
  viewModel: 'loginViewModel',
  constructor: function () {
    this.callParent(arguments);

    Ext.Ajax.request({
      url: CONFIG.restRoot + '/camel/rest/system/login',
      scope: this,
      method: "GET",
      callback: function (options, success, response) {
        var rtn = Ext.JSON.decode(response.responseText);

        if (rtn) {
          this.setStyle('background', rtn.color + ' url(resources/images/' + rtn.image + ')  no-repeat');
          Ext.util.Cookies.set('loginImageTag', rtn.image.split('_')[1]);
          Ext.util.Cookies.set('title', rtn.title);
          Ext.util.Cookies.set('favicon', rtn.favicon);

          var loginView = Ext.create(rtn.component);

          this.add(loginView);

          document.title = rtn.title;
          DynamicLoading.favicon(rootPath + rtn.favicon);
        }
      }
    });
  }
});