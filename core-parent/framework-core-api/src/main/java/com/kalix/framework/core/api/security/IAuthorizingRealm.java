package com.kalix.framework.core.api.security;

import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.permission.PermissionResolverAware;
import org.apache.shiro.authz.permission.RolePermissionResolverAware;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.Initializable;
import org.apache.shiro.util.Nameable;

/**
 * @类描述：封装shiro统一接口
 * @创建人： sunlingfeng
 * @创建时间：2014/12/10
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface IAuthorizingRealm extends Realm, Authorizer, Initializable, PermissionResolverAware
        , RolePermissionResolverAware, Nameable, LogoutAware, CacheManagerAware {
}
