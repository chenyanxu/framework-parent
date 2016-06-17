package com.kalix.framework.core.api.system;

import com.kalix.framework.core.api.IService;

/**
 * Created by sunlf on 2016-02-26.
 * 堆栈服务接口类
 */
public interface IStackService extends IService {
    /**
     * 发布消息到队列
     *
     * @param topic   topic
     * @param msgJson JSON 格式的消息
     * @param seconds 超时时间,传0为一直有效
     */
    void publish(String topic, String msgJson, int seconds);
    /**
     * 读取消息队列
     * @param topic   topic
     * @return JSON String
     * */
    String consume(String topic);
}
