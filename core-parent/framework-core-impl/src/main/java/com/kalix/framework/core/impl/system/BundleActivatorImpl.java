package com.kalix.framework.core.impl.system;

import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.KalixCascade;
import com.kalix.framework.core.util.SystemUtil;
import org.json.JSONObject;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.persistence.Table;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @author Administrator
 * @create 2016-10-13 16:18.
 */
public abstract class BundleActivatorImpl implements BundleActivator {
    protected ICacheManager cacheManager = null;

    public BundleActivatorImpl() {
        try {
            this.cacheManager = JNDIHelper.getJNDIServiceForName(ICacheManager.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SystemUtil.startBundlePrintln(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        SystemUtil.stopBundlePrintln(bundleContext);
    }

    /**
     * 注册级联信息
     *
     * @param me
     * @throws Exception
     */
    public void registerCascade(Class<? extends PersistentEntity> me) throws Exception {
        // 获取当前需要注册实体类的table注解信息
        Table table = me.getAnnotation(Table.class);
        // 没有table注解信息，不需要注册
        if (table == null) {
            return;
        }

        // 从redis中取出级联信息
        String jedisString = cacheManager.get(KalixCascade.alias);
        JSONObject jsonCascade = new JSONObject();
        if (jedisString != null) {
            jsonCascade = new JSONObject(jedisString);
        }

        Field[] fields = me.getDeclaredFields();
        for (Field f : fields) {
            KalixCascade cascade = f.getAnnotation(KalixCascade.class);
            if (cascade != null) {
                if (cascade.deletable()) {
                    JSONObject object = new JSONObject();
                    object.put("operation", "delete");
                    object.put("table", table.name());
                    object.put("primaryKey", "id");
                    object.put("foreignKey", cascade.foreignKey());

                    jsonCascade = writeJson(jsonCascade, cascade.beans(), me.getName(), object);
                }
            }
        }
        // 保存级联信息到redis
        cacheManager.save(KalixCascade.alias, jsonCascade.toString());
    }

    /**
     * 反注册级联信息
     *
     * @throws Exception
     */
    public void unRegisterCascade(Class<? extends PersistentEntity> me) throws Exception {
        if (cacheManager.exists(KalixCascade.alias)) {
            String jedisString = cacheManager.get(KalixCascade.alias);
            if (jedisString != null) {
                boolean isChange = false;
                JSONObject jsonCascade = new JSONObject(jedisString);
                for (Iterator mainCascadeIterator = jsonCascade.keys(); mainCascadeIterator.hasNext(); ) {
                    String mainCascadeKey = (String) mainCascadeIterator.next();
                    JSONObject mainJsonCascade = jsonCascade.getJSONObject(mainCascadeKey);
                        if (mainJsonCascade.has(me.getName())) {
                            isChange = true;
                            mainJsonCascade.remove(me.getName());
                            jsonCascade.put(mainCascadeKey, mainJsonCascade);
                        }
                }

                // 有反注册信息，修改redis缓存
                if (isChange) {
                    cacheManager.save(KalixCascade.alias, jsonCascade.toString());
                }
            }
        }
    }

    /**
     * 写入级联信息至json
     *
     * @param jsonCascade
     * @param mainCascadeKey
     * @param relationCascadeKey
     * @param object
     * @return
     */
    private JSONObject writeJson(JSONObject jsonCascade, String mainCascadeKey, String relationCascadeKey, JSONObject object) {
        JSONObject mainJsonCascade;
        if (jsonCascade.has(mainCascadeKey)) {
            mainJsonCascade = jsonCascade.getJSONObject(mainCascadeKey);
        } else {
            mainJsonCascade = new JSONObject();
        }

        mainJsonCascade.put(relationCascadeKey, object);

        jsonCascade.put(mainCascadeKey, mainJsonCascade);

        return jsonCascade;
//        for (Iterator mainCascadeIterator = jsonCascade.keys(); mainCascadeIterator.hasNext(); ) {
//            String mainCascadeKey = (String) mainCascadeIterator.next();
//            JSONObject mainJsonCascade = jsonCascade.getJSONObject(mainCascadeKey);
//            for (Iterator relationCascadeIterator = mainJsonCascade.keys(); relationCascadeIterator.hasNext(); ) {
//                String relationCascadeKey = (String) relationCascadeIterator.next();
//                JSONObject relationJsonCascade = mainJsonCascade.getJSONObject(relationCascadeKey);
//
//            }
//        }
    }
}
