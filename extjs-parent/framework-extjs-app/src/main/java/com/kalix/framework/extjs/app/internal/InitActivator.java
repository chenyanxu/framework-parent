package com.kalix.framework.extjs.app.internal;

import com.kalix.framework.core.api.osgi.KalixBundleActivator;
import com.kalix.framework.core.util.SystemUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

public class InitActivator extends KalixBundleActivator {
    private ServiceReference reference;
    private HttpService httpService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);

        reference = bundleContext.getServiceReference(HttpService.class.getName());
        httpService = (HttpService) bundleContext.getService(reference);

        if(deploy){
            httpService.registerResources(contextPath+"/app", "/min/app", null);
        }
        else{
            httpService.registerResources(contextPath+"/app", "/app", null);
        }

        httpService.registerResources(contextPath+"/resources", "/resources", null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(bundleContext);

        if (reference != null)
            bundleContext.ungetService(reference);

        if(httpService!=null){
            httpService.unregister(contextPath+"/app");
            httpService.unregister(contextPath+"/resources");
        }
    }
}
