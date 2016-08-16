/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.SecurityGridColumnCommon', {
  extend: 'Ext.grid.column.Action',
  alias: 'widget.securityGridColumnCommon',
  xtype: 'securityGridColumnCommon',
  header: '操作',
  verifyItems:[],
  hideColumnFun: function (value, meta, record) {
    return 'x-hide-grid-column';
  },
  listeners: {
    afterrender: function () {
      var scope = this;
      var params = '';
      var tempPermissions=[];

      for (var index = 0; index < scope.verifyItems.length; ++index) {
        tempPermissions[index] = CONFIG.routePath.join(':') + ':' + scope.verifyItems[index].permission;
      }

      params=tempPermissions.join('_');

      if (params != '') {
        //查询授权
        Ext.Ajax.request({
          url: CONFIG.restRoot + '/camel/rest/system/applications/modules/children/buttons/' + params,
          method: "GET",
          async: false,
          callback: function (options, success, response) {
            var resp = Ext.JSON.decode(response.responseText);
            var respButtons = resp.buttons;

            _.forEach(scope.verifyItems, function (item) {

              if (item.permission == '') {
              }
              else {
                var findObj = _.find(respButtons, function (button) {
                  return button.permission.split(':').reverse()[0] == item.permission;
                });

                if (findObj.status) {
                  scope.items.push(item);
                }
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