package com.leyou.item.web;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * date:2020-05-06
 * author:zhangxiaoshuai
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("item")
//    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){
//
//        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
//    }
    @Autowired
    private BrandService brandService;

    /**
     * 根据parent_id进行查询，rest风格返回结果
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){

        return ResponseEntity.ok(this.categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据cid查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryBrandByCids(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }


    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ids查询分类
     * @param ids
     * @return
     */
    @GetMapping("ids")
    public ResponseEntity<List<String>>  queryByCategoryNamesIds(@RequestParam("ids")List<Long> ids){
        return  ResponseEntity.ok(this.categoryService.queryByCategoryNamesIds(ids));
    }


}
