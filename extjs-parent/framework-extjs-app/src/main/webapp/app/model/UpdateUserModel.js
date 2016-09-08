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
            name: 'password'
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
            name: 'phone'
        },
        {
            name: 'mobile',
            validators: [{type: 'presence'}, {type: 'mobile'}]
        },
        {
            name: 'loginIp'
        },
        {
            name: 'code',
            type: 'int'
        },
        {
            name: 'sex'
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
        }, {
            name: 'availableText',
            calculate: function (data) {
                var options = [
                    ['1', '启用'],
                    ['0', '停用']
                ];
                var available = data.available;

                return _.find(options, function (item) {
                    return item[0] === available;
                })[1];
            }
        }]
});
