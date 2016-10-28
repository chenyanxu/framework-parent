/**
 * 机构表单控制器
 *
 * @author zangyanming <br/>
 *         date:2016-3-10
 * @version 1.0.0
 */
Ext.define('kalix.controller.BaseTreeFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseTreeFormController',
    onReset: function () {
        var view = this.getView();
        view.close();

        //var model=this.getView().lookupViewModel().get('rec');
        //model.set(model.modified);
    },
    /**
     * 保存操作.
     * @returns {Ext.panel.Panel}
     */
    onSave: function () {
        var form = this.getView();
        var me = this.getView();
        var vm = form.lookupViewModel();

        if (0 == vm.get('rec').get('id')) {
            form.getForm().method = 'POST';
        }
        else {
            form.getForm().method = 'PUT';
        }

        if (form.isValid()) {
            form.submit({
                success: function (form, action) {
                    if (action.result.failure) {
                        Ext.MessageBox.alert(CONFIG.ALTER_TITLE_FAILURE, action.result.msg);
                        return;
                    }
                    kalix.Notify.success(action.result.msg, CONFIG.ALTER_TITLE_SUCCESS);

                    var tree = vm.get('rec').get('tree');
                    var store = tree.getStore();

                    store.reload();

                    vm.get('win').close()
                },
                failure: function (form, action) {
                    Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, action.result.msg);
                }
            });
        }
    }
});