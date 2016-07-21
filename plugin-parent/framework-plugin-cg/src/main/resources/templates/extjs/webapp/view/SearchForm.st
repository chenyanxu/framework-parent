/**
 * <moduleDescription>查询表单
 * @author
 * @version 1.0.0
 */
Ext.define('<extjsPrefix>.view.<beanName>SearchForm', {
    extend: 'kalix.view.components.common.BaseSearchForm',
    alias: 'widget.<pomName>SearchForm',
    xtype: '<pomName>SearchForm',
    storeId: '<pomName>Store',
    items: [
        <SearchFormItems>
    ]
});
