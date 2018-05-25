package com.kalix.framework.core.api.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by sunlf on 2015/7/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseWebPage implements Serializable {
    private String id;
    private String text;
    private String description;
    private String iconCls;
    private String routeId;
    private Boolean supportMobile;
    private int index;
    private String permission;
    private String cfgKey;

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

    public Boolean getSupportMobile() {
        return supportMobile;
    }

    public void setSupportMobile(Boolean supportMobile) {
        this.supportMobile = supportMobile;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getCfgKey() {
        return cfgKey;
    }

    public void setCfgKey(String cfgKey) {
        this.cfgKey = cfgKey;
    }
}
