package com.notime.mall.sekill;

import com.notime.mall.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;

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


    /*实现 抢购
    * */
    // 同个时间内， 只有一个线程能成功

    @ResponseBody
    @RequestMapping("mi")
    public  String  xiaomi() {
        //用watch

        Jedis jedis = redisUtil.getJedis();


        String watch = jedis.watch("Key");
        String stock = jedis.get("stock");
        Transaction multi = jedis.multi();
        int i = Integer.parseInt(stock);
        if (i > 0) {
//            i--;
//          锁不住的
//
//            multi.set("stock", "" + i);
            Response<Long> stock1 = multi.incrBy("stock", -1);
            List<Object> exec = multi.exec();
            if (exec != null && exec.size() > 0) {
                System.err.println("剩下=" + exec.get(0));


            } else {
                System.err.println("抢购失败"+ exec.get(0));

            }


        }else{
            System.err.println("抢购失败");

        }
        jedis.close();
        return stock;
    }

    // 坑  要用incryby 才能锁住




    // 先到先得
    // 先到先得
    @ResponseBody
    @RequestMapping("two")
    public void  two(){
        RSemaphore semaphore = redissonClient.getSemaphore("stock");
        boolean b = semaphore.tryAcquire();

        if (b){
            System.err.println(Thread.currentThread().getName()+"抢购成功");
        }else {
            System.err.println(Thread.currentThread().getName()+"抢购失败");

        }




    }

    // 先到先得
    @ResponseBody
    @RequestMapping("three")
    public void  two(String userId){




        //  限制抢购数量  一个账号只能抢一次  用setnx 不ok 不让抢
        Jedis jedis = redisUtil.getJedis();
        Long setnx = jedis.setnx(userId, "123");
        String setex = jedis.setex(userId,  60 * 30, "123");
        if (StringUtils.isNotBlank(setex)&&setex.equals("OK")){
            RSemaphore semaphore = redissonClient.getSemaphore("stock");
            boolean b = semaphore.tryAcquire();

            if (b){
                System.err.println(Thread.currentThread().getName()+"抢购成功");
            }else {
                System.err.println(Thread.currentThread().getName()+"抢购失败");

            }
        }

    }
}
