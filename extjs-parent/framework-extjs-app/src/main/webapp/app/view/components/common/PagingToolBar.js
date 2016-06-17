/**
 * 分页工具条
 *
 * @author majian <br/>
 *         date:2015-7-6
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.PagingToolBar', {
    extend: 'Ext.toolbar.Paging',
    alias: 'widget.pagingToolBarComponent',
    xtype: 'pagingToolBarComponent',
    border: false,
    displayMsg: '本页显示 {0} - {1} 条，共计 {2} 条',
    emptyMsg: "没有数据",
    beforePageText: "当前页",
    afterPageText: "共{0}页",
    firstText: "第一页",
    prevText: "上一页",
    nextText: "下一页",
    lastText: "最后页",
    refreshText: "刷新",
    displayInfo: true,
    pageSizeList:[10,20,30,40,50],
    pageSize:10,
    getPagingItems: function () {
        var parentItems = this.callParent(arguments);
        var me = this;

        return Ext.Array.insert(parentItems, 0,
            [
                {
                    xtype: 'combobox',
                    width:70,
                    scope: me,
                    editable: false,
                    store: me.pageSizeList,
                    value:me.pageSize,
                    listeners:{
                        select:function( combo, record, eOpts ){
                            var store=combo.findParentByType('pagingtoolbar').getStore();

                            store.pageSize=combo.value;
                            store.loadPage(1);
                        }
                    }
                }]);
    }

});