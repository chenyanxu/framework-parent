/**
 * 用户修改个人信息、密码模型
 */

Ext.define('kalix.model.UpdateUserModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {
            name: 'loginName',
            validators: [{type: 'presence'}]
        }, {
            name: 'password',
            validators: [{type: 'presence'}]
        },
        {
            name: 'confirmPassword'
        },
        {
            name: 'name',
            validators: [{type: 'presence'}]
        },
        {
            name: 'position'
        },
        {
            name: 'email',
            validators: [{type: 'presence'}, {type: 'mail'}]
        },
        {
            name: 'phone',
        },
        {
            name: 'mobile',
            validators: [{type: 'presence'}, {type: 'mobile'}]
        },
        {
            name: 'loginIp'
        },
        {
            name: 'is_ent_user',
            type: 'int'
        },
        {
            name: 'org'
        },
        {
            name: 'duty'
        },
        {
            name: 'role'
        },
        {
            name: 'workGroup'
        },
        {
            name: 'availableOptions',
            defaultValue: [
                ['1', '启用'],
                ['0', '停用']
            ]
        },
        {
            name: 'available',
            type: 'string',
            defaultValue: '1'
        },
        {
            name: 'availableText'
        }
    ]
});
