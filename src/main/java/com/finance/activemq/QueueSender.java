package com.finance.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import javax.annotation.Resource;

/**
 * Created by zt on 2016/12/25.
 */
@Component("queueSender")
public class QueueSender {

    @Resource(name = "jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 发送一条消息到指定队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public void send(String queueName, final String message) {
        jmsTemplate.send(queueName, session -> session.createTextMessage(message));
    }

    /**
     * 发送一条消息到指定队列,使用默认队列名称
     *
     * @param message 消息内容
     */
    public void send(final String message) {
        jmsTemplate.convertAndSend(message);
    }

    /**
     * 发送一条消息到指定队列
     *
     * @param object 消息内容
     */
    public void send(String queueName, final Serializable object) {
        jmsTemplate.send(queueName, session -> session.createObjectMessage(object));
    }

}
