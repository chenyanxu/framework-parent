package com.kalix.framework.core.security.authc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        Subject subject=null;
        if (httpRequest.getHeader("JSESSIONID") != null && !httpRequest.getHeader("JSESSIONID").isEmpty()) {
            subject = getSubject(request, response);
            if(subject.getPrincipal()==null) { //非授权用户
                HttpServletResponse httpResponse= (HttpServletResponse)response;
                httpResponse.sendError(401);
                return false;
            }
        }

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
