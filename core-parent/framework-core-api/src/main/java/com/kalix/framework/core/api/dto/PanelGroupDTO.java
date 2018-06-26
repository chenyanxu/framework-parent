package com.kalix.framework.core.api.dto;


import java.io.Serializable;

/**
 * @类描述： 前台表格PanelGroup数据模型
 * @创建人： hqj
 * @创建时间：2018/6/26
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class PanelGroupDTO implements Serializable {

    private String key;                 // 数据唯一标识
    private String iconName;            // 图标名称，如"peoples"
    private String iconBackgroundColor; // 图标背景颜色，如"#40c9c6"
    private String text;                // 显示文本内容
    private Integer startVal = 0;       // 初始值
    private Integer endVal;             // 当前统计值
    private Integer duration;           // 持续时间值

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(String iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStartVal() {
        return startVal;
    }

    public void setStartVal(Integer startVal) {
        this.startVal = startVal;
    }

    public Integer getEndVal() {
        return endVal;
    }

    public void setEndVal(Integer endVal) {
        this.endVal = endVal;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
