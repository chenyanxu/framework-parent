package com.kalix.framework.core.api.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于客户端使用的带泛型的JsonData
 *  例子：
 *  GenericJsonData<OrganizationDTO> jsonData = new GenericJsonData();
 *  if (getStr != null) {
 *  Type type = new TypeToken<GenericJsonData<OrganizationDTO>>() {}.getType();
 *  jsonData = SerializeUtil.unserializeJson(getStr, type);
 * }
 * Created by sunlf on 2015/7/3.
 */
public class GenericJsonData<T> {
    private Long totalCount;
    private List<T> data=new ArrayList<>();

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
