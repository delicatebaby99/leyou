package com.leyou.search.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Category;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * date:2020-10-13
 * author:zhangxiaoshuai
 */
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 根据搜索条件分页查询商品数据
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {

        return ResponseEntity.ok(searchService.search(request));
    }


}
