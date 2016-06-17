/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.SecurityGridColumnRUD', {
    extend: 'Ext.grid.column.Action',
    alias: 'widget.securityGridColumnRUD',
    xtype: 'securityGridColumnRUD',
    permissions: [],
    header: '操作',
    iconPath:'',
    hideColumnFun: function (value, meta, record) {
        return 'x-hide-grid-column';
    },
    items: [
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
        }],
    listeners: {
        beforerender: function () {
            var scope = this;

            _.forEach(scope.items, function (item) {
                item.getClass = scope.hideColumnFun;
            });

            if(scope.iconPath!=''){
                scope.items[0].icon=scope.iconPath+'_view.png';
                scope.items[1].icon=scope.iconPath+'_edit.png';
                scope.items[2].icon=scope.iconPath+'_delete.png';
                scope.items[0].hasPermission = false;
                scope.items[1].hasPermission = false;
                scope.items[2].hasPermission = false;
            }

            if (this.permissions.length > 0) {
                var params = this.permissions.join('_');
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
                                        scope.items[0].getClass = null;
                                        scope.items[0].hasPermission = true;
                                        break;
                                    case 'edit':
                                        scope.items[1].getClass = null;
                                        scope.items[0].hasPermission = true;
                                        break;
                                    case 'delete':
                                        scope.items[2].getClass = null;
                                        scope.items[0].hasPermission = true;
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