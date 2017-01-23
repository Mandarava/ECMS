package com.finance.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Created with IDEA
 * Created by ${jie.chen} on 2016/7/11.
 */
public class ActiveMQConsumer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String args[]) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection;//连接
        Session session;//会话
        Destination destination;//消息的目的地
        MessageConsumer messageConsumer;//消息的生产者

        //创建会话工厂
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
        try {
            connection = connectionFactory.createConnection();
            connection.start(); //启动连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("queueDemo"); // 获得消息队列
            messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(new ConsumerMessageListener());
            connection.setExceptionListener(new ConsumerExceptionListener());
            ((ActiveMQConnection) connection).addTransportListener(new ActiveMQTransportListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
