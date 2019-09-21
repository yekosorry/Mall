package com.notime.mall.mqtest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * @author Administrator
 *
 *  测试 mq的两种模式
 */
public class Producer {


    public static void main(String[] args) {

        queue();
    }

    public static void topic(){
        ActiveMQConnectionFactory mqConnection= new ActiveMQConnectionFactory("tcp://localhost:61616");


        Connection connection = null;
        try {
            connection = mqConnection.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);


            Topic topic = session.createTopic("Stronger");
            MessageProducer producer = session.createProducer(topic);

            ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();

            activeMQTextMessage.setText("can you give me a  cup of water ");
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(activeMQTextMessage);
            session.commit();
            connection.close();


        } catch (JMSException e) {
            e.printStackTrace();
        }


    }


    public static void queue() {

        ActiveMQConnectionFactory mqConnection= new ActiveMQConnectionFactory("tcp://192.168.3.66:61616");


        Connection connection = null;
        try {
            connection = mqConnection.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);


            Queue drink = session.createQueue("drink");
            MessageProducer producer = session.createProducer(drink);

            ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();

            activeMQTextMessage.setText("can you give me a  cup of water ");
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(activeMQTextMessage);
            session.commit();
            connection.close();


        } catch (JMSException e) {
            e.printStackTrace();
        }


    }


}
