package com.kalix.framework.core.util;

import org.apache.log4j.lf5.viewer.configure.ConfigurationManager;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;

/**
 * @类描述：OSGI ConfigAdmin的工具类，用于获得指定的配置文件
 * @创建人： sunlingfeng
 * @创建时间：2014/10/22
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class ConfigUtil {

    /**
     * 根据主键获得对应的数值
     *
     * @param key      主键
     * @param configId 配置文件名称
     * @return
     */
    public static Object getConfigProp(String key, String configId) {
        try {
            ConfigurationAdmin configurationAdmin = JNDIHelper.getJNDIServiceForName(ConfigurationAdmin.class.getName());
            Configuration config = configurationAdmin.getConfiguration(configId);
            Dictionary<String, Object> dictionary = config.getProperties();

            config.setBundleLocation(null);

            return dictionary.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Dictionary<String, Object> getAllConfig(String configId) {
        try {
            ConfigurationAdmin configurationAdmin = JNDIHelper.getJNDIServiceForName(ConfigurationAdmin.class.getName());
            Configuration config = configurationAdmin.getConfiguration(configId);
            Dictionary<String, Object> dictionary = config.getProperties();

            config.setBundleLocation(null);

            return dictionary;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
