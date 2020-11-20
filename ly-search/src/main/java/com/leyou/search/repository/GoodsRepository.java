package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
