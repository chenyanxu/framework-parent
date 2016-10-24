package com.kalix.framework.core.impl.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.dto.AuditDTOBean;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.api.annotation.KalixCascade;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.osgi.service.event.Event;

import java.io.IOException;
import java.util.*;

/**
 * @author chenyanxu
 */
public abstract class ShiroGenericBizServiceImpl<T extends IGenericDao, TP extends PersistentEntity> extends GenericBizServiceImpl<T, TP> {
    protected IShiroService shiroService;
    protected ICacheManager cacheManager;

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public ShiroGenericBizServiceImpl() {
        try {
            this.shiroService = JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());
            this.cacheManager = JNDIHelper.getJNDIServiceForName(ICacheManager.class.getName());
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

    /**
     * 解析redis中存储的级联信息
     *
     * @param map   查询结果map<实体类名, 执行语句>
     * @param jsonCascade   redis中存储的级联信息
     * @param cascadeKey    要解析的key（实体类）
     * @param id    删除的实体类主键值
     * @param searchSql 拼接的操作查询串
     * @return
     */
    private Map<String, String> getCascade(Map<String, String> map, JSONObject jsonCascade, String cascadeKey, Long id, String searchSql) {
        if (jsonCascade.has(cascadeKey)) {
            JSONObject mainJsonCascade = jsonCascade.getJSONObject(cascadeKey);

            for (Iterator iter = mainJsonCascade.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                // 判断需要查找的key是否在map中存在，防止循环递归
                if (map.get(key) == null || map.get(key).isEmpty()) {
                    JSONObject object = mainJsonCascade.getJSONObject(key);
                    if (object != null) {
                        String operation = object.get("operation").toString();
                        String table = object.get("table").toString();
                        String primaryKey = object.get("primaryKey").toString();
                        String foreignKey = object.get("foreignKey").toString();

                        String operationSql = "";
                        String mySearchSql = "";

                        if (searchSql == "") {
                            // 不存在查询语句
                            operationSql = operation + " from " + table + " where " + foreignKey + " = " + id;
                            mySearchSql = "select " + primaryKey + " from " + table + " where " + foreignKey + " = " + id;
                        }
                        else {
                            // 存在查询语句，拼接in条件
                            operationSql = operation + " from " + table + " where " + foreignKey + " in (" + searchSql + ") ";
                            mySearchSql = "select id from " + table + " where " + foreignKey + " in (" + searchSql + ") ";
                        }

                        // 递归查询
                        map = getCascade(map, jsonCascade, key, id, mySearchSql);

                        // 查询结果存储于map
                        map.put(key, operationSql);
                    }
                }
            }
        }

        return map;
    }

    @Override
    public void beforeDeleteEntity(Long id, JsonStatus status) {
        String jedisString = cacheManager.get(KalixCascade.alias);
        if (jedisString != null && !jedisString.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put(super.persistentClass.getName(), "");
            // 递归方式搜素是否有关于自己的依赖信息，并存储在map中
            map = getCascade(map, new JSONObject(jedisString), super.persistentClass.getName(), id, "");

            for (String key : map.keySet()) {
                if (map.get(key) != null && !map.get(key).isEmpty()) {
                    dao.updateNativeQuery(map.get(key));
                }
            }
        }


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
