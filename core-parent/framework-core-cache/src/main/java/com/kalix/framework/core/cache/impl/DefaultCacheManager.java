package com.kalix.framework.core.cache.impl;


import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.framework.core.util.SystemUtil;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @类描述：cache implement class
 * @创建人：sunlf
 * @创建时间：2014-07-01 下午3:47
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class DefaultCacheManager implements ICacheManager {

    private ShardedJedisPool pool;


    public void setPool(ShardedJedisPool pool) {
        this.pool = pool;
    }

    /**
     * 测试是否可以连接Redis
     */
    public void init() {
        ShardedJedis jedis = pool.getResource();
        try{
            jedis.set("hello","world");
            jedis.del("hello");
            SystemUtil.succeedPrintln("Connect to Redis Server succeed!");
        }
        catch (Exception e){
            SystemUtil.errorPrintln("Can not connect to Redis Server!");
        }
        finally {
            pool.returnResource(jedis);
        }
    }
    @Override
    public <T> void save(String key, T value) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = pool.getResource();
        if (value instanceof String) {
            jedis.set(key, (String) value);
        } else {
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        }


        // 释放对象池
        pool.returnResource(jedis);
    }


    @Override
    public <T> void save(String key, T value, int sec) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = pool.getResource();
        if (value instanceof String) {
            jedis.setex(key, sec, (String) value);
        } else {
            jedis.setex(key.getBytes(), sec, SerializeUtil.serialize(value));
        }

        // 释放对象池
        pool.returnResource(jedis);
    }

    public String get(String key) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = pool.getResource();
        String value = null;
        if (jedis.exists(key)) {
            value = jedis.get(key);
        }
        // 释放对象池
        pool.returnResource(jedis);
        return value;
    }

    public Boolean exists(String key) {
        ShardedJedis jedis = pool.getResource();
        boolean flag = jedis.exists(key);
        pool.returnResource(jedis);
        return flag;
    }

    public byte[] getObj(String key) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = pool.getResource();
        byte[] value = null;
        if (jedis.exists(key)) {
            value = jedis.get(key.getBytes());
        }
        // 释放对象池
        pool.returnResource(jedis);
        return value;
    }

    @Override
    public void del(String key) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = pool.getResource();
        if (jedis.exists(key)) {
            jedis.del(key);
        }

        // 释放对象池
        pool.returnResource(jedis);
    }


    public void deleteByKeysPattern(String keysPattern) {
        ShardedJedis jedis = pool.getResource();
        Set<String> keys = jedis.hkeys(keysPattern);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String s = it.next();
            System.out.println(s);
        }
        // 释放对象池
        pool.returnResource(jedis);


    }

    public Set<byte[]> getKeysByKeysPattern(String keysPattern) {
        ShardedJedis jedis = pool.getResource();
        Set<byte[]> keys = jedis.hkeys(keysPattern.getBytes());

        // 释放对象池
        pool.returnResource(jedis);

        return keys;
    }
}
