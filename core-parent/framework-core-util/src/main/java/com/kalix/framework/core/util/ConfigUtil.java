package com.kalix.framework.core.util;

import org.apache.log4j.lf5.viewer.configure.ConfigurationManager;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Objects;

/**
 * @类描述：OSGI ConfigAdmin的工具类，用于获得指定的配置文件
 * @创建人： sunlingfeng
 * @创建时间：2014/10/22
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class ConfigUtil {
    private static ConfigurationAdmin configurationAdmin=null;


    /**
     * 根据主键获得对应的数值
     *
     * @param key      主键
     * @param configId 配置文件名称
     * @return
     */
    public static Object getConfigProp(String key, String configId) {
        try {
            if(configurationAdmin==null){
                configurationAdmin = JNDIHelper.getJNDIServiceForName(ConfigurationAdmin.class.getName());
            }

            Configuration config = configurationAdmin.getConfiguration(configId);
            Dictionary<String, Object> dictionary = config.getProperties();

            config.setBundleLocation(null);

            if(dictionary==null){
                throw new RuntimeException("配置文件 ["+configId+"] 不存在");
            }

            Object obj=dictionary.get(key);

            if(obj==null){
                throw new RuntimeException("配置项 ["+configId+" - "+key+"] 不存在");
            }

            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Dictionary<String, Object> getAllConfig(String configId) {
        try {
            if(configurationAdmin==null){
                configurationAdmin = JNDIHelper.getJNDIServiceForName(ConfigurationAdmin.class.getName());
            }

            Configuration config = configurationAdmin.getConfiguration(configId);
            Dictionary<String, Object> dictionary = config.getProperties();

            config.setBundleLocation(null);

            if(dictionary==null){
                throw new RuntimeException("配置文件["+configId+"]不存在");
            }

            return dictionary;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存配置文件信息
     * @return
     */
    public static  void saveAllConfig(Dictionary dictionary,String configId) {
        try {
            if(configurationAdmin==null){
                configurationAdmin = JNDIHelper.getJNDIServiceForName(ConfigurationAdmin.class.getName());
            }

            Configuration config = configurationAdmin.getConfiguration(configId);
            config.update(dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
