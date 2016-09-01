/**
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.BaseTree', {
    extend: 'Ext.tree.Panel',
    requires:['kalix.controller.BaseTreeController'],
    alias: 'widget.baseTree',
    xtype: 'baseTree',
    controller:'baseTreeController',
    autoLoad:false,
    singleExpand: true,
    rootVisible : false,
    config:{
        expandId: -1
    },
    listeners:{
        load: function(root) {
            if(this.config.expandId!=-1){
                var nodeExpand = root.getNodeById(this.config.expandId);

                if (nodeExpand) {
                    if(nodeExpand.data.leaf){
                        nodeExpand.parentNode.expand();
                    }
                    else{
                        nodeExpand.expand();
                    }
                }
            }
            else{
                //auto expand the first child of the root when expandId not assigned
                var childNodes=this.getRootNode().childNodes;

                if(childNodes.length==1){
                    childNodes[0].expand();
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