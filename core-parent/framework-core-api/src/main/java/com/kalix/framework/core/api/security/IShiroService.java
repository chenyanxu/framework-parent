package com.kalix.framework.core.api.security;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by sunlf on 2015/7/23.
 * 安全服务
 */
public interface IShiroService extends IService {
    /**
     * 获得当前登录用户的登录名
     *
     * @return 登录名
     */
    String getCurrentUserLoginName();

    /**
     * 获得当前登录用户的真实姓名
     *
     * @return 真实姓名
     */
    String getCurrentUserRealName();

    /**
     * 获得当前登录用户ID
     *
     * @return 用户ID
     */
    String getCurrentUserId();

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

    /**
     * 判断shiro session 超时
     * @return
     */
    List<String> sessionExpires();

    /**
     * 删除超时的sessionId记录
     * @param sessionId
     */
    void removeSessionExpire(String sessionId);

    /**
     * session设置(登录以后设置，保证session一致)
     * @param session
     */
    void setSession(Session session);

    boolean checkSessionTimeout();
}
