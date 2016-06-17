package com.kalix.framework.core.web.impl;

import com.kalix.framework.core.api.web.IFooter;

/**
 * Created by sunlf on 2015/7/13.
 */
public class FooterImpl implements IFooter {
    @Override
    public String getId() {
        return "footer";
    }

    @Override
    public String getText() {
        return "footer";
    }

    @Override
    public String getDescription() {
        return "footer";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getRouteId() {
        return "Kalix.view.main.region.Bottom";
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
