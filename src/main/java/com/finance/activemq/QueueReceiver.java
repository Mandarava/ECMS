package com.finance.activemq;

import org.springframework.stereotype.Component;

/**
 * Created by zt on 2016/12/25.
 */
@Component("queueReceiver")
public class QueueReceiver {

    public void handle(String message) {
        System.out.println("QueueReceiver收到消息： " + message);
    }

}
