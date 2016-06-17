package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IBaseWebPage;

/**
 * 菜单的抽象类
 */
public abstract class AbstractWebPageImpl implements IBaseWebPage {
    protected String id;
    protected String text;
    protected String icon;
    protected String routeId;
    protected int index;
    protected String permission = null;
    protected String description;

    @Override
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setComponentClass(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
