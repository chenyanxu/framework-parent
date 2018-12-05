package com.kalix.framework.core.security.authc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SystemLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
//            ICacheManager cacheManager = OsgiUtil.waitForServices(ICacheManager.class, null);
//            Session session = subject.getSession(false);
//            if (session != null) {
//                String refreshCode = (String)session.getAttribute("refresh_token");
//                if (refreshCode != null && !refreshCode.isEmpty()) {
//                    cacheManager.del(refreshCode);
//                }
//            }
            subject.logout();
        } catch (Exception ise) {
            ise.printStackTrace();
        }
        if (redirectUrl.endsWith("/")) {
            redirectUrl += "login";
        } else {
            redirectUrl += "/login";
        }
        issueRedirect(request, response, redirectUrl);
        return false;
    }
}
