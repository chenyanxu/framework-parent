package com.kalix.framework.core.api.security.model;

import java.io.Serializable;

/**
 * Created by sunlf on 2015/11/22.
 * 记录用户登录和登出的消息体
 */
public class Audit implements Serializable {
    private String appName; //应用名称
    private String funName;//功能名称
    private String actor;//操作人
    private String action;//操作
    private String content;//操作内容

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }
}
