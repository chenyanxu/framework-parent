package com.kalix.framework.core.impl.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.dto.AuditDTOBean;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.JNDIHelper;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.event.Event;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author chenyanxu
 */
public abstract class ShiroGenericBizServiceImpl<T extends IGenericDao, TP extends PersistentEntity> extends GenericBizServiceImpl<T, TP> {
    private IShiroService shiroService;

    public ShiroGenericBizServiceImpl() {
        try {
            this.shiroService = JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());
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

        entity.setCreateById(shiroService.getCurrentUserId());
        entity.setUpdateById(shiroService.getCurrentUserId());

        //记录业务监控数据
        AuditDTOBean dtoBean = new AuditDTOBean();
        dtoBean.setClsName(this.entityClassName);
        dtoBean.setActor(userName);
        if (entity.getId() > 0) {
            dtoBean.setAction("更新");
            final TP oldEntity = (TP) dao.get(entity.getId());
            dtoBean.setOldEntity(oldEntity);
            dtoBean.setNewEntity(entity);
            postEvent(EVENT_TOPIC + "update", dtoBean, oldEntity, entity);
        } else {
            dtoBean.setAction("添加");
            dtoBean.setNewEntity(entity);
            postEvent(EVENT_TOPIC + "save", dtoBean, null, entity);
        }
        super.beforeSaveEntity(entity, status);
    }

    @Override
    public void beforeUpdateEntity(TP entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");

        if (StringUtils.isNotEmpty(userName)) {
            entity.setUpdateBy(userName);
        }

        entity.setUpdateById(shiroService.getCurrentUserId());

        //记录业务监控数据
        AuditDTOBean dtoBean = new AuditDTOBean();
        dtoBean.setClsName(this.entityClassName);
        dtoBean.setActor(userName);
        dtoBean.setAction("更新");
        final TP oldEntity = (TP) dao.get(entity.getId());
        dtoBean.setOldEntity(oldEntity);
        dtoBean.setNewEntity(entity);
        postEvent(EVENT_TOPIC + "update", dtoBean, oldEntity, entity);

        super.beforeUpdateEntity(entity, status);
    }

    @Override
    public void beforeDeleteEntity(Long id, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        //记录业务监控数据 delete
        AuditDTOBean dtoBean = new AuditDTOBean();
        dtoBean.setActor(userName);
        dtoBean.setClsName(this.entityClassName);
        dtoBean.setAction("删除");
        final TP oldEntity = (TP) dao.get(id);
        dtoBean.setOldEntity(oldEntity);
        postEvent(EVENT_TOPIC + "delete", dtoBean, oldEntity, null);

    }

    private void postEvent(String topic, Object obj, Object oldEntity, Object newEntity) {
        if (eventAdmin != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getFactory().enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
            Dictionary properties = new Hashtable();
            try {
                properties.put("message", mapper.writeValueAsString(obj));
                properties.put("oldEntity", mapper.writeValueAsString(oldEntity));
                properties.put("newEntity", mapper.writeValueAsString(newEntity));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Event event = new Event(topic, properties);
            eventAdmin.postEvent(event);
        }
    }

    public IShiroService getShiroService() {
        return this.shiroService;
    }

    public void setShiroService(IShiroService shiroService) {
    }
}
