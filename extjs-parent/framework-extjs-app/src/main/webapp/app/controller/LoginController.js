/**
 * 登陆视图控制器
 *
 * date:2015-10-23

 ext 要求 Ext.form.Panel 组件提交时需要服务端返回一个 json 类型的响应
 其中必须包含 success 字段，否则会认为请求失败，即使相应的 http 代码为 200
 而且，如果 success 的值为 false，也会认为请求失败，会调用 failure 方法

 这种前后端耦合度太高了，简直是 ...
 这里面主要的问题是作为客户端的 ext 强制要求服务端响应的内容，而不是客户端去适应服务器
 如果服务端不符合 ext 要求的接口规范，那么需要更改服务端 api
 如果是一对一的项目没有太大的问题，
 而如果服务端是一个公共服务接口，那么要求更改服务端 api 基本是不可能的

 另外 failure 方法调用判定的设计违反常规，200响应码居然都会被认为请求失败

 解决方案
 不使用默认的 submit 方法，自己实现客户端表单提交逻辑，将表单提交作为通用的异步请求来处理

 */
Ext.define('kalix.controller.LoginController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loginController',
    //处理回车键提交
    onKeyup: function (event, el) {
        if (event.getKey() == event.ENTER) {
            el.blur();
            this.onLogin();
        }
    },

    onLogin: function () {
        var form = Ext.getCmp('loginForm').getForm();

        if (form.isValid()) {
            form.submit({
                success: function (form, action) {
                    var resp = action.result;
                    if (resp.location) {
                        var urlSplit=window.location.href.split('#');
                        var anchor='';

                        if(urlSplit.length==2) {
                            anchor='#'+urlSplit[1];
                        }

                        window.location.href = resp.location+anchor;
                    }
                },
                failure: function (form, action) {
                    if (action.response.status == 200) {
                        Ext.MessageBox.alert(CONFIG.ALTER_TITLE_FAILURE, action.result.message);
                    } else {
                        Ext.MessageBox.alert(CONFIG.ALTER_TITLE_FAILURE, '您的请求太复杂了，我们正在寻找解决方案...');
                    }
                    Ext.MessageBox.focus();
                }
            });
        } else {
            console.log('show tip');
        }
    },
    onReset: function () {
        this.getView().getForm().reset();
    }
})
;
