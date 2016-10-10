/**
 *  base model of all workflow business model
 *  @author sunlf
 *
 */
Ext.define('kalix.model.WorkflowBaseModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {
            name: 'title' //标题
        },
        {
            name: 'businessNo' //业务编号
        },
        {
            name: 'orgId', //组织机构Id
            validators: [{type: 'presence'}]
        },
        {
            name: 'orgName' //组织机构名称
        },
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
