package com.notime.mall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PaymentInfo;
import com.notime.mall.api.service.PaymentInfoService;
import com.notime.mall.mapper.PaymentInfoMapper;
import com.notime.mall.mq.MqUtil;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;
    @Override
    public void addPaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatePay(PaymentInfo paymentInfo) {
        Example example = new Example(PaymentInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderSn",paymentInfo.getOrderSn());
        paymentInfoMapper.updateByExampleSelective(paymentInfo,example);
    }
    @Autowired
    MqUtil mqUtil;


/*查询支付状态*/
    @Override
    public void sendPaymentCheck(PaymentInfo paymentInfo, int count) {
        ConnectionFactory connectionFactory = mqUtil.getConnectionFactory();
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue queue = session.createQueue("PAYMENT_CHECK");
            MessageProducer producer = session.createProducer(queue);
            MapMessage message = new ActiveMQMapMessage();
            message.setString("orderSn",paymentInfo.getOrderSn());

        //    message.setString("count",count);
            message.setInt("count",count);

            //  开启延迟消息
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*10);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(message);
            session.commit();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }




    /* 更新支付信息 */
    @Override
    public void sendPaymentQueue(PaymentInfo paymentInfo) {
        // 发送消息
        ConnectionFactory connectionFactory = mqUtil.getConnectionFactory();
        //为什么是空啊
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();


            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            // 与消费者的对应PAYMENT_SUCCESS
            Queue queue = session.createQueue("PAYMENT_SUCCESS");
            MessageProducer producer = session.createProducer(queue);

            // 将需要的信息发送给消费者
            MapMessage message = new ActiveMQMapMessage();
            message.setString("orderSn",paymentInfo.getOrderSn());
            message.setString("status","success");


            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(message);
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaymentInfo getInfoByOrderSn(String orderSn) {

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderSn(orderSn);
        PaymentInfo paymentInfo1 = paymentInfoMapper.selectOne(paymentInfo);
        return paymentInfo1;
    }


}

