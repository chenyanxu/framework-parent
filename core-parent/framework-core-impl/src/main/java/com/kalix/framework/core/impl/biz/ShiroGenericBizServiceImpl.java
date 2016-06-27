package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.Assert;
import org.apache.commons.lang.StringUtils;

/**
 * @author chenyanxu
 */
public abstract class ShiroGenericBizServiceImpl<T extends IGenericDao, TP extends PersistentEntity> extends GenericBizServiceImpl<T, TP> {
    private IShiroService shiroService;

    @Override
    public void beforeSaveEntity(TP entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setCreateBy(userName);
            entity.setUpdateBy(userName);
        }

        super.beforeSaveEntity(entity,status);
    }

    @Override
    public void beforeUpdateEntity(TP entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setUpdateBy(userName);
        }

        super.beforeUpdateEntity(entity,status);
    }

    public IShiroService getShiroService() {
        return this.shiroService;
    }

    public void setShiroService(IShiroService shiroService) {
        this.shiroService = shiroService;
    }
}
