package com.notime.mall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.*;
import com.notime.mall.api.service.PmsBaseAttrInfoService;
import com.notime.mall.api.service.PmsBaseAttrValueService;
import com.notime.mall.api.service.PmsSearchSkuInfoService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    PmsSearchSkuInfoService pmsSearchSkuInfoService;

    @Reference
    PmsBaseAttrInfoService pmsBaseAttrInfoService;

    @Reference
    PmsBaseAttrValueService pmsBaseAttrValueService;

    // 跳转搜索页面
    @RequestMapping("list.html")
    public String search( PmsSearchParam pmsSearchParam, Model model) {
        // 为什么不使用requestbody
        // 传递过来后台的不是json数据吗   渲染提交 不是ajax 提交
        //页面传过来的具体有什么属性
        /*
        *   @Id
    private long id;
    private String skuName;   //搜索商品名称
    private String skuDesc;    //搜索商品信息 smart
    private String catalog3Id;  // 点击平台属性跳转搜索页面
    private BigDecimal price;    // 显示价格
    private String skuDefaultImg;  //显示图片信息
    private double hotScore;    // 热点值  商品排序
    private String productId;    // 是那个平台属性
    private List<PmsSkuAttrValue> skuAttrValueList;  // 有哪些销售属性

        * */



        /*只需要返回list 和前端所需要的数据就行  跳转页面实现 前端完成
        前端需要  所有商品的信息 skuinfoList
                所有商品共同的平台属性  pmsBaseAttrInfos  保证每个属性下都有商品

        * */


        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = pmsSearchSkuInfoService.search(pmsSearchParam);
        model.addAttribute("skuLsInfoList", pmsSearchSkuInfoList);

        // 所有商品的平台属性    private List<PmsSkuAttrValue> skuAttrValueList; valueId


        Set<String> valueSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {

                String valueId = pmsSkuAttrValue.getValueId();
                // valueId 有重复的 需要set来去重
                valueSet.add(valueId);
            }
        }

        //  将set 转换为String  sql时用in 来查询
        String join = StringUtil.join(valueSet, ",");

        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoService.getLIstAttrInfoByValueId(join);
///···························································································································//
        //http://localhost:8083/list.html?valueId=140&valueId=137

        // 点击的平台属性后  平台属性消失 生成面包屑

        // 去掉就是 pmsBaseAttrInfoList 中删除 不是只删除8gb 而是把手机内存这个选项全部删除 判断条件是什么
        // 遍历 pmsBaseAttrInfoList
        //  再遍历 attrvalueList
        // 比较Id  与searchparam比较 相同就
        // valueid 有共同的attId  attrId 就是pmsBaseAttrInfo的Id  就删除它 就删除了整个平台属性
        String[] valueId = pmsSearchParam.getValueId();
        List<PmsSearchCrumb> pmsSearchCrumbList = new ArrayList<>();
    //    Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfoList.iterator();  放外面只能遍历一次
        if (valueId != null && valueId.length > 0) {

            for (String searchId : valueId) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfoList.iterator();
                // 先搞面包屑
                String urlParamCrumb = MergeUrl(pmsSearchParam, searchId);
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setUrlParam(urlParamCrumb);
                pmsSearchCrumb.setValueId(searchId);
             //   pmsSearchCrumb.setValueName(searchId);  // 需要属性值名称 pmsBaseInfo
                pmsSearchCrumbList.add(pmsSearchCrumb);
                while (iterator.hasNext()){
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    // 找到要删除的valuId  对应的PmsBaseAttrInfo
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String id = pmsBaseAttrValue.getId();
                        if (Objects.equals(id,searchId)){
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            iterator.remove();
                        }
                    }
                }
            }

        }





