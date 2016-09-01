/**
 * @author chenyanxu
 */
Ext.define('kalix.controller.BaseGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseGridController',
    //==custom property
    cfgForm: '',         //the add and edit window full name
    cfgViewForm: '',    //the view windows full name
    cfgModel: '',       //the model bind to the view
    //custom property==
    onView: function (grid, rowIndex, colIndex) {
        var viewModel = this.getViewModel();
        var selModel = grid.getStore().getData().items[rowIndex];
        var view = Ext.create(this.cfgViewForm);
        var vm = view.lookupViewModel();

        vm.set('rec', selModel);
        vm.set('iconCls', vm.get('viewIconCls'));
        vm.set('title', vm.get('viewTitle'));
        vm.set('view_operation', true);
        vm.set('store',this.getView().store);
        this.viewModelExtraInit(vm);
        view.show();
        grid.setSelection(selModel);
    },
    itemdblclick: function (target, record, item, index, e, eOpts) {
        var grid = this.getView();
        var columns = grid.columns;
        var lastColumn = columns[columns.length - 1];
        var findViewItem=false;

        if (lastColumn.text == '操作') {
            var items = lastColumn.items;

            for (var idx = 0; idx < items.length; ++idx) {
                var item = items[idx];

                if (item.handler == 'onView') {
                    this.onView(grid, index, 0);
                    findViewItem=true;

                    break;
                }
            }
        }

        if(!findViewItem) {
            Ext.Msg.alert(CONFIG.ALTER_TITLE_INFO, '无查看权限');
        }
    },
    /**
     * 打开添加操作.
     */
    onAdd: function (target) {
        var view = Ext.create(this.cfgForm);
        var vm = view.lookupViewModel();

        vm.set('rec', Ext.create(this.cfgModel));
        vm.set('iconCls', vm.get('addIconCls'));
        vm.set('title', vm.get('addTitle'));
        vm.set('store',this.getView().store);

        this.viewModelExtraInit(vm);

        view.show();
    },
    /**
     * 打开编辑操作.
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onEdit: function (grid, rowIndex, colIndex) {
        var selModel = grid.getStore().getData().items[rowIndex];
        var view = Ext.create(this.cfgForm);
        var vm = view.lookupViewModel();

        vm.set('rec', selModel);
        vm.set('iconCls', vm.get('editIconCls'));
        vm.set('title',vm.get('editTitle'));
        vm.set('store',this.getView().store);

        this.viewModelExtraInit(vm);

        view.show();
        grid.setSelection(selModel);
    },
    onDelete: function (grid, rowIndex, colIndex) {
        var model = grid.getStore().getData().items[rowIndex];
        var store = grid.getStore();

        Ext.Msg.confirm("警告", "确定要删除吗？", function (button) {
            if (button == "yes") {
                store.proxy.extraParams = {};
                store.remove(model);
                store.sync(
                    {
                        callback: function (batch) {
                            store.currentPage = 1;
                            store.load();

                            var res = Ext.JSON.decode(batch.operations[0].getResponse().responseText);

                            if (batch.operations[0].success) {
                                kalix.Notify.success(res.msg, CONFIG.ALTER_TITLE_SUCCESS);
                            }
                            else {
                                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res.msg);
                            }
                        }
                    }
                );
            }
        });
    },
    onBatchDelete:function(){
        var grid = this.getView();
        var selModel = grid.getSelectionModel();
        var batchDeleteUrl = this.getViewModel().get("batchDeleteUrl");
        if (selModel.hasSelection()) {
            Ext.Msg.confirm("警告", "确定要删除吗？", function (button) {
                if (button == "yes") {
                    var rows = selModel.getSelected();
                    var ids = "";
                    for (var i = 0; i < rows.length; i++) {
                        var row = rows.getAt(i);
                        if (row != null && row.id != null) {
                            ids += row.id;
                            if (i + 1 != rows.length) {
                                ids += ":";
                            }
                        }
                    }
                    Ext.Ajax.request({
                        url: batchDeleteUrl + "?ids=" + ids,
                        method: 'DELETE',
                        callback: function (options, success, response) {
                            var resp = Ext.JSON.decode(response.responseText);
                            if (resp.success) {
                                kalix.Notify.success(resp.msg, CONFIG.ALTER_TITLE_SUCCESS);
                                var store = grid.getStore();
                                store.reload();
                            }
                            else{
                                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, resp.msg);
                            }
                        }
                    });
                }
            });
        } else {
            Ext.Msg.show({
                title: '提示',
                message: '至少应该选择一条记录进行操作',
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.WARNING,
                fn: null
            });
        }
    },
    addTooltip: function (value, metadata, record, rowIndex, colIndex, store) {
        metadata.tdAttr = 'data-qtip="' + value + '"';
        return value;
    },
    viewModelExtraInit:function(vm){
        //If have extra init,overwrite this method
    }
    //,
    //  //excel upload
    //  onChange: function (target, event, domValue) {
    //    var form = target.findParentByType('form');
    //    var store = this.getView().getStore();
    //
    //    scope = {store: store};
    //
    //    form.submit({
    //        url: CONFIG.restRoot + '/camel/rest/excel/upload?' +
    //        'ConfigId=' + form.ConfigId +
    //        '&EntityName=' + form.EntityName +
    //        '&ServiceInterface=' + form.ServiceInterface,
    //        waitMsg: '正在上传...',
    //        scope: scope,
    //        success: function (fp, o) {
    //            store.currentPage = 1;
    //            store.load();
    //            kalix.Notify.success(o.result.msg, CONFIG.ALTER_TITLE_SUCCESS);
    //        },
    //        failure: function (fp, o) {
    //            Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, o.result.msg);
    //        }
    //    });
    //},
    ////金额格式化
    //renderMoney: function (val, metadata, record, rowIndex, colIndex, store) {
    //    var out = Ext.util.Format.currency(val);
    //    out = out + '元';
    //    metadata.tdAttr = 'data-qtip="' + out + '"';
    //    return out;
    //},
    ////百分比格式化
    //renderPercent: function (val, metadata, record, rowIndex, colIndex, store) {
    //    var percentage = (val * 100).toFixed(2)+'%';
    //    return percentage;
    //}
});