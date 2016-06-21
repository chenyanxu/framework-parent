package com.kalix.framework.core.api.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/3.
 */
public class JsonData {
    private Long totalCount;
    private List data=new ArrayList<>();

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
