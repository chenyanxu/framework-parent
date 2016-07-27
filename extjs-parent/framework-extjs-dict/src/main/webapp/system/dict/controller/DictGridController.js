/**
 * 字典表格控制器
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.dict.controller.DictGridController', {
    extend: 'kalix.controller.BaseGridController',
    alias: 'controller.dictGridController',
    viewModelExtraInit:function(vm){
       if(0==vm.get('rec').id){
           vm.set('add_operation',true);
       }
       else {
           vm.set('add_operation',false);
       }
    }
});