package com.finance.redis.mq.listener;

import com.finance.redis.mq.AbstractRedisMessageListener;
import com.finance.redis.mq.ChannelEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("schedulerMessageListener")
public class SchedulerMessageListener extends AbstractRedisMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerMessageListener.class);

    public SchedulerMessageListener() {
        super(ChannelEnum.SCHEDULER_QUARTZ_RELOAD);
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            logger.info("Message '{}' received in channel {}", message, channel);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("Successfully subscribed to {}. Number of subscriptions: {}", channel, subscribedChannels);
    }


    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("Unsubscribed from {}. Number of subscriptions: {}", channel, subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        logger.debug("PMessage '{}' received in channel {} and pattern {}", message, channel, pattern);
    }


    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("Successfully PSubscribe to {}. Number of subscriptions: {}", pattern, subscribedChannels);
    }


    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("Successfully PUnsubscribe from {}. Number of subscriptions: {}", pattern, subscribedChannels);
    }

}
