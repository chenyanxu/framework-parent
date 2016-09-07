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
Ext.theme={
  isClassic:function(){
    switch (CONFIG.theme){
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
    return Ext.Date.format(d, '"Y-m-d T H:i:s"');
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

