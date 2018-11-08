package com.kalix.framework.core.api.security;

import com.kalix.framework.core.api.security.model.EnumDataAuth;

/**
 * Created by Administrator on 2016-12-05.
 */
public interface IDataAuthService {
    @Deprecated
    boolean isAuth(String entityClassName, String userId);

    /**
     * 获得用户的数据权限
     *
     * @param userId
     * @return
     */
    EnumDataAuth getDataAuth(String userId);
}
