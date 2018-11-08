package com.kalix.framework.core.api.dao;

import com.kalix.framework.core.api.persistence.UserEntity;

import java.io.Serializable;

/**
 * Created by chenyanxu on 2017/2/27.
 */
public interface IUserEntityDao<T extends UserEntity,PK extends Serializable> extends IGenericDao<T,PK> {
    T getUser(String loginName);
    T getUser(Long id);
    void updateUserLoginInfo(String id,String loginIP);
}
