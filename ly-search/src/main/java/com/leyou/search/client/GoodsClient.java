package com.leyou.search.client;

import com.leyou.item.api.GoodsAPI;
import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsAPI {

}
