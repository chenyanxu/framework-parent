package com.kalix.framework.core.api.persistence;

/**
 * @类描述：${INPUT}
 * @创建人： sunlingfeng
 * @创建时间：2014/9/9
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface WorkflowStaus {
    //INACTIVE("未申请"), ACTIVE("处理中"), FINISH("结束");
    //INACTIVE, ACTIVE, FINISH;
    public static short INACTIVE = 0;//未申请
    public static short ACTIVE = 1;//处理中
    public static short FINISH = 2;//结束
    //private String statusCode;

    // WorkflowStaus(String s) {
    //    statusCode = s;
    //}

    //public String getStatusCode() {
    //   return statusCode;
    //}

    //@Override
    //public String toString() {
    //     return "111";
    // }
}
