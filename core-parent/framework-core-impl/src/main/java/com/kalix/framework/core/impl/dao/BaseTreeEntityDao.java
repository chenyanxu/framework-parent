package com.kalix.framework.core.impl.dao;

import com.kalix.framework.core.api.dao.IBaseTreeEntityDao;
import com.kalix.framework.core.api.persistence.BaseTreeEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqj on 2017/12/04.
 */
public abstract class BaseTreeEntityDao<T extends BaseTreeEntity, PK extends Serializable> extends GenericDao<T, PK> implements IBaseTreeEntityDao<T, PK> {
    @Override
    public List<T> findByCode(String id, String code) {
        String hql = "select ob from " + this.classSimpleName + " ob where ob.id <> ?1 and ob.code = ?2 order by ob.code";
        return (List<T>) this.find(hql, id, code);
    }

    @Override
    public List<T> findByCode(String code) {
        String hql = "select ob from " + this.classSimpleName + " ob where ob.code like ?1 order by ob.code";
        return (List<T>) this.find(hql, code + "%");
    }

    @Override
    public List<T> findByName(String parentId, String id, String name) {
        String hql = "select ob from " + this.classSimpleName + " ob where ob.parentId = ?1 and ob.id <> ?2 and ob.name = ?3 order by ob.code";
        return (List<T>) this.find(hql, parentId, id, name);
    }

    @Override
    public List<T> findByParentId(String parentId) {
        String hql = "select ob from " + this.classSimpleName + " ob where ob.parentId = ?1 order by ob.code";
        return (List<T>) this.find(hql, parentId);
    }

    @Override
    public List<T> findById(List<String> id) {
        if (id != null && !id.isEmpty()) {
            String hql = "select ob from " + this.classSimpleName + " ob where ob.id in (?1) order by ob.code";
            return (List<T>) this.find(hql, id);
        } else {
            return new ArrayList<>();
        }
    }
}
