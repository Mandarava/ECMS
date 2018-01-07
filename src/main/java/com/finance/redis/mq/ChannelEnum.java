package com.finance.redis.mq;

public enum ChannelEnum {

    /**
     * 更新定时任务
     */
    SCHEDULER_QUARTZ_RELOAD("scheduler.quartz.reload");

    private String channel;

    ChannelEnum(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

}
