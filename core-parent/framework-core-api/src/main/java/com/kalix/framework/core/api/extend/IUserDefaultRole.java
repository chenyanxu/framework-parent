package com.kalix.framework.core.api.extend;

/**
 * Created by sunlf on 2018-03-07.
 * 扩展功能
 * 用户默认角色接口类
 */
public interface IUserDefaultRole {
    final static String USER_TYPE = "userType"; // osgi服务需要注册的key

    String getRoleName();
}
