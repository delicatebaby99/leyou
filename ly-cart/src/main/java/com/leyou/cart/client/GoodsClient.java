package com.leyou.cart.client;

import com.leyou.item.api.GoodsAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-21
 * author:zhangxiaoshuai
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsAPI {
}
