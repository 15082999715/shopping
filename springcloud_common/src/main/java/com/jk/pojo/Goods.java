package com.jk.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Goods {

    private Integer id;
    private String goodName;
    private Integer goodCount;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date goodTime;
    private Integer goodPrice;
    private String goodImg;
    private String goodNorms; //规格
    private String goodVender;//订货商
    private Integer userId;
    private Integer totalPrice;//总价格

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", goodName='" + goodName + '\'' +
                ", goodCount=" + goodCount +
                ", goodTime=" + goodTime +
                ", goodPrice=" + goodPrice +
                ", goodImg='" + goodImg + '\'' +
                ", goodNorms='" + goodNorms + '\'' +
                ", goodVender='" + goodVender + '\'' +
                ", userId=" + userId +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
