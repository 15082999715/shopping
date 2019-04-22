package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.pojo.ConstantUtil;
import com.jk.pojo.ProductInfo;
import com.jk.pojo.Shop;
import com.jk.pojo.User;
import com.jk.service.ShopServiceWeb;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    @Autowired
    private ShopServiceWeb shopServiceWeb;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("queryList")
    @ResponseBody
    public List<User> queryList(){
        return  shopServiceWeb.queryList();
    }

    /**
     *  查询购物车
     * @return
     */
    /*@RequestMapping("queryCartProduct")
    @ResponseBody
    public List<ProductInfo> queryCartProduct(){
        return  shopServiceWeb.queryCartProduct();
    }*/


    @RequestMapping("queryProduct")
    @ResponseBody
    public JSONObject queryProduct(Integer page, Integer rows, Shop shop){
        JSONObject result = new JSONObject();
        Client client = elasticsearchTemplate.getClient();
        Integer startIndex = rows*(page-1);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("shop").setTypes("elshop");
        if(shop.getInfo() !=null && shop.getInfo() != "" ){
            searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("info", shop.getInfo())));
        }

        if(shop.getStartPrice() !=null){
            searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("price").gte(shop.getStartPrice())));
        }
        if(shop.getEndPrice() !=null){
            searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("price").lte(shop.getEndPrice())));
        }


        //价格排序
        if(shop.getSortPrice() == ConstantUtil.ORDER_SORT_ASC){
            searchRequestBuilder.addSort("price", SortOrder.ASC);
        }else if(shop.getSortPrice() == ConstantUtil.ORDER_SORT_DESC){
            searchRequestBuilder.addSort("price", SortOrder.DESC);
        }
        searchRequestBuilder.setFrom(startIndex).setSize(rows);
        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("info");
        highlightBuilder.preTags("<font color='red' >");
        highlightBuilder.postTags("</font>");
        searchRequestBuilder.highlighter(highlightBuilder);

        SearchResponse searchResponse = searchRequestBuilder.get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println("total = [" + total + "]");

        Iterator<SearchHit> iterator = hits.iterator();

        List<Shop> list = new ArrayList<Shop>();

        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            Map<String, HighlightField> highlightFields = next.getHighlightFields();

            String sourceAsString = next.getSourceAsString();
            HighlightField info = highlightFields.get("info");
            Shop shopBean = JSON.parseObject(sourceAsString, Shop.class);
            //取得定义的高亮标签
            if(info !=null) {
                Text[] fragments = info.fragments();
                //为thinkName（相应字段）增加自定义的高亮标签
                String title = "";
                for (Text text1 : fragments) {
                    title += text1;
                }
                shopBean.setInfo(title);
            }
            list.add(shopBean);
        }
        result.put("total",total);
        result.put("rows",list);
        return result;
    }








}
