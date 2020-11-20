package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * date:2020-06-10
 * author:zhangxiaoshuai
 */
public interface BrandMapper extends BaseMapper<Brand> {

    //新增品牌分类表

    @Insert("INSERT INTO tb_category_brand(category_id,brand_id) VALUES(#{cid},#{bid})")
    int saveCategoryBrand(@Param("cid")Long cid,@Param("bid")Long bid);

    @Delete("DELETE  FROM `tb_category_brand` WHERE `brand_id`=#{bid}")
    int deleteCategoryBrand(@Param("bid")Long bid);

    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> selectCategoryByBid(@Param("bid")Long bid);

    @Select("SELECT b.* FROM tb_brand b LEFT JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(Long cid);

}
