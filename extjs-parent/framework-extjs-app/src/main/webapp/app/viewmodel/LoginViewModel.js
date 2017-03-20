//app 主窗口容器

Ext.define('kalix.viewmodel.LoginViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loginViewModel',
    data: {
        username: '',
        password: '',
        vcode:''
    }
});