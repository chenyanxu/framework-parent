package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.util.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/19.
 */
public class CustomProcessor implements Processor {

    private Map<String, Object> rtnMap = null;

    public CustomProcessor() {
        this.rtnMap = new HashMap<>();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            this.rtnMap.clear();
            HttpServletRequest request = ObjectHelper.cast(HttpServletRequest.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));
            HttpServletResponse response = ObjectHelper.cast(HttpServletResponse.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_RESPONSE));

            String className = "";
            String method = request.getMethod();
            if (method.equals("GET")) {
                className = request.getParameter("classname");
                if (StringUtils.isEmpty(className)) {
                    rtnMap.put("success", false);
                    rtnMap.put("msg", "请求地址无效，缺少参数className!");
                    return;
                }
                CustomServlet customServlet = (CustomServlet) Class.forName(className).newInstance();
                customServlet.doGet(request, response);
            } else if (method.equals("POST")) {
                String contentType = request.getHeader("Content-Type");
                if (contentType.equals("application/x-www-form-urlencoded")) {
                    className = request.getParameter("classname");
                    if (StringUtils.isEmpty(className)) {
                        rtnMap.put("success", false);
                        rtnMap.put("msg", "请求地址无效，缺少参数className!");
                        return;
                    }
                    CustomServlet customServlet = (CustomServlet) Class.forName(className).newInstance();
                    customServlet.doPost(request, response);
                } else if (contentType.equals("text/plain")) {
                    Object body = exchange.getIn().getBody();
                    Map map = (Map) body;
                    className = (String) map.get("classname");
                    if (StringUtils.isEmpty(className)) {
                        rtnMap.put("success", false);
                        rtnMap.put("msg", "请求地址无效，缺少参数className!");
                        return;
                    }
                    CustomServlet customServlet = (CustomServlet) Class.forName(className).newInstance();
                    customServlet.doAxiosPost(request, response, map);
                } else {
                    String errMsg = "http.header_Content-Type_not_implemented";
                    rtnMap.put("success", false);
                    rtnMap.put("msg", "请求出错，原因：" + errMsg);
                    return;
                }
            } else {
                String errMsg = "http.method_not_implemented";
                rtnMap.put("success", false);
                rtnMap.put("msg", "请求出错，原因：" + errMsg);
                return;
            }
        } catch (Exception e) {
            rtnMap.put("success", false);
            rtnMap.put("msg", "请求出错，原因：" + e.getMessage());
        } finally {
            exchange.getOut().setBody(rtnMap);
        }
    }
}
