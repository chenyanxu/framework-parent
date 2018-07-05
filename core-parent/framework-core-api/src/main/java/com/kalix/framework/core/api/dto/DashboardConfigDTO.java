package com.kalix.framework.core.api.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @类描述： dashboard-config(etc)数据模型
 * @创建人： hqj
 * @创建时间：2018/7/4
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class DashboardConfigDTO implements Serializable {

    private Boolean hasPanelGroup;
    private Integer panelCount;
    private List<PanelGroupDTO> panels;

    public Boolean getHasPanelGroup() {
        return hasPanelGroup;
    }

    public void setHasPanelGroup(Boolean hasPanelGroup) {
        this.hasPanelGroup = hasPanelGroup;
    }

    public Integer getPanelCount() {
        return panelCount;
    }

    public void setPanelCount(Integer panelCount) {
        this.panelCount = panelCount;
    }

    public List<PanelGroupDTO> getPanels() {
        return panels;
    }

    public void setPanels(List<PanelGroupDTO> panels) {
        this.panels = panels;
    }
}
