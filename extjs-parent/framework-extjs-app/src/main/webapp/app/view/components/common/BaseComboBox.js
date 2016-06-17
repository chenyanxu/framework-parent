/**the base class for all key/value combox
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseComboBox', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.baseComboBox',
    labelAlign: 'right',
    xtype: 'baseComboBox',
    queryMode: 'remote',
    minChars: 0,
    queryField: '',//use inside only
    modelField: '',//config in the child class
    //to satisfy the new rest service which params as (int page,int limit,string jsonStr)
    //wee need to convert the query string in queryPlan to json string.
    listeners: {
        beforequery: function (queryPlan, opts) {
            queryPlan.query = '{' + this.queryField + ':\'' + queryPlan.query + '\'}';

            return true;
        }
    },
    //by default, the combobox send a query to server when we click the trigger button every time.
    //we don't need this in the base component.
    //So override the callback function.Only let the collapse/expand action enable.
    onTriggerClick: function () {
        var me = this;

        if (!me.readOnly && !me.disabled) {
            if (me.isExpanded) {
                me.collapse();
            }
            else {
                me.expand();
            }
        }
    },
    constructor: function () {
        this.callParent(arguments);
        this.queryField = this.queryParam;//the queryParam will change for rest request,we need the default value in beforequery callback.
        this.queryParam = 'jsonStr';//override the property to match the param in server method.

        var scope = {target: this, firstQuery: true};

        this.store.on('load', function (store, records, successful, eOpts) {
            if (this.firstQuery) {//be sure the method only called once.
                this.firstQuery = false;
            }
            else {
                return;
            }

            var rec = this.target.findParentByType('window').getViewModel().data.rec;//get the model which bind to the parent form window.
            var obj = null;
            var notFind = true;
            if (rec.id != 0) {//we do nothing when in the add operation.
                Ext.Array.forEach(records, function (item) {//check existence of target model in the store data.
                    if (item.id == rec.get(this.target.modelField)) {
                        notFind = false;
                    }
                }, {'rec': rec, 'obj': obj, 'target': this.target});

                if (notFind && rec.get(this.target.modelField)) {//when the target not found and the target model field value not null.
                    Ext.Ajax.request({//the method must sync
                        async: false,
                        url: store.proxy.url + '/' + rec.get(this.target.modelField),
                        success: function (response, opts) {
                            obj = Ext.decode(response.responseText);
                        }
                    });

                    if (obj) {
                        var mobj = Ext.create('Ext.data.Model', obj);//we create a new model when we find the target in remote server.

                        store.removeAll();//clear the auto load records,we don't need them for combobox selection.
                        store.add(Ext.create('Ext.data.Model', obj));
                        this.target.setSelection(mobj)
                    }
                }
            }
        }, scope);
    }
})
