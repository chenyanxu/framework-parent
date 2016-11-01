package com.kalix.framework.core.api.exception;

/**
 * kalix 异常基类
 */
public class KalixRuntimeException extends RuntimeException {
    private String content; //系统返回的错误描述

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 构造函数
     *
     * @param content
     * @param message
     */
    public KalixRuntimeException(String content, String message) {
        super(message);
        this.content = content;
    }

    /**
     * 不允许构造无参数的类
     */
    private KalixRuntimeException() {

    }
}
