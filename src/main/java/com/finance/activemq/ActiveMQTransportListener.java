package com.finance.activemq;

import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by zt on 2016/10/5.
 */
@Component("activeMQTransportListener")
public class ActiveMQTransportListener implements TransportListener {

    private Logger logger = Logger.getLogger(ActiveMQTransportListener.class);

    /**
     * 对消息传输命令进行监控
     */
    @Override
    public void onCommand(Object o) {

    }

    /**
     * 对监控到的异常进行触发
     */
    @Override
    public void onException(IOException error) {
        logger.error("onException -> 消息服务器连接错误......", error);
    }

    /**
     * 当failover时触发
     */
    @Override
    public void transportInterupted() {
        logger.warn("transportInterupted -> 消息服务器连接发生中断...");
        //这里就可以状态进行标识了

    }

    /**
     * 监控到failover恢复后进行触发
     */
    @Override
    public void transportResumed() {
        logger.info("transportResumed -> 消息服务器连接已恢复...");
        //这里就可以进行状态标识了
    }
}
