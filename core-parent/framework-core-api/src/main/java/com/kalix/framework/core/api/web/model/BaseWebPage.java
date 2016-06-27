package com.kalix.framework.core.api.web.model;

/**
 * Created by sunlf on 2015/7/13.
 */
public class BaseWebPage {
    private String id;
    private String text;
    private String description;
    private String iconCls;
    private String routeId;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String icon) {
        this.iconCls = icon;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
