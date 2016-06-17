package com.kalix.framework.core.impl.listener;

import com.kalix.framework.core.api.system.IPollingService;
import com.kalix.framework.core.impl.system.PollingManager;

/**
 * Created by zangyanming on 2016/2/25.
 */
public class PollingListener {
    public void register(IPollingService polling) {
        PollingManager.getInstall().add(polling);
    }

    public void unregister(IPollingService polling) {
        if (polling != null) {
            PollingManager.getInstall().remove(polling);
        }

    }
}
