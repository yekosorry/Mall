package com.notime.mall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.bean.PmsSkuSaleAttrValue;
import com.notime.mall.api.bean.UmsMember;
import com.notime.mall.api.service.PmsProductInfoService;
import com.notime.mall.api.service.PmsSkuInfoService;
import com.notime.mall.api.service.PmsSkuSaleAttrValueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class ItemController {


// 学习Thymeleaf
    // 取值
    //判断
    // 循环
    // 函数调用
    // 三元表达
    // 页面引入
    // 对象取值

    @RequestMapping("sdf")
    public String test(Model model) {
        String yeko = "火箭头槌";
        model.addAttribute("hello", yeko);

        String flag = "1";
        model.addAttribute("flag", flag);
        ArrayList<Object> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        model.addAttribute("list", list);
        String num = "1";
        model.addAttribute("num", num);

        UmsMember umsMember = new UmsMember();
        umsMember.setUsername("千年");
        model.addAttribute("umsMember", umsMember);


        return "themeleafTest";
    }

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @Reference
    PmsProductInfoService pmsProductInfoService;

    @Reference
    PmsSkuSaleAttrValueService pmsSkuSaleAttrValueService;


    // 根据skuid  跳转页面详情
    // skuId 自动提示不是驼峰命名可还行
    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, Model model) throws InterruptedException {
        PmsSkuInfo pmsSkuInfo = pmsSkuInfoService.getSkuInfoBySkuId(skuId);
        // 前端都是skuInfo 不是自定义的
        model.addAttribute("skuInfo", pmsSkuInfo);

        // 显示销售属性
        String productId = pmsSkuInfo.getProductId();
        // 返回pmsproductAttr 而不是pmsproductAttrValue
        // 因为返回pmsproductAttr中有而不是pmsproductAttrValue的transiant
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfoService.getSpuSaleAttrListByProductId(productId, skuId);
//spuSaleAttrListCheckBySku

        model.addAttribute("spuSaleAttrListCheckBySku", pmsProductSaleAttrList);


        // 实现 点击属性跳转页面
        //  让每个属性对应skuid  直接跳转skuid  实现静态化数据  不用每次都查数据库

        // 返回的是所在的skuInfo  所在的spu 所有的pmsskusaleattrvale

        // List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList1 =  pmsSkuSaleAttrValueService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());


        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoService.getSkuInfoByProductId(pmsSkuInfo.getProductId());

        // 拼接 hsahmapstr   根据key  |attr_value_id|attr_value_id  V skuId


        Map<String, String> valuesSkuinfo = new HashMap<>();

        // k 和 v 都底是什么
        // k sale attr value id  同个skuod 的所有
        // v skuid
        //   for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList1) {

        //  key = key+"|"+pmsSkuSaleAttrValue.getSaleAttrValueId();
        //   valuesSkuinfo.put(key,pmsSkuSaleAttrValue.getSkuId());
//  一个spu
        // 判断skuId 相等的Key 连接
        // 为什么不遍历spuId 下有多少个sku  每个sku 下有多少attr value -id 的组合呢

        // 是由两个因素确定一个因素 还是一个因素确定两个因素
        // 本来一个spu下的每个skuid下的 attr-value-id组合就是不同的
        //      pmsSkuSaleAttrValue.get
        //  }

        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {
            //    System.err.println(skuInfo);
            String key = "";
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {

                String saleAttrValueId = pmsSkuSaleAttrValue.getSaleAttrValueId();
                key = key + "|" + saleAttrValueId;
            }

            valuesSkuinfo.put(key, skuInfo.getId());
        }

        System.err.println(valuesSkuinfo);
        String valueSkuJson = JSON.toJSONString(valuesSkuinfo);
        System.err.println("asd" + valueSkuJson);

        model.addAttribute("valueSkuJson", valueSkuJson);
        return "item";
    }
    /*

spring.thymeleaf.cache=false    热部署

spring.thymeleaf.mode=LEGACYHTML5  这个什么意思


为什么不显示销售属性
没有是返回spuSaleAttrListCheckBySku
     */



    /*  删除所在行 cy
    复制cd
     选中代码块 cw
      插入自定义模块 cj
      方法参数显示cp
     显示方法所在位置cq
     前往方法所在位置cu
      ctrl 左右  在单词左右
      版本控制 alt `
    显示文件目标弹出层 alt + f1
      shift  滚轮 当前文件左右移动
      格式整理
       导入类  ctrl alt   +o

     对代码try catch ctrl alt t
      所有查找 ctrl shift
      合并下一行  ctrl shift j
      大小写切换  CTRL SHIFT U
      对当前类生成测试类 ctrl shift t

       代码块注释   ctrl shift  /
      展开关折叠所有代码 ctrl shift +-

        自动

*/


}

