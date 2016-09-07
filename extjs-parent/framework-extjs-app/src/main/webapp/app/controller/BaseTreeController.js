/**
 * @author 基础树表格控制器
 */
Ext.define('kalix.controller.BaseTreeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseTreeController',
    /**
     * 刷新.
     * @returns {Ext.panel.Panel}
     */
    onRefresh: function () {
        var grid = this.getView();
        var store = grid.getStore();

        store.reload();
    },
    /**
     * 展开.
     * @returns {Ext.panel.Panel}
     */
    onExpandAll: function () {
        this.getView().expandAll();
    },
    /**
     * 收起
     */
    onCollapseAll: function () {
        try{
            this.getView().collapseAll();
        }
        catch(e){
            var grid = this.getView();
            var store = grid.getStore();

            store.reload();
        }
    },
    /**
     * 打开添加操作.
     */
    onAdd: function () {
        var rows = this.getView().getSelectionModel().getSelection();
        var model=Ext.create('Ext.data.Model');

        model.set('url',this.getView().store.proxy.url);
        model.set('id',0);
        model.set('tree',this.getView());

        if(rows!=null&&rows.length>0){
            if(rows[0]!=null){
                model.set('parentName',rows[0].data.name);
                model.set('parentId',rows[0].data.id);
            }
        }else{
            model.set('parentName','根');
            model.set('parentId',-1);
        }

        this.showWindow(model);
    },
    /**
     * 打开编辑操作.
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onEdit: function (grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var model=Ext.create('Ext.data.Model');

        model.set('url',this.getView().store.proxy.url);
        model.set('id',rec.data.id);
        model.set('name',rec.data.name);
        model.set('code',rec.data.code);
        model.set('parentId',rec.data.parentId);
        model.set('parentName',rec.data.parentName);

        model.set('tree',this.getView());

        if(rec.data.parentId =='root'){
            model.set('parentId',-1);
        }

        this.showWindow(model);
    },
    /**
     * 删除单个操作.
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onDelete: function (grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        Ext.Msg.confirm("警告", "确定要删除吗？", function (button) {
            if (button == "yes") {
                Ext.Ajax.request({
                    url: grid.store.proxy.url + "/" + rec.id,
                    method: 'DELETE',
                    callback: function (options, success, response) {
                        var resp = Ext.JSON.decode(response.responseText);
                        if (resp != null && resp.success) {
                            kalix.Notify.success(resp.msg, CONFIG.ALTER_TITLE_SUCCESS);
                            var store = grid.getStore();
                            store.reload();
                        } else {
                            Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, resp.msg);
                        }
                    }
                });
            }
        });
    },
    showWindow:function(model){
        model.modified = {};
        model.dirty = false;

        var treeForm = Ext.create(this.cfgForm);

        treeForm.lookupViewModel().set('rec',model);
        treeForm.getForm().url=model.get('url');

        var win = Ext.create('Ext.Window', {
            width: 400,
            border: false,
            modal: true,
            iconCls: model.get('id')==0?'iconfont icon-add':'iconfont icon-edit',
            title: model.get('id') == 0 ? '添加' : '编辑',
            items: [treeForm]
        });

        win.show();
    }
});