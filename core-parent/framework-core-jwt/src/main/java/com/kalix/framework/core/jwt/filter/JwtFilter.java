package com.kalix.framework.core.jwt.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.jwt.IJwtService;

import com.kalix.framework.core.util.HttpUtil;
import com.kalix.framework.core.util.JNDIHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class JwtFilter implements Filter {
    private IJwtService jwtService;
    private List<String> exceptUrlList = new ArrayList<>();

    public void setJwtService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }
    public JwtFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if(jwtService==null) {
            if(JNDIHelper.getJNDIServiceForNameNoCatch(IJwtService.class.getName())) {
                jwtService = JNDIHelper.getJNDIServiceForName(IJwtService.class.getName());
            }
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;


        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        httpServletResponse
                .setHeader(
                        "Access-Control-Allow-Headers",
                        "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,JSESSIONID");

        httpServletResponse.setHeader("Access-Control-Allow-Credentials",
                "true");

        httpServletResponse.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        httpServletResponse.setHeader("Access-Control-Max-Age", "1209600");

        httpServletResponse.setHeader("Access-Control-Expose-Headers", "access_token");

        httpServletResponse.setHeader("Access-Control-Request-Headers", "access_token");

        httpServletResponse.setHeader("Expires", "-1");

        httpServletResponse.setHeader("Cache-Control", "no-cache");

        httpServletResponse.setHeader("pragma", "no-cache");

        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.setStatus(200);
            return;
        }


        try {
            boolean oauthUrl = true;
            //check the request url weather in the exceptURl or not
            for (String exceptUrl : exceptUrlList) {
                if (Pattern.compile(exceptUrl).matcher(httpServletRequest.getPathInfo()).matches()) {
                    oauthUrl = false;
                    break;
                }
            }
            if (oauthUrl) {
                String auth= checkToken(httpServletRequest);
                //auth="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiIiwidW5pcXVlX25hbWUiOiLnrqHnkIblkZgiLCJ1c2VyaWQiOiJhZG1pbiIsImlzcyI6InJlc3RhcGl1c2VyIiwiYXVkIjoiMDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjYiLCJleHAiOjE1Mjc0MDI4OTEsIm5iZiI6MTUyNzIzMDA5MX0.lMbqoAd0HLIlBQMV9SwZE5HMvfB4ORH6hmVtfzdqFxo";
                if (auth != null)
                {
                        if(jwtService!=null)
                        {
                            if (jwtService.parseJWT(auth,  jwtService.getAudien().getBase64Secret()) != null)
                            {
                                chain.doFilter(request, response);
                            }
                        }

                }

            }

        }catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error trying to access oauth server", e);
        }

    }


    private String checkToken(HttpServletRequest req) {
        String accessToken = "";

        if (req.getHeader("AccessToken") != null) {
            accessToken = req.getHeader("AccessToken");
            return accessToken;
        }

        if (req.getParameter("AccessToken") != null) {
            accessToken = req.getParameter("AccessToken");
            return accessToken;
        }
        if (req.getHeader("access_token") != null) {
            accessToken = req.getHeader("access_token");
            return accessToken;
        }

        if (req.getParameter("access_token") != null) {
            accessToken = req.getParameter("access_token");
            return accessToken;
        }

        if (HttpUtil.getCookieByName(req, "access_token") != null) {
            accessToken = HttpUtil.getCookieByName(req, "access_token").getValue();
        }
        return accessToken;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exceptUrls = filterConfig.getInitParameter("exceptUrls");
    }

    @Override
    public void destroy() {
    }

    public List<String> getExceptUrlList() {
        return exceptUrlList;
    }

    public void setExceptUrlList(List<String> exceptUrlList) {
        this.exceptUrlList = exceptUrlList;
    }
}
