

package com.kalix.framework.core.impl.persistence;


import com.kalix.framework.core.api.persistence.PersistEntityService;

import javax.persistence.EntityManager;

public class PersistEntityServiceImpl implements PersistEntityService {
    //    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * {@inheritDoc}
     */
   /* public <T extends PersistentEntity> T save(T entity) {
        if (entity.getIdentifier() != null) {
            return em.merge(entity);
        } else {
            em.persist(entity);
            return entity;
        }
    }*/

    /**
     * {@inheritDoc}
     */
    /*public <T extends PersistentEntity> Collection<T> save(Collection<T> entities) {
        List<T> updated = new ArrayList<T>(entities.size());
        for (T e : entities) {
            updated.add(em.merge(e));
        }
        return updated;
    }*/

    /**
     * {@inheritDoc}
     */
    /*@Override
    public <T extends BizEntity> T findPlatformEntityByKey(Class<T> aClass, String key) {
        final Query query = em.createQuery("select c from " + aClass.getName() + " c where c.key = ?1");
        query.setParameter(1, key);
        final List<Object> resultList = query.getResultList();
        if (resultList.size() == 1) {
            return (T) resultList.get(0);
        } else {
            return null;
        }*/

//    }
}
