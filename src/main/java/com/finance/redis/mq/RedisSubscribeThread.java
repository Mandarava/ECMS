package com.finance.redis.mq;

import com.finance.util.myutil.JedisUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisPubSub;

@Component
@Scope("prototype")
public class RedisSubscribeThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscribeThread.class);
    private static final int SLEEP_SECOND = 60;
    private final JedisPubSub subscriber;
    private final String channel;
    @Autowired
    private JedisUtil jedisUtil;

    public RedisSubscribeThread(JedisPubSub jedisPubSub, String channel) {
        this.subscriber = jedisPubSub;
        this.channel = channel;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                logger.info("Start to subscribe channel {}", channel);
                jedisUtil.subscribe(subscriber, channel);
            } catch (Exception e) {
                logger.error("Redis Subscribing error occurred!", e);
                unsubscribe();
            }
            sleep(SLEEP_SECOND);
        }
    }

    private void unsubscribe() {
        try {
            logger.info("Start to unsubscribe channel {}", channel);
            if (subscriber.isSubscribed()) {
                subscriber.unsubscribe(channel);
                logger.info("unsubscribe success.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
