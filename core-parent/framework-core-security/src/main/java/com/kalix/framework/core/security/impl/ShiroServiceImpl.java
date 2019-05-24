package com.kalix.framework.core.security.impl;

import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.exception.UnAuthException;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.security.CustomWebSecurityManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.WebSecurityManager;

import java.util.List;

/**
 * Created by sunlf on 2015/7/23.
 */
public class ShiroServiceImpl implements IShiroService {
    private WebSecurityManager securityManager;
    private Session session;

    public void setSecurityManager(WebSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void init() {
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Override
    public String getCurrentUserRealName() {
        Session session = getSession();
        String rtn = (String) session.getAttribute(PermissionConstant.SYS_CURRENT_USER_REAL_NAME);

        return rtn;
    }

    @Override
    public Long getCurrentUserId() {
        Session session = getSession();
        if (session == null) {
            this.getSubject().logout();
            return null;
        }
        Object userId = session.getAttribute(PermissionConstant.SYS_CURRENT_USER_ID);
        if (userId == null) {
            System.out.println("session 超时");
            this.getSubject().logout();
            // throw new UnAuthException("session overtime", "no detail msg");
            return null;
        }
        Long rtn = Long.valueOf(userId.toString());
        return rtn;
    }

    @Override
    public String getCurrentUserLoginName() {
        Session session = getSession();
        String rtn = (String) session.getAttribute(PermissionConstant.SYS_CURRENT_USER_LOGIN_NAME);

        return rtn;
    }

    @Override
    public Subject getSubject() {
        Subject subject=SecurityUtils.getSubject();
        if (subject.getPrincipal()==null)
            throw new UnknownSessionException();
        return subject;
    }

    @Override
    public Session createSession() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            session = subject.getSession(true);
        }
        return session;
    }

    @Override
    public Session getSession() {
//        if (session == null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                session = subject.getSession(false);
            }
//            if (session==null){
//                session = subject.getSession(true);
//            }
//        }
        return session;
    }

    @Override
    public JsonStatus validSession(String sessionId) {
        JsonStatus jsonStatus = new JsonStatus();
        Subject requestSubject = new Subject.Builder().sessionId(sessionId).buildSubject();
        if (requestSubject.getPrincipal() != null) {
            jsonStatus.setSuccess(true);
            jsonStatus.setFailure(false);
            jsonStatus.setMsg("验证成功！");
        } else {
            jsonStatus.setSuccess(false);
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("验证失败！");
        }
        return jsonStatus;
    }

    public JsonStatus validPermission(String sessionId, String permission) {
        JsonStatus jsonStatus = new JsonStatus();
        Subject requestSubject = new Subject.Builder().sessionId(sessionId).buildSubject();
        if (requestSubject.getPrincipal() != null) {
            if (requestSubject.hasRole(permission)) {
                jsonStatus.setSuccess(true);
                jsonStatus.setFailure(false);
                jsonStatus.setMsg("授权成功！");
            } else {
                jsonStatus.setSuccess(false);
                jsonStatus.setFailure(true);
                jsonStatus.setMsg("授权失败！");
            }

        } else {
            jsonStatus.setSuccess(false);
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("验证失败！");
        }
        return jsonStatus;
    }

    public List<String> sessionExpires() {
        return ((CustomWebSecurityManager)securityManager).sessionExpires();
    }

    public void removeSessionExpire(String sessionId) {
        ((CustomWebSecurityManager)securityManager).removeSessionExpire(sessionId);
    }

    public void setSession(Session session){
        this.session = session;
    }

    public boolean checkSessionTimeout() {
        List<String> expiredSession = sessionExpires();
        // System.out.println("expiredSession:" + expiredSession.size());
        session = getSession();
//         SecurityUtils.getSubject().getSession(false);
//        if (session == null) {
//            return true;
//        }
        if (session != null) {
            String sessionId = session.getId().toString();
            if (expiredSession.contains(sessionId)) {
                removeSessionExpire(sessionId);
                return true;
            } else {
                try{
                    Object userId = session.getAttribute(PermissionConstant.SYS_CURRENT_USER_ID);
                    if (userId == null) {
                        SecurityUtils.getSubject().logout();
                        return true;
                    }
                    else{
                        session.touch(); //延长session时间
                    }
                } catch(UnknownSessionException | ExpiredSessionException e) {
                    System.out.println("session异常问题==========="
                            + " session id:" + session.getId());
                    try {
                        SecurityUtils.getSubject().logout();
                    } finally {
                        session = null;
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
