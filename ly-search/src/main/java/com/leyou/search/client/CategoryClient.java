package com.leyou.search.client;

import com.leyou.item.api.CategoryAPI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryAPI {

}
