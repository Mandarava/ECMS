package com.finance.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class ActiveMQProducer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String queueName = "queueDemo";

    public static void main(String[] args) {
        // 连接工厂
        ConnectionFactory connectionFactory;
        // 连接
        Connection connection = null;
        // 会话
        Session session;
        // 消息的目的地
        Destination destination;
        // 消息的生产者
        MessageProducer messageProducer;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 发送消息
            sendMessage(session, messageProducer);
            // 因为开了事务的，所以需要提交事务
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer messageProducer) {
        try {
            TextMessage textMessage = session.createTextMessage("发送消息");
            messageProducer.send(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
