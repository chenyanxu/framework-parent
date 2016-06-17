package com.kalix.framework.core.api.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/14.
 */
public class BodyBean {
    private List<WebApplicationBean> applicationBeanList = new ArrayList<>();

    public List<WebApplicationBean> getApplicationBeanList() {
        return applicationBeanList;
    }

    public void setApplicationBeanList(List<WebApplicationBean> applicationBeanList) {
        this.applicationBeanList = applicationBeanList;
    }
}
