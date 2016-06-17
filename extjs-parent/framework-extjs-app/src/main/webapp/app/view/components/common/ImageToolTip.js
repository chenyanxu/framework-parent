/**the base class for all key/value combox
 * @author chenyanxu
 */
Ext.define('kalix.view.components.common.ImageToolTip', {
    extend: 'Ext.Img',
    alias: 'widget.imageToolTip',
    xtype: 'imageToolTip',
    src:'resources/images/help.png',
    alt:'help',
    style:'cursor:help',
    tooltip:'',
    listeners:{
        render:function(target){
            Ext.create('Ext.tip.ToolTip', {
                target: target.ariaEl.dom,
                html:target.tooltip
            });
        }
    }
});
