package <packageName>.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import <packageName>.api.biz.I<beanName>BeanService;
import <packageName>.api.dao.I<beanName>BeanDao;
import <packageName>.entities.<beanName>Bean;

/**
 * @类描述： 
 * @创建人：  
 * @创建时间： 
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class <beanName>BeanServiceImpl extends ShiroGenericBizServiceImpl\<I<beanName>BeanDao, <beanName>Bean> implements I<beanName>BeanService {
    private JsonStatus jsonStatus = new JsonStatus();

    public <beanName>BeanServiceImpl() {
        super.init(<beanName>Bean.class.getName());
    }
}
