package com.notime.mall.sekill;

import com.notime.mall.redis.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class StockController {

    @Autowired
    RedisUtil  redisUtil;


    @Autowired
    RedissonClient redissonClient;
    @ResponseBody
    @RequestMapping("test")
    public  String  getStock(){
        // 每次去redis中获取库存
         // 每访问一次 stock -1
//        Jedis jedis = null;
//        int  i =0;
//        //  将stock String  转换为数字
//        RLock rLock = redissonClient.getLock("anyone");
//        rLock.lock();
//
//        try {
//
//            //jedis 的创建要放在try里面才能是锁住？
//             jedis = redisUtil.getJedis();
//            String stock = jedis.get("stock");
//             i = Integer.parseInt(stock);
//            if (i>0){
//
//                i--;
//                jedis.set("stock",i+"");
//                System.err.println("售出一件，剩下"+i);
//
//
//            }
//            else {
//                System.err.println("售完");
//            }
//        }finally {
//            rLock.unlock();
//            if (jedis!=null){
//                jedis.close();
//            }
//
//
//        }
//
//        return  i+"";





        /*
          Jedis jedis = null;
        long restStock = 0;

        RLock rLock = redissonClient.getLock("a");
        rLock.lock();
        try {
            jedis = redisUtil.getJedis();
            BigDecimal stock = new BigDecimal(jedis.get("stock"));
            int i = stock.compareTo(new BigDecimal("0"));// -1 0 1
            if(i>0){
                // 有库存
                //restStock = jedis.incrBy("stock", -1);
                long stocklong = stock.longValue();
                stocklong -- ;
                jedis.set("stock",stocklong+"");
                System.out.println("售出一件商品，剩余库存数量"+stocklong);
                restStock = stocklong;
            }else {
                restStock = 0;
                System.out.println("库存售罄");
            }
        }finally {
            rLock.unlock();
            jedis.close();
        }
        return restStock+"";
         */

        RLock lock = redissonClient.getLock("1");
        lock.lock();
        Jedis jedis = redisUtil.getJedis();
        String stock = jedis.get("stock");

        int i = Integer.parseInt(stock);
        if (i>0){
            i--;
            System.err.println("售出意见剩余"+i);
            jedis.set("stock",""+i);
        }
        lock.unlock();
        jedis.close();
        return  i+"";

    }
}
