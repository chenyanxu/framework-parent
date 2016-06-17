/**
 * This class is the view model for the Main view of the application.
 */
Ext.define('kalix.viewmodel.MessagebarViewModel', {
  extend : 'Ext.app.ViewModel',
  alias : 'viewmodel.messagebarViewModel',

  data : {
    message : Ext.create('Ext.data.Model',{
      count:0,
      iconCls:'x-fa fa-envelope-o'})
  }
});
