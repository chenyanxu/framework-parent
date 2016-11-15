package com.kalix.framework.core.api.exception;

/**
 * kalix 异常基类
 */
public class KalixRuntimeException extends RuntimeException {
    private String detailMsg; //系统返回的详细错误描述

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    /**
     * 构造函数
     *
     * @param detailMsg
     * @param message
     */
    public KalixRuntimeException(String message, String detailMsg) {
        super(message);
        this.detailMsg = detailMsg;
    }

    /**
     * 不允许构造无参数的类
     */
    private KalixRuntimeException() {

    }
}
