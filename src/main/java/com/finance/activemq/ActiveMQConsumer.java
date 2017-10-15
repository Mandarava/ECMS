package com.finance.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class ActiveMQConsumer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String queueName = "queueDemo";
    private static String topicName = "topicDemo";

    public static void main(String[] args) {
        // 连接工厂
        ConnectionFactory connectionFactory;
        // 连接
        Connection connection;
        // 会话
        Session session;
        // 消息的目的地
        Destination destination;
        // 消息的消费者
        MessageConsumer messageConsumer;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(new ConsumerMessageListener());
            connection.setExceptionListener(new ConsumerExceptionListener());
            ((ActiveMQConnection) connection).addTransportListener(new ActiveMQTransportListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * durable topic consumer
     */
    public static void durableTopicConsumber() {
        // 连接工厂
        ConnectionFactory connectionFactory;
        // 连接
        Connection connection;
        // 会话
        Session session;
        // 消息的目的地
        Topic topic;
        // 消息的消费者
        TopicSubscriber topicSubscriber;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("UniqueClientId");
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic(topicName);
            topicSubscriber = session.createDurableSubscriber(topic, "topic");
            topicSubscriber.setMessageListener(new ConsumerMessageListener());
            connection.setExceptionListener(new ConsumerExceptionListener());
            ((ActiveMQConnection) connection).addTransportListener(new ActiveMQTransportListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
