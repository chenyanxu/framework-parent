/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.SecurityGridColumnCommon', {
  extend: 'Ext.grid.column.Action',
  alias: 'widget.securityGridColumnCommon',
  xtype: 'securityGridColumnCommon',
  header: '操作',
  hideColumnFun: function (value, meta, record) {
    return 'x-hide-grid-column';
  },
  listeners: {
    beforerender: function () {
      var scope = this;
      var params = '';

      scope.items.forEach(function (item) {
        if (item.permission != '') {
          if (params == '') {
            params = item.permission;
          }
          else {
            params = params + '_' + item.permission;
          }
        }
      });

      if (params != '') {
        //查询授权
        Ext.Ajax.request({
          url: CONFIG.restRoot + '/camel/rest/system/applications/modules/children/buttons/' + params,
          method: "GET",
          async: false,
          callback: function (options, success, response) {
            var resp = Ext.JSON.decode(response.responseText);
            var respButtons = resp.buttons;

            _.forEach(scope.items, function (item) {
              var findObj = _.find(respButtons, function (button) {
                return button.permission == item.permission;
              });

              if (findObj.status) {
                item.hasPermission = true;
              }
              else {
                item.getClass = scope.hideColumnFun;
                item.hasPermission = false;
              }
            });
          },
          failure: function (xhr, params) {
            console.log('Permission call failure!');
          }
        });
      }
    }
  }
});