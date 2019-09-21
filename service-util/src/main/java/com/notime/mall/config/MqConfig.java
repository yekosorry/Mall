package com.notime.mall.config;

import com.notime.mall.mq.MqUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.Session;

@Configuration
public class MqConfig {
    @Value("tcp://localhost:61616")
    String brokerUrl = "tcp://localhost:61616";


    @Value("true")
    String  listenerEnable ="true";

//    @Value("${spring.activemq.broker-url:disabled}")
//    String brokerUrl ;
//
//    @Value("${activemq.listener.enable:disabled}")
//    String listenerEnable;

    @Bean
    public MqUtil getMqUtil(){
        MqUtil mqUtil = new MqUtil();
        mqUtil.init(brokerUrl);
        return  mqUtil;

}

        // 消息监听器  jmsListener
    @Bean(name="jmsListener")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory){

        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();

        defaultJmsListenerContainerFactory.setConnectionFactory(activeMQConnectionFactory);

        defaultJmsListenerContainerFactory.setConcurrency("5");

        defaultJmsListenerContainerFactory.setRecoveryInterval(5000L);

        defaultJmsListenerContainerFactory.setSessionTransacted(false);   // 不需要事务？？

        defaultJmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);


        return  defaultJmsListenerContainerFactory;
    };


    @Bean
    public  ActiveMQConnectionFactory activeMQConnectionFactory(){

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        return activeMQConnectionFactory;
    }


}
