package com.finance.activemq;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by zt on 2016/10/5.
 */
@Component("consumerMessageListener")
public class ConsumerMessageListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(ConsumerMessageListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            // 监听发送到消息队列的文本消息，作强制转换。
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("接收到的消息内容是：" + textMessage.getText());
                // TODO: 你喜欢的任何事情...
            } catch (JMSException e) {
                LOGGER.error("", e);
            }
        }
    }

}
