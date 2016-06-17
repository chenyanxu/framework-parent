Ext.define('kalix.validator.Mobile', {
    extend: 'Ext.data.validator.Format',
    alias: 'data.validator.mobile',

    type: 'mobile',

    config: {
        message: '手机号码格式不正确',
        matcher: /^1[3,4,5,7,8]\d{9}$/
    },
    validate: function (value) {
        var valid = !(value === undefined || value === null);

        if (!valid || value === '') {
            return true;
        }

        var matcher = this.getMatcher(),
            result = matcher && matcher.test(value);

        return result ? result : this.getMessage();
    }
});