package com.kalix.framework.core.security;


import com.kalix.framework.core.api.cache.ICacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * Created by admin on 2014/7/3.
 */
public class ShiroCacheManager implements IShiroCacheManager {

    private ICacheManager cacheManager;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new ShiroCache<>(name, cacheManager);
    }

    @Override
    public void destroy() throws Exception {

    }

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
