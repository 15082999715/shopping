package com.jk.pojo;

import java.util.Date;

public class ShopGoods {

    private Integer id;
    private String goodName;
    private Integer goodCount;
    private Date goodTime;
    private Integer goodPrice;
    private String goodImg;
    private String goodNorms; //规格
    private String goodVender;//订货商

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Date getGoodTime() {
        return goodTime;
    }

    public void setGoodTime(Date goodTime) {
        this.goodTime = goodTime;
    }

    public Integer getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(Integer goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getGoodImg() {
        return goodImg;
    }

    public void setGoodImg(String goodImg) {
        this.goodImg = goodImg;
    }

    public String getGoodNorms() {
        return goodNorms;
    }

    public void setGoodNorms(String goodNorms) {
        this.goodNorms = goodNorms;
    }

    public String getGoodVender() {
        return goodVender;
    }

    public void setGoodVender(String goodVender) {
        this.goodVender = goodVender;
    }

    @Override
    public String toString() {
        return "ShopGoods{" +
                "id=" + id +
                ", goodName='" + goodName + '\'' +
                ", goodCount=" + goodCount +
                ", goodTime=" + goodTime +
                ", goodPrice=" + goodPrice +
                ", goodImg='" + goodImg + '\'' +
                ", goodNorms='" + goodNorms + '\'' +
                ", goodVender='" + goodVender + '\'' +
                '}';
    }
}
