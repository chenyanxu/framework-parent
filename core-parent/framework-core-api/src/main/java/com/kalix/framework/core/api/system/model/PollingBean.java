package com.kalix.framework.core.api.system.model;

/**
 * Created by zangyanming on 2016/2/25.
 */
public class PollingBean {
    private String id;//id
    private String type;//类型
    private int interval;//时间间隔 毫秒
    private String url;//地址
    private boolean isStop;//是否停止轮询
    private String callbackHandler;//轮询回调函数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public String getCallbackHandler() {
        return callbackHandler;
    }

    public void setCallbackHandler(String callbackHandler) {
        this.callbackHandler = callbackHandler;
    }
}
