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

    /**
     * 获得菜单下按钮的定义，以,分隔，例如：add,delete,edit,view
     * @return
     */
    String getButtons();
}
