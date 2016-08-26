Ext.define('kalix.view.MainTreelist', {
    extend: 'Ext.list.Tree',
    xtype: 'maintreelist',
    store: "navigationTreeStore",
    constructor: function () {
        this.callParent(arguments);

        var navigationTreeStore = this.getStore();
        var self = this;
        var last = [];

        navigationTreeStore.on("beforeload", function () {
            last = [];
            navigationTreeStore.each(function (recorder) {
                last.push(recorder);
            });
            navigationTreeStore.treeSelInfo.tree=this;
        }, this);

        navigationTreeStore.on("load", function () {
            _.each(last, function (item) {
                var destoryItem = self.getItem(item);
                //if the module parent item has been destroy.
                //the child menu item will auto release.
                //this will happen in case that module is expanded.
                if (destoryItem != null) {
                    destoryItem.destroy();
                }
            });

            var hasFind=false;
            //the auto select tree item logic.
            navigationTreeStore.each(function (recorder) {
                var item = self.getItem(recorder);

                if (navigationTreeStore.treeSelInfo.selected) {
                    var childNodes=item.getNode().childNodes;

                    for(var idx=0;idx<childNodes.length;++idx)
                    {
                        var childItem=self.getItem(childNodes[idx]);
                        //we don't care about the app level anchor.
                        //we just match the menu level anchor.
                        //so we can config the menu unit to any module.
                        if(childItem.getNode().getData().routeId.split('/')[1]==navigationTreeStore.treeSelInfo.level2){
                            item.expand();
                            self.setSelection(childItem.getNode());
                            hasFind=true;
                            break;
                        }
                    }

                    if(hasFind)
                    {
                        return false;
                    }
                }
            });

        }, this);
    },
    //The routeId:
    //module/menu
    //menu
    selectTreeItem:function(routeId){
        console.log(routeId);

        if(routeId&&(routeId.split('/').length==2||routeId.split('/').length==1)){
            var routeSplit=routeId.split('/');
            var menuRouteId='';

            if(routeSplit.length==2){
                menuRouteId=routeSplit[1].toLowerCase();
            }
            else{
                menuRouteId=routeId.toLowerCase();;
            }

            for(var levelIndex1=0;levelIndex1<this.rootItem._node.childNodes.length;++levelIndex1){
                var hasFind=false;
                var node=this.rootItem._node.childNodes[levelIndex1];

                for(var levelIndex2=0;levelIndex2<node.childNodes.length;++levelIndex2){
                    if(node.childNodes[levelIndex2].get('routeId').split('/')[1].toLowerCase()==menuRouteId){
                        hasFind=true;

                        var treeItem=this.getItem(node);

                        treeItem.expand();
                        this.setSelection(node.childNodes[levelIndex2]);

                        break;
                    }
                }

                if(hasFind){
                    break;
                }
            }
        }
    }
});
