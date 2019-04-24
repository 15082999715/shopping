package com.jk.pojo;

import java.io.Serializable;

public class GoodsInfo implements Serializable {
    private static final long serialVersionUID = -5711753525402785207L;
    private Integer id;
    private Integer goodCount;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "id=" + id +
                ", goodCount=" + goodCount +
                ", userId=" + userId +
                '}';
    }
}
