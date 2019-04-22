package com.jk.mapper;

import com.jk.pojo.ProductInfo;
import com.jk.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShopMapper {
    @Select("select * from tt_user")
    List<User> queryList();


    //ProductInfo queryProductInfo(Integer productId);
}
