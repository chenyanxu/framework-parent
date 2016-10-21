package com.kalix.framework.core.api.osgi;

import com.kalix.framework.core.util.SystemUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author chenyanxu
 */
public abstract class BaseBundleActivator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SystemUtil.startBundlePrintln(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        SystemUtil.stopBundlePrintln(bundleContext);
    }
}
