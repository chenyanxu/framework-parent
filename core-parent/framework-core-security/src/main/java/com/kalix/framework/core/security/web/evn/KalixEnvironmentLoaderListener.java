package com.kalix.framework.core.security.web.evn;

import com.kalix.framework.core.util.ConfigUtil;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.ResourceConfigurable;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.MutableWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;

import javax.servlet.ServletContext;

/**
 * @author chenyanxu
 */
public class KalixEnvironmentLoaderListener extends EnvironmentLoaderListener {
    @Override
    protected WebEnvironment createEnvironment(ServletContext sc) {

        Class<?> clazz = determineWebEnvironmentClass(sc);
        if (!MutableWebEnvironment.class.isAssignableFrom(clazz)) {
            throw new ConfigurationException("Custom WebEnvironment class [" + clazz.getName() +
                    "] is not of required type [" + WebEnvironment.class.getName() + "]");
        }

        String configLocations = sc.getInitParameter(CONFIG_LOCATIONS_PARAM);
        boolean configSpecified = StringUtils.hasText(configLocations);

        if (configSpecified && !(ResourceConfigurable.class.isAssignableFrom(clazz))) {
            String msg = "WebEnvironment class [" + clazz.getName() + "] does not implement the " +
                    ResourceConfigurable.class.getName() + "interface.  This is required to accept any " +
                    "configured " + CONFIG_LOCATIONS_PARAM + "value(s).";
            throw new ConfigurationException(msg);
        }

        MutableWebEnvironment environment = (MutableWebEnvironment) ClassUtils.newInstance(clazz);

        environment.setServletContext(sc);

        if (configSpecified && (environment instanceof ResourceConfigurable)) {
            ((ResourceConfigurable) environment).setConfigLocations(configLocations);
        }

        String path = (String) ConfigUtil.getConfigProp("path", "ConfigShiro");

        if (path != null && !path.isEmpty()) {
            ((ResourceConfigurable) environment).setConfigLocations(path);
        }

        customizeEnvironment(environment);

        LifecycleUtils.init(environment);

        return environment;
    }
}
