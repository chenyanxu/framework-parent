Ext.define('kalix.view.components.common.BaseItemSelectorWindow', {
  extend: 'Ext.window.Window',
  requires: [
    'Ext.ux.form.ItemSelector',
    'kalix.view.components.common.BaseSearchForm',
    'kalix.controller.BaseItemSelectorWindowController'
  ],
  alias: 'widget.baseItemSelectorWindow',
  xtype: 'baseItemSelectorWindow',
  controller:'baseItemSelectorWindowController',
  Config:{
    fieldLabel:'',
    fromTitle: '',
    toTitle: '',
    baseUrl:'',   //the base url for the relation operation
    recoredId:-1, //the parent id of the relation table
    store:null,   //the child store
    selectItems:[]//the child ids that has selected into relation table
  },
  width:600,
  border: false,
  modal: true,
  resizable: false,
  buttonAlign: 'center',
  constructor:function(){
    this.callParent(arguments);
    me=this;

    if(!me.Config.fieldLabel){
      me.Config.fieldLabel='名称';
    }

    if(!me.Config.fromTitle){
      me.Config.fromTitle='可选项';
    }

    if(!me.Config.toTitle){
      me.Config.toTitle='已选项';
    }

    this.add(
      {
        xtype: 'baseSearchForm',
        margin: '5 5 0 5',
        iconCls:'',
        controller:'baseItemSelectorWindowController',
        items: [
          {
            xtype: 'textfield',
            bind:{
              fieldLabel: me.Config.fieldLabel
            },
            labelAlign: 'left',
            labelWidth: 30,
            width: 200,
            name: 'name'
          }
        ]
      }
    );

    if(me.Config.store){
      me.Config.selectItems=[];

      if(me.Config.baseUrl && me.Config.baseUrl!='' &&
         me.Config.recoredId){
        Ext.Ajax.request({
          url:me.Config.baseUrl+'/'+me.Config.recoredId+'/users/ids',
          async:false,
          method: 'GET',
          callback: function (options, success, response) {
            me.Config.selectItems = Ext.JSON.decode(response.responseText);
          }
        });
      }

      this.add(
        {
          xtype: 'itemselector',
          width:585,
          height:400,
          margin: '2 5 5 5',
          displayField: 'name',
          valueField: 'id',
          msgTarget: 'side',
          fromTitle: me.Config.fromTitle,
          toTitle:me.Config.toTitle,
          store:me.Config.store,
          value:me.Config.selectItems
        }
      );
    }
  },
  items: [],
  buttons: [
    {
      text: '保存',
      iconCls:'iconfont icon-save iconfont-btn-small',
      handler: 'onSave'
    },
    {
      text: '重置',
      iconCls:'iconfont icon-reset iconfont-btn-small',
      handler: 'onResetItemSelector'
    }
  ]
});