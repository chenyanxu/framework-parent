package com.kalix.framework.core.impl.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.kalix.framework.core.api.exception.KalixRuntimeException;
import com.kalix.framework.core.api.exception.UnAuthException;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.OsgiUtil;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.http.common.HttpConsumer;
import org.apache.camel.http.common.HttpHelper;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultExchange;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenyanxu
 */
public class KalixCamelHttpTransportServlet extends CamelHttpTransportServlet {
    private IShiroService shiroService;
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log.trace("Service: {}", request);

        if (request.getHeader("JSESSIONID") != null && !request.getHeader("JSESSIONID").isEmpty()) {
            ThreadContext.bind(new Subject.Builder().sessionId(request.getHeader("JSESSIONID")).buildSubject());
        } else {
            // 解决没有cookies时报错问题
            if (request.getCookies() != null && request.getCookies().length > 0) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        ThreadContext.bind(new Subject.Builder().sessionId(cookie.getValue()).buildSubject());
                        break;
                    }
                }
            }
        }
        // Session session = SecurityUtils.getSubject().getSession();
        String grant = request.getParameter("grant");
        if (!"client".equals(grant)) {
            shiroService = OsgiUtil.waitForServices(IShiroService.class, null);
            Session session = shiroService.getSession();
            if (session != null) {
                if (isDataAuthRequest(request)) {
                    session.setAttribute("DataAuthApp", request.getPathInfo());
                } else {
                    session.setAttribute("DataAuthApp", "");
                }
            }
        }

        HttpConsumer consumer = this.resolve(request);
        if (consumer == null) {
            this.log.debug("No consumer to service request {}", request);
            response.sendError(404);
        } else if (consumer.isSuspended()) {
            this.log.debug("Consumer suspended, cannot service request {}", request);
            response.sendError(503);
        } else if ("OPTIONS".equals(request.getMethod())) {
            /*String exchange1;
            if (consumer.getEndpoint().getHttpMethodRestrict() != null) {
                exchange1 = "OPTIONS," + consumer.getEndpoint().getHttpMethodRestrict();
            } else {
                exchange1 = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH";
            }

            response.addHeader("Allow", exchange1);*/
            setCorsResponse(response);
            response.setStatus(200);
        } else if (consumer.getEndpoint().getHttpMethodRestrict() != null && !consumer.getEndpoint().getHttpMethodRestrict().contains(request.getMethod())) {
            response.sendError(405);
        } else if ("TRACE".equals(request.getMethod()) && !consumer.isTraceEnabled()) {
            response.sendError(405);
        } else {
            DefaultExchange exchange = new DefaultExchange(consumer.getEndpoint(), ExchangePattern.InOut);
            if (consumer.getEndpoint().isBridgeEndpoint()) {
                exchange.setProperty("CamelSkipGzipEncoding", Boolean.TRUE);
                exchange.setProperty("CamelSkipWwwFormUrlEncoding", Boolean.TRUE);
            }

            if (consumer.getEndpoint().isDisableStreamCache()) {
                exchange.setProperty("CamelDisableHttpStreamCache", Boolean.TRUE);
            }

            ClassLoader oldTccl = this.overrideTccl(exchange);
            HttpHelper.setCharsetFromContentType(request.getContentType(), exchange);
            exchange.setIn(new HttpMessage(exchange, consumer.getEndpoint(), request, response));
//            exchange.setIn(new HttpMessage(exchange, request, response));
            String contextPath = consumer.getEndpoint().getPath();
            exchange.getIn().setHeader("CamelServletContextPath", contextPath);
            String httpPath = (String) exchange.getIn().getHeader("CamelHttpPath");
            if (contextPath != null && httpPath.startsWith(contextPath)) {
                exchange.getIn().setHeader("CamelHttpPath", httpPath.substring(contextPath.length()));
            }

            try {
                consumer.createUoW(exchange);
            } catch (Exception var16) {
                this.log.error("Error processing request", var16);
                throw new ServletException(var16);
            }

            try {
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Processing request for exchangeId: {}", exchange.getExchangeId());
                }

                consumer.getProcessor().process(exchange);
            } catch (Exception var15) {
                exchange.setException(var15);
            }

            try {
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Writing response for exchangeId: {}", exchange.getExchangeId());
                }

                Integer e = consumer.getEndpoint().getResponseBufferSize();
                if (e != null) {
                    this.log.trace("Using response buffer size: {}", e);
                    response.setBufferSize(e.intValue());
                }
                //consumer.getBinding().writeResponse(exchange, response);
                //===CODE_START===
                /**
                 * change code to deal with all runtime exception.
                 * avoid the origin error back to client side.
                 **/
                if (exchange.getException() == null) {
                    consumer.getBinding().writeResponse(exchange, response);
                } else {
                    if (exchange.getException() instanceof UnAuthException) { //session超时，拦截并返回401
                        response.sendError(401);
                    }
                    response.setHeader("Content-Type", " text/html;charset=utf-8");

                    if (exchange.getException() instanceof KalixRuntimeException) {
                        response.getWriter().write("{\"success\":false,\"msg\":\"" + exchange.getException().getMessage() + "\"," +
                                "\"detail\":\"" + ((KalixRuntimeException) exchange.getException()).getDetailMsg() + "\"}");
                    } else if (exchange.getException() instanceof JsonMappingException) {
                        if (exchange.getException().getMessage().contains("FAIL_ON_EMPTY_BEANS")) {
                            response.getWriter().write("{\"success\":false,\"msg\":\"数据不存在\",\"detail\":\"" + exchange.getException().getMessage() + "\"}");
                        } else {
                            response.getWriter().write("{\"success\":false,\"msg\":\"序列化失败\",\"detail\":\"" + exchange.getException().getMessage() + "\"}");
                        }
                    } else {
                        response.getWriter().write("{\"success\":false,\"msg\":\"操作失败\",\"detail\":\"" + exchange.getException().getMessage() + "\"}");
                    }
                }
                //===CODE_END===
            } catch (IOException var17) {
                this.log.error("Error processing request", var17);
                throw var17;
            } catch (Exception var18) {
                this.log.error("Error processing request", var18);
                throw new ServletException(var18);
            } finally {
                consumer.doneUoW(exchange);
                this.restoreTccl(exchange, oldTccl);
            }
        }
    }

    /**
     * 数据权限，处理请求的地址，条件为查询为get，后缀为s结尾
     */
    private boolean isDataAuthRequest(HttpServletRequest request) {
        boolean path = request.getServletPath().equals("/camel/rest");
        String bizName = request.getPathInfo();
        //请求的最后是以s结尾的
        boolean getAll = bizName.substring(bizName.length() - 1, bizName.length()).equals("s");
        boolean isBizReq = bizName.split("/").length == 2; // 请求只包括一个字符串"/"
        if ((request.getMethod().equals("GET")) && path && getAll && isBizReq)
            return true;
        return false;
    }

    private void setCorsResponse(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://172.18.84.109:8080");

        httpServletResponse
                .setHeader(
                        "Access-Control-Allow-Headers",
                        "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,access_token");

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
    }
}
