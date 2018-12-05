package com.kalix.framework.core.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class CustomSessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {
        System.out.println("session 创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        System.out.println("session 停止：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("session 超时：" + session.getId());
    }
}
