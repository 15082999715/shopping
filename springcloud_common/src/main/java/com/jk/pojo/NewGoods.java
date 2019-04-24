package com.jk.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "goods")
public class NewGoods {

    private Integer userid; //mongoDB id类型为string类型的
    private List<ShopGoods>list;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public List<ShopGoods> getList() {
        return list;
    }

    public void setList(List<ShopGoods> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NewGoods{" +
                "userid=" + userid +
                ", list=" + list +
                '}';
    }
}
