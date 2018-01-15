package com.kalix.framework.core.api.config.model;

/**
 * Created by Administrator_ on 2018/1/15.
 */
public class ConfigBean {
    private String id;   //id
    private String name; //名称
    private String value; //数据
    private String type;  //类型
    private String desc;  //描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
