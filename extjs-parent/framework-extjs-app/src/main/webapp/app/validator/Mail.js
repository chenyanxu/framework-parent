/**
 * Validates that the value is a valid email.
 */
Ext.define('kalix.validator.Mail', {
  extend: 'Ext.data.validator.Format',
  alias: 'data.validator.mail',

  type: 'mail',

  config: {
    /**
     * @cfg {String} message
     * The error message to return when the value is not a valid email
     */
    message: '邮箱格式无效',

    // http://en.wikipedia.org/wiki/Email_address#Local_part
    // http://stackoverflow.com/a/2049510
    // http://isemail.info/
    // http://blog.stevenlevithan.com/archives/capturing-vs-non-capturing-groups
    //
    // 1. Can begin with a double-quote ONLY IF the local part also ends in a double-quote.
    // 2. Can NOT BEGIN with a period.
    // 3. Can NOT END with a period.
    // 4. Can not have MORE THAN ONE period in a row.
    //
    // Let's break this down:
    //
    // ^(")?
    // The local part may begin with double-quotes, but only if it also ends with it.
    // See the back-reference.  Capturing.
    //
    // (?:[^\."])
    // Here we've defined that the local part cannot begin with a period or a double-quote.  Non-capturing.
    //
    // (?:(?:[\.])?(?:[\w\-!#$%&'*+/=?^_`{|}~]))*
    // After the first character is matched, the regex ensures that there is not more than one period
    // in a row.  Then, this nested grouping allows for zero or more of the accepted characters.
    // NOTE that this also ensures that any character not defined in the character class
    // is invalid as an ending character for the local part (such as the period).
    //
    // \1@
    // The local part of the address is a backreference to the first (and only) capturing group that allows
    // for a double-quote to wrap the local part of an email address.
    /**
     * @cfg {RegExp} matcher
     * A matcher to check for simple emails. This may be overridden.
     */
    matcher: /^(")?(?:[^\."])(?:(?:[\.])?(?:[\w\-!#$%&'*+\/=?\^_`{|}~]))*\1@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/
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
