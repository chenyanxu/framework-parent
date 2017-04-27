/**
 *  base model of all business model
 *  @author chenyanxu
 *
 */
Ext.define('kalix.model.BaseModel', {
  extend: 'Ext.data.Model',
  requires: [
    'kalix.validator.Presence',
    'kalix.validator.Length',
    'kalix.validator.Mail',
    'kalix.validator.Mobile'
  ],
  constructor: function () {
    this.callParent(arguments);

    if (arguments.length == 0) {
      this.set('id', 0);

      var dateFields = this.fields.filter(function (item) {
        return item.type == 'date';
      });

      for (var fIndex = 0; fIndex < dateFields.length; ++fIndex) {
        var tempDate = this.get(dateFields[fIndex].name);

        if (tempDate) {
          tempDate.setSeconds(0);
          tempDate.setMilliseconds(0);
        }
      }

      this.modified = {};
      this.dirty = false;
    }
  },
  set: function () {
    var rtn = this.callParent(arguments);

    if (this.vm) {
      this.vm.set('dirty', this.dirty);
    }

    return rtn;
  },
  fields: [
    {
      name: 'id'
    },
    {
      name: 'version'
    },
    {
      name: 'createBy'
    },
    {
      name: 'creationDate'
    },
    {
      name: 'updateBy'
    },
    {
      name: 'updateDate'
    }
  ]
});
