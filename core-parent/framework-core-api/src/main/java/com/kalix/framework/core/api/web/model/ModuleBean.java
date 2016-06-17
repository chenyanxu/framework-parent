package com.kalix.framework.core.api.web.model;

import java.util.List;

/**
 * Created by sunlf on 2015/7/14.
 */
public class ModuleBean extends BaseWebPage {
    private boolean selectable;
    private boolean expanded;
    private String applicationId;

    private List<MenuBean> children;

    public List<MenuBean> getChildren() {
        return children;
    }

    public void setChildren(List<MenuBean> children) {
        this.children = children;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
