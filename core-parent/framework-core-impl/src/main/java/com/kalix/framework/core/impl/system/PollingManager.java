package com.kalix.framework.core.impl.system;

import com.kalix.framework.core.api.system.IPollingService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyanming on 2016/2/25.
 * 维护IPolling列表
 */
public class PollingManager {
    private static PollingManager install;
    //    private static Comparator<IPollingService> COMPARATOR = new Comparator<IPollingService>() {
//        // This is where the sorting happens.
//        public int compare(IPollingService o1, IPollingService o2) {
//            return o1.getIndex() - o2.getIndex();
//        }
//    };
    private List<IPollingService> PollingList = new ArrayList<IPollingService>();

    private PollingManager() {
    }

    public synchronized static PollingManager getInstall() {
        if (install == null) {
            install = new PollingManager();
        }
        return install;
    }

    public void add(IPollingService Polling) {
        PollingList.add(Polling);
    }

    public void remove(IPollingService Polling) {
        PollingList.remove(Polling);
    }

    public List<IPollingService> getPollingList() {
        if (PollingList != null)
            ;//Collections.sort(PollingList, COMPARATOR);
        return PollingList;
    }
}
