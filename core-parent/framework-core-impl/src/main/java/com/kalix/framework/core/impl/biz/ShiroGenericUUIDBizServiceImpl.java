package com.kalix.framework.core.impl.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.kalix.framework.core.api.annotation.KalixCascade;
import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.dto.AuditDTOBean;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentUUIDEntity;
import com.kalix.framework.core.api.security.IDataAuthService;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.api.security.model.EnumDataAuth;
import com.kalix.framework.core.api.web.model.QueryDTO;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.HttpClientUtil;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.osgi.service.event.Event;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author chenyanxu
 */
public abstract class ShiroGenericUUIDBizServiceImpl<T extends IGenericDao, TP extends PersistentUUIDEntity> extends GenericUUIDBizServiceImpl<T, TP> {
    protected IShiroService shiroService;
    protected ICacheManager cacheManager;
    protected IDataAuthService dataAuthService;

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public ShiroGenericUUIDBizServiceImpl() {
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

        entity.setCreateById(String.valueOf(shiroService.getCurrentUserId()));
        entity.setUpdateById(String.valueOf(shiroService.getCurrentUserId()));

        //记录业务监控数据
        AuditDTOBean dtoBean = new AuditDTOBean();
        dtoBean.setClsName(this.entityClassName);
        dtoBean.setActor(userName);
        if (entity.getId() != null && !entity.getId().isEmpty()) {
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

        entity.setUpdateById(String.valueOf(shiroService.getCurrentUserId()));

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
     * @param map         查询结果map<实体类名, 执行语句>
     * @param jsonCascade redis中存储的级联信息
     * @param cascadeKey  要解析的key（实体类）
     * @param id          删除的实体类主键值
     * @param searchSql   拼接的操作查询串
     * @return
     */
    protected Map<String, String> getCascade(Map<String, String> map, JSONObject jsonCascade, String cascadeKey, String id, String searchSql) {
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
                        } else {
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
    public void beforeDeleteEntity(String id, JsonStatus status) {
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

        super.beforeDeleteEntity(id, status);
    }

    protected void postEvent(String topic, Object obj, Object oldEntity, Object newEntity) {
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

    @Override
    public QueryDTO addDataAuthQueryDTO(QueryDTO queryDTO) {
        try {
            Map<String, String> jsonMap = queryDTO.getJsonMap();
            if (jsonMap == null) {
                jsonMap = new HashMap<String, String>();
            }
            Long userId = shiroService.getCurrentUserId();
            if (this.dataAuthService == null) {
                this.dataAuthService = JNDIHelper.getJNDIServiceForName(IDataAuthService.class.getName());
            }
            EnumDataAuth enumDataAuth = dataAuthService.getDataAuth(userId);
            String ids = "";
            switch (enumDataAuth) {
                // 本人数据
                case SELF:
                    jsonMap.put("createById", String.valueOf(userId));
                    break;
                // 所有数据
                case ALL:
                    break;
                // 所在组织机构数据
                case SELF_ORG:
                    /*ids = this.findIdsByUserId(userId, enumDataAuth, 0); //按用户ids过滤
                    jsonMap.put("createbyid:in", ids);*/
                    ids = this.findIdsByUserId(String.valueOf(userId), enumDataAuth, 1); //按组织机构ids过滤
                    jsonMap.put("orgId:in", ids);
                    break;
                // 所在组织机构及以下子机构数据
                case SELF_AND_CHILD_ORG:
                    /*ids = this.findIdsByUserId(userId, enumDataAuth, 0); //按用户ids过滤
                    jsonMap.put("createbyid:in", ids);*/
                    ids = this.findIdsByUserId(String.valueOf(userId), enumDataAuth, 1); //按组织机构ids过滤
                    jsonMap.put("orgId:in", ids);
                    break;
            }
            queryDTO.setJsonMap(jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryDTO;
    }

    @Override
    public String addDataAuthNativeSql(String sql, String tableAlias, Boolean hasWhere) {
        try {
            String dataAuthSql = "";
            Long userId = shiroService.getCurrentUserId();
            if (this.dataAuthService == null) {
                this.dataAuthService = JNDIHelper.getJNDIServiceForName(IDataAuthService.class.getName());
            }
            EnumDataAuth enumDataAuth = dataAuthService.getDataAuth(userId);
            String ids = "";
            switch (enumDataAuth) {
                // 本人数据
                case SELF:
                    if (tableAlias == null || tableAlias.equals("")) {
                        dataAuthSql = "createbyid=" + userId;
                    } else {
                        dataAuthSql = tableAlias + ".createbyid=" + userId;
                    }
                    break;
                // 所有数据
                case ALL:
                    break;
                // 所在组织机构数据
                case SELF_ORG:
                    /*ids = this.findIdsByUserId(userId, enumDataAuth, 0); //按用户ids过滤
                    jsonMap.put("createbyid:in", ids);*/
                    ids = this.findIdsByUserId(String.valueOf(userId), enumDataAuth, 1); //按组织机构ids过滤
                    if (tableAlias == null || tableAlias.equals("")) {
                        dataAuthSql = "orgid in(" + ids + ")";
                    } else {
                        dataAuthSql = tableAlias + ".orgid in(" + ids + ")";
                    }
                    break;
                // 所在组织机构及以下子机构数据
                case SELF_AND_CHILD_ORG:
                    /*ids = this.findIdsByUserId(userId, enumDataAuth, 0); //按用户ids过滤
                    jsonMap.put("createbyid:in", ids);*/
                    ids = this.findIdsByUserId(String.valueOf(userId), enumDataAuth, 1); //按组织机构ids过滤
                    if (tableAlias == null || tableAlias.equals("")) {
                        dataAuthSql = "orgid in(" + ids + ")";
                    } else {
                        dataAuthSql = tableAlias + ".orgid in(" + ids + ")";
                    }
                    break;
            }
            if (!dataAuthSql.equals("")) {
                if (hasWhere) {
                    sql += " and " + dataAuthSql;
                } else {
                    sql += " where " + dataAuthSql;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    /**
     * 查找指定用户id其所在组织机构或组织机构及其下属机构的所有用户ids或组织机构ids
     *
     * @param userId
     * @param enumDataAuth
     * @param usersOrOrgs
     * @return
     */
    protected String findIdsByUserId(String userId, EnumDataAuth enumDataAuth, Integer usersOrOrgs) {
        String ids = "";
        String url = "";
        if (usersOrOrgs == 1) {
            url = "/orgs/ids?userId=" + userId + "&includeChildOrg=";
        } else {
            url = "/users/ids?userId=" + userId + "&includeChildOrg=";
        }
        switch (enumDataAuth) {
            case SELF_ORG:
                url += "false";
                break;
            case SELF_AND_CHILD_ORG:
                url += "true";
                break;
        }
        String getStr = "";
        try {
            String sessionId = this.getShiroService().getSession().getId().toString();
            String access_token = this.getShiroService().getSession().getAttribute("access_token").toString();
            getStr = HttpClientUtil.shiroGet(url, sessionId, access_token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Long> list = new ArrayList<>();
        if (getStr != null) {
            Type type = new TypeToken<ArrayList<Long>>() {
            }.getType();
            list = SerializeUtil.unserializeJson(getStr, type);
        }

        for (Long obj : list) {
            ids = ids + "," + String.valueOf(obj);
        }
        if (!ids.isEmpty())
            ids = ids.substring(1, ids.length());
        return ids;
    }

    public void setDataAuthService(IDataAuthService dataAuthService) {
        this.dataAuthService = dataAuthService;
    }
}
