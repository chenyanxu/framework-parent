Ext.define('kalix.validator.Presence', {
    extend: 'Ext.data.validator.Validator',
    alias: 'data.validator.presence',

    type: 'presence',

    config: {
        /**
         * @cfg {String} message
         * The error message to return when the value is not specified.
         */
        message: '必填项',

        /**
         * @cfg {Boolean} allowEmpty
         * `true` to allow `''` as a valid value.
         */
        allowEmpty: false
    },
    validate: function (value) {
        var valid = !(value === undefined || value === null);
        if (valid && !this.getAllowEmpty()) {
            valid = !(value === '');
        }
        return valid ? true : this.getMessage();
    }
});