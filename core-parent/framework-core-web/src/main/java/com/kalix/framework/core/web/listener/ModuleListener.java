package com.kalix.framework.core.web.listener;

import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.web.manager.ModuleManager;

/**
 * Created by sunlf on 2015/7/18.
 */
public class ModuleListener {
    public void register(IModule module) {
        ModuleManager.getInstall().add(module);
    }

    public void unregister(IModule module) {
        if(module!=null){
            ModuleManager.getInstall().remove(module);
        }

    }
}
