package com.kalix.framework.core.api.exception;

/**
 * Created by Administrator on 2018-02-05.
 * 授权失败异常类
 */
public class UnAuthException extends KalixRuntimeException {
    /**
     * 构造函数
     *
     * @param message
     * @param detailMsg
     */
    public UnAuthException(String message, String detailMsg) {
        super(message, detailMsg);
    }
}
