package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.util.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
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
            } else if (method.equals("POST")) {
                className = request.getParameter("classname");
            } else {
                String errMsg1 = "http.method_not_implemented";
                rtnMap.put("success", false);
                rtnMap.put("msg", "请求出错，原因："+ errMsg1);
                return;
            }

            if (StringUtils.isEmpty(className)) {
                response.addHeader("Content-Type", "application/json; charset=utf-8");
                rtnMap.put("success", false);
                rtnMap.put("msg", "请求地址无效，缺少参数className!");
                return;
            }

            CustomServlet customServlet = (CustomServlet) Class.forName(className).newInstance();
            if (method.equals("GET")) {
                customServlet.doGet(request, response);
            } else if (method.equals("POST")) {
                customServlet.doPost(request, response);
            }
        } catch (Exception e) {
            rtnMap.put("success", false);
            rtnMap.put("msg", "请求出错，原因：" + e.getMessage());
        } finally {
            exchange.getIn().setBody(rtnMap);
        }
    }
}
