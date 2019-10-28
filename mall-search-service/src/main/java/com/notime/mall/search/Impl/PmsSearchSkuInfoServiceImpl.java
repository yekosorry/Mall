package com.notime.mall.search.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsSearchParam;
import com.notime.mall.api.bean.PmsSearchSkuInfo;
import com.notime.mall.api.service.PmsSearchSkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jodd.util.StringUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class PmsSearchSkuInfoServiceImpl implements PmsSearchSkuInfoService {

    @Autowired
    JestClient  jestClient;

    @Override
    public List<PmsSearchSkuInfo> search(PmsSearchParam pmsSearchParam) {
        // 输入 搜索的参数  输出 searchskuinfo

        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] valueId = pmsSearchParam.getValueId();


        //dsl
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(200);
        searchSourceBuilder.from(0);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (StringUtil.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }


        if (StringUtil.isNotBlank(keyword)){
          //  MatchQueryBuilder keyword1 = new MatchQueryBuilder("keyword", keyword);
            MatchQueryBuilder keyword1 = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(keyword1);
        }

        if (valueId!=null&&(valueId.length>0)){
            for (String id : valueId) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",id);
                boolQueryBuilder.filter(termQueryBuilder);
            }

        }
        searchSourceBuilder.query(boolQueryBuilder);

        String dsl = searchSourceBuilder.toString();

        System.err.println(dsl);

        Search search = new Search.Builder(dsl).addIndex("searchmapping").addType("searchSkuInfo").build();
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();        try {
            SearchResult result = jestClient.execute(search);
            // 都搜索出的数据进行封装
            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hitList = result.getHits(PmsSearchSkuInfo.class);
        // 放在try 里面不是全局变量
        //    List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> searchSkuInfoVoidHit : hitList) {
                PmsSearchSkuInfo pmsSearchSkuInfo = searchSkuInfoVoidHit.source;
                pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return pmsSearchSkuInfoList;
    }
}
