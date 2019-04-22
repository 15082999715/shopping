package com.jk.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("shopping-service")
public interface ShopServiceWeb extends ShopServiceApi{
}
