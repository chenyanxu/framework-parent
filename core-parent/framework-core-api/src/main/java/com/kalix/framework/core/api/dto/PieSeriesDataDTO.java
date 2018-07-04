package com.kalix.framework.core.api.dto;


import java.io.Serializable;

/**
 * @类描述： 前台表格Pie-Series数据模型
 * @创建人： hqj
 * @创建时间：2018/7/4
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class PieSeriesDataDTO implements Serializable {

    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
