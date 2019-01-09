package com.kalix.framework.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomSessionManager extends DefaultWebSessionManager {
    private List<String> sessionExpires = new ArrayList<>();

    public List<String> getSessionExpires() {
        return sessionExpires;
    }

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            Session session = (Session)request.getAttribute(sessionId.toString());
            if (session != null){
                return session;
            }
        }
        // ServletRequest request = WebUtils.getRequest(sessionKey);
        Session session = null;
        try {
            session = super.retrieveSession(sessionKey);
        } catch(Exception e) {
            ServletResponse response = WebUtils.getResponse(sessionKey);
            if (request != null && response != null) {
                try {
                    SecurityUtils.getSubject().logout();
                    WebUtils.issueRedirect(request, response, "/login");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (request != null && session != null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
//        System.out.println("onExpiration=======================");
        sessionExpires.add(s.getId().toString());
    }

    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
//        System.out.println("onInvalidation=======================");
    }

    @Override
    protected void afterExpired(Session session) {
//        boolean isDelete = isDeleteInvalidSessions();
//        System.out.println("---------------" + isDelete);
        super.afterExpired(session);
    }

    public void removeSessionExpire(String sessionId) {
        if(sessionExpires.contains(sessionId)) {
            sessionExpires.remove(sessionId);
        }
    }
}
