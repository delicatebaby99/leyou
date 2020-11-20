package com.leyou.search.client;

import com.leyou.item.api.SpecificationAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationAPI {
}
