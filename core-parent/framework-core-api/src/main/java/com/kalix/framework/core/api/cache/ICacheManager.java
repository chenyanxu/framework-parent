package com.kalix.framework.core.api.cache;

import com.kalix.framework.core.api.IService;

import java.util.Set;

/**
 * @类描述：cache interface
 * @创建人：sunlf
 * @创建时间：2014-07-01 下午3:47
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface ICacheManager extends IService {

    /**
     * 保存对象
     *
     * @param key   key
     * @param value value
     */
    <T> void save(String key, T value);

    /**
     * 保存对象（带失效时间）
     *
     * @param key
     * @param value
     * @param sec   单位（秒）
     * @param <T>
     */
    <T> void save(String key, T value, int sec);

    /**
     * 是否存在KEY
     *
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 获取对象
     *
     * @param key
     * @return
     */
    String get(String key);

    byte[] getObj(String key);

    /**
     * 删除对象
     *
     * @param key
     */
    void del(String key);

    /**
     * 删除被匹配到了的key
     *
     * @param keysPattern
     */
    void deleteByKeysPattern(String keysPattern);


    /**
     * 获取匹配到了的key
     *
     * @param keysPattern
     * @return
     */
    Set<byte[]> getKeysByKeysPattern(String keysPattern);
}
