package com.kalix.framework.core.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.web.model.QueryDTO;

import java.util.List;

/**
 * @类描述： 对外业务服务的根接口
 * @创建人：sunlf
 * @创建时间：2014-3-27 下午1:01:59
 * @修改人：sunlf
 * @修改时间：2014-3-27 下午1:01:59
 * @修改备注：
 */


public interface IBizService<T extends PersistentEntity> extends IService {

    /**
     * 删除前执行函数.
     * @param id
     * @param status
     */
    void beforeDeleteEntity(Long id, JsonStatus status);

    /**
     * 删除后执行函数
     * @param id
     * @param status
     */
    void afterDeleteEntity(Long id, JsonStatus status);

    /**
     * 是否执行删除.
     * @param entityId
     * @param status
     * @return
     */
    boolean isDelete(Long entityId, JsonStatus status);

    /**
     * 删除实体.
     * @param entityId
     * @return
     */
    JsonStatus deleteEntity(long entityId);

    /**
     * 批量删除实体.
     * @param entityIds
     * @return
     */
    JsonStatus batchDeleteEntity(String entityIds);

    /**
     * 删除实体.
     * @param entity
     * @return
     */
    @Deprecated
    JsonStatus removeEntity(T entity);

    /**
     * 执行删除.
     * @param entityId
     * @param jsonStatus
     */
    void doDelete(long entityId, JsonStatus jsonStatus);

    /**
     * 执行批量删除.
     * @param entityIds
     * @param jsonStatus
     */
    void doBatchDelete(String entityIds, JsonStatus jsonStatus);

    /**
     * 保存前执行.
     * @param entity
     * @param status
     */
    void beforeSaveEntity(T entity, JsonStatus status);

    /**
     * 保存后执行.
     * @param entity
     * @param status
     */
    void afterSaveEntity(T entity, JsonStatus status);

    /**
     * 是否保存.
     * @param entity
     * @param status
     * @return
     */
    boolean isSave(T entity, JsonStatus status);

    /**
     * 保存实体.
     * @param entity
     * @return
     */
    JsonStatus saveEntity(T entity);

    /**
     * 执行保存.
     * @param entity
     * @param jsonStatus
     */
    void doSave(T entity, JsonStatus jsonStatus);

    /**
     * 更新前执行.
     * @param entity
     * @param status
     */
    void beforeUpdateEntity(T entity, JsonStatus status);

    /**
     * 更新后执行.
     * @param entity
     * @param status
     */
    void afterUpdateEntity(T entity, JsonStatus status);

    /**
     * 是否更新.
     * @param entity
     * @param status
     * @return
     */
    boolean isUpdate(T entity, JsonStatus status);

    /**
     * 更新实体.
     * @param entity
     * @return
     */
    JsonStatus updateEntity(T entity);

    /**
     * 执行更新.
     * @param entity
     * @param jsonStatus
     */
    void doUpdate(T entity, JsonStatus jsonStatus);

    Object saveEntityAndReturn(PersistentEntity entity);

    /**
     * 按条件查询
     * @param queryDTO
     * @return
     */
    JsonData getAllEntityByQuery(QueryDTO queryDTO);

    /**
     * @param page
     * @param limit
     * @param jsonStr
     * @return
     */
    JsonData getAllEntityByQuery(int page, int limit, String jsonStr);

    /**
     * @param page
     * @param limit
     * @param jsonStr
     * @return
     */
    JsonData getAllByNativeQuery(int page, int limit, String jsonStr);

    /**
     * 查询分页
     * @param pageNumber
     * @param pageSize
     * @return
     */
    JsonData getAllEntity(int pageNumber, int pageSize);

    List getAllEntity();

    /**
     * 用于报表的查询，返回全部数据
     *
     * @return
     */
    JsonData getAllEntityforReport(String jsonStr);

    T getEntity(long entityId);

    List<Object> getFieldValuesByIds(Object[] ids,String fieldName);
}
