package com.kalix.framework.core.api.security;

/**
 * Created by Administrator on 2016-12-05.
 */
public interface IDataAuthService {
    boolean isAuth(String entityClassName, String userid);
}
