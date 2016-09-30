/*
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.controller.MultiComboBoxController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.multiComboBoxController',

    'onAfterRender': function() {
        var view = this.getView();
        var button = view.items.getAt(1);

        button.setText(view.displayText);
    },
    'onRender': function () {
        var view = this.getView();
        var textField = view.items.getAt(0);
        var button = view.items.getAt(1);

        textField.name = view.valueFieldName;
        if (view.storeUrl != '') {
            var store = Ext.create("kalix.store.MultiComboBoxStore");
            store.setProxy({
                type: 'ajax',
                url: view.storeUrl
            });

            store.load({
                'callback': function (records, options, success) {
                    var parent = button;
                    if (records != null && records != '') {
                        for (var i = 0; i < records[0].data.data.length; i++) {
                            var item = Ext.create("Ext.menu.CheckItem");
                            item.text = records[0].data.data[i][view.menuItemText];
                            item.value = records[0].data.data[i][view.menuItemValue];
                            item.checkHandler = 'onItemClick';
                            parent.menu.add(item);
                        }
                    }
                }
            });
        }
    },
    'onItemSelectAllClick': function (item) {
        var view = this.getView();
        var textField = view.items.getAt(0);
        var button = view.items.getAt(1);
        var menuItems =  button.menu.items;

        if (view.actions) {
            view.actions = false;
            textField.setValue('');
            for (var i = 2; i < menuItems.length; i++) {
                menuItems.items[i].setChecked(item.checked);
            }
            view.actions = true;

            view.callback();
        }
    },
    'onItemClick': function (item) {
        var view = this.getView();
        var textField = view.items.getAt(0);
        var button = view.items.getAt(1);
        var menuItems =  button.menu.items;

        if (view.actions) {
            view.actions = false;
            textField.setValue('');
            for (var i = 2; i < menuItems.length; i++) {
                if (menuItems.items[i].checked) {
                    if (textField.value == '') {
                        textField.setValue(menuItems.items[i].value);
                    }
                    else {
                        textField.setValue(textField.value + ',' + menuItems.items[i].value);
                    }
                }
            }
            menuItems.items[0].setChecked(textField.value == '');
            view.actions = true;

            view.callback();
        }
    }
});
