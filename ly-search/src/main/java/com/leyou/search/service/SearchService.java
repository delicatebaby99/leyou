package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@Service
@Slf4j
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;


    /**
     * 查询结果将数据分装成全端需要的goods类型
     * @param spu
     * @return
     */

    public Goods buildGoods(Spu spu) {
        //查询分类
        List<Category> categories = categoryClient.queryBrandByCids(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        //搜索字段
        String all = spu.getTitle() + StringUtils.join(names, " ") + brand.getName();

        //查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnums.GOODS_SKU_NOT_FOUND);
        }
//        List<Long> priceList = skuList.stream().map(Sku::getPrice).collect(Collectors.toList());
        List<Long> priceList = new ArrayList<>();
        //skuList中属性太多，很多不需要，所以进行处理
        List<Map<String, Object>> skus = new ArrayList<>();
        for (Sku s : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("title", s.getTitle());
            map.put("price", s.getPrice());
            map.put("image", StringUtils.substringBefore(s.getImages(), ","));
            skus.add(map);
            priceList.add(s.getPrice());
        }

        //查询规格参数
        List<SpecParam> params = specClient.queryParamList(spu.getCid3(), null, true,null);

        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        //获取通用参数规格
        Map<String, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        //获取特有规格参数
        Map<String, List<String>> specialSpec = JsonUtils
                .nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {
                });

        //规格参数,key是规格参数名称，value是规格参数值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            //规格名称
            String key = param.getName();
            Object value = "";
            //判断是否为通用规格
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId().toString());
                //判断是否是数值类型
                if (param.getNumeric()) {
                    //处理成段
                    value = chooseSegment(value.toString(), param);
                }

            } else {
                value = specialSpec.get(param.getId().toString());

            }
            specs.put(key, value);
        }


        Goods goods = new Goods();

        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all); // 搜索字段，包含标题，分类，品牌，规格等
        goods.setPrice(priceList);// 所有sku的价格集合
        goods.setSkus(JsonUtils.serialize(skus));// 所有sku集合的json格式
        goods.setSpecs(specs);//所有的可搜索的规格参数
        goods.setSubTitle(spu.getSubTitle());

        return goods;
    }

    /**
     * 根据参数判断范围
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 根据搜索条件分页查询
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        //查询从0开始
        Integer page = request.getPage() - 1;
        Integer size = request.getSize();
        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //0、结果过滤(过滤掉不需要的字段,需要id，subtitle，sku)
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        //1、分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        //2、搜素条件  QueryBuilders.matchQuery("all", request.getKey())
        QueryBuilder basicQuery = BuildBasicQueryWithFilter(request);
        queryBuilder.withQuery(basicQuery);
        //3、聚合分类和品牌
        String categoryAggName = "category_agg"; // 商品分类聚合名称
        String brandAggName = "brand_agg"; // 品牌聚合名称
        // 3.1对商品分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 3.2对品牌进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //4、查询，获取聚合结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        //5、解析结果
        //5.1解析分页结果

        long total = result.getTotalElements();
        long totalPages = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        //5.2分析聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        //6根据商品分类判断是否需要聚合
        List<Map<String,Object>> specs=null;

        if (categories!=null&&categories.size()==1){
            //商品分类存在并且数量为1，可以聚合规格参数
             specs = buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }
        return new SearchResult(total, totalPages, goodsList, categories, brands,specs);
    }

    /**
     * 构建查询参数（查询条件和过滤条件）
     * @param request
     * @return
     */
    private QueryBuilder BuildBasicQueryWithFilter(SearchRequest request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 商品分类和品牌要特殊处理
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // 字符串类型，进行term查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }


//    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
//        List<Map<String,Object>> specs=new ArrayList<>();
////       1 查询所需聚合的规格参数
//        List<SpecParam> params = specClient.queryParamList(cid, null, true);
////        params.forEach(p-> System.out.println(p));
////       2 聚合
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
////        2.1带上查询条件
//        queryBuilder.withQuery(basicQuery);
////        2.2聚合
//        for (SpecParam param:params) {
//            String name=param.getName();
//            queryBuilder.addAggregation(
//                    AggregationBuilders.terms(name).field("spec."+name+".keyword"));
//        }
////        获取结果
//        AggregatedPage<Goods> result = templates.queryForPage(queryBuilder.build(), Goods.class);
////        解析结果
//        Aggregations aggs = result.getAggregations();
//        for (SpecParam param : params) {
//            String name = param.getName();
//            StringTerms terms = aggs.get(name);
//            Map<String, Object> map = new HashMap<>();
//            map.put("k",name);
//            map.put("options",terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));
//            specs.add(map);
//        }
//        return specs;
//    }

    /**
     * 聚合出规格参数
     *
     * @param cid
     * @param query
     * @return
     */
    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder query) {
        try {
            // 不管是全局参数还是sku参数，只要是搜索参数，都根据分类id查询出来
            List<SpecParam> params = this.specClient.queryParamList(cid,null, true,null);
            List<Map<String, Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);

            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));

            });

            // 查询
            Map<String, Aggregation> aggs = this.template.query(queryBuilder.build(),
                    SearchResponse::getAggregations).asMap();

            // 解析聚合结果
            params.forEach(param -> {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                spec.put("k", key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });

            return specs;
        } catch (
                Exception e)

        {
            log.error("规格聚合出现异常：", e);
            return null;
        }

    }



    /**
     * 从bucket中获取id查询对应品牌名称
     * @param terms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brands = this.brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 从bucket中获取id查询对应分类名称
     * @param terms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categories = this.categoryClient.queryBrandByCids(ids);
            return categories;
        } catch (Exception e) {
            return null;
        }

    }


    public void createIndex(Long id) throws IOException {

        Spu spu = this.goodsClient.querySpuById(id);
        // 构建商品
        Goods goods = this.buildGoods(spu);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }

}
