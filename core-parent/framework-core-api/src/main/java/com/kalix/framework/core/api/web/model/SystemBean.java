package com.kalix.framework.core.api.web.model;

/**
 * Created by sunlf on 2015/7/13.
 */
public class SystemBean {
    private HeaderBean headerBean;
    private FooterBean footerBean;
    private BodyBean bodyBean;

    public HeaderBean getHeaderBean() {
        return headerBean;
    }

    public void setHeaderBean(HeaderBean headerBean) {
        this.headerBean = headerBean;
    }

    public FooterBean getFooterBean() {
        return footerBean;
    }

    public void setFooterBean(FooterBean footerBean) {
        this.footerBean = footerBean;
    }

    public BodyBean getBodyBean() {
        return bodyBean;
    }

    public void setBodyBean(BodyBean bodyBean) {
        this.bodyBean = bodyBean;
    }
}
