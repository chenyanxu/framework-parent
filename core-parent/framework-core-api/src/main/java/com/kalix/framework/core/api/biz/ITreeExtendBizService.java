package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.persistence.BaseTreeExtendEntity;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;

/**
 * @类描述： 对外业务服务的根接口
 * @创建人：hqj
 * @创建时间：2018-07-16
 * @修改人：
 * @修改时间：
 * @修改备注：
 */

public interface ITreeExtendBizService<T extends BaseTreeExtendEntity> extends IBizService<T> {

    JsonData getEntitiesByFK(Long fk, Integer page, Integer limit, String jsonStr, String sort);

    JsonStatus saveEntityByFK(Long fk, T entity);

    JsonStatus updateEntityByFK(Long fk, T entity);

    JsonStatus deleteEntityByFK(Long fk, Long entityId);
}
