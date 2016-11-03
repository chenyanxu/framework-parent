/**
 * @author chenyanxu
 *
 */

Ext.define('kalix.store.BaseStore', {
  extend : 'Ext.data.Store',
  requires: ['kalix.Notify'],
  autoLoad: false,
  pageSize: 20,
  remoteSort:true,
  //==custom property
  proxyUrl:'', //the proxy url to init in the BaseStore's constructor
  //custom property==
  constructor: function () {
    this.callParent(arguments);
    if(this.proxyUrl!='')
    {
      this.proxy.url=this.proxyUrl;
    }
  },
  listeners:{
    //listener function for shiro session timeout
    load:function(target, records, successful, eOpts ){
      var rtnJson=Ext.JSON.decode(eOpts.getResponse().responseText);

      if(rtnJson.message!=undefined &&
          'login'==rtnJson.message){
        location.reload();
      }

      if (rtnJson.success != undefined && false == rtnJson.success) {
        Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, rtnJson);
      }
    }
  },
  proxy: {
    type: 'rest',
    url: '',
    actionMethods:{
      create: 'POST',
      read: 'GET',
      update: 'PUT',
      destroy: 'DELETE'
    },
    reader:{
      rootProperty:'data',
      totalProperty:'totalCount'
    }
  }
});
