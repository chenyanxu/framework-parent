/**
 * This class is the view model for the Main view of the application.
 */
Ext.define('kalix.viewmodel.MainViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.mainViewModel',

    data: {
        currentView: null,
        theme: CONFIG.theme
    }
});
