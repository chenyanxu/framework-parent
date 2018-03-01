package com.kalix.framework.core.api.config.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator_ on 2018/1/15.
 */
public class ClassTypeBean {

    private String classType; // 分类
    private List<ConfigBean> configBean = new ArrayList();

    public List<ConfigBean> getConfigBean() {
        return configBean;
    }

    public void setConfigBean(List<ConfigBean> configBean) {
        this.configBean = configBean;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

}
