package com.finance.activemq;

import org.springframework.stereotype.Component;

/**
 * Created by zt on 2016/12/25.
 */
@Component("topicReceiver")
public class TopicReceiver {

    public void handle(String message) {
        System.out.println("TopicReceiver收到消息： " + message);
    }

}
