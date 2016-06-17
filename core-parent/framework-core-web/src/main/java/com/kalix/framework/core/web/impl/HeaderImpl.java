package com.kalix.framework.core.web.impl;

import com.kalix.framework.core.api.web.IHeader;

/**
 * Created by sunlf on 2015/7/13.
 */
public class HeaderImpl implements IHeader {
    @Override
    public String getId() {
        return "Header";
    }

    @Override
    public String getText() {
        return "Header";
    }

    @Override
    public String getDescription() {
        return "Header";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getRouteId() {
        return "Kalix.view.main.region.Top";
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
