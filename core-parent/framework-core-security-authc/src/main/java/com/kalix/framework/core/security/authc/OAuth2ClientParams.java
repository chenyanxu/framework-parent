package com.kalix.framework.core.security.authc;

/**
 * 封装OAuth Server端认证需要的参数
 */
public class OAuth2ClientParams {
    public static final String BASE_URL = "http://localhost:8181/oauth2/";

    public static final String CLIENT_ID = "c1ebe466-1cdc-4bd3-ab69-77c3561b9dee"; // 应用id CLIENT_ID

    public static final String CLIENT_SECRET = "d8346ea2-6017-43ed-ad68-19c0f971738b"; // 应用secret CLIENT_SECRET

    public static final String USERNAME = "admin"; // 用户名

    public static final String PASSWORD = "123456"; // 密码

    public static final String OAUTH_SERVER_URL = BASE_URL + "authorize"; // 授权地址

    public static final String OAUTH_SERVER_TOKEN_URL = BASE_URL + "accessToken"; // ACCESS_TOKEN换取地址

    public static final String OAUTH_SERVER_REFRESH_TOKEN_URL = BASE_URL + "refreshToken"; // refresh_token 获取新的accessToken

    public static final String OAUTH_SERVER_REDIRECT_URI = "http://aimeizi.net"; // 回调地址

    public static final String CODE = "code";
}
