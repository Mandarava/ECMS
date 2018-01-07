package com.finance.redis.mq;

import redis.clients.jedis.JedisPubSub;

public abstract class AbstractRedisMessageListener extends JedisPubSub {

    /**
     * 消息订阅频道
     */
    private ChannelEnum channelEnum;

    public AbstractRedisMessageListener(ChannelEnum channelEnum) {
        super();
        this.channelEnum = channelEnum;
    }

    public ChannelEnum getChannelEnum() {
        return channelEnum;
    }

}
