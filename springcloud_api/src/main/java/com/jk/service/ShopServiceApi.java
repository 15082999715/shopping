package com.jk.service;

import com.jk.pojo.ProductInfo;
import com.jk.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ShopServiceApi {

    @GetMapping("queryList")
    List<User> queryList();

    /*@GetMapping("queryCartProduct")
    List<ProductInfo> queryCartProduct();*/
}
