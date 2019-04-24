package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.pojo.*;
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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ShopController {

    @Autowired
    private ShopServiceWeb shopServiceWeb;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    /**
     * elasticsearch搜索
     * @param page
     * @param rows
     * @param shop
     * @return
     */
    @RequestMapping("queryProduct")
    @ResponseBody
    public JSONObject queryProduct(Integer page, Integer rows, Shop shop){
        JSONObject result = new JSONObject();
        Client client = elasticsearchTemplate.getClient();
        Integer startIndex = rows*(page-1);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("shop").setTypes("elshop");
        if(shop.getName() !=null && shop.getName() != "" ){
            searchRequestBuilder.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name", shop.getName())));
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
        highlightBuilder.field("name");
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
            HighlightField name = highlightFields.get("name");
            Shop shopBean = JSON.parseObject(sourceAsString, Shop.class);
            //取得定义的高亮标签
            if(name !=null) {
                Text[] fragments = name.fragments();
                //为thinkName（相应字段）增加自定义的高亮标签
                String title = "";
                for (Text text1 : fragments) {
                    title += text1;
                }
                shopBean.setName(title);
            }
            list.add(shopBean);
        }
        result.put("total",total);
        result.put("rows",list);
        return result;
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(User user, String valCode, HttpSession session) {
        //判断账号是否存在
        User user2 = shopServiceWeb.findUserByUserName(user.getUsername());
        if(user2==null){
            return "账号不存在";
        }
        //判断密码是否正确
        if(!user.getPassword().equals(user2.getPassword())){
            return "密码错误";
        }
        session.setAttribute("user", user2);
        return "登录成功";
    }

    @RabbitListener(queues="good")
    @ResponseBody
    public void addGood(String good){
        //从消息队列中获取数据
        JSONObject jsonObject = JSONObject.parseObject(good);
        Goods goods = jsonObject.toJavaObject(Goods.class);
        //新建一个条件查询
        Query query = new Query();
        NewGoods newGoods = new NewGoods();
        List<ShopGoods> list = new ArrayList<>();
        //创建一个对象
        ShopGoods goodToMongo = new ShopGoods();
        goodToMongo.setId(goods.getId());
        goodToMongo.setGoodName(goods.getGoodName());
        goodToMongo.setGoodCount(goods.getGoodCount());
        goodToMongo.setGoodImg(goods.getGoodImg());
        goodToMongo.setGoodNorms(goods.getGoodNorms());
        goodToMongo.setGoodPrice(goods.getGoodPrice());
        goodToMongo.setGoodVender(goods.getGoodVender());
        //判断用户是否登录，不登录设置为0
        if(goods.getUserId()==null){
            goods.setUserId(0);
        }
        //将用户的id存放到要存入mongodb的表中
        newGoods.setUserid(goods.getUserId());
        //创建条件查询
        query.addCriteria(Criteria.where("userid").is(goods.getUserId()));
        //从MongoDB数据库中查询数据
        List<NewGoods> newGoods1 = mongoTemplate.find(query, NewGoods.class);
        //判断数据库中是否存在数据
        if(newGoods1.size()!=0){
            //如果存在，删除数据库中的数据
            mongoTemplate.remove(query,NewGoods.class);
            //遍历数据库中数据，取出存放里面的list集合
            List<ShopGoods> goodsList=null;
            for (NewGoods goodfor : newGoods1){
                goodsList=goodfor.getList();
            }
            //定义临时变量
            ShopGoods mongdb=null;
            int count=0;
            for(ShopGoods shopGoods : goodsList){
                //判断商品id是否一致，若一致向list集合添加商品，并改变临时变量count数量
                if(goodToMongo.getId()==shopGoods.getId()){
                    list.add(shopGoods);
                    count++;
                }//判断商品id是否一致，集合商品id是否为空
                else if(goodToMongo.getId()!=shopGoods.getId() && shopGoods.getId()!=null){
                    list.add(shopGoods);
                }//判断商品id与集合商品id是否一致 不一致给对象赋值
                if (goodToMongo.getId()!= shopGoods.getId()){
                    mongdb=goodToMongo;
                }
            }
            //判断临时变量的值是否改变 若没有改变 添加值
            if(count==0){
                list.add(mongdb);
            }
        }else {
            //若集合为空则直接添加到list
            list.add(goodToMongo);
        }
        newGoods.setList(list);
        //存入到mongodb
        mongoTemplate.insert(newGoods);

    }


    /*@Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("addShoppingCart")
    @ResponseBody
    public void addShoppingCart(Integer id,Integer count){
        Map<String, Integer> map = new HashMap<>();
        map.put("id",id);
        map.put("count",count);
        amqpTemplate.convertAndSend("shopping-Cart", map);
    }

    @DeleteMapping("deleteShoppingCart")
    @ResponseBody
    public void deleteShoppingCart(Integer[] ids){
        amqpTemplate.convertAndSend("delete-shopping-Cart", ids);
    }*/






}
