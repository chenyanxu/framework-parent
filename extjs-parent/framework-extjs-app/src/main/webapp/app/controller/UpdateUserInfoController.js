/**
 * 主控制器
 *
 * date:2015-10-26
 */

Ext.define('kalix.controller.UpdateUserInfoController', {
    extend: 'kalix.controller.BaseWindowController',
    alias: 'controller.updateUserInfoController',
    requires: ['kalix.Notify'],
    onChange: function (target, event, domValue) {
        var form = target.findParentByType('form');
        var me = target.findParentByType('window');
        var store = me.viewModel.get('store');
        var mainId = me.viewModel.get('rec').id

        scope={mainId:mainId,store:store};

        form.submit({
            url: CONFIG.restRoot + '/camel/rest/upload',
            waitMsg: '正在上传...',
            scope: scope,
            success: function (fp, o) {
                if (o.result.success) {
                    me.viewModel.get('rec').set('icon', o.result.attachmentPath);
                    kalix.Notify.success('头像上传成功！！！', CONFIG.ALTER_TITLE_SUCCESS);
                }
            },
            failure: function(fp, o) {
                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, o.result.msg);
            }
        });
    }
});
