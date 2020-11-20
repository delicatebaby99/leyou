package com.leyou.order.client;

import com.leyou.item.api.GoodsAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-24
 * author:zhangxiaoshuai
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsAPI {
}