package com.notime.mall.mqtest;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consummer {

    public static void main(String[] args) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER, 
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://192.168.3.66:61616");

        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination drink = session.createQueue("drink");

            MessageConsumer consumer = session.createConsumer(drink);
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