package com.kalix.framework.core.impl.biz;

import com.google.gson.Gson;
import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.web.model.BaseDTO;
import com.kalix.framework.core.api.web.model.QueryDTO;
import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.BeanUtil;
import com.kalix.framework.core.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.*;


/**
 * @类描述： 对外服务的抽象接口实现, 封装了基本的对数据库的操作
 * @创建人：sunlf
 * @创建时间：2014-3-27 下午1:01:59
 * @修改人：chenyanxu
 * @修改时间：2015-10-27 下午1:01:59
 * @修改备注：
 */
public abstract class GenericBizServiceImpl<T extends IGenericDao, TP extends PersistentEntity> implements IBizService<TP> {
    protected T dao;
    protected String entityClassName;
    protected Class<T> persistentClass;
    protected final Logger logger = Logger.getLogger(this.getClass());
    protected EventAdmin eventAdmin;

    public GenericBizServiceImpl() {
        Object obj = this.getClass().getGenericSuperclass();
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = genericSuperclass.getActualTypeArguments()[1];
        if (type instanceof Class) {
            this.persistentClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            this.persistentClass = (Class<T>) ((ParameterizedType) type).getRawType();
        }

        this.entityClassName = this.persistentClass.getName();
    }

    public void setDao(T dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void doDelete(long entityId, JsonStatus jsonStatus) {
        dao.remove(entityId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("删除成功！");
    }

    @Override
    @Transactional
    public void doBatchDelete(String entityIds, JsonStatus jsonStatus) {
        dao.removeBatch(entityIds);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("批量删除成功！");
    }

    @Override
    @Transactional
    public void doSave(TP entity, JsonStatus jsonStatus) {
        if (entity.getId() == 0) {
            jsonStatus.setMsg("添加成功！");
        } else {
            jsonStatus.setMsg("修改成功！");
        }

        PersistentEntity newObj = (PersistentEntity) dao.save(entity);

        jsonStatus.setTag(String.valueOf(newObj.getId()));
        jsonStatus.setSuccess(true);
    }

    @Override
    @Transactional
    public void doUpdate(TP entity, JsonStatus jsonStatus) {
        dao.save(entity);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("修改成功！");
    }

    @Override
    public void beforeDeleteEntity(Long id, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/before/delete", id);
    }

    @Override
    public void afterDeleteEntity(Long id, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/after/delete", id);
    }

    @Override
    public boolean isDelete(Long entityId, JsonStatus status) {
        return true;
    }

    @Override
    @Transactional
    public JsonStatus deleteEntity(long entityId) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            if (isDelete(entityId, jsonStatus)) {
                beforeDeleteEntity(entityId, jsonStatus);
                doDelete(entityId, jsonStatus);
                afterDeleteEntity(entityId, jsonStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("删除失败！");
        }
        return jsonStatus;
    }

    @Override
    @Transactional
    public JsonStatus batchDeleteEntity(String entityIds) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            doBatchDelete(entityIds, jsonStatus);
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("批量删除失败！");
        }

        return jsonStatus;
    }

    @Override
    @Transactional
    public JsonStatus removeEntity(TP entity) {
        return deleteEntity(entity.getId());
    }

    @Override
    @Transactional
    public void beforeSaveEntity(TP entity, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/before/save", entity);
    }

    @Override
    public void afterSaveEntity(TP entity, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/after/save", entity);
    }

    @Override
    public boolean isSave(TP entity, JsonStatus status) {
        return true;
    }


    @Override
    @Transactional
    public JsonStatus saveEntity(TP entity) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            if (isSave(entity, jsonStatus)) {
                beforeSaveEntity(entity, jsonStatus);
                doSave(entity, jsonStatus);
                afterSaveEntity(entity, jsonStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            /*if (entity.getId() == 0)
                jsonStatus.setMsg("添加失败！");
            else
                jsonStatus.setMsg("修改失败！");*/
            jsonStatus.setMsg("服务器异常，操作失败！");
        }
        return jsonStatus;

    }

    @Override
    public void beforeUpdateEntity(TP entity, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/before/update", entity);
    }

    @Override
    public void afterUpdateEntity(TP entity, JsonStatus status) {
        postEvent(this.entityClassName.replace(".", "/") + "/after/update", entity);
    }

    @Override
    public boolean isUpdate(TP entity, JsonStatus status) {
        return true;
    }

    @Override
    @Transactional
    public JsonStatus updateEntity(TP entity) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            if (isUpdate(entity, jsonStatus)) {
                beforeUpdateEntity(entity, jsonStatus);
                doUpdate(entity, jsonStatus);
                afterUpdateEntity(entity, jsonStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg("修改失败！");
        }
        return jsonStatus;
    }


