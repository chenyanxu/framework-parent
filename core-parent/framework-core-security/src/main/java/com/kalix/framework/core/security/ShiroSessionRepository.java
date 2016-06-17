package com.kalix.framework.core.security;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * 项目名称:  urgent-project
 * 类描述:
 * 创建人:    sunlf
 * 创建时间:  2014/7/5 22:28
 * 修改人:    sunlf
 * 修改时间:  2014/7/5 22:28
 * 修改备注:  [说明本次修改内容]
 */
public interface ShiroSessionRepository {
    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    Session getSession(Serializable sessionId);

    Collection<Session> getAllSessions();
}
