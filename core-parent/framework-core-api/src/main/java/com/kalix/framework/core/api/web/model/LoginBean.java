package com.kalix.framework.core.api.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/14.
 */
public class LoginBean {
    private String color;
    private String image;
    private String component;
    private String title;
    private String favicon;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
}
