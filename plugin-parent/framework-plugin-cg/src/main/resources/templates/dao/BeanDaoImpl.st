package <packageName>.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import <packageName>.api.dao.I<beanName>BeanDao;
import <packageName>.entities.<beanName>Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class <beanName>BeanDaoImpl extends GenericDao\<<beanName>Bean, Long> implements I<beanName>BeanDao {
    @Override
    @PersistenceContext(unitName = "<artifactIdPrefix>-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
    //todo add custom query

}
