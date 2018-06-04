package com.kalix.framework.core.jwt.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.dto.AudienceBean;
import com.kalix.framework.core.api.jwt.IJwtService;
import com.kalix.framework.core.util.JNDIHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator_ on 2018/5/24.
 */
public class HTTPBearerAuthorizeAttribute extends ShiroAuthenticationFilter {

    private final static String HttpMethod_POST = "POST";
    private IJwtService jwtService;

    public void setJwtService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    public String getAccessToken(ServletResponse response,Map userInfo) {
        String  accessToken = null;
        try {
            accessToken = getAccessToken(userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }


    /**
     * 获取accessToken
     *
     * @return
     */
    private  String getAccessToken(Map userInfo) throws Exception {
        String accessToken="";
        if(jwtService==null) {
            if(JNDIHelper.getJNDIServiceForNameNoCatch(IJwtService.class.getName())) {
                jwtService = JNDIHelper.getJNDIServiceForName(IJwtService.class.getName());
            }
        }
        if(jwtService!=null) {
            AudienceBean audienceEntity = jwtService.getAudien();
             accessToken = jwtService.createJWT(userInfo.get("name").toString(), userInfo.get("user_name").toString(),
                    "", audienceEntity.getClientId(), audienceEntity.getName(),
                    audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());
          //  session.setAttribute(PermissionConstant.SYS_CURRENT_USER_JWTTOKEN,accessToken);
        }
        return accessToken;
    }
}
