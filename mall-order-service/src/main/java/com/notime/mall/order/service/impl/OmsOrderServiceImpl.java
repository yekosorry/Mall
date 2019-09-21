package com.notime.mall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.notime.mall.api.bean.OmsOrder;
import com.notime.mall.api.bean.OmsOrderItem;
import com.notime.mall.api.service.OmsOrderService;
import com.notime.mall.mq.MqUtil;
import com.notime.mall.order.mapper.OmsOrderItemMapper;
import com.notime.mall.order.mapper.OmsOrderMapper;
import com.notime.mall.redis.RedisUtil;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    OmsOrderMapper omsOrderMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public void addOmsorder(OmsOrder omsOrder) {
        omsOrderMapper.insertSelective(omsOrder);
    }

    @Override
    public boolean checkTradeCode(String memberId, String tradeCod) {
        Jedis jedis = redisUtil.getJedis();
       //  弱一致性  lua表达式将获取和删除合并 jedis.get("memberId:"+memberId+tradeCod);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long eval = (Long) jedis.eval(script, Collections.singletonList("memberId:"+memberId+":tradeCode"),
                Collections.singletonList(tradeCod));
        if (eval==1){
            return  true;
        }

        return false;

    }

    @Override
    public String generateTradeCode(String memberId) {
        Jedis jedis = redisUtil.getJedis();

        // 不断进入结算页面 就不断刷新时tradeCode
        String uuid = UUID.randomUUID().toString();
        jedis.setex("memberId:"+memberId+":tradeCode",60*30, uuid);
        jedis.close();

        return uuid;
    }

    @Override
    public void updateOrder(OmsOrder omsOrder) {

        Example example = new Example(OmsOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderSn",omsOrder.getOrderSn());

        //为什么无法更新 paytype

         omsOrderMapper.updateByExampleSelective(omsOrder, example);
        OmsOrder omsOrder1 = omsOrderMapper.selectOne(omsOrder);



    }

    @Autowired
    MqUtil mqUtil;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;
    
    @Override
    public void sendWare(OmsOrder omsOrder) {
        // 发送更新库存的信息
        ConnectionFactory connectionFactory = mqUtil.getConnectionFactory();
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
            Queue queue = session.createQueue("WARE_CHECK");
            MessageProducer producer = session.createProducer(queue);


            //需要 orderinfo
            OmsOrder info = new OmsOrder();
            info.setOrderSn(omsOrder.getOrderSn());
            OmsOrder orderForMessage = omsOrderMapper.selectOne(info);

            // order 中的orderitem
            OmsOrderItem item = new OmsOrderItem();
            item.setOrderSn(omsOrder.getOrderSn());
            List<OmsOrderItem> select = omsOrderItemMapper.select(item);
            orderForMessage.setOmsOrderItems(select);

            ActiveMQTextMessage text = new ActiveMQTextMessage();
            text.setText(JSON.toJSONString(orderForMessage));

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(text);
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