//        if (valueId != null && valueId.length > 0) {
//            // 对List 的增加删除 要用iterator  用for 会下角标越界  还没for完就被删除掉了
//            Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfoList.iterator();
//            while (iterator.hasNext()) {
//                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
//                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
//                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
//                    String id = pmsBaseAttrInfo.getId();
//                    for (String searchId : valueId) {
//                        // 根据searchId 获取pmsBaseAttrInfo 删除掉 ？  还是遍历一个一个删除attrvalueList
//                        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoService.getLIstAttrInfoByValueId(searchId);
//                        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
//                            String baseAttrInfoId = baseAttrInfo.getId();
//
//                            if (Objects.equals(baseAttrInfoId, id) && pmsBaseAttrInfo != null) {
//                                iterator.remove();
//                            }
//                        }
//                        //  放这里不行    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) { 循环了多次
//                        // 面包只循环valueIdlist
//                        String urlParamCrumb = MergeUrl(pmsSearchParam, searchId);
//                        PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
//                        pmsSearchCrumb.setUrlParam(urlParamCrumb);
//                        pmsSearchCrumb.setValueId(searchId);
//                        pmsSearchCrumb.setValueName(searchId);
//                        pmsSearchCrumbList.add(pmsSearchCrumb);
//                        // 合并面包屑
//                    }
//
//                }
//            }
//        }


        model.addAttribute("attrList", pmsBaseAttrInfoList);


        // 根据valueId 查询出PmsBaseAttrInfo


        //点击平台属性跳转http://localhost:8083/list.html?null&valueId=136
        // 显示面包屑和平台属性 <a th:href="'/list.html?'+${urlParam}+'&valueId='+${attrValue.id}"  th:text="${attrValue.valueName}">属性值</a></li>
        // 需要urlparam  PmsSearchCrumb

        //   String urlParam = getUrlParam(pmsSearchParam) ;
        String urlParam = MergeUrl(pmsSearchParam);
        model.addAttribute("urlParam", urlParam);

        //```````````````````````````````````````````````````````````````````````````````````//
        // 面包屑的生成attrValueSelectedList   PmsSearchCrumb
        // 还是生成url 地址


        /*
        <a class="select-attr"
           th:each="baseAttrValueSelected:${attrValueSelectedList}"
              th:href="'list.html?'+${baseAttrValueSelected.urlParam}"
                th:utext=" ${baseAttrValueSelected.valueName} +'<b> ✖ </b>'"  > 2G<b> ✖ </b>
        </a>

       面包屑的urlparam 是当前请求url  -valuedId 平台属性

       有多个面包屑，每个url是当前url 减去当前的valueid  当前valueid 我不要 遍历当前valueIdList
         */
