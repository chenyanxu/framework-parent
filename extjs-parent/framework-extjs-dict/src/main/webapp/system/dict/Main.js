/**
 * Dict ext js
 * @author chenyanxu
 */
Ext.define('kalix.dict.Main', {
  extend: 'kalix.container.BaseContainer',
  requires: [
    'kalix.dict.view.DictGrid',
    'kalix.dict.view.DictSearchForm'
  ],
  constructor: function () {
    var storeId= this.$className.split('.').reverse()[1]+'Store';

    this.items = [
      {
        title: '字典查询',
        xtype: 'dictSearchForm'
      },
      {
        xtype: 'dictGridPanel',
        title: '字典列表',
        store: {type: storeId}
      }
    ];

    this.callParent(arguments);
  }
});