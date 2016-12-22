/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
      'kalix.view.components.common.SecurityToolbar',
      'kalix.view.components.common.PagingToolBar',
      'kalix.view.components.common.SecurityGridColumnCommon',
      'kalix.view.components.common.SecurityGridColumnRUD',
      'kalix.plugin.AutoHeightPlugin',
      'kalix.plugin.ZOrderPlugin'
    ],
    alias: 'widget.baseGrid',
    xtype: 'baseGrid',
    autoLoad: true,
    stripeRows: true,
    margin: 5,
    plugins: ['autoheightplugin', 'zorderPlugin'],
    bind: {
      iconCls: '{grid_iconCls}'
    },
    listeners: {
      itemdblclick: 'itemdblclick'
    },
    viewConfig: {
      enableTextSelection: true
    },
    bbar: [
      {
        xtype: 'pagingToolBarComponent',
        border: false,
        padding: 0,
        listeners: {
          afterrender: function (c, obj) {
            var store = c.findParentByType('grid').store;
            this.setConfig('store', store);
            this.items.getAt(0).setValue(store.pageSize);
          }
        }
      }
    ],
    constructor: function () {
      if (this.columns instanceof Array) {
          if (this.autoLoad) {
              for (var cIndex = this.columns.length - 1; cIndex >= 0; --cIndex) {
                  if (this.columns[cIndex].xtype && this.columns[cIndex].xtype.indexOf('DictGrid') > -1) {
                      this.autoLoad = false;
                      this.columns[cIndex].lastDictColumnInGrid = true;
                      break;
                  }
              }
          }


        for (var cIndex = 0; cIndex < this.columns.length; ++cIndex) {
          if (this.columns[cIndex].xtype && this.columns[cIndex].xtype == 'rownumberer') {
            this.columns[cIndex].width = 50;
            this.columns[cIndex].flex = 0;
            this.columns[cIndex].text = '行号';
            this.columns[cIndex].align = 'center';
            this.columns[cIndex].renderer = null;
          }
          else if (this.columns[cIndex].xtype){ //&& this.columns[cIndex].xtype.indexOf('DictGrid') > -1) {
            this.columns[cIndex].renderer = null;
          }
          else if (this.columns[cIndex].renderer === undefined) {
            this.columns[cIndex].renderer = 'addTooltip';
          }

          if (!this.columns[cIndex].flex && this.columns[cIndex].flex != 0 && this.columns[cIndex].xtype != 'rownumberer') {
            this.columns[cIndex].flex = 1;
          }
        }
      }

      this.callParent(arguments);
    }
  }
);
