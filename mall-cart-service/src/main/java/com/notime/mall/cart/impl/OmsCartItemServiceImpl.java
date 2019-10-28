package com.notime.mall.cart.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.notime.mall.api.bean.OmsCartItem;
import com.notime.mall.api.service.OmsCartItemService;
import com.notime.mall.cart.mapper.OmsCartItemMapper;
import com.notime.mall.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {


    // 数据需要在redis 也放一份  方便显示购物车  无需再访问db
        // key   userid cartinfoid  cartinfo
            // 测试 hvals memberId:1
            //hgetall memberId:1

    // 坑  不转为json数据就知识地址值了
    @Autowired
    RedisUtil   redisUtil;
    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Override
    public void addCartItem(OmsCartItem omsCartItem) {

       if (omsCartItem != null){
           // 为什么 属性不全
           // 自己添加算了
           // 前端传过来的只有 productSkuId 与quantity啊
           // 这里加  或者controller 哪里加
            omsCartItemMapper.insertSelective(omsCartItem);
           Jedis jedis = redisUtil.getJedis();
           //jedis.hset("memberId:"+omsCartItem.getMemberId(),"cartItemId:"+omsCartItem.getId(),"cartItemInfo:"+ JSON.toJSONString(omsCartItem));
           jedis.hset("memberId:"+omsCartItem.getMemberId(),"cartItemId:"+omsCartItem.getId(),JSON.toJSONString(omsCartItem));
          //导致后面无法准换hvals无法获取原值 转换未object   "cartItemInfo:"+ JSON.toJSONString(omsCartItem)

           jedis.close();
       }
    }

    @Override
    public OmsCartItem isExitsCart(OmsCartItem omsCartItem) {
        // 不同omscartitem 不同的字段是什么   product_sku_id
        // product_sku_id + memberId  才能定位唯一的
        String productSkuId = omsCartItem.getProductSkuId();
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setProductSkuId(productSkuId);  // skuinfoId
        cartItem.setMemberId(omsCartItem.getMemberId());   //userid
        OmsCartItem cartItem1 = omsCartItemMapper.selectOne(cartItem);
        return cartItem1;
    }

    @Override
    public void updateCartItem(OmsCartItem omsCartItemForDb) {
        // 可以设置主键id 找到该商品      @GeneratedValue(strategy = GenerationType.IDENTITY)
         omsCartItemMapper.updateByPrimaryKeySelective(omsCartItemForDb);

        Jedis jedis = redisUtil.getJedis();
        jedis.hset("memberId:"+omsCartItemForDb.getMemberId(),"cartItemId:"+omsCartItemForDb.getId(),JSON.toJSONString(omsCartItemForDb));

        jedis.close();





    }

    @Override
    public List<OmsCartItem> getCartListCookie(String memberId) {
        //从redis 中获取 List<OmsCartItem>
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        Jedis jedis = redisUtil.getJedis();
        List<String> hvals = jedis.hvals("memberId:" + memberId);
        if (hvals!=null&&hvals.size()>0){
           // JSON.parseArray(hvals,OmsCartItem.class)
            for (String hval : hvals) {
                OmsCartItem cartItem = JSON.parseObject(hval, OmsCartItem.class);


                omsCartItemList.add(cartItem);



            }
        }
        jedis.close();
        return omsCartItemList;
    }

    @Override
    public OmsCartItem checkCart(String productSkuId, String memberId,String isChecked ) {
        // 找到它  更新它的ischecked
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(productSkuId);
        OmsCartItem omsCartItemFormDb = omsCartItemMapper.selectOne(omsCartItem);
       // System.err.println(omsCartItemFormDb);
        omsCartItemFormDb.setIsChecked(isChecked);
         omsCartItemMapper.updateByPrimaryKeySelective(omsCartItemFormDb);
        //redis
        Jedis jedis = redisUtil.getJedis();
        jedis.hset("memberId:"+omsCartItemFormDb.getMemberId(),"cartItemId:"+omsCartItemFormDb.getId(),JSON.toJSONString(omsCartItemFormDb));
        jedis.close();
        return omsCartItemFormDb;
    }

    @Override
    public void deleteList(List<OmsCartItem> cartItemList) {

        Jedis jedis = redisUtil.getJedis();
        System.err.println(cartItemList);
        for (OmsCartItem cartItem : cartItemList) {
            //   if (cartItem.getIsChecked()=="1"){  // 不是这样判断的弟弟们
            if ("1".equals(cartItem.getIsChecked())){
         // 这样是删除不掉的我透       int delete = omsCartItemMapper.delete(cartItem);  是不是给的条件太多了 这样不是应该更好吗
                OmsCartItem cartItem1 = new OmsCartItem();
                cartItem1.setId(cartItem.getId());
                omsCartItemMapper.delete(cartItem1);
                jedis.hdel("memberId:"+cartItem.getMemberId(),"cartItemId:"+cartItem.getId());
            }
        }
        // redis 删除啊
        jedis.close();
    }
}
