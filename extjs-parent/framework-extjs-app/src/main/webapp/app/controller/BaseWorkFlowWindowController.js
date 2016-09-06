/**
 * @author zangyanming
 * 目的：创建此类的目的是为工作流表单提供验证的控制，因为工作流表单的验证与普通表单的form不一致，因此验证方式需要改变
 */
Ext.define('kalix.controller.BaseWorkFlowWindowController', {
    extend: 'kalix.controller.BaseWindowController',
    alias: 'controller.baseWorkFlowWindowController',
    /**
     * 保存/更新操作.
     *
     */
    onSave: function () {
        var viewModel = this.getViewModel();
        var model = viewModel.get('rec');
        var view = this.getView();
        var store = viewModel.get('store');

        store.proxy.extraParams = {};

        if (model.isValid()) {
            if (!model.dirty) {
                Ext.Msg.alert(CONFIG.ALTER_TITLE_INFO, '未修改数据');
                return;
            }

            model.modified = model.data;

            if (0 == model.id) {
                store.add(model);
            }

            store.sync(
                {
                    success: function () {
                        view.close();
                        model.dirty = false;
                    },
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
        } else {
            var validation = _.pick(model.getValidation().data, function (value, key, object) {
                return value !== true;
            });

            var formItems = arguments[0].findParentByType('window').items;

            //循环遍历有多少个form，目前大多数表单只有一个form
            for (var formIndex = 0; formIndex < formItems.length; ++formIndex) {
                var fieldItems = formItems.getAt(formIndex).items;

                //循环遍历表单中的第一层items
                for (var fieldIndex = 0; fieldIndex < fieldItems.length; ++fieldIndex) {
                    //记录表单的提示信息
                    var fieldLabel;
                    if(fieldItems.getAt(fieldIndex).items.length == 0) {
                        fieldLabel = fieldItems.getAt(fieldIndex).body.dom.innerText;
                    }

                    var fieldItem = fieldItems.getAt(fieldIndex);
                    var subFieldItem = fieldItem.items;
                    //循环遍历第一层下面的itmes，并验证相关信息。
                    for(var subFieldIndex = 0; subFieldIndex <subFieldItem.length; ++subFieldIndex) {
                        if (subFieldItem.items[subFieldIndex].bind != null) {
                            var msg = validation[subFieldItem.items[subFieldIndex].bind.value.stub.path.split('.')[1]];

                            if (msg != undefined) {
                                //subFieldItem.items[subFieldIndex].setActiveError(msg);
                                kalix.Notify.warning(fieldLabel + ":" + msg,CONFIG.ALTER_TITLE_INFO);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
});
