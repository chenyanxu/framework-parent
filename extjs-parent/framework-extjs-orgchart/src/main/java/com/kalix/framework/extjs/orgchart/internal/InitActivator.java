package com.kalix.framework.extjs.orgchart.internal;

import com.kalix.framework.core.api.osgi.KalixBundleActivator;
import com.kalix.framework.core.util.SystemUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
/**
 * @author chenyanxu
 */
public class InitActivator extends KalixBundleActivator {
    private ServiceReference reference;
    private HttpService httpService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SystemUtil.startBundlePrintln(bundleContext);

        reference = bundleContext.getServiceReference(HttpService.class.getName());
        httpService = (HttpService) bundleContext.getService(reference);

        httpService.registerResources(contextPath + "/app/orgchart", "/orgchart", null);
        httpService.registerResources(contextPath + "/orgchart/resources", "/resources", null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        SystemUtil.stopBundlePrintln(bundleContext);

        if (reference != null)
            bundleContext.ungetService(reference);

        if(httpService!=null){
            httpService.unregister(contextPath + "/app/orgchart");
            httpService.unregister(contextPath + "/orgchart/resources");
        }
    }
}
