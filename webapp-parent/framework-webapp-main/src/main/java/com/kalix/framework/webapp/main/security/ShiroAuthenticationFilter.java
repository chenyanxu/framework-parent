package com.kalix.framework.webapp.main.security;

import com.kalix.framework.core.security.authc.filter.KalixAuthenticationFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

/**
 * Created by chenyanxu on 2017/3/21.
 */
public class ShiroAuthenticationFilter extends KalixAuthenticationFilter {
    @Override
    public String getToken() {
        Session session = SecurityUtils.getSubject().getSession();
        return String.valueOf(session.getId());
    }
}
