/**
 *  base model of all workflow business model
 *  @author sunlf
 *
 */
Ext.define('kalix.model.WorkflowBaseModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {
            name: 'status' //工作流状态
        },
        {
            name: 'currentNode' //当前节点
        },
        {
            name: 'processInstanceId' //流程实例id
        },
        {
            name: 'auditResult' //审批最终结果
        }
    ]
});
