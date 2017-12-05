package com.kalix.framework.core.api.dao;

import com.kalix.framework.core.api.persistence.BaseTreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 树结构表管理DAO接口
 * @author hqj date:2017-12-04
 * @version 1.0.0
 */
public interface IBaseTreeEntityDao<T extends BaseTreeEntity,PK extends Serializable> extends IGenericDao<T,PK> {

    /**
     * 查询指定代码的实体（不包括指定的id）
     *
     * @param id
     * @param code
     * @return
     */
    List<T> findByCode(Long id, String code);

    /**
     * 查询指定代码的实体
     *
     * @param code
     * @return
     */
    List<T> findByCode(String code);

    /**
     * 查询指定父代码下指定名称的实体（不包括指定的id）
     *
     * @param parentId
     * @param id
     * @param name
     * @return
     */
    List<T> findByName(Long parentId, Long id, String name);

    /**
     * 查询指定父代码的实体
     *
     * @param parentId
     * @return
     */
    List<T> findByParentId(Long parentId);

    /**
     * 查询所有id集合中的实体
     *
     * @param id
     * @return
     */
    List<T> findById(List<Long> id);
}
