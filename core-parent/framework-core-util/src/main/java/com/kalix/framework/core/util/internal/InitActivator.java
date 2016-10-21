package com.kalix.framework.core.util.internal;

import com.kalix.framework.core.util.SystemUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @类描述：osgi的初始化类
 * @创建人： sunlingfeng
 * @创建时间：2014/12/17
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class InitActivator implements BundleActivator {
    private static BundleContext context;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SystemUtil.startBundlePrintln(bundleContext);
        context = bundleContext;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        SystemUtil.stopBundlePrintln(bundleContext);
        this.context = null;
    }

    public static BundleContext getBundleContext() {
        return context;
    }
}
