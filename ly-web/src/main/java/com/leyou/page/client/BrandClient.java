package com.leyou.page.client;

import com.leyou.item.api.BrandAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@FeignClient("item-service")
public interface BrandClient extends BrandAPI {
}
