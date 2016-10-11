/** for attachment upload
 * @aurthor author
 */
Ext.define('kalix.view.UploadIcon', {
    extend: 'Ext.form.Panel',
    requires: ['kalix.attachment.view.AttachmentFileField'],
    alias: 'widget.uploadIcon',
    xtype: 'uploadIcon',
    frame: true,
    timeout: 60,
    height:20,
    items: [
        {
            xtype: 'attachmentFileField',
            width:60,
            buttonOnly:true,
            buttonText: '上传',
            name: 'attachment',
            listeners:{
                change:function(target,value, eOpts){
                    target.findParentByType('updateUserInfoWindow').getController().onChange(target, value, eOpts);
                }
            }
        }
    ],
    listeners:{
        afterrender: function (target) {
            target.ariaEl.dom.style.border='none';
        }
    }
});/**
 * Created by Administrator on 2016/10/10.
 */
