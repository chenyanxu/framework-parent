package com.kalix.framework.core.impl.dao;

import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.dto.PersistentBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @类描述：DAO数据访问通用实现类
 * @创建人：hqj
 * @创建时间：2018-7-6
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class BaseGenericDaoImpl extends GenericDao<PersistentBean, Long> implements IGenericDao<PersistentBean, Long> {

    @Override
    @PersistenceContext(unitName = "framework-core-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}

