/**
 * This class is the view model for the Main view of the application.
 */
Ext.define('kalix.viewmodel.ProfilebarViewModel', {
  extend : 'Ext.app.ViewModel',
  alias : 'viewmodel.profilebarViewModel',

  data : {
    user : {
      name : Ext.util.Cookies.get('currentUserRealName') || '系统管理员',
      icon:Ext.util.Cookies.get('currentUserIcon'),
      quit: CONFIG.restRoot + '/logout'
    }
  }
});
