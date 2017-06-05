package com.kalix.framework.core.security.authc.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalix.framework.core.security.authc.Constants;
import com.kalix.framework.core.security.authc.Status;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;

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

public class Oauth2Filter implements Filter {
    private List<String> exceptUrlList = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exceptUrls = filterConfig.getInitParameter("exceptUrls");
//        if (exceptUrls != null) {
//            for (String exceptUrl : exceptUrls.split(";")) {
//                exceptUrlList.add(exceptUrl);
//            }
//        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            boolean oauthUrl = true;
            //check the request url weather in the exceptURl or not
            for (String exceptUrl : exceptUrlList) {
                if (Pattern.compile(exceptUrl).matcher(req.getPathInfo()).matches()) {
                    oauthUrl = false;
                    break;
                }
            }

            if (oauthUrl) {
                //构建OAuth资源请求
                //OAuthAccessResourceRequest oauthQueryRequest = new OAuthAccessResourceRequest((HttpServletRequest) request, ParameterStyle.QUERY); // queryString 方式获取参数
                //OAuthAccessResourceRequest oauthHeaderRequest = new OAuthAccessResourceRequest((HttpServletRequest) request, ParameterStyle.HEADER); // 从HttpHead头中获取参数
//                Cookie cookies[] = req.getCookies();
                String accessToken = "";

                if(req.getHeader("access_token")!=null){
                    accessToken=req.getHeader("access_token");
                }

                if(req.getParameter("access_token")!=null){
                    accessToken=req.getParameter("access_token");
                }
//
//                for (int cIndex = cookies.length - 1; cIndex >= 0; --cIndex) {
//                    if (cookies[cIndex].getName().equals("access_token")) {
//                        accessToken = cookies[cIndex].getValue();
//                        break;
//                    }
//                }
                //验证Access Token
                if (!checkAccessToken(accessToken)) {
                    // 如果不存在/过期了，返回未验证错误，需重新验证
                    oAuthFailResponse(res);
                    return;
                }
            }

            chain.doFilter(request, response);
        }
//        catch (OAuthProblemException e) {
//            try {
//                oAuthFailResponse(res);
//            } catch (OAuthSystemException ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error trying to access oauth server", ex);
//            }
//        }
        catch (OAuthSystemException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error trying to access oauth server", e);
        }
    }

    /**
     * oAuth认证失败时的输出
     *
     * @param res
     * @throws OAuthSystemException
     * @throws IOException
     */
    private void oAuthFailResponse(HttpServletResponse res) throws OAuthSystemException, IOException {
        OAuthResponse oauthResponse = OAuthRSResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setRealm(Constants.RESOURCE_SERVER_NAME)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .buildHeaderMessage();
        res.addHeader("Content-Type", "application/json; charset=utf-8");
        Gson gson = new GsonBuilder().create();
        res.addHeader(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
        //PrintWriter writer = res.getWriter();
        ServletOutputStream sos = res.getOutputStream();

        sos.write(gson.toJson(getStatus(401, Constants.INVALID_ACCESS_TOKEN)).getBytes());
        sos.flush();
        sos.close();
        //writer.write(gson.toJson(getStatus(401,Constants.INVALID_ACCESS_TOKEN)));
        //writer.flush();
        //writer.close();
    }

    /**
     * 验证accessToken
     *
     * @param accessToken
     * @return
     * @throws IOException
     */
    private boolean checkAccessToken(String accessToken) throws IOException {
        URL url = new URL(Constants.CHECK_ACCESS_CODE_URL + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.disconnect();
        return HttpServletResponse.SC_OK == conn.getResponseCode();
    }

    private Status getStatus(int code, String msg) {
        Status status = new Status();
        status.setCode(code);
        status.setMsg(msg);
        return status;
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
