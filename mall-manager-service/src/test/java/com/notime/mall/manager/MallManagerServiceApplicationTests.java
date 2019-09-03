package com.notime.mall.manager;

import com.notime.mall.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallManagerServiceApplicationTests {


    @Autowired
    RedisUtil redisUtil;
    @Test
    public void contextLoads() {



        // 测试jedis
      //  RedisUtil redisUtil = new RedisUtil();
//        Jedis jedis = redisUtil.getJedis();
//        String ping = jedis.ping();
//
//        System.err.println(ping);
       // Long test = "OK";

    }

}
