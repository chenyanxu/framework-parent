package com.kalix.framework.core.api.persistence;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/3.
 */
@ApiModel(value = "分页查询数据<br>JsonData")
public class JsonData {
    public static JsonData jsonData = new JsonData();
    @ApiModelProperty("总记录数")
    private Long totalCount = 0L;
    @ApiModelProperty("当前页记录集")
    private List data = new ArrayList<>();

    public static JsonData toJsonData (List newData) {
        if (newData != null) {
            jsonData.setData(newData);
            jsonData.setTotalCount((long) newData.size());
        }
        return jsonData;
    }
    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
