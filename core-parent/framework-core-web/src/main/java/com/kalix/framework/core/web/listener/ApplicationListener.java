package com.kalix.framework.core.web.listener;

import com.kalix.framework.core.api.web.IApplication;
import com.kalix.framework.core.web.manager.ApplicationManager;

/**
 * Created by sunlf on 2015/7/18.
 */
public class ApplicationListener {
    public void register(IApplication application) {
        ApplicationManager.getInstall().add(application);
    }

    public void unregister(IApplication application) {
        if (application != null) {
            ApplicationManager.getInstall().remove(application);
        }
    }
}
