package com.kalix.framework.core.impl.system;

import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.api.system.IDictBeanService;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.SerializeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenyanxu
 */
public abstract class BaseDictServiceImpl<T extends IGenericDao, TP extends PersistentEntity> extends ShiroGenericBizServiceImpl<T, TP> implements IDictBeanService<TP> {
    private String dictCacheId = null;
    private String appName = null;

    public BaseDictServiceImpl() {
        String simpleName = this.getClass().getSimpleName();
        String nameSplit[] = simpleName.split("Dict");

        if (nameSplit.length > 0) {
            dictCacheId = nameSplit[0].toLowerCase() + "_dict_cache";
            appName = nameSplit[0].toLowerCase();
        }
    }

    @Override
    public JsonStatus saveEntity(TP entity) {
        if (dictCacheId != null && this.cacheManager.exists(dictCacheId)) {
            this.cacheManager.del(dictCacheId);
        }

        return super.saveEntity(entity);
    }

    @Override
    public JsonStatus updateEntity(TP entity) {
        if (dictCacheId != null && this.cacheManager.exists(dictCacheId)) {
            this.cacheManager.del(dictCacheId);
        }

        return super.updateEntity(entity);
    }

    @Override
    public List getAllEntity() {
        List rtn = null;

        if (dictCacheId != null) {
            if (this.cacheManager.exists(dictCacheId)) {
                rtn = SerializeUtil.unserialize(cacheManager.getObj(dictCacheId));
            } else {
                Integer cacheTimeout = DictConfigManager.getInstall().getCacheTime(appName + "_dict_cache_timeout");

                rtn = super.getAllEntity();
                this.cacheManager.save(dictCacheId, rtn, cacheTimeout);
            }
        }

        return rtn;
    }

    @Override
    public List<Map> getDictTypes(String query) {
        List<Map> dictTypes = DictConfigManager.getInstall().getDictTypes(appName + "_dict_types");

        if (query != null && !query.trim().equals("")) {
            List<Map> rtn = new ArrayList<>();

            for (Map map : dictTypes) {
                if (map.get("name").toString().contains(query.trim())) {
                    rtn.add(map);
                }
            }

            return rtn;
        }

        /*下行代码没发现什么作用 zangyanming*/
        //getByTypeAndValue("岗位名称", 0);

        return dictTypes;
    }

    @Override
    public TP getByTypeAndValue(String type, Integer value) {
        //find the bean class annotation:
        //if it contain the @Table,we can get the name from it
        //Table tb=persistentClass.getAnnotation(Table.class);
        String tbName = dao.getTableName();
        String sql = "select * from %s where type='%s' and value=%d";

        if (tbName != null) {
            sql = String.format(sql, tbName, type, value);
            return getEntityBySql(sql);
        }

        return null;
    }

    @Override
    public TP getByTypeAndLabel(String type, String label) {
        //find the bean class annotation:
        //if it contain the @Table,we can get the name from it
        //Table tb=persistentClass.getAnnotation(Table.class);
        String tbName = dao.getTableName();
        String sql = "select * from %s where type='%s' and label='%s'";

        if (tbName != null) {
            sql = String.format(sql, tbName, type, label);
            return getEntityBySql(sql);
        }

        return null;
    }

    private TP getEntityBySql(String sql) {
        List list = dao.findByNativeSql(sql, persistentClass);
        if (list.size() == 1) {
            return (TP) list.get(0);
        } else {
            return null;
        }
    }
}
