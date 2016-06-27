package com.kalix.framework.core.api.security;

import com.kalix.framework.core.api.persistence.JsonStatus;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Created by sunlf on 2015/7/23.
 * 安全服务
 */
public interface IShiroService {
    /**
     * 获得当前登录的用户名称
     *
     * @return 用户名称
     */
    String getCurrentUserName();

    /**
     * 返回当前登录用户
     * @return
     */
    Subject getSubject();

    /**
     * 获得shiro的session
     *
     * @return
     */

    Session getSession();

    /**
     * sessionid校验用户是否登录
     *
     * @return
     */
    JsonStatus validSession(String sessionId);

    /**
     * 根据sessionId校验该用户是否具有权限
     *
     * @param sessionId  session id
     * @param permission 功能id
     * @return
     */
    JsonStatus validPermission(String sessionId, String permission);
}
