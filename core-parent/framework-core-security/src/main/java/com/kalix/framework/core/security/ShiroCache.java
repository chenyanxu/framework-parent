package com.kalix.framework.core.security;

import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.framework.core.util.SerializeUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by admin on 2014/7/3.
 */
public class ShiroCache<K, V> implements Cache<K, V> {

    private final String SHIRO_CACHE = "shiro-cache:";

    private String name;

    private ICacheManager cacheManager;

    public ShiroCache(String name, ICacheManager cacheManager) {
        this.name = name;
        this.cacheManager = cacheManager;
    }

    @Override
    public V get(K key) throws CacheException {
        String zkey = getCacheKey(key);
        byte[] byteValue = cacheManager.getObj(zkey);
        return (V) SerializeUtil.unserialize(byteValue);
    }

    @Override
    public V put(K k, V value) throws CacheException {
        V previos = get(k);
        String key = getCacheKey(k);
        cacheManager.save(key, value);
        return previos;
    }

    @Override
    public V remove(K k) throws CacheException {
        V previos = get(k);
        String key = getCacheKey(k);
        cacheManager.del(key);
        return previos;
    }

    @Override
    public void clear() throws CacheException {

        /*cacheManager.deleteByKeysPattern(this.SHIRO_CACHE
                + "*");*/
    }

    @Override
    public int size() {
        if (keys() == null)
            return 0;
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        Set<byte[]> byteSet = cacheManager.getKeysByKeysPattern(this.SHIRO_CACHE + "*");
        Set<K> keys = new HashSet<K>();
        for (byte[] bs : byteSet) {
            keys.add((K) SerializeUtil.unserialize(bs));
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        Set<byte[]> byteSet = cacheManager.getKeysByKeysPattern(this.SHIRO_CACHE + "*");
        List<V> result = new LinkedList<V>();
        for (byte[] bs : byteSet) {
            try {
                String key = new String(bs, "utf-8");
                result.add((V) SerializeUtil.unserialize(cacheManager
                        .getObj(key)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    //自定义realm中的授权/认证的类名加上授权/认证英文名字
    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ICacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private String getCacheKey(Object key) {
        return this.SHIRO_CACHE + getName() + ":" + key;
    }
}
