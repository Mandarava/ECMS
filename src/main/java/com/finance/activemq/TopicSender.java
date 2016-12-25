package com.finance.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zt on 2016/12/25.
 */
@Component("topicSender")
public class TopicSender {

    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 发送一条消息到指定队列
     *
     * @param message 消息内容
     */
    public void send(final String message) {
        jmsTemplate.convertAndSend(message);
    }

    public void send(String topicName, final String message) {
        jmsTemplate.send(topicName, session -> session.createTextMessage(message));
    }
}
