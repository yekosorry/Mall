package com.notime.mall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsSearchSkuInfo;
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.service.PmsSkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchServiceApplicationTests {


    // 将数据库searchinfo装进es 进去
    // 需要先在es建立mapping  更高效的对数据分词


    @Autowired
    JestClient jestClient;
    @Reference
    PmsSkuInfoService pmsSkuInfoService;
    @Test
    public void contextLoads() throws IOException {

        // searchmapping/searchSkuInfo/_search
        // 将skuinfo 封装成  searchinfo 再put进去

        List<PmsSkuInfo> allSkuInfo = pmsSkuInfoService.getAllSkuInfo();

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : allSkuInfo) {

            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);

            // PmsSearchSkuInfo  id  是Long   PmsSkuInfo 是String 需要手动设置

            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            System.err.println(pmsSearchSkuInfo.getSkuAttrValueList());
            Index index = new Index.Builder(pmsSearchSkuInfo).index("searchmapping").type("searchSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();

            jestClient.execute(index);


        }

    }

    //模拟用户对es的查询
    //keyword catalog3 菜单属性 sale _attr_valueId

    /*需要searchSourceBuilder
      query 时 需要什么就什么querybuild
    * */

    @Test
    public void testSearch() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.size(200);  //默认10

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","140");
        boolQueryBuilder.filter(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        String dsl = searchSourceBuilder.toString();
        Search search = new Search.Builder(dsl).addIndex("searchmapping").addType("searchSkuInfo").build();

        SearchResult searchResult = jestClient.execute(search);

        // 返回的是list searchinfo 封装一下=
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);


        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {

            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;

            System.err.println(pmsSearchSkuInfo);

            // 为什么遍历是空
            // put 的时候有把  skuAttrValueList.valueId 放进去吗
            // 没有放进去   pmsSkuInfoService.getAllSkuInfo();


            //skuAttrValueList.valueId  是什么
              // 对应的是 平台销售属性  baseAttrInfo
                        //为什么不是pmsBaseAttrValue
                            // 需要更多的属性且 封装了pmsBaseAttrValue




        }

    }

}
