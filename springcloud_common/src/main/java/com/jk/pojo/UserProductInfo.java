package com.jk.pojo;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.mapping.Document;

//import org.springframework.web.bind.annotation.Mapping;

@Document(collection = "UserProductInfo")
public class UserProductInfo {

    private String id;

    private Integer userId;

    private JSONArray productInfo;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONArray getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(JSONArray productInfo) {
        this.productInfo = productInfo;
    }
}
