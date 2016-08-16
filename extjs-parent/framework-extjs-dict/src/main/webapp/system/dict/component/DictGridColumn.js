/**
 * @author chenyanxu
 */
Ext.define('kalix.dict.component.DictGridColumn', {
    extend: 'Ext.grid.column.Template',
    tpl: "",
    alias: 'widget.dictGridColumn',
    xtype: 'dictGridColumn',
    colorConfig: null,
    storeClass:'',
    lastDictColumnInGrid:false,
    listeners: {
        beforerender: function () {
            Ext.require(this.storeClass ,function(){
                var storeName=this.storeClass.split('.').reverse()[0];
                var storeId=storeName.substr(0,1).toLowerCase()+storeName.substr(1,storeName.length-1);
                var store=Ext.app.Application.instance.getApplication().getStore(storeId);

                this.customeFn=function(){
                    var store=arguments[0];

                    store.clearFilter();
                    store.filter('type', this.dictType);

                    var data = store.getData().clone().items;

                    if (data.length > 0) {
                        var tplStr = '';

                        for (var idx = 0; idx < data.length; ++idx) {
                            var tempValue = data[idx].data.value;
                            var tempLabel = data[idx].data.label;

                            if (this.colorConfig) {
                                if (this.colorConfig[tempLabel]) {
                                    tplStr += '<tpl if="' + this.dataIndex + '==' + tempValue + '"><span style="color:' + this.colorConfig[tempLabel] + '">' + tempLabel + '</span></tpl>';
                                }
                                else {
                                    tplStr += '<tpl if="' + this.dataIndex + '==' + tempValue + '"><span style="color:' + this.colorConfig['default'] + '">' + tempLabel + '</span></tpl>';
                                }
                            }
                            else {
                                tplStr += '<tpl if="' + this.dataIndex + '==' + tempValue + '">' + tempLabel + '</tpl>';
                            }
                        }

                        tplStr += '<tpl if="' + this.dataIndex + '==' + -1 + '">无字典</tpl>';

                        var tpl = new Ext.XTemplate(tplStr);
                        this.tpl = tpl;
                    }
                    else {
                        this.tpl = new Ext.XTemplate("<tpl>无字典</tpl>");
                    }

                    if(this.lastDictColumnInGrid){
                        this.findParentByType('grid').getStore().load();
                    }
                };

                store.on('load',this.customeFn,this);

                if(store.totalCount>0){
                    this.customeFn(store);
                }
            },this);
        }
    }
});