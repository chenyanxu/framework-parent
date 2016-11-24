/**
 * @author chenyanxu
 */
Ext.define('kalix.controller.BaseWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.baseWindowController',
    requires: ['kalix.Notify'],
    /**
     * 重置操作.
     *
     */
    onReset: function () {
        var viewModel = this.getViewModel();
        var model = viewModel.get('rec');

        model.set(model.modified);

        var xtype = this.getView().items.getAt(0).xtype;

        if ('baseTableForm' == xtype) {
            for (var itemIndex = 0; itemIndex < this.getView().items.getAt(0).items.length; ++itemIndex) {
                if (this.getView().items.getAt(0).items.getAt(itemIndex).items.length == 1 &&
                    this.getView().items.getAt(0).items.getAt(itemIndex).items.getAt(0).xtype == 'tableFormRadioGroup') {
                    this.getView().items.getAt(0).items.getAt(itemIndex).items.getAt(0).fireEvent('afterrender');
                }
            }
        }

        this.getView().fireEvent('customReset');
    },

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
                Ext.Msg.alert(CONFIG.ALTER_TITLE_INFO, '信息未变化');
                return;
            }

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
                            Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res);
                        }
                    }
                }
            );
        } else {
            var validation = _.pick(model.getValidation().data, function (value, key, object) {
                return value !== true;
            });

            var formItems = arguments[0].findParentByType('window').items;

            for (var formIndex = 0; formIndex < formItems.length; ++formIndex) {
                var fieldItems = formItems.getAt(formIndex).items;

                for (var fieldIndex = 0; fieldIndex < fieldItems.length; ++fieldIndex) {
                    var fieldItem = fieldItems.getAt(fieldIndex);
                    if (fieldItem.bind != null) {
                        var msg = validation[fieldItem.bind.value.stub.path.split('.')[1]];

                        if (msg != undefined) {
                            fieldItem.setActiveError(msg);
                        }
                    }
                }
            }
        }
    },
    onClose: function (panel, eOpts) {
        var viewModel = this.getViewModel();
        var model = viewModel.get('rec');

        if (model == null) {
            return;
        }

        //判断是否为查看窗口的关闭
        if (viewModel.data.view_operation) {
            model.set(model.modified);
            panel.destroy();
            return;
        }

        if (model.dirty) {
            var store = viewModel.get('store');

            Ext.Msg.confirm("警告", "要保存修改吗？", function (button) {
                if (button == "yes") {
                    if (model.isValid()) {
                        model.modified = model.data;

                        if (0 == model.id) {
                            store.add(model);
                        }

                        store.sync(
                            {
                                callback: function (batch) {
                                    store.currentPage = 1;
                                    store.load();

                                    var res = Ext.JSON.decode(batch.operations[0].getResponse().responseText);

                                    if (batch.operations[0].success) {
                                        kalix.Notify.success(res.msg, CONFIG.ALTER_TITLE_SUCCESS);
                                        panel.destroy();
                                    }
                                    else {
                                        Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res);
                                        panel.show();
                                    }
                                }
                            }
                        );
                    } else {
                        panel.show();
                        Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, "表单验证失败！");
                    }
                }
                else {
                    model.set(model.modified);
                    panel.destroy();
                }
            });
        }
        else{
            panel.destroy();
        }
    },
    onBeforerender: function (target, eOpts) {
        var formItems = target.items;
        var model = target.lookupViewModel().get('rec');

        for (var formIndex = 0; formIndex < formItems.length; ++formIndex) {
            var fieldItems = formItems.getAt(formIndex).items;

            for (var fieldIndex = 0; fieldIndex < fieldItems.length; ++fieldIndex) {
                var fieldItem = fieldItems.getAt(fieldIndex);

                if (fieldItem.config.bind != null && fieldItem.config.bind.value) {
                    var bindValueSplit = fieldItem.config.bind.value.replace('}', '').split('.');

                    if (bindValueSplit.length == 2) {
                        var instanceValidators = model.getField(bindValueSplit[1]).instanceValidators;

                        if (instanceValidators != undefined && instanceValidators[0].type == 'presence') {
                            fieldItem.beforeLabelTextTpl = '<span class="field-required" data-qtip="必填选项">*</span>'
                        }
                        else if (!fieldItem.allowBlank) {
                            fieldItem.beforeLabelTextTpl = '<span class="field-required" data-qtip="必填选项">*</span>'
                        }
                    }
                }
            }
        }
    }
});
