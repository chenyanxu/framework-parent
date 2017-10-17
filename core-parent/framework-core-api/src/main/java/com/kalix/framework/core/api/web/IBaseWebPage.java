package com.kalix.framework.core.api.web;

/**
 * Created by sunlf on 2015/7/13.
 * 所有web页面的用于extjs的基础接口
 */
public interface IBaseWebPage {
    String getId();
    String getText();
    String getDescription();
    String getRouteId();
    int getIndex();
    String getPermission();
    String getIconCls();

    Boolean getSupportMobile();
}
