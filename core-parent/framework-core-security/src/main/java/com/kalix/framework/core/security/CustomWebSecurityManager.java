package com.kalix.framework.core.security;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import java.util.List;

public class CustomWebSecurityManager extends DefaultWebSecurityManager {

//    private CustomSessionManager sessionManager;
//
//    public CustomWebSecurityManager() {
//        sessionManager = (CustomSessionManager)getSessionManager();
//    }

    public List<String> sessionExpires() {
        CustomSessionManager sessionManager = (CustomSessionManager)getSessionManager();
        return sessionManager.getSessionExpires();
    }

    public void removeSessionExpire(String sessionId) {
        CustomSessionManager sessionManager = (CustomSessionManager)getSessionManager();
        sessionManager.removeSessionExpire(sessionId);
    }
}
