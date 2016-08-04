package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Administrator on 2016-08-04.
 */
public class Applicationconfig implements ManagedService {
    private EventAdmin eventAdmin;
    private BundleContext bundleContext;
    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(IApplication.class.getName(), null);
            if (refs != null) {
                for (ServiceReference ref : refs) {
                    IApplication service= (IApplication) bundleContext.getService(ref);
                    service.updateConfig();
                }
            }

        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        //postUpdateApplicationEvent();
    }

    private void postUpdateApplicationEvent() {
        Dictionary properties = new Hashtable();
        properties.put("message", "");
        Event event = new Event("com/kalix/updateApplicationConfig", properties);
        eventAdmin.postEvent(event);
    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }




}
