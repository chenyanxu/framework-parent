package com.kalix.framework.core.api.system;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.PersistentEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */
public interface IDictBeanService<TP extends PersistentEntity> extends IBizService<TP> {
    List<Map> getDictTypes(String query);
}
