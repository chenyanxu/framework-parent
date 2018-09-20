package com.kalix.framework.extend.impl.biz;

import com.kalix.framework.core.api.annotation.KalixCascade;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.dto.AuditDTOBean;
import com.kalix.framework.core.api.exception.KalixRuntimeException;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.extend.api.biz.ILogicDeleteService;
import com.kalix.framework.extend.api.entities.BaseLogicDeleteEntity;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hqj
 */
public abstract class LogicDeleteGenericBizServiceImpl<T extends IGenericDao, TP extends BaseLogicDeleteEntity>
        extends ShiroGenericBizServiceImpl<T, TP> implements ILogicDeleteService<TP> {

    /**
     * 执行逻辑删除.
     *
     * @param entity
     * @param jsonStatus
     */
    @Override
    @Transactional
    public void doLogicDelete(TP entity, JsonStatus jsonStatus) {
        dao.save(entity);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("删除成功！");
    }

    /**
     * 执行批量逻辑删除.
     *
     * @param entityIds
     * @param reason
     * @param jsonStatus
     */
    @Override
    @Transactional
    public void doBatchLogicDelete(String entityIds, String reason, JsonStatus jsonStatus) {
        String strIds = entityIds;
        if (strIds != null && strIds.trim().length() > 0) {
            Object object;
            strIds = strIds.replaceAll(",", ":");
            strIds = strIds.replaceAll(";", ":");
            String[] arrayIds = strIds.split(":");
            for (int i = 0; i < arrayIds.length; i++) {
                if (arrayIds[i] != null && arrayIds[i].trim().length() > 0) {
                    object = dao.get(arrayIds[i]);
                    TP entity = (TP) object;
                    entity.setDelFlag("1");
                    entity.setReason(reason);
                    dao.save(object);
                }
            }
        }
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("批量删除成功！");
    }

    /**
     * 逻辑删除前执行函数.
     *
     * @param entity
     * @param status
     */
    @Override
    public void beforeLogicDeleteEntity(TP entity, JsonStatus status) {
        String jedisString = cacheManager.get(KalixCascade.alias);
        if (jedisString != null && !jedisString.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put(super.persistentClass.getName(), "");
            // 递归方式搜素是否有关于自己的依赖信息，并存储在map中
            Long id = entity.getId();
            map = getCascade(map, new JSONObject(jedisString), super.persistentClass.getName(), id, "");

            for (String key : map.keySet()) {
                if (map.get(key) != null && !map.get(key).isEmpty()) {
                    dao.updateNativeQuery(map.get(key));
                }
            }
        }

        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setUpdateBy(userName);
        }
        entity.setUpdateById(shiroService.getCurrentUserId());
        entity.setDelFlag("1");

        //记录业务监控数据 logicDelete
        AuditDTOBean dtoBean = new AuditDTOBean();
        dtoBean.setClsName(this.entityClassName);
        dtoBean.setActor(userName);
        dtoBean.setAction("删除");
        final TP oldEntity = (TP) dao.get(entity.getId());
        dtoBean.setOldEntity(oldEntity);
        dtoBean.setNewEntity(entity);
        postEvent(EVENT_TOPIC + "logicDelete", dtoBean, oldEntity, entity);

        super.postEvent(this.entityClassName.replace(".", "/") + "/before/logicDelete", entity);
    }

    /**
     * 逻辑删除后执行函数
     *
     * @param entity
     * @param status
     */
    @Override
    public void afterLogicDeleteEntity(TP entity, JsonStatus status) {
        super.postEvent(this.entityClassName.replace(".", "/") + "/after/logicDelete", entity);
    }

    /**
     * 是否执行逻辑删除.
     *
     * @param entity
     * @param status
     * @return
     */
    @Override
    public boolean isLogicDelete(TP entity, JsonStatus status) {
        return true;
    }

    /**
     * 逻辑删除实体.
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public JsonStatus logicDeleteEntity(TP entity) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            if (isLogicDelete(entity, jsonStatus)) {
                beforeLogicDeleteEntity(entity, jsonStatus);
                doLogicDelete(entity, jsonStatus);
                afterLogicDeleteEntity(entity, jsonStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new KalixRuntimeException("删除失败！", e.getMessage());
        }
        return jsonStatus;
    }

    /**
     * 批量逻辑删除实体.
     *
     * @param entityIds
     * @param reason
     * @return
     */
    @Override
    @Transactional
    public JsonStatus batchLogicDeleteEntity(String entityIds, String reason) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            doBatchLogicDelete(entityIds, reason, jsonStatus);
        } catch (Exception e) {
            throw new KalixRuntimeException("批量删除失败！", e.getMessage());
        }
        return jsonStatus;
    }
}
