/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.SecurityGridColumnRUD', {
  extend: 'Ext.grid.column.Action',
  alias: 'widget.securityGridColumnRUD',
  xtype: 'securityGridColumnRUD',
  permissions: [],
  header: '操作',
  iconPath: '',
  verifyItems:[
    {
      iconCls: 'iconfont icon-view-column',
      tooltip: '查看',
      handler: 'onView'
    },
    {
      iconCls: 'iconfont icon-edit-column',
      tooltip: '编辑',
      handler: 'onEdit'
    }, {
      iconCls: 'iconfont icon-delete',
      tooltip: '删除',
      handler: 'onDelete'
    }
  ],
  items: [],
  listeners: {
    afterrender: function () {
      var scope = this;

      scope.items=[];

      if (this.permissions.length > 0) {
        var params;
        var tempPermissions=[];

        for (var index = 0; index < this.permissions.length; ++index) {
          tempPermissions[index] = CONFIG.routePath.join(':') + ':' + this.permissions[index];
        }

        params=tempPermissions.join('_');

        Ext.Ajax.request({
          url: CONFIG.restRoot + '/camel/rest/system/applications/modules/children/buttons/' + params,
          method: "GET",
          async: false,
          callback: function (options, success, response) {
            var resp = Ext.JSON.decode(response.responseText);
            var respButtons = resp.buttons;

            _.forEach(respButtons, function (item) {
              if (item.status) {
                var permissionSplit = item.permission.split(':');

                switch (permissionSplit[permissionSplit.length - 1]) {
                  case 'view':
                    scope.items.push(scope.verifyItems[0]);
                    break;
                  case 'edit':
                    scope.items.push(scope.verifyItems[1]);
                    break;
                  case 'delete':
                    scope.items.push(scope.verifyItems[2]);
                    break;
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