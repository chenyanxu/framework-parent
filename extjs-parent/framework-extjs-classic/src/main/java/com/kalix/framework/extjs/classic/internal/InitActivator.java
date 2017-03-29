package com.kalix.framework.extjs.classic.internal;

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

        httpService.registerResources(contextPath + "/extjs/classic", "/classic", null);
        httpService.registerResources(contextPath + "/extjs62/classic", "/classic62", null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(bundleContext);

        if (reference != null)
            bundleContext.ungetService(reference);

        if (httpService != null) {
            httpService.unregister(contextPath + "/extjs/classic");
            httpService.unregister(contextPath + "/extjs62/classic");
        }
    }
}
