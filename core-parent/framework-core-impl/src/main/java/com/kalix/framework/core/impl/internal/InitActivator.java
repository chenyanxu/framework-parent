package com.kalix.framework.core.impl.internal;

import com.kalix.framework.core.api.osgi.BaseBundleActivator;
import com.kalix.framework.core.impl.listener.KalixStartupListener;
import org.osgi.framework.BundleContext;
import java.util.logging.Logger;

public class InitActivator extends BaseBundleActivator {
    private static final Logger LOG = Logger.getLogger(InitActivator.class.getName());
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        new KalixStartupListener(LOG, bundleContext);
        super.start(bundleContext);
    }
}
