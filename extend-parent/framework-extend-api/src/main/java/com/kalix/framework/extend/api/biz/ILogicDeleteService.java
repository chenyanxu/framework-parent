package com.kalix.framework.extend.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.extend.api.entities.BaseLogicDeleteEntity;

/**
 * @类描述： 对外业务服务的根接口
 * @创建人：hqj
 * @创建时间：2018-9-12
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface ILogicDeleteService<T extends BaseLogicDeleteEntity> extends IService {
    /**
     * 逻辑删除前执行函数.
     *
     * @param entity
     * @param status
     */
    void beforeLogicDeleteEntity(T entity, JsonStatus status);

    /**
     * 逻辑删除后执行函数
     *
     * @param entity
     * @param status
     */
    void afterLogicDeleteEntity(T entity, JsonStatus status);

    /**
     * 是否执行逻辑删除.
     *
     * @param entity
     * @param status
     * @return
     */
    boolean isLogicDelete(T entity, JsonStatus status);

    /**
     * 逻辑删除实体.
     *
     * @param entity
     * @return
     */
    JsonStatus logicDeleteEntity(T entity);

    /**
     * 批量逻辑删除实体.
     *
     * @param entityIds
     * @param reason
     * @return
     */
    JsonStatus batchLogicDeleteEntity(String entityIds, String reason);

    /**
     * 执行逻辑删除.
     *
     * @param entity
     * @param jsonStatus
     */
    void doLogicDelete(T entity, JsonStatus jsonStatus);

    /**
     * 执行批量逻辑删除.
     *
     * @param entityIds
     * @param reason
     * @param jsonStatus
     */
    void doBatchLogicDelete(String entityIds, String reason, JsonStatus jsonStatus);
}
