package com.kalix.framework.core.mq.redis;

public interface Callback {
    public void onMessage(String message);
}
