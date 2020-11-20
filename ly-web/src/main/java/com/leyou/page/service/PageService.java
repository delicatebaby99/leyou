package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * date:2020-10-16
 * author:zhangxiaoshuai
 */
@Service
public class PageService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;



//    public Map<String, Object> loadModel(Long spuId){
//
//            // 查询spu
//            Spu spu = this.goodsClient.querySpuById(spuId);
//
//            // 查询spu详情
//            SpuDetail detail = this.goodsClient.querySpuDetailById(spuId);
//
//            // 查询skus
//            List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
//
//            // 查询品牌
//            List<Brand> brands = this.brandClient.queryBrandByIds(Arrays.asList(spu.getBrandId()));
//
//            // 查询分类
//            List<Category> categories = this.categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
//
//            // 查询组内参数
//            List<SpecGroup> specGroups = this.specClient.querySpecsByCid(spu.getCid3());
//
//        // 查询所有特有规格参数
//        List<SpecParam> specParams = this.specClient.queryParamList( spu.getCid3(), null, false);
//        // 处理规格参数
//        Map<Long, String> paramMap = new HashMap<>();
//        specParams.forEach(param->{
//            paramMap.put(param.getId(), param.getName());
//        });
//
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("spu", spu);
//            map.put("spuDetail", detail);
//            map.put("skus", skus);
//            map.put("brand", brands.get(0));
//            map.put("categories", categories);
//            map.put("groups", specGroups);
//            map.put("paramMap", paramMap);
//
//            return map;
//
//    }

    public Map<String,Object> loadData(Long spuId){

        Map<String,Object> model = new HashMap<>();

        //根据SpuID查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);
        //查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);
        //查询分类； Map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryCategoryNameByIds(cids);
//        names.forEach(name-> System.out.println(name));
        //初始化一个分类Map
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }
//        categories.forEach(category-> System.out.println(category));

        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //skus
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
        //查询规格参数组
        List<SpecGroup> specs = this.specificationClient.querySpecsByCid(spu.getCid3());
        System.out.println("规格组数量：----------------"+specs.size());

        // 查询所有特有规格参数
        List<SpecParam> specParams = this.specificationClient.queryParamList( spu.getCid3(),null, null, false);
        // 处理规格参数
        Map<Long, String> paramMap = new HashMap<>();
        specParams.forEach(param->{
            System.out.println(param.getName());
            paramMap.put(param.getId(), param.getName());
        });

        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("detail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("specs",specs);
        model.put("paramMap",paramMap);

        return  model;
    }



}
