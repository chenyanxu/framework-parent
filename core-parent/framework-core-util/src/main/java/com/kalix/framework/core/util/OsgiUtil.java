package com.kalix.framework.core.util;

import com.kalix.framework.core.util.internal.InitActivator;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @类描述：OSGI 工具类
 * @创建人： sunlingfeng
 * @创建时间：2014/12/18
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class OsgiUtil {

    private static final long DEFAULT_TIMEOUT = 5000;

    /**
     * 等待固定时间后，获得对应的osgi服务
     *
     * @param classType
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> T waitForServices(Class<T> classType, String filter) {
        try {
            String flt;
            if (filter != null) {
                if (filter.startsWith("(")) {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + classType.getName() + ")" + filter + ")";
                } else {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + classType.getName() + ")(" + filter + "))";
                }
            } else {
                flt = "(" + Constants.OBJECTCLASS + "=" + classType.getName() + ")";
            }

            BundleContext context = InitActivator.getBundleContext();
            Filter osgiFilter = FrameworkUtil.createFilter(flt);
            ServiceTracker st = new ServiceTracker(context, osgiFilter, null);
            st.open();
            T object = (T) st.waitForService(DEFAULT_TIMEOUT);
            st.close();
            return object;
        } catch (Exception e) {
            SystemUtil.errorPrintln("Failed to find services for " + classType.getName() + e.getMessage());
            return null;
        }
    }

    /**
     * 获得指定接口的全部osgi服务
     *
     * @param classType
     * @param filter
     * @param <E>
     * @return
     * @throws InvalidSyntaxException
     */

    public static <E> List<E> getServices(Class<E> classType, String filter)
            throws InvalidSyntaxException {
        BundleContext bundleContext = InitActivator.getBundleContext();
        List<E> services = null;
        final ServiceReference[] refs = bundleContext.getServiceReferences(classType.getName(), filter);
        if (refs != null) {
            services = new ArrayList<>(refs.length);
            if (refs != null && refs.length > 0) {
                for (ServiceReference ref : refs) {
                    E service = (E) bundleContext.getService(ref);
                    services.add(service);
                }
            }
        } else
            services = Collections.unmodifiableList(new ArrayList<E>());
        return services;
    }
}
