package com.finance.util.myutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Component
public class JedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    @Autowired
    private JedisPool jedisPool;

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    private void releaseJedis(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.subscribe(jedisPubSub, channels);
        } catch (Exception e) {
            throw e;
        } finally {
            this.releaseJedis(jedis);
        }
    }

    public void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.publish(channel, message);
        } catch (Exception e) {
            throw e;
        } finally {
            this.releaseJedis(jedis);
        }
    }


}