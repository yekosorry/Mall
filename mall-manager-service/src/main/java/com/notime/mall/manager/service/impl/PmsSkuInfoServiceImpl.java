
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.notime.mall.api.bean.*;
import com.notime.mall.api.service.PmsSkuInfoService;
import com.notime.mall.constanst.RedisConst;
import com.notime.mall.manager.mapper.PmsSkuAttrValueMapper;
import com.notime.mall.manager.mapper.PmsSkuImageMapper;
import com.notime.mall.manager.mapper.PmsSkuInfoMapper;
import com.notime.mall.manager.mapper.PmsSkuSaleAttrValueMapper;
import com.notime.mall.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PmsSkuInfoServiceImpl implements PmsSkuInfoService {


    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // PmsSkuInfo
        /// 还有pmsskuImageList    pmsSkuAttrValueList  PmsSkuAttrValueList
        //skuid
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        String id = pmsSkuInfo.getId();
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(id);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        // 不了解数据结构 isImg怎么说 是设置的吗
        // pmsSkuInfo.getSkuDefaultImg().var

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(id);
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();

        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(id);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);

        }


    }

    @Override
    public PmsSkuInfo getSkuInfoBySkuId(String skuId) throws InterruptedException {
        // 用redsission 实现对是
        Jedis jedis = redisUtil.getJedis();
        String key = RedisConst.SKU_PREFIX+skuId+ RedisConst.SKU_SUFFIX;
        String skuInfoJson = jedis.get(key);

        if (skuInfoJson == null) {

            String  skuLockKey = RedisConst.SKU_PREFIX+skuId+ RedisConst.SKULOCK_SUFFIX;
       //     String lock = jedis.set(skuLockKey,"OK","nx","px",10000);
            // redis
            String uuid = UUID.randomUUID().toString();
             Long lock = jedis.setnx(skuLockKey, uuid);
             Long pexpire = jedis.pexpire(skuLockKey, RedisConst.EXP_SEC);

            //  2.9.0 可以 3.1.0就不行  有了新方法却不会用 可以分布的啊
            // 能否取号成功  获得分布锁
            if(pexpire == 1){
                //操作数据库
                PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
                PmsSkuImage pmsSkuImage = new PmsSkuImage();
                pmsSkuImage.setSkuId(skuId);
                List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
                pmsSkuInfo.setSkuImageList(pmsSkuImageList);

                PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
                pmsSkuAttrValue.setSkuId(skuId);
                List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
                pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);

                PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
                pmsSkuSaleAttrValue.setSkuId(skuId);
                List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
                pmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);

                String skuInfoJsonStr = JSON.toJSONString(pmsSkuInfo);
                jedis.setex(key, RedisConst.EXP_SEC,skuInfoJsonStr);
                // 解锁  将数据放进缓存 可能用不完生命周期 不能干等什么周期过期  提高效率 需要手动解锁

                //jedis.del(skuLockKey);
                  //  直接删除 存在 要删除的key 已过期  删除了新的key 的情况  导致了多个服务器对数据操作
                  // 找到要删除的key的value 准确删除
//                String lockvalue = jedis.get(skuLockKey);
//                if (Objects.areEqual(lockvalue,uuid)){
//                    jedis.del(skuLockKey);
//                }


                //  上面的还存在 获取值的瞬间  key过期了  jedis删除了新的key
                // 用luna 脚本 将get 与delete 合并
                String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script, Collections.singletonList(skuLockKey),Collections.singletonList(uuid));




                //
                jedis.close();
                return  pmsSkuInfo;
            }else {
                // 自旋 睡眠等待
                Thread.sleep(1000);
                jedis.close();
                return  getSkuInfoBySkuId(skuId);
            }
        }else {

            PmsSkuInfo pmsSkuInfo = JSON.parseObject(skuInfoJson, PmsSkuInfo.class);
            return pmsSkuInfo;
        }
    }

    /*
        @Override
        public PmsSkuInfo getSkuInfoBySkuId(String skuId) throws InterruptedException {

            // 实现分布式锁
            //  每次访问数据库前要看能不能redis中设置  相当于取号
            // setnxpx  set不成功自旋   查看缓存  没有再查数据库
            //set成功 访问数据库

         // 先查缓存
            Jedis jedis = redisUtil.getJedis();
            String key = RedisConst.SKU_PREFIX+skuId+ RedisConst.SKU_SUFFIX;
            String skuInfoJson = jedis.get(key);

            if (skuInfoJson == null) {

                String  skuLockKey = RedisConst.SKU_PREFIX+skuId+ RedisConst.SKULOCK_SUFFIX;
                String lock = jedis.set(skuLockKey,"OK","nx","px",10000);
                //  2.9.0 可以 3.1.0就不行  有了新方法却不会用 可以分布的啊
                // 能否取号成功  获得分布锁
                if("OK".equals(lock)){
                       //操作数据库
                    PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
                    PmsSkuImage pmsSkuImage = new PmsSkuImage();
                    pmsSkuImage.setSkuId(skuId);
                    List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
                    pmsSkuInfo.setSkuImageList(pmsSkuImageList);

                    PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
                    pmsSkuAttrValue.setSkuId(skuId);
                    List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
                    pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);

                    PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
                    pmsSkuSaleAttrValue.setSkuId(skuId);
                    List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
                    pmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);

                    String skuInfoJsonStr = JSON.toJSONString(pmsSkuInfo);
                    jedis.setex(key,RedisConst.EXP_SEC,skuInfoJsonStr);
                    jedis.close();
                    return  pmsSkuInfo;
                }else {
                      // 自旋 睡眠等待
                    Thread.sleep(1000);
                    jedis.close();
                    return  getSkuInfoBySkuId(skuId);
                }
            }else {

                PmsSkuInfo pmsSkuInfo = JSON.parseObject(skuInfoJson, PmsSkuInfo.class);
                return pmsSkuInfo;
            }

        }
    */
    /*

        @Override
        public PmsSkuInfo getSkuInfoBySkuId(String skuId) {
    //        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
    //        pmsSkuInfo.setId(skuId);
    //        List<PmsSkuInfo> select = pmsSkuInfoMapper.select(pmsSkuInfo);


                //加入redis 缓存
            Jedis jedis = redisUtil.getJedis();
              // 设置key bean id filed  === sku skuid info
             String  key = RedisConst.SKU_PREFIX+skuId+RedisConst.SKU_SUFFIX;
            String skuInfoJson = jedis.get(key);
            if (skuInfoJson!=null){
                // 有缓存数据  返回缓存的数据  需要的是json数据
                PmsSkuInfo pmsSkuInfo1 = JSON.parseObject(skuInfoJson, PmsSkuInfo.class);

                jedis.close();
                System.err.println("有缓存数据  返回缓存的数据  需要的是json数据");
                return  pmsSkuInfo1;
            }else {
                // 没有缓存 查询数据库   把查询到的放进缓存
                // 根据主键查找 有selectByprimaryKey
                PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
                if (pmsSkuInfo==null){
                    return  null;
                }

                System.err.println("没有缓存 查询数据库   把查询到的放进缓存");

    //            @Transient
    //    List<PmsSkuImage> skuImageList;
    //    @Transient
    //    List<PmsSkuAttrValue> skuAttrValueList;
    //    @Transient
    //    List<PmsSkuSaleAttrValue> skuSaleAttrValueList;

                PmsSkuImage pmsSkuImage = new PmsSkuImage();
                pmsSkuImage.setSkuId(skuId);
                List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
                pmsSkuInfo.setSkuImageList(pmsSkuImageList);

                PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
                pmsSkuAttrValue.setSkuId(skuId);
                List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
                pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);

                PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
                pmsSkuSaleAttrValue.setSkuId(skuId);
                List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
                pmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);

                String skuInfoJsonStr = JSON.toJSONString(pmsSkuInfo);
                jedis.setex(key,RedisConst.EXP_SEC,skuInfoJsonStr);
                jedis.close();
                return  pmsSkuInfo;
            }


      // 实现分布式锁
            //  每次访问数据库前要看能不能redis中设置  相当于取号
            // setnxpx  set不成功自旋   查看缓存  没有再查数据库
             //set成功 访问数据库


        }

        */
    @Override
    public List<PmsSkuInfo> getSkuInfoByProductId(String productId) {

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setProductId(productId);

        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.select(pmsSkuInfo);
        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {
            String skuId = skuInfo.getId();

            PmsSkuImage pmsSkuImage = new PmsSkuImage();
            pmsSkuImage.setSkuId(skuId);
            List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
            skuInfo.setSkuImageList(pmsSkuImageList);

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            skuInfo.setSkuAttrValueList(pmsSkuAttrValueList);

            PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
            pmsSkuSaleAttrValue.setSkuId(skuId);
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
            skuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);


        }

        return   pmsSkuInfoList;
    }



    @Override
    public List<PmsSkuInfo> getAllSkuInfo() {

        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectAll();

        /*
           @Transient
    List<PmsSkuSaleAttrValue> skuSaleAttrValueList;
     @Transient
    List<PmsSkuImage> skuImageList;
    @Transient
    List<PmsSkuAttrValue> skuAttrValueList;
    @Transient
    List<PmsSkuSaleAttrValue> skuSaleAttrValueList;

        * */

        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {
            String skuId = skuInfo.getId();

            PmsSkuImage pmsSkuImage = new PmsSkuImage();
            pmsSkuImage.setSkuId(skuId);
            List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
            skuInfo.setSkuImageList(pmsSkuImageList);

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            skuInfo.setSkuAttrValueList(pmsSkuAttrValueList);

            PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
            pmsSkuSaleAttrValue.setSkuId(skuId);
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
            skuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);


        }

        return pmsSkuInfoList;

    }

    @Override
    public PmsSkuInfo getSkuInfoById(String productSkuId) {

      //   为什么不行啊PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(productSkuId);
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        return skuInfo;
    }


}
