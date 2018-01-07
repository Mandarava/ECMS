package com.finance.redis.mq;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

/**
 * Redis消息订阅服务，仅作为demo，不适用于生产环境
 *
 * @author Administrator
 */
@Component
public class RedisMessageManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageManager.class);

    @Autowired
    private Map<String, AbstractRedisMessageListener> messageListenerImplements;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("正在启动redis消息监听器…………");
        registerMessageListeners();
        logger.info("启动redis消息监听器完成。");
    }

    public void registerMessageListeners() {
        try {
            if (MapUtils.isNotEmpty(messageListenerImplements)) {
                for (Entry<String, AbstractRedisMessageListener> listenerEntry : messageListenerImplements.entrySet()) {
                    AbstractRedisMessageListener listener = listenerEntry.getValue();
                    registerListener(listener);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void registerListener(AbstractRedisMessageListener listener) {
        if (listener == null) {
            return;
        }
        try {
            String channel = listener.getChannelEnum().getChannel();
            RedisSubscribeThread runnable = applicationContext.getBean(RedisSubscribeThread.class, listener, channel);
            Thread thread = new Thread(runnable);
            thread.setName("RedisMQ_" + listener.getClass().getSimpleName());
            thread.start();
        } catch (Exception e) {
            logger.error("注册redis消息监听器出错", e);
        }
    }

}
