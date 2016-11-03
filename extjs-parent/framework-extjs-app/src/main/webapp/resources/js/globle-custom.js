/**
 * 全局定义
 *
 * @author chenyanxu
 */
//全局配置
var CONFIG = {
  ALTER_TITLE_SUCCESS: "成功信息",
  ALTER_TITLE_FAILURE: "失败信息",
  ALTER_TITLE_ERROR: "错误信息",
  ALTER_TITLE_INFO: "提示信息",
  restRoot: '',
  theme: 'theme-triton',
  routePath: [] //record the temp route path
};

//define the var to avoid the js error about Ext no defined when ext-all.js not load
var Ext;

if (Ext) {
//Ext附加函数
//change a json obj to key value array
  Ext.JSON.toArray = function (obj) {
    var rtn = [];

    for (var key in obj) {
      rtn[rtn.length] = {key: key, value: obj[key]};
    }

    return rtn;
  };
//judge whether the theme type is classic
  Ext.theme = {
    isClassic: function () {
      switch (CONFIG.theme) {
        case 'theme-classic':
          return true;
        case 'theme-gray':
          return true;
        default:
          return false;
      }
    }
  }
//Ext函数重写
  Ext.override(Ext.JSON, {
    encodeDate: function (d) {
      return Ext.Date.format(d, '"Y-m-d H:i:s"');
    }
  });
//override the render function: edit the zIndex of the class extend from panel.
//Fix navigation bar covered by the dynamic generate component bug.
  Ext.override(Ext.panel.Panel, {
    render: function () {
      this.callParent(arguments);
      this.ariaEl.dom.style.zIndex = 0;
    }
  });

  Ext.override(Ext.window.MessageBox, {
    hide: function () {
      this.callParent(arguments);

      if (this.dockedItems.getAt(1).items.length == 5) {
        var btn = this.dockedItems.getAt(1).items.getAt(4);

        btn.setVisible(false);
      }
    },
    alert: function (title, message, fn, scope) {
      if (Ext.isString(title)) {
        title = {
          title: title,
          message: typeof(message) == 'object' ? message.msg : message,
          buttons: this.OK,
          fn: fn,
          scope: scope,
          minWidth: this.minWidth
        };
      }

      var dockedItems = this.dockedItems;
      var bbar=dockedItems.getAt(dockedItems.length - 1);
      if (bbar.items.length == 4) {
        this.dockedItems.getAt(dockedItems.length - 1).add({
          xtype: 'button', text: '详情', handler: function () {
            var tag = this.findParentByType('messagebox').tag;

            if (tag && tag.detial) {
              alert(tag.detial);
            } else {
              alert('无详细信息');
            }
          }
        });
      }
      else if(bbar.items.length ==5){
        var btn = bbar.items.getAt(4);

        btn.setVisible(true);
      }

      if (typeof(message) == 'object') {
        this.tag = message;
      }
      else {
        this.tag = undefined;
      }

      return this.show(title);
    }
  });
}
