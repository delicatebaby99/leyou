package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
public interface CategoryAPI {


    /**
     * 根据cid查询品牌
     * @param ids
     * @return
     */
    @GetMapping("category/list/ids")
   List<Category> queryBrandByCids(@RequestParam("ids") List<Long> ids);

    /**
     * 根据ids查询分类
     * @param ids
     * @return
     */
    @GetMapping("category/ids")
    List<String>  queryCategoryNameByIds(@RequestParam("ids")List<Long> ids);

}
