package com.kalix.framework.core.security.listener;

import com.kalix.framework.core.util.ConfigUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * Created by chenyanxu on 2017/3/21.
 */
public class VCodeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //release version need enable the vcode servlet
        String appName = servletContextEvent.getServletContext().getInitParameter("app");

        if(Boolean.valueOf((String) ConfigUtil.getConfigProp(appName+"_vcode","ConfigWebContext"))) {
            ServletContext sc = servletContextEvent.getServletContext();

            // Register Servlet
            ServletRegistration sr = sc.addServlet("kaptcha",
                    "com.google.code.kaptcha.servlet.KaptchaServlet");
            sr.addMapping("/images/kaptcha");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
