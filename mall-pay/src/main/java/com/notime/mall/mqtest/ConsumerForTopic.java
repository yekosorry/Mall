package com.notime.mall.mqtest;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerForTopic {

    public static void main(String[] args) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://192.168.3.66:61616");

        try {
            Connection connection = activeMQConnectionFactory.createConnection();
         // topic 要实现持久
            connection.setClientID("1");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic stronger = session.createTopic("Stronger");

           // MessageConsumer consumer = session.createConsumer(stronger);
            // 持久
            MessageConsumer consumer = session.createDurableSubscriber(stronger, "1");
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage){}
                    try {
                        String text = ((TextMessage) message).getText();
                        System.err.println("consumer"+text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JMSException e) {


        }


    }
}
