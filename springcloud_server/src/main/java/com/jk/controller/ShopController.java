package com.jk.controller;

import com.alibaba.fastjson.JSONArray;
import com.jk.pojo.ConstantUtil;
import com.jk.pojo.ProductInfo;
import com.jk.pojo.User;
import com.jk.mapper.ShopMapper;
import com.jk.pojo.UserProductInfo;
import com.jk.service.ShopServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class ShopController implements ShopServiceApi {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    //@Autowired
    //private RedisTemplate redisTemplate;



    @Override
    public List<User> queryList() {
        return shopMapper.queryList();
    }

    /*@Override
    public List<ProductInfo> queryCartProduct() {
        List<ProductInfo> list = new ArrayList<ProductInfo>();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(1));
        UserProductInfo one = mongoTemplate.findOne(query, UserProductInfo.class);
        if(one !=null ) {
            JSONArray productInfo = one.getProductInfo();
            for (int i = 0; i < productInfo.size(); i++) {
                ProductInfo product = new ProductInfo();
                //获取商品id
                Integer productId = productInfo.getJSONArray(i).getInteger(0);
                String cacheKey = ConstantUtil.PRODUCT_INFO_CACHE + productId;
                if (redisTemplate.hasKey(cacheKey)) {
                    product = (ProductInfo) redisTemplate.opsForValue().get(cacheKey);
                } else {
                    product = shopMapper.queryProductInfo(productId);
                    redisTemplate.opsForValue().set(cacheKey, product);
                    redisTemplate.expire(cacheKey, 60, TimeUnit.DAYS);
                }
                Integer count = productInfo.getJSONArray(i).getInteger(1);
                product.setCount(count);
                list.add(product);
            }
        }
        return list;
    }*/





}
