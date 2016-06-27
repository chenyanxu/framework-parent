package com.kalix.framework.core.api.web;


/**
 * Created by sunlf on 2015/7/13.
 */
public interface IMenu extends IBaseWebPage {
    /**
     * 是否为叶子节点
     * @return
     */
    boolean isLeaf();

    /**
     * 返回模块ID
     * @return
     */
    String getModuleId();

    /**
     * 返回上级菜单
     * @return
     */
    String getParentMenuId();
}
