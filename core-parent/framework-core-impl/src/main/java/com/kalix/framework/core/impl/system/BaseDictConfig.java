package com.kalix.framework.core.impl.system;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 *@author chenyanxu
 */
public class BaseDictConfig implements ManagedService {
    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        Dictionary<String,String> tempDict=(Dictionary<String, String>) dictionary;

        Enumeration<String> keys=tempDict.keys();

        while(keys.hasMoreElements()){
            String key=keys.nextElement();

            DictConfigManager.getInstall().add(key,tempDict.get(key));
        }
    }
}
