//app 主窗口容器

Ext.define('kalix.viewmodel.BaseViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.baseViewModel',
    data: {
        addIconCls:'iconfont icon-add',
        editIconCls: 'iconfont icon-edit',
        viewIconCls:'iconfont icon-view',
        addTitle:'添加',
        editTitle:'修改',
        viewTitle:'查看',
        rec:null,
        view_operation:false
    }
});