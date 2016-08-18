/**
 * 主控制器
 *
 * date:2015-10-26
 */

Ext.define('kalix.controller.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.mainController',

    listen: {
        controller: {
            '#': {
                unmatchedroute: 'onRouteChange'
            }
        }
    },
    setCurrentView: function (hashTag) {
        var appName;

        if(hashTag.path.length>0){
            appName=hashTag.path[0];
            if(appName=='workflow'){
                appName='oa';
            }
            else if(appName=='sys'){
                appName='admin';
            }
        }

        var navTreeSelection=this.getReferences().navigationTreeList.getSelection();

        if(navTreeSelection){
            CONFIG.routePath=[appName,navTreeSelection.parentNode.id,navTreeSelection.id];
        }

        hashTag = hashTag || '';

        for (var idx = 0; idx < hashTag.path.length; ++idx) {
            hashTag.path[idx] = hashTag.path[idx].substr(0, 1).toLowerCase() + hashTag.path[idx].substr(1);
        }

        hashTag = hashTag.path.join('.');
        console.log(hashTag)

        var mainCard = this.getReferences().mainCardPanel;
        var mainLayout = mainCard.getLayout();
        var lastView = this.getViewModel().getData().currentView;
        var existingItem = mainCard.child('component[routeId=' + hashTag + ']');
        var newView;

        // Kill any previously routed window
        if (lastView && lastView.isWindow) {
            lastView.destroy();
        }

        lastView = mainLayout.getActiveItem();
        if (!existingItem) {
            //newView = Ext.create('kalix.' + (hashTag.toLowerCase() || 'pages.Error404Window') + '.Main', {
            newView = Ext.create('kalix.' + (hashTag || 'pages.Error404Window') + '.Main', {

                hideMode: 'offsets',
                routeId: hashTag
            });
        }

        if (!newView || !newView.isWindow) {
            // !newView means we have an existing view, but if the newView isWindow
            // we don't add it to the card layout.
            if (existingItem) {
                // We don't have a newView, so activate the existing view.
                if (existingItem !== lastView) {
                    mainLayout.setActiveItem(existingItem);
                }
                newView = existingItem;
                var currentView=newView.items.getAt(existingItem.items.length - 1);
                if (currentView!=null && currentView instanceof Ext.grid.Panel)
                    currentView.getStore().reload();
            } else {
                // newView is set (did not exist already), so add it and make it the
                // activeItem.
                Ext.suspendLayouts();
                mainLayout.setActiveItem(mainCard.add(newView));
                Ext.resumeLayouts(true);
            }
        }

        //var node = Ext.getStore('NavigationTree').findNode('routeId', hashTag);

        //this.getReferences().navigationTreeList.setSelection(node);

        if (newView.isFocusable(true)) {
            newView.focus();
        }
    },

    onToggleNavigationSize: function () {
        var me = this,
            refs = me.getReferences(),
            navigationList = refs.navigationTreeList,
            wrapContainer = refs.mainContainerWrap,
            collapsing = !navigationList.getMicro(),
            new_width = collapsing ? 64 : 250;

        if (Ext.isIE9m || !Ext.os.is.Desktop) {
            Ext.suspendLayouts();

            refs.senchaLogo.setWidth(new_width);

            navigationList.setWidth(new_width);
            navigationList.setMicro(collapsing);

            Ext.resumeLayouts(); // do not flush the layout here...

            // No animation for IE9 or lower...
            wrapContainer.layout.animatePolicy = wrapContainer.layout.animate = null;
            wrapContainer.updateLayout(); // ... since this will flush them
        } else {
            if (!collapsing) {
                // If we are leaving micro mode (expanding), we do that first so that the
                // text of the items in the navlist will be revealed by the animation.
                navigationList.setMicro(false);
            }

            // Start this layout first since it does not require a layout
            refs.senchaLogo.animate({
                dynamic: true,
                to: {
                    width: new_width
                }
            });

            // Directly adjust the width config and then run the main wrap container layout
            // as the root layout (it and its chidren). This will cause the adjusted size to
            // be flushed to the element and animate to that new size.
            navigationList.width = new_width;
            wrapContainer.updateLayout({
                isRoot: true
            });

            // We need to switch to micro mode on the navlist *after* the animation (this
            // allows the "sweep" to leave the item text in place until it is no longer
            // visible.
            if (collapsing) {
                navigationList.on({
                    afterlayoutanimation: function () {
                        navigationList.setMicro(true);
                    },
                    single: true
                });
            }
        }
    },
    onNavigationTreeSelectionChange: function (tree, node) {
        if (node) {
            var applicationId = node.parentNode.get('applicationId');
            var routeId = node.get("routeId");

            this.redirectTo(applicationId + '/' + routeId.split('/')[1]);
        }
    },
    onRouteChange: function (hash) {
        var toolbarStore = Ext.getStore('mainToolbarStore');
        var treeStore = Ext.getStore('navigationTreeStore');

        if ('' == hash) {
            toolbarStore.load({
                scope: this, callback: function (records, operation, success) {
                    if (records.length > 0) {
                        this.redirectTo(records[0].id);
                    }
                }
            });

            return;
        }


        hash = this.parseHash(hash);
        toolbarStore.load();
        //do some init for menu refresh when the anchor like #app/menu
        if (hash.path.length > 1) {
            treeStore.treeSelInfo.selected = true;
            treeStore.treeSelInfo.level1 = hash.path[0];
            treeStore.treeSelInfo.level2 = hash.path[1];
            //if the menu tree has been selected,we got the routeId from the
            //select tree item binging module.
            if (treeStore.treeSelInfo.tree != null) {
                treeStore.load({hashToken: this.getFirstPath(hash)});
                hash.path = treeStore.treeSelInfo.tree.getSelection().get('routeId').split('/');
                this.setCurrentView(hash);
            }
        }
        else {
            treeStore.load({hashToken: this.getFirstPath(hash)});
            this.setCurrentView(hash);
            treeStore.treeSelInfo.selected = false;
        }
        //if the tree menu isn't selected and url anchor like #app/menu.
        //it must in the situation when we refresh the page.
        //in this case,the tree need to be auto selected at right place.
        //we couldn't get the tree selection item because the select status
        //will lost after page reload. so we need get the info from the async
        //treeStore load.
        if (hash.path.length > 1 && treeStore.treeSelInfo.tree == null) {
            treeStore.load({
                    scope: this,
                    hashToken: this.getFirstPath(hash),
                    callback: function (records, operation, success) {
                        var hasFind = false;

                        for (var pidx = 0; pidx < records.length; ++pidx) {
                            var children = records[pidx].get('children')

                            for (var cidx = 0; cidx < children.length; ++cidx) {
                                var routeId = children[cidx].routeId;

                                if (routeId && hash.path[1] == routeId.split('/')[1]) {
                                    hash.path = routeId.split('/');
                                    this.setCurrentView(hash);
                                    hasFind = true;
                                    break;
                                }
                            }

                            if (hasFind) {
                                break;
                            }
                        }
                    }
                }
            );
        }
    },

    getFirstPath: function (hashObj) {
        return hashObj.path[0];
    },

    // admin/deashoard?name=soni
    parseHash: function (hash) {
        var ary = hash.split('?');
        var path = ary[0];
        var query = '';
        if (ary.length > 1) {
            query = ary[1];
        }
        return {
            path: path.split('/'),
            query: this.parseQuery(query)
        };
    },

    parseQuery: function (query) {
        if (!query) {
            return query;
        }
        var querys = query.split('&');
        return querys.reduce(function (previousValue, currentValue) {
            var ary = currentValue.split('=');
            var key = ary[0];
            var value = ary[1];
            if (previousValue[key] !== undefined) {
                if (Array.isArray(previousValue[key])) {
                    previousValue[key].push(value);
                } else {
                    previousValue[key] = [previousValue[key], value];
                }
            } else {
                previousValue[key] = value;
            }

            return previousValue;
        }, {});
    },
    afterrender: function () {
        //var scope = this;
        //
        //Ext.Ajax.request({
        //    url: 'camel/rest/system/pollings',
        //
        //    success: function (response, opts) {
        //        var obj = Ext.decode(response.responseText);
        //        if (obj != null && obj.length > 0) {
        //            for (var i = 0; i < obj.length; i++) {
        //                Ext.direct.Manager.addProvider(Ext.create('Ext.direct.PollingProvider',
        //                    {
        //                        type: obj[i].type,
        //                        url: obj[i].url,
        //                        interval: obj[i].interval,
        //                        id: obj[i].id
        //                    }
        //                ));
        //
        //                var poll = Ext.direct.Manager.getProvider(obj[i].id);
        //
        //                poll.on('data', scope[obj[i].callbackHandler]);
        //
        //                if (obj[i].isStop) {
        //                    poll.disconnect();
        //                }
        //            }
        //        }
        //    },
        //
        //    failure: function (response, opts) {
        //        console.log('server-side failure with status code ' + response.status);
        //    }
        //});
    },
    onWorkflowMsg: function (provider, e, eOpts) {
        var tag = e.data.tag;
        if (tag != null && tag.length > 0) {
            var obj = Ext.decode(tag);
            var content = obj.content;
            var title = obj.title;
            kalix.Notify.success(content, title);
        }
    },

    onWorkflowMsgCount: function (provider, e, eOpts) {
        var cnt = e.data.tag;
        var messageBar = Ext.getCmp('messagebarId');
        messageBar.lookupViewModel().getData().message.set('count', cnt);
        if (cnt == 0)
            messageBar.lookupViewModel().getData().message.set('iconCls', 'x-fa fa-envelope-o');
        else
            messageBar.lookupViewModel().getData().message.set('iconCls', 'x-fa fa-envelope');
    },
    onThemeChange: function (target, newValue, oldValue, eOpts) {
        if (target.firstLoad) {
            target.firstLoad = false;
        }
        else {
            Ext.Ajax.request({
                url: 'camel/rest/system/preferences?key=theme&value=' + newValue,
                method: 'PUT',
                success: function (response, opts) {
                    var obj = Ext.decode(response.responseText);

                    location.reload();
                },
                failure: function (response, opts) {
                    console.log('server-side failure with status code ' + response.status);
                },
                callback: function () {
                }
            });
        }
    },
    //this method called when Navigation Tree Select from a button
    //in different module
    onNavigationSpecial: function (target) {
        if(target&&target.routeId){
            this.getReferences().navigationTreeList.selectTreeItem(target.routeId);
        }
        else{
            this.getReferences().navigationTreeList.selectTreeItem(target);
        }
    }
});
