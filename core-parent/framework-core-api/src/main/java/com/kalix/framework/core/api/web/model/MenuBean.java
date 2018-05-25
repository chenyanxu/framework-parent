package com.kalix.framework.core.api.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/14.
 */
public class MenuBean extends BaseWebPage {
    private List<MenuBean> children = new ArrayList<>();
    private boolean leaf;
    private String applicationId;
    private String moduleId;
    private String buttons;

    public List<MenuBean> getChildren() {
        return children;
    }

    public void setChildren(List<MenuBean> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getButtons() {
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
