package com.finance.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zt on 2016/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mybatis.xml")
public class TestSendMessage {
    private final String QUEUENAME = "default_queue";

    @Autowired
    private QueueSender queueSender;

    @Autowired
    private TopicSender topicSender;

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            queueSender.send(QUEUENAME, "Hi，这是第 " + (i + 1) + " 条消息！");
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            queueSender.send("Hi，这是第 " + (i + 1) + " 条消息！");
        }
    }

    @Test
    public void test4() {
        for (int i = 0; i < 5; i++) {
            topicSender.send("Hi，这是第 " + (i + 1) + " 条消息！");
        }
    }

}
