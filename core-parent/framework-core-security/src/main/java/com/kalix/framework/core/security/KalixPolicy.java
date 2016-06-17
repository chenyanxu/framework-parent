package com.kalix.framework.core.security;

import org.apache.camel.component.shiro.security.ShiroSecurityPolicy;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;

/**
 * Created by sunlf on 2015/7/11.
 */
public class KalixPolicy extends ShiroSecurityPolicy {
    private org.apache.shiro.mgt.SecurityManager securityManager;

    @Override
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void init() {
        SecurityUtils.setSecurityManager(securityManager);
        setAlwaysReauthenticate(false);
        setBase64(true);
    }
}
