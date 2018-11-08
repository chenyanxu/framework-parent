package com.kalix.framework.core.impl.dao;

import com.kalix.framework.core.api.dao.IBaseTreeExtendEntityDao;
import com.kalix.framework.core.api.persistence.BaseTreeExtendEntity;
import com.kalix.framework.core.api.persistence.JsonData;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by hqj on 2018/07/16.
 */
public abstract class BaseTreeExtendEntityDao<T extends BaseTreeExtendEntity, PK extends Serializable> extends GenericDao<T, PK> implements IBaseTreeExtendEntityDao<T, PK> {
    protected Class<T> baseTreeExtendEntityClass;

    public BaseTreeExtendEntityDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = genericSuperclass.getActualTypeArguments()[0];
        if (type instanceof Class) {
            this.baseTreeExtendEntityClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            this.baseTreeExtendEntityClass = (Class<T>) ((ParameterizedType) type).getRawType();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByTreeId(String treeId) {
        String hql = "select t from " + this.classSimpleName + " t where t.treeId = ?1";
        return (List<T>) this.find(hql, treeId);
    }

    @Override
    public JsonData findByTreeId(String orgId, Integer page, Integer limit, String jsonStr, String sort) {
        String sql = "select t.* from (select a.* from " + super.getTableName() + " a where a.treeid = ?1) t";
        sql += CommonMethod.createWhereCondition(jsonStr, sort);
        return this.findByNativeSql(sql, page, limit, this.baseTreeExtendEntityClass, orgId);
    }
}