//      遍历了两次  可以合并
//        String[] allValueId = pmsSearchParam.getValueId();
//        List<PmsSearchCrumb> pmsSearchCrumbList = new ArrayList<>();
//
//        if (allValueId != null && allValueId.length > 0) {
//            for (String id : allValueId) {
//                // 遍历url 的valuedId  生成每个valueId 对应的面包屑url    =  当前url - 当前valueid
//                // 怎么-呢  就是生成url 时 判断valuid = 我的valuid 我就不啥也不干  不等于再拼接
//                // String  urlParamCrumb =getUrlParamForCrumb(pmsSearchParam,id); // 当前请求的valueId 我不要
//                // 合并
//                String urlParamCrumb = MergeUrl(pmsSearchParam, id);
//                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
//                pmsSearchCrumb.setUrlParam(urlParamCrumb);
//                pmsSearchCrumb.setValueId(id);
//                pmsSearchCrumb.setValueName(id);
//                pmsSearchCrumbList.add(pmsSearchCrumb);
//            }
//        }

        model.addAttribute("attrValueSelectedList", pmsSearchCrumbList);
        return "list";

    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String id) {

        String[] valueId = pmsSearchParam.getValueId();
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        /*https://list.jd.com/list.html?cat=9987,653,655
        &sort=sort_rank_asc&trans=1
        &ev=3753_76033@5_122869&JL=3_CPU型号_骁龙845#J_crumbsBar
        catalog3Id 后面凭借valueId
        * */

        String urlParam = "";

        if (StringUtils.isNoneBlank(keyword)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&keyword=" + keyword;
            } else {
                urlParam = "keyword=" + keyword;
            }
        }

        if (StringUtils.isNoneBlank(catalog3Id)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&catalog3Id=" + catalog3Id;
            } else {
                urlParam = "catalog3Id=" + catalog3Id;
            }
        }

        if (valueId != null && valueId.length > 0) {
            for (String idurl : valueId) {
                if (!Objects.equals(id, idurl)) { // 不是这样 比较的啊

                    if (StringUtils.isNoneBlank(urlParam)) {
                        urlParam = urlParam + "&valueId=" + idurl;
                    } else {
                        urlParam = "valueId=" + idurl;
                    }
                }
            }
        }

        System.err.println(urlParam);
        return urlParam;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String[] valueId = pmsSearchParam.getValueId();
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        /*https://list.jd.com/list.html?cat=9987,653,655
        &sort=sort_rank_asc&trans=1
        &ev=3753_76033@5_122869&JL=3_CPU型号_骁龙845#J_crumbsBar
        catalog3Id 后面凭借valueId
        * */

        String urlParam = "";

        if (StringUtils.isNoneBlank(keyword)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&keyword=" + keyword;
            } else {
                urlParam = "keyword=" + keyword;
            }
        }

        if (StringUtils.isNoneBlank(catalog3Id)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&catalog3Id=" + catalog3Id;
            } else {
                urlParam = "catalog3Id=" + catalog3Id;
            }
        }

        if (valueId != null && valueId.length > 0) {
            for (String id : valueId) {
                if (StringUtils.isNoneBlank(urlParam)) {
                    urlParam = urlParam + "&valueId=" + id;
                } else {
                    urlParam = "valueId=" + id;
                }
            }
        }
        //http://localhost:8083/list.html?valueId=140&valueId=137

        // 点击的平台属性后  平台属性消失 生成面包屑

        // 去掉就是 pmsBaseAttrInfoList 中删除 不是只删除8gb 而是把手机内存这个选项全部删除 判断条件是什么
        // 遍历 pmsBaseAttrInfoList
        //  再遍历 attrvalueList
        // 比较Id  与searchparam比较 相同就
        // valueid 有共同的attId  attrId 就是pmsBaseAttrInfo的Id  就删除它 就删除了整个平台属性
        return urlParam;
    }


    //  合并 getUrlParam  getUrlParamforCramb

    //  可变形参  可以不传入id

    private String MergeUrl(PmsSearchParam pmsSearchParam, String... id) {

        String[] valueId = pmsSearchParam.getValueId();
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        String urlParam = "";

        if (StringUtils.isNoneBlank(keyword)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&keyword=" + keyword;
            } else {
                urlParam = "keyword=" + keyword;
            }
        }

        if (StringUtils.isNoneBlank(catalog3Id)) {
            //第一个不加& 就是为空时不加 不为空加
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&catalog3Id=" + catalog3Id;
            } else {
                urlParam = "catalog3Id=" + catalog3Id;
            }
        }

        if (valueId != null && valueId.length > 0) {
            for (String idurl : valueId) {
                //  id 有没有   id传过来是个数组
                // 没有就是当前url  没有就是面包屑的url
                //id[0]==null 已经下角标异界了 因为id ==null

                if (id == null || id.length == 0) {
//                    if (StringUtils.isNoneBlank(urlParam)){
                    urlParam = urlParam + "&valueId=" + idurl;
//                    }else {
//                        urlParam="valueId="+idurl;
//                    }
                } else {
                    if (!Objects.equals(id[0], idurl)) {
                        System.err.println("id=" + id);
                        System.err.println("id[0]=" + id[0]);
                        //这里是id 的话  数组与字符串永远不等  生成的面包屑就是当前url
                        // 生成的面包屑无法退回 id 是数组
//                        if (StringUtils.isNoneBlank(urlParam)){
                        urlParam = urlParam + "&valueId=" + idurl;
//                        }else {
//                            urlParam="valueId="+idurl;
//                        }

                        //  为什么无需判断valueId 前面是否为空
                        // 不准用户直接list.html?valuId进入
                        // 要点击三级目录进入

                    }
                }

            }
        }

        return urlParam;
    }
}
