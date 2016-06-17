package com.kalix.framework.core.web.listener;

import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.web.manager.MenuManager;

/**
 * Created by sunlf on 2015/7/18.
 */
public class MenuListener {
    public void register(IMenu menu) {
        MenuManager.getInstall().add(menu);
    }

    public void unregister(IMenu menu) {
        if (menu != null) {
            MenuManager.getInstall().remove(menu);
        }
    }
}
