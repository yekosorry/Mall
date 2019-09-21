package com.notime.mall.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.ConnectionFactory;

public class MqUtil {
    PooledConnectionFactory pooledConnectionFactory = null;


   // 这个能初始化吗
    public ConnectionFactory init(String brokerUrl) {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);

         pooledConnectionFactory.setReconnectOnException(true);
         pooledConnectionFactory.setMaxConnections(5);
         pooledConnectionFactory.setIdleTimeout(10000);

         return  pooledConnectionFactory;

    }


   public ConnectionFactory getConnectionFactory(){

       return  pooledConnectionFactory;

   }


}
