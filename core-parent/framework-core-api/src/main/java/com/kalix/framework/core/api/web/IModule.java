package com.kalix.framework.core.api.web;

import java.util.List;

/**
 * Created by sunlf on 2015/7/13.
 */
public interface IModule extends IBaseWebPage {
    /**
     * 返回菜单列表
     * @return
     */
    List<IMenu> getMenus();

    /**
     * 返回应用ID
     * @return
     */
    String getApplicationId();

    String getIconCls();

    boolean isSelectable();

    boolean isExpanded();
}
