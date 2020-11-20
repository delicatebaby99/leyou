package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;

import com.leyou.item.service.BrandService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


/**
 * date:2020-06-10
 * author:zhangxiaoshuai
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 分页查询
     * @param key 查询关键字
     * @param page 当前页
     * @param rows 每页显示行数
     * @param sortBy 根据。。排序
     * @param desc 升序或降序
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
@RequestParam(value = "key",required = false)String key,
@RequestParam(value = "page",defaultValue = "1")Integer page,
@RequestParam(value = "rows",defaultValue = "5")Integer rows,
@RequestParam(value = "sortBy",required = false)String sortBy,
@RequestParam(value = "desc",defaultValue = "false")Boolean desc
    ){
        PageResult<Brand> result = brandService.queryBrandByPage(key, page, rows, sortBy, desc);

        System.out.println(result.getItems());
       return ResponseEntity.ok(result);

    }

    /**
     * 新增品牌
     * @param brand 品牌信息
     * @param cids 所属分类信息
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids")List<Long> cids){

        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 查询品牌根据id
     * @param id
     * @return
     */
    @GetMapping("bid/{id}")
    public ResponseEntity<Brand> queryBrandByBid(@PathVariable("id") Long id){
        Brand brand= this.brandService.queryBrandByBid(id);

        return ResponseEntity.ok(brand);
    }

    /**
     * 修改品牌信息
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping()
    public ResponseEntity<Void> updBrand(
            Brand brand,
            @RequestParam("cids")List<Long> cids){

     this.brandService.updateBrandById(brand,cids);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid")Long bid){
        this.brandService.deleteBrandByBid(bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid")Long cid){
        return ResponseEntity.ok(this.brandService.queryBrandByCid(cid));
    }

    /**
     * 根据商品品牌id查询商品品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        return ResponseEntity.ok(brandService.queryById(id));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){

        return ResponseEntity.ok(brandService.queryByIds(ids));
    }



}
