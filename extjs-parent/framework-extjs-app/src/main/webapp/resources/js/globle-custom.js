/**
 * 全局定义
 *
 * @author chenyanxu
 */
//全局配置
var CONFIG = {
  ALTER_TITLE_SUCCESS: "成功信息",
  ALTER_TITLE_FAILURE: "失败信息",
  ALTER_TITLE_ERROR: "错误信息",
  ALTER_TITLE_INFO: "提示信息",
  restRoot: '',
  theme: 'theme-triton',
  routePath: [], //record the temp route path
  currentUserLoginName:null,
  JSESSIONID:null,
  access_token:null,
  timestamp:Date.now()
};
//define the var to avoid the js error about Ext no defined when ext-all.js not load
var Ext;

if (Ext) {
//Ext附加函数
//change a json obj to key value array
  Ext.JSON.toArray = function (obj) {
    var rtn = [];

    for (var key in obj) {
      rtn[rtn.length] = {key: key, value: obj[key]};
    }

    return rtn;
  };
//judge whether the theme type is classic
  Ext.theme = {
    isClassic: function () {
      switch (CONFIG.theme) {
        case 'theme-classic':
          return true;
        case 'theme-gray':
          return true;
        default:
          return false;
      }
    }
  }
//Ext函数重写
  Ext.override(Ext.JSON, {
    encodeDate: function (d) {
      return Ext.Date.format(d, '"Y-m-d H:i:s"');
    }
  });
//override the render function: edit the zIndex of the class extend from panel.
//Fix navigation bar covered by the dynamic generate component bug.
  Ext.override(Ext.panel.Panel, {
    render: function () {
      this.callParent(arguments);
      this.ariaEl.dom.style.zIndex = 0;
    }
  });

  Ext.override(Ext.window.MessageBox, {
    hide: function () {
      this.callParent(arguments);

      if (this.dockedItems.getAt(1).items.length == 5) {
        var btn = this.dockedItems.getAt(1).items.getAt(4);

        btn.setVisible(false);
      }
    },
    alert: function (title, message, fn, scope) {
      if (Ext.isString(title)) {
        title = {
          title: title,
          message: typeof(message) == 'object' ? message.msg : message,
          buttons: this.OK,
          fn: fn,
          scope: scope,
          minWidth: this.minWidth
        };
      }

      if (typeof(message) == 'object') {
        this.tag = message;

        var dockedItems = this.dockedItems;
        var bbar = dockedItems.getAt(dockedItems.length - 1);
        if (bbar.items.length == 4) {
          this.dockedItems.getAt(dockedItems.length - 1).add({
            xtype: 'button', text: '详情', handler: function () {
              var tag = this.findParentByType('messagebox').tag;

              if (tag && tag.detail) {
                alert(tag.detail);
              } else {
                alert('无详细信息');
              }
            }
          });
        }
        else if (bbar.items.length == 5) {
          var btn = bbar.items.getAt(4);

          btn.setVisible(true);
        }
      }
      else {
        this.tag = undefined;
      }

      return this.show(title);
    }
  });

  Ext.override(Ext.form.field.Text,{
    onKeyUp: function(e) {
      this.fireEvent('keyup', this, e);

      if(e.keyCode==13){
        var parentComp=this.findParentByType('baseSearchForm');

        if(parentComp){
          parentComp.fireEvent("customEntryKeyUp");
        }
      }
    }
  });
}

//add method to array array when the browser not support
if ( !Array.prototype.forEach ) {

  Array.prototype.forEach = function forEach( callback, thisArg ) {

    var T, k;

    if ( this == null ) {
      throw new TypeError( "this is null or not defined" );
    }
    var O = Object(this);
    var len = O.length >>> 0;
    if ( typeof callback !== "function" ) {
      throw new TypeError( callback + " is not a function" );
    }
    if ( arguments.length > 1 ) {
      T = thisArg;
    }
    k = 0;

    while( k < len ) {

      var kValue;
      if ( k in O ) {

        kValue = O[ k ];
        callback.call( T, kValue, k, O );
      }
      k++;
    }
  };
}

if (!Array.prototype.map) {
  Array.prototype.map = function(callback, thisArg) {

    var T, A, k;

    if (this == null) {
      throw new TypeError(" this is null or not defined");
    }

    // 1. Let O be the result of calling ToObject passing the |this| value as the argument.
    var O = Object(this);

    // 2. Let lenValue be the result of calling the Get internal method of O with the argument "length".
    // 3. Let len be ToUint32(lenValue).
    var len = O.length >>> 0;

    // 4. If IsCallable(callback) is false, throw a TypeError exception.
    // See: http://es5.github.com/#x9.11
    if (typeof callback !== "function") {
      throw new TypeError(callback + " is not a function");
    }

    // 5. If thisArg was supplied, let T be thisArg; else let T be undefined.
    if (thisArg) {
      T = thisArg;
    }

    // 6. Let A be a new array created as if by the expression new Array(len) where Array is
    // the standard built-in constructor with that name and len is the value of len.
    A = new Array(len);

    // 7. Let k be 0
    k = 0;

    // 8. Repeat, while k < len
    while(k < len) {

      var kValue, mappedValue;

      // a. Let Pk be ToString(k).
      //   This is implicit for LHS operands of the in operator
      // b. Let kPresent be the result of calling the HasProperty internal method of O with argument Pk.
      //   This step can be combined with c
      // c. If kPresent is true, then
      if (k in O) {

        // i. Let kValue be the result of calling the Get internal method of O with argument Pk.
        kValue = O[ k ];

        // ii. Let mappedValue be the result of calling the Call internal method of callback
        // with T as the this value and argument list containing kValue, k, and O.
        mappedValue = callback.call(T, kValue, k, O);

        // iii. Call the DefineOwnProperty internal method of A with arguments
        // Pk, Property Descriptor {Value: mappedValue, : true, Enumerable: true, Configurable: true},
        // and false.

        // In browsers that support Object.defineProperty, use the following:
        // Object.defineProperty(A, Pk, { value: mappedValue, writable: true, enumerable: true, configurable: true });

        // For best browser support, use the following:
        A[ k ] = mappedValue;
      }
      // d. Increase k by 1.
      k++;
    }

    // 9. return A
    return A;
  };
}

if (!Array.prototype.filter) {
  Array.prototype.filter = function(fun /*, thisp*/){
    var len = this.length;
    if (typeof fun != "function"){
      throw new TypeError();
    }
    var res = new Array();
    var thisp = arguments[1];
    for (var i = 0; i < len; i++){
      if (i in this){
        var val = this[i]; // in case fun mutates this
        if (fun.call(thisp, val, i, this)) {
          res.push(val);
        }
      }
    }
    return res;
  };
}
