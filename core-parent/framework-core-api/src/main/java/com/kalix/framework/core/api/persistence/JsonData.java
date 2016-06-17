package com.kalix.framework.core.api.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlf on 2015/7/3.
 */
public class JsonData {
    private Long totalCount;
    private List<PersistentEntity> data=new ArrayList<>();

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<PersistentEntity> getData() {
        return data;
    }

    public void setData(List<PersistentEntity> data) {
        this.data = data;
    }
}
