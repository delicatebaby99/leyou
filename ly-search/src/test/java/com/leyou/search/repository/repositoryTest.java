package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class repositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandClient brandClient;

    @Test
    public void CreateIndex() {
        //创建索引
        elasticsearchTemplate.createIndex(Goods.class);
        //配置映射
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData() {
        int size = 0;
        int page = 1;
        int rows = 100;
        do {
            //查询spu信息
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            //构建goods
            List<Spu> spuList = result.getItems();
            List<Goods> goodsList = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());
//            goodsList.forEach(goods -> {
//                System.out.println(goods);
//            });
            //存入索引库
            goodsRepository.saveAll(goodsList);
            page++;
            size = spuList.size();
        } while (size == 100);


    }

    @Test
    public void teste() {
        List<Sku> skuList = goodsClient.querySkuBySpuId(2l);
        skuList.forEach(sku -> {
            System.out.println(sku);
        });

    }

}