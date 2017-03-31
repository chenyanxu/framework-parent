package com.kalix.framework.core.security.authc.filter;

import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.security.DefaultUserNamePasswordToken;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.UnicodeConverter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * shiro首頁验证处理过滤器
 * Created by sunlf on 2015/7/10.
 */
public abstract class KalixAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory
            .getLogger(KalixAuthenticationFilter.class);

    private static final String ERROR_MSG = "{\"success\":false,\"message\":\"%s\",\"detail\":\"%s\"}";
    public static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    protected String appName;
    protected Boolean deploy;
    protected Boolean checkVCode;

    //自动加载deploy配置 方便验证码的判断
    public KalixAuthenticationFilter(){
        appName =this.getClass().getSimpleName().split("AuthenticationFilter")[0].toLowerCase();
        this.checkVCode=Boolean.valueOf((String) ConfigUtil.getConfigProp(appName+"_vcode", "ConfigWebContext"));
        this.deploy=Boolean.valueOf((String) ConfigUtil.getConfigProp("deploy", "ConfigWebContext"));
    }

    /*
     *  主要是针对登入成功的处理方法。对于请求头是AJAX的之间返回JSON字符串。
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
                                     Subject subject, ServletRequest request, ServletResponse response)
            throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        /*if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {// 不是ajax请求
            issueSuccessRedirect(request, response);
        } else */
        {
            Session session = SecurityUtils.getSubject().getSession();
            String realName = String.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_REAL_NAME));
            String loginName = String.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_LOGIN_NAME));
            String userId = String.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_ID));
            String userIcon = String.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_ICON));
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");

            Cookie cookieRealName = new Cookie("currentUserRealName", UnicodeConverter.string2UnicodeCookie(realName));
            Cookie cookieLoginName = new Cookie("currentUserLoginName", UnicodeConverter.string2UnicodeCookie(loginName));
            Cookie cookieUserId = new Cookie("currentUserId", UnicodeConverter.string2UnicodeCookie(userId));
            Cookie cookieUserIcon = new Cookie("currentUserIcon", UnicodeConverter.string2UnicodeCookie(userIcon));

            String contextPath = httpServletRequest.getContextPath();

            httpServletResponse.addCookie(cookieRealName);
            httpServletResponse.addCookie(cookieLoginName);
            httpServletResponse.addCookie(cookieUserId);
            httpServletResponse.addCookie(cookieUserIcon);

            PrintWriter out = httpServletResponse.getWriter();
            String rtnPage = "";

            if (this.deploy) {
                rtnPage = "/index.jsp";
            } else {
                rtnPage = "/index-debug.jsp";
            }

            out.println("{\"success\":true," +
                        "\"location\":\"" + contextPath + rtnPage +
                        "\",\"message\":\"登入成功\"," +
                        "\"user\":{\"name\":\"" + realName +
                        "\",\"token\":\"" + session.getId() + "\",\"id\":\""+userId+"\"}}");
            out.flush();
            out.close();
        }

        return false;
    }

    /**
     * 主要是处理登入失败的方法
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
//        if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request)
//                .getHeader("X-Requested-With"))) {// 不是ajax请求
//            setFailureAttribute(request, e);
//            return true;
//        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            String message = e.getClass().getSimpleName();
            if ("IncorrectCredentialsException".equals(message)) {
                out.println(String.format(ERROR_MSG, "密码错误",message));
            } else if ("UnknownAccountException".equals(message)) {
                out.println(String.format(ERROR_MSG, "账号不存在",message));
            } else if ("LockedAccountException".equals(message)) {
                out.println(String.format(ERROR_MSG, "账号被锁定",message));
            } else {
                e.printStackTrace();
                out.println(String.format(ERROR_MSG, "未知错误",message));
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String loginType;


        loginType = WebUtils.getCleanParam(request, "loginType");
        if (loginType == null) {
            loginType = "main"; //set default value
        }


        return new DefaultUserNamePasswordToken(username, password, loginType);
    }

    /**
     * 所有请求都会经过的方法。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {

        //判断是否是登陆操作
        if (isLoginRequest(request, response)) {
            //验证登陆方式
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                if(this.checkVCode) {
                    //判断是否为ajax异步请求
                    if ("XMLHttpRequest"
                            .equalsIgnoreCase(((HttpServletRequest) request)
                                    .getHeader("X-Requested-With"))) {
                        String vcode = request.getParameter("vcode");
                        HttpServletRequest httpservletrequest = (HttpServletRequest) request;
                        String vvcode = (String) httpservletrequest
                                .getSession()
                                .getAttribute(KAPTCHA_SESSION_KEY);

                        if (vvcode == null || "".equals(vvcode)
                                || !vvcode.equals(vcode)) {
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json;charset=UTF-8");
                            PrintWriter out = response.getWriter();
                            out.println(String.format(ERROR_MSG, "验证码错误!", ""));
                            out.flush();
                            out.close();
                            return false;
                        }
                    } else {
                        //不需要验证码
                    }
                }

                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                // allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
                        + "Authentication url [" + getLoginUrl() + "]");
            }
            // saveRequestAndRedirectToLogin(request, response);
            //判断是否为ajax异步请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.println(String.format(ERROR_MSG,"login",""));
                out.flush();
                out.close();
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }
}