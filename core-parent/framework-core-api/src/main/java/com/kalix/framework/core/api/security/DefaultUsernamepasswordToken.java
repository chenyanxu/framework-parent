package com.kalix.framework.core.api.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class DefaultUsernamepasswordToken extends UsernamePasswordToken {

    /**
     * Shiro 构造方法
     */
    public DefaultUsernamepasswordToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public DefaultUsernamepasswordToken() {

    }

    /**
     * 判断登录类型
     */
    private String loginType;

    public String getLoginType() {
        return this.loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }


}