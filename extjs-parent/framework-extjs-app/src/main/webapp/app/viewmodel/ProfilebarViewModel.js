/**
 * This class is the view model for the Main view of the application.
 */
Ext.define('kalix.viewmodel.ProfilebarViewModel', {
  extend : 'Ext.app.ViewModel',
  alias : 'viewmodel.profilebarViewModel',

  data : {
    user : {
      name : Ext.util.Cookies.get('currentUserName') || '系统管理员',
      quit: CONFIG.restRoot + '/logout'
    }
  }
});
