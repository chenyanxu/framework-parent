/**
 * 自定义formPanel，项目中的form必须扩展该类
 *         date:2015-10-16
 * @version 1.0.0
 */
Ext.define('kalix.view.components.common.TableFormPanel', {
    extend: 'Ext.form.Panel',
    xtype: 'baseTableForm',
    bodyStyle: 'border-color:black;border-width:1px 1px 0px 1px',
    padding: 1,
    listeners: {
        beforerender: function () {
            var parentWidth = this.findParentByType('container').width;

            for (var itemIndex = 0; itemIndex < this.items.getCount(); ++itemIndex) {
                var tempItem = this.items.getAt(itemIndex);

                if (tempItem.colspan) {
                    tempItem.setWidth(parentWidth * tempItem.colspan / this.layout.columns);
                }
                else {
                    tempItem.setWidth(parentWidth / this.layout.columns);
                }

                if (tempItem.customStyle) {
                    tempItem.setBodyStyle(tempItem.bodyStyle + 'text-align:center;border-color:black;border-width:0px 1px 1px 0px');
                }
                else {
                    if (tempItem.config.html) {
                        tempItem.setBodyStyle('padding:10px;font-size:18px;font-weight:bold;text-align:center;border-color:black;border-width:0px 1px 1px 0px');
                    }
                    else {
                        tempItem.setBodyStyle('padding:5px;font-size:18px;text-align:center;border-color:black;border-width:0px 1px 1px 0px');
                    }
                }


                if (tempItem.config.html && tempItem.config.html.indexOf('<br') > 0) {
                    tempItem.setBodyStyle('padding:10px;font-size:18px;text-align:center;border-color:black;border-width:0px 1px 1px 0px');
                }

                if (tempItem.required) {
                    tempItem.html = tempItem.html + '<span style="color:red">*</span>';
                }

                if (tempItem.readOnly) {
                    tempItem.html = '<span style="color:#BFBFBF">' + tempItem.html + '</span>';
                }

                tempItem.setHeight(40);

                if (tempItem.items.length == 1) {
                    tempItem.setLayout('fit');
                }
            }
        }
    }
});
