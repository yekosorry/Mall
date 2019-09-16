package com.notime.mall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.OmsOrder;
import com.notime.mall.api.service.OmsOrderService;
import com.notime.mall.order.mapper.OmsOrderMapper;
import com.notime.mall.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
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
}
