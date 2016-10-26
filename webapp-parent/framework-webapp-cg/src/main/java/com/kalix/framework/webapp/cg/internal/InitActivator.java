package com.kalix.framework.webapp.cg.internal;

import com.kalix.framework.core.api.osgi.BaseBundleActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


public class InitActivator extends BaseBundleActivator {
    public static String mainPath="";

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);

        Bundle[] bundles=bundleContext.getBundles();

        for (Bundle bundle:bundles) {
            if(bundle.getSymbolicName().equals("com.kalix.framework.webapp.main")){
                mainPath=bundle.getHeaders().get("Web-ContextPath").toString();

                if(mainPath.equals("/")){
                    mainPath="";
                }

                break;
            }
        }
    }
}
