/**
 * @author chenyanxu
 */
Ext.define('kalix.dict.component.DictCombobox', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.dictCombobox',
    editable: false,
    xtype: 'dictCombobox',
    queryMode: 'local',
    valueField: 'value',
    displayField: 'label',
    listeners:{
        beforerender:function(){
            var storeName=this.storeClass.split('.').reverse()[0];
            var storeId=storeName.substr(0,1).toLowerCase()+storeName.substr(1,storeName.length-1);
            var store = Ext.app.Application.instance.getApplication().getStore(storeId);
            var tempStore = Ext.create('Ext.data.Store');

            store.filter('type',this.dictType);
            tempStore.setData(store.getData().clone());
            this.setStore(tempStore);

            return true;
        }
    }
});