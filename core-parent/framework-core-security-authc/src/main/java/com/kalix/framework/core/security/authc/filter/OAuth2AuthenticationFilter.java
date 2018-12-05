package com.kalix.framework.core.security.authc.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalix.framework.core.security.authc.OAuth2ClientParams;

import javax.servlet.ServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sunlf on 2017/4/20.
 * oauth2 授权验证返回token
 */
public class OAuth2AuthenticationFilter extends ShiroAuthenticationFilter {

    private final static String HttpMethod_POST = "POST";

    @Override
    public Map<String, String> getAccessToken(ServletResponse response) {
        // String authCode, accessToken = null;
        Map<String, String> accessTokenMap = null;
        try {
            String authCode = getAuthCode();
            accessTokenMap = getAccessToken(authCode);

            //to simplify the web client ajax request we write the oauth2 access token in the cookie
            //Cookie cookieAccessToken = new Cookie("access_token",accessToken);
            //((HttpServletResponse)response).addCookie(cookieAccessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessTokenMap;
    }

    private static String getAuthCode() throws Exception {

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", OAuth2ClientParams.USERNAME);
        params.put("password", OAuth2ClientParams.PASSWORD);
        params.put("client_id", OAuth2ClientParams.CLIENT_ID);
        params.put("response_type", OAuth2ClientParams.CODE);
        params.put("redirect_uri", OAuth2ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        processUrl(params, postStr);

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(OAuth2ClientParams.OAUTH_SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod_POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        connection.setInstanceFollowRedirects(false);// 必须设置该属性
        String location = connection.getHeaderField("Location");

        return location.substring(location.indexOf("=") + 1);
    }

    private static void processUrl(Map<String, Object> params, StringBuilder postStr) throws UnsupportedEncodingException {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postStr.length() != 0) {
                postStr.append('&');
            }
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    private static Map<String, String> getAccessToken(String authCode) throws Exception {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("client_id", OAuth2ClientParams.CLIENT_ID);
        params.put("client_secret", OAuth2ClientParams.CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        params.put("redirect_uri", OAuth2ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        processUrl(params, postStr);

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(OAuth2ClientParams.OAUTH_SERVER_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod_POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(response.toString(), Map.class);
        // String accessToken = map.get("access_token");
        // return accessToken;
        return map;
    }
}
