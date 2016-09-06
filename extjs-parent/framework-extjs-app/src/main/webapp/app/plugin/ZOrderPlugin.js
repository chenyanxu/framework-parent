/**
 * Fix navigation bar covered by the dynamic generate component bug.
 * @author chenyanxu
 */
Ext.define('kalix.plugin.ZOrderPlugin', {
  extend: 'Ext.AbstractPlugin',
  alias: 'plugin.zorderPlugin',
  init: function (container) {
    this.callParent(arguments);
    this.cmp.on({
      afterrender: function (target) {
        this.ariaEl.dom.style.zIndex = 0;
      }
    });
  },
  destroy: function () {
  }
});
