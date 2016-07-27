/**
 * 字典模型
 *
 * @author majian <br/>
 *         date:2015-7-9
 * @version 1.0.0
 */
Ext.define('kalix.dict.model.DictModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {name: 'label', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'description', type: 'string'}
    ]
});