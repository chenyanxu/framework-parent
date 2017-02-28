package com.kalix.framework.core.impl.security;

import com.kalix.framework.core.api.dao.IUserEntityDao;
import com.kalix.framework.core.api.persistence.UserEntity;
import com.kalix.framework.core.api.security.ILoginService;
import com.kalix.framework.core.util.MD5Util;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyanxu on 2017/2/28.
 */
public abstract class LoginServiceImpl implements ILoginService {
    protected IUserEntityDao dao;

    @Override
    public boolean validateUserStatus(String username) {
        UserEntity user = dao.getUser(username);

        if (user == null) {
            throw new UnknownAccountException("Unknown Account Exception!");
        }
        if (user.getAvailable() == 0) {
            throw new LockedAccountException("Unknown Account Exception!");
        }

        return true;
    }

    @Override
    public Map login(String username, String password) {
        Map map = new HashMap();
        int result = -1;

        UserEntity user = dao.getUser(username);
        if (user == null) {
            throw new UnknownAccountException("Unknown Account Exception!");
        }
        if (user.getAvailable() != 1L) {
            throw new LockedAccountException("Account LockedException!");
        }
        //判断密码和用户类型是否对应
        if (MD5Util.encode(password).equals(user.getPassword())) {
            Map resMap = new HashMap();
            resMap.put("user_id", user.getId());
            resMap.put("name", user.getName());
            resMap.put("user_name", user.getLoginName());
            resMap.put("password", user.getPassword());
            resMap.put("user_icon", user.getIcon());
            map.put("response", resMap);

        } else {
            throw new IncorrectCredentialsException("Incorrect Credentials Exception!");
        }

        return map;

    }

    @Override
    public void updateUserLoginInfo(long id, String loginIp) {
        dao.updateUserLoginInfo(id, loginIp);
    }

    public void setDao(IUserEntityDao userEntityDao) {
        this.dao = userEntityDao;
    }
}