    @Override
    @Transactional
    public Object saveEntityAndReturn(PersistentEntity entity) {
        return dao.save(entity);
    }


    @Override
    public JsonData getAllEntityByQuery(QueryDTO queryDTO) {
        Assert.notNull(queryDTO, "查询条件不能为空.");
        return dao.getAll(queryDTO.getPage(), queryDTO.getLimit(), dao.buildCriteriaQuery(queryDTO));
    }

    /**
     * 只适应于字符类型的关联查询场景，需要自己实现getNativeQueryStr和getResultClass方法
     *
     * @param page
     * @param limit
     * @param jsonStr
     * @return
     */
    @Override
    public JsonData getAllByNativeQuery(int page, int limit, String jsonStr) {

        Map<String, String> jsonMap = SerializeUtil.json2Map(jsonStr);
        //获得查询的sql语句
        String sql = getNativeQueryStr();
        Assert.notNull(sql, "查询条件不能为空.");
        //获得返回的结果类
        Class<? extends BaseDTO> cls = getResultClass();
        Assert.notNull(cls, "返回查询结果类不能为空.");

        String posSql = " order by a.creationDate desc";
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            sql = sql + " and a." + entry.getKey() + " like '%" + entry.getValue() + "%'";
        }

        return dao.findByNativeSql(sql + posSql, page, limit, cls, null);
    }

    /**
     * 需要重写该类实现getAllByNativeQuery
     *
     * @return
     */
    protected String getNativeQueryStr() {
        return null;
    }

    /**
     * 需要重写该类实现getAllByNativeQuery
     *
     * @return
     */
    protected Class<? extends BaseDTO> getResultClass() {
        return null;
    }


    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr) {
        QueryDTO queryDto = new QueryDTO();

        if (page == null) {
            queryDto.setPage(0);
        } else {
            queryDto.setPage(page);
        }

        if (limit == null) {
            queryDto.setLimit(0);
        } else {
            queryDto.setLimit(limit);
        }

        queryDto.setJsonMap(SerializeUtil.json2Map(jsonStr));

        return getAllEntityByQuery(queryDto);
    }

    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        List<Map> sortList;
        String sortJsonStr=jsonStr;

        if (sort != null) {
            sortList = SerializeUtil.unserializeJson(sort, List.class);

            if(sortList!=null && sortList.size()==1){
                String sortField= (String) sortList.get(0).get("property");
                String direction= (String) sortList.get(0).get("direction");

                sortJsonStr=jsonStr.substring(0,jsonStr.length()-1)+",\""+sortField+":sort\":\""+direction+"\"}";
            }
        }

        return getAllEntityByQuery(page, limit, sortJsonStr);
    }

    @Override
    public JsonData getAllEntity(int pageNumber,
                                 int pageSize) {
        return dao.getAll(pageNumber, pageSize);
    }

    @Override
    public List getAllEntity() {
        return dao.getAll();
    }

    /**
     * 用于报表的查询，返回全部数据
     *
     * @return
     */
    @Override
    public JsonData getAllEntityforReport(String jsonStr) {
        QueryDTO queryDto = new QueryDTO();
        queryDto.setJsonMap(SerializeUtil.json2Map(jsonStr));
        return dao.getAll(dao.buildCriteriaQuery(queryDto));
    }

    @Override
    public TP getEntity(long entityId) {
        return (TP) dao.get(entityId);
    }

    @Override
    public List<Object> getFieldValuesByIds(Object[] ids, String fieldName) {
        if (ids == null || ids.length <= 0)
            return null;
        String sql = "SELECT a FROM %s a WHERE a.id in (%s)";
        List queryIds = new ArrayList<Long>();

        for (int index = 0; index < ids.length; ++index) {
            if (ids[index] != null && !ids[index].equals("")) {
                if (!queryIds.contains(ids[index])) {
                    queryIds.add(ids[index]);
                }
            }
        }

        sql = String.format(sql, this.entityClassName, StringUtils.join(queryIds, ","));

        List records = this.dao.find(sql);

        if (records.size() > 0) {
            Map fieldValueMap = BeanUtil.getBeanFieldValueMap(records, fieldName);
            List rtn = new ArrayList<Object>();

            for (int idsIndex = 0; idsIndex < ids.length; ++idsIndex) {
                rtn.add(fieldValueMap.get(ids[idsIndex].toString()));
            }

            return rtn;
        }

        return null;
    }


    /**
     * 具体的业务类必须调用该方法
     *
     * @param entityClassName 实体类的名字
     */
    public void init(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    private void postEvent(String topic, Object obj) {
        if (eventAdmin != null) {
            Gson gson = new Gson();
            Dictionary properties = new Hashtable();
            properties.put("message", gson.toJson(obj));
            Event event = new Event(topic, properties);
            eventAdmin.postEvent(event);
        }
    }
}
