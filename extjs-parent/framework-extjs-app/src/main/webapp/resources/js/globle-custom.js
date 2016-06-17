/**
 * 全局定义
 *
 * @author chenyanxu
 */
var CONFIG = {
  ALTER_TITLE_SUCCESS: "成功信息",
  ALTER_TITLE_FAILURE: "失败信息",
  ALTER_TITLE_ERROR: "错误信息",
  ALTER_TITLE_INFO: "提示信息",
  restRoot: '',
  theme: 'theme-triton'
};

Ext.JSON.toArray = function (obj) {
  var rtn = [];

  for (var key in obj) {
    rtn[rtn.length] = {key: key, value: obj[key]};
  }

  return rtn;
};

Ext.JSON.encodeDate = function (d) {
  return Ext.Date.format(d, '"Y-m-d H:i:s"');
};

