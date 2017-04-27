Ext.define('kalix.view.components.common.BaseImage', {
  extend: 'Ext.Img',
  alias: 'widget.baseImage',
  xtype: 'baseImage',
  requires: ['kalix.Notify'],

  src: 'resources/images/default_user.png',

  onRender: function () {
    var me = this;
    me.callParent(arguments);
    me.imgEl.dom.onerror = me.onImageLoadError;
    var icon = this.findParentByType('updateUserInfoWindow').viewModel.get('rec').get('icon');
    if (icon != null && icon != '') {
      me.setSrc(icon);
    }
  },

  onImageLoadError: function () {
    if (this.src != '') {
      kalix.Notify.error('无法连接数据库！！！', CONFIG.ALTER_TITLE_ERROR);
    }

    if (this.title != '') {
      this.src = this.title;
    }
  },

  onDestroy: function () {
    var me = this;
    me.imgEl.dom.onerror = null;
    me.callParent(arguments);
  }
});
/**
 * Created by Administrator on 2016/10/11.
 */
