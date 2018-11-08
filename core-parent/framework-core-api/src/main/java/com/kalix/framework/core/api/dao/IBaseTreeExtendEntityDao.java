package com.kalix.framework.core.api.dao;

import com.kalix.framework.core.api.persistence.BaseTreeExtendEntity;
import com.kalix.framework.core.api.persistence.JsonData;

import java.io.Serializable;
import java.util.List;

/**
 * 树扩展结构表管理DAO接口
 *
 * @author hqj date:2018-07-16
 * @version 1.0.0
 */
public interface IBaseTreeExtendEntityDao<T extends BaseTreeExtendEntity, PK extends Serializable> extends IGenericDao<T, PK> {

    /**
     * 通过树结构id查找实体信息
     *
     * @param treeId
     * @return
     */
    List<T> findByTreeId(String treeId);

    /**
     * 分页查询，通过树结构id查找实体信息
     *
     * @param treeId
     * @param page
     * @param limit
     * @param sort
     * @return
     */
    JsonData findByTreeId(String treeId, Integer page, Integer limit, String jsonStr, String sort);
}
