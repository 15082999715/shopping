package com.jk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {

    /**
     * 跳转到手机展示页面
     * @return
     */
    @RequestMapping("toShopList")
    public String toShopList(){
        return "shopList";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("toRegister")
    public String toList(){
        return "reg";
    }

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "/login";
    }

    /**
     * 跳转到购物主页面
     * @return
     */
    @RequestMapping("toHomePage")
    public String toHomePage(){
        return "homepage";
    }

    /**
     * 跳转购物车页面
     * @return
     */
    @RequestMapping("toShopCart")
    public String toShopCart(){
        return "shopCart";
    }



}
