package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IApplication;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author chenyanxu
 */
public class BaseApplicationConfig implements ManagedService{
    private BundleContext bundleContext;
    private IApplication application;
    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        Dictionary<String,String> tempDict=(Dictionary<String, String>) dictionary;

        if( tempDict.get("APPLICATION_APP_ID")!=null &&
            tempDict.get("APPLICATION_APP_TEXT")!=null &&
            tempDict.get("APPLICATION_APP_ICONCLS")!=null &&
            tempDict.get("APPLICATION_APP_INDEX")!=null &&
            tempDict.get("APPLICATION_APP_PERMISSION")!=null){

            if(application==null){
                Dictionary<String,String> propertys=new Hashtable<>();

                propertys.put("APPLICATION_ID",tempDict.get("APPLICATION_APP_ID"));

                application=new BaseApplicationImpl(bundleContext);
                bundleContext.registerService(IApplication.class.getName(),application,propertys);
            }

            application.updateConfig(tempDict);
        }
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
