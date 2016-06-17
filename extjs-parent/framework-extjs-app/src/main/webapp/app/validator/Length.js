Ext.define('kalix.validator.Length', {
    extend: 'Ext.data.validator.Bound',
    alias: 'data.validator.length',

    type: 'length',

    config: {
        /**
         * @cfg {Number} min
         * The minimum length value.
         */

        /**
         * @cfg {Number} max
         * The maximum length value.
         */

        /**
         * @cfg {String} minOnlyMessage
         * The error message to return when the value is less than the minimum
         * length and only a minimum is specified.
         */
        minOnlyMessage: '长度应不少于 {0}',

        /**
         * @cfg {String} maxOnlyMessage
         * The error message to return when the value is more than the maximum
         * length and only a maximum is specified.
         */
        maxOnlyMessage: '长度应不大于 {0}',

        /**
         * @cfg {String} bothMessage
         * The error message to return when the value length is not in the specified
         * range and both the minimum and maximum are specified.
         */
        bothMessage: '长度范围：{0}到{1}'
    },

    getValue: function (v) {
        return String(v).length;
    }
});
