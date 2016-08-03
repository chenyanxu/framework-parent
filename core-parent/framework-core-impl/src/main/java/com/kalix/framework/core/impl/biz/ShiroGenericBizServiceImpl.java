package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.JNDIHelper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * @author chenyanxu
 */
public abstract class ShiroGenericBizServiceImpl<T extends IGenericDao, TP extends PersistentEntity> extends GenericBizServiceImpl<T, TP> {
    private IShiroService shiroService;

    public ShiroGenericBizServiceImpl(){
        try {
            this.shiroService= JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeSaveEntity(TP entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setCreateBy(userName);
            entity.setUpdateBy(userName);
        }

        super.beforeSaveEntity(entity,status);
    }

    @Override
    public void beforeUpdateEntity(TP entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setUpdateBy(userName);
        }

        super.beforeUpdateEntity(entity,status);
    }

    public IShiroService getShiroService() {
        return this.shiroService;
    }

    public void setShiroService(IShiroService shiroService) {}
}
