package com.kalix.framework.core.web.manager;

import com.kalix.framework.core.api.web.IMenu;

import java.util.*;

/**
 * Created by sunlf on 2015/7/13.
 * 维护IApplication列表
 */
public class MenuManager {
    private static MenuManager install;

    private Map<String, List<IMenu>> menuMap = new HashMap<String, List<IMenu>>();
    private static Comparator<IMenu> COMPARATOR = new Comparator<IMenu>() {
        // This is where the sorting happens.
        public int compare(IMenu o1, IMenu o2) {
            return o1.getIndex() - o2.getIndex();
        }
    };
    private MenuManager() {
    }

    public synchronized static MenuManager getInstall() {
        if (install == null) {
            install = new MenuManager();
        }
        return install;
    }

    public void add(IMenu menu) {
        List<IMenu> menuList = menuMap.get(menu.getModuleId());
        if (menuList == null) {
            menuList = new ArrayList<IMenu>();
            menuMap.put(menu.getModuleId(), menuList);
        }
        menuList.add(menu);
    }

    public void remove(IMenu menu) {
        List<IMenu> moduleList = menuMap.get(menu.getModuleId());
        moduleList.remove(menu);
    }

    public List<IMenu> getMenuList(String moduleId) {
        List<IMenu> list = menuMap.get(moduleId);
        if (list != null)
            Collections.sort(list, COMPARATOR);
        return list;
    }
}
