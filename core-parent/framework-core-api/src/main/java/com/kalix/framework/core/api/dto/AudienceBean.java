package com.kalix.framework.core.api.dto;

/**
 * Created by Administrator_ on 2018/5/22.
 */
public class AudienceBean {
    private String clientId;
    private String base64Secret;
    private String name;
    private int expiresSecond;
    private int refresh_expiresSecond;

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getBase64Secret() {
        return base64Secret;
    }
    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getExpiresSecond() {
        return expiresSecond;
    }
    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    public int getRefresh_expiresSecond() {
        return refresh_expiresSecond;
    }

    public void setRefresh_expiresSecond(int refresh_expiresSecond) {
        this.refresh_expiresSecond = refresh_expiresSecond;
    }
}
