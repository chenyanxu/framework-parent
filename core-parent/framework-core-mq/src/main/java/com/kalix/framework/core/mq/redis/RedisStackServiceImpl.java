package com.kalix.framework.core.mq.redis;

import com.kalix.framework.core.api.system.IStackService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by sunlf on 2016-02-26.
 * redis 实现的消息队列
 */
public class RedisStackServiceImpl implements IStackService {
    private JedisPool jedisPool;

    @Override
    public void publish(String topic, String msgJson, int seconds) {

        Jedis jedis = getJedis();
        try {
//        Producer producer = new Producer(jedis, topic);
//        producer.publish(msgJson, seconds);
            jedis.rpush(topic, msgJson);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(jedis);
        }

    }

    @Override
    public String consume(String topic) {
        Jedis jedis = getJedis();
        try {
//        Consumer consumer = new Consumer(jedis, "consumer", topic);
//        String str=consumer.consume();
            String str = jedis.lpop(topic);
            if (str != null)
                return str;
            else
                return "";
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(jedis);
        }
        return "";

    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    private void returnResource(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

}
