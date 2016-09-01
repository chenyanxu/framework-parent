/**
 * @author chenyanxu
 */

Ext.define('kalix.view.components.common.BaseSearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'kalix.controller.BaseSearchFormController'
    ],
    alias: 'widget.baseSearchForm',
    xtype: 'baseSearchForm',
    controller: {
        type: 'baseSearchFormController'
    },
    bodyPadding: 10,
    layout: 'column',
    margin: 10,
    defaults: {border: 0},
    iconCls: 'iconfont icon-query',
    listeners: {
        beforerender: function () {
            this.add({
                    xtype: 'button',
                    text: '查询',
                    margin: '0 0 0 10',
                    handler: 'onSearch',
                    iconCls:'iconfont icon-query iconfont-btn-small'
                },
                {
                    xtype: 'button',
                    text: '重置',
                    margin: '0 0 0 10',
                    handler: 'onReset',
                    iconCls:'iconfont icon-reset iconfont-btn-small'
                });
        },
        //Fix navigation bar covered by the dynamic generate component bug.
        afterrender:function(){
            this.ariaEl.dom.style.zIndex=0;
        }
    }
});
