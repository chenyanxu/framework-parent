package com.kalix.framework.core.security.impl;

import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.WebSecurityManager;

/**
 * Created by sunlf on 2015/7/23.
 */
public class ShiroServiceImpl implements IShiroService {
    private WebSecurityManager securityManager;

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
        Long rtn = Long.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_ID).toString());

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
        try {
            return SecurityUtils.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Session getSession() {
        return SecurityUtils.getSubject().getSession();
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
}
