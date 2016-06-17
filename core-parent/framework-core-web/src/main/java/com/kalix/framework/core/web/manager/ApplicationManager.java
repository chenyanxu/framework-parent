package com.kalix.framework.core.web.manager;

import com.kalix.framework.core.api.web.IApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sunlf on 2015/7/13.
 * 维护IApplication列表
 */
public class ApplicationManager {
    private static ApplicationManager install;
    private static Comparator<IApplication> COMPARATOR = new Comparator<IApplication>() {
        // This is where the sorting happens.
        public int compare(IApplication o1, IApplication o2) {
            return o1.getIndex() - o2.getIndex();
        }
    };
    private List<IApplication> applicationList = new ArrayList<IApplication>();

    private ApplicationManager() {
    }

    public synchronized static ApplicationManager getInstall() {
        if (install == null) {
            install = new ApplicationManager();
        }
        return install;
    }

    public void add(IApplication application) {
        applicationList.add(application);
    }

    public void remove(IApplication application) {
        applicationList.remove(application);
    }

    public List<IApplication> getApplicationList() {
        if (applicationList != null)
            Collections.sort(applicationList, COMPARATOR);
        return applicationList;
    }
}
