/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseTree', {
    extend: 'Ext.tree.Panel',
    requires:['kalix.controller.BaseTreeGridController'],
    alias: 'widget.baseTree',
    xtype: 'baseTree',
    controller:'baseTreeGridController',
    autoLoad:false,
    singleExpand: true,
    rootVisible : false,
    config:{
        expandId: -1
    },
    listeners:{
        load: function(root) {
            var nodeExpand = root.getNodeById(this.config.expandId);

            if (nodeExpand) {
                if(nodeExpand.data.leaf){
                    nodeExpand.parentNode.expand();
                }
                else{
                    nodeExpand.expand();
                }
            }
        },
        beforeitemmouseup:function( target, record ){
            if(record.data.expanded){
                if(this.config.expandId!=record.id)
                {
                    this.config.expandId=record.parentNode.id
                }
            }
            else{
                if(!record.data.leaf){
                    this.config.expandId= record.data.id
                }
            }
        }
    }
});