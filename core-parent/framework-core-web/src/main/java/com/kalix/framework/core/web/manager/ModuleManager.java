package com.kalix.framework.core.web.manager;

import com.kalix.framework.core.api.web.IModule;

import java.util.*;

/**
 * Created by sunlf on 2015/7/13.
 * 维护IApplication列表
 */
public class ModuleManager {
    private static ModuleManager install;
    //排序
    private static Comparator<IModule> COMPARATOR = new Comparator<IModule>() {
        // This is where the sorting happens.
        public int compare(IModule o1, IModule o2) {
            return o1.getIndex() - o2.getIndex();
        }
    };

    private Map<String, List<IModule>> moduleMap = new HashMap<String, List<IModule>>();

    private ModuleManager() {
    }

    public synchronized static ModuleManager getInstall() {
        if (install == null) {
            install = new ModuleManager();
        }
        return install;
    }

    public void add(IModule module) {
        List<IModule> moduleList = moduleMap.get(module.getApplicationId());
        if (moduleList == null) {
            moduleList = new ArrayList<IModule>();
            moduleMap.put(module.getApplicationId(), moduleList);
        }
        moduleList.add(module);
    }

    public void remove(IModule module) {
        List<IModule> moduleList = moduleMap.get(module.getApplicationId());
        moduleList.remove(module);
    }

    public List<IModule> getModuleList(String moduleId) {
        List<IModule> list = moduleMap.get(moduleId);
        if (list != null)
            Collections.sort(list, COMPARATOR);
        return list;
    }

    public List<IModule> getAllModules() {
        List<IModule> modules = new ArrayList<IModule>();
        for (List<IModule> list : moduleMap.values()) {
            for (IModule module : list) {
                modules.add(module);
            }
        }
        return modules;
    }
}
