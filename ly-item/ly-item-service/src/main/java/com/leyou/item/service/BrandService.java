package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * date:2020-06-10
 * author:zhangxiaoshuai
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;


    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 开始分页
        if (rows!=-1){
            PageHelper.startPage(page, rows);
        }

        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        // 解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);

        PageResult<Brand> result = new PageResult<>(info.getTotal(), info.getList());
//        System.out.println(result);
        // 返回结果
        return result;
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌，id采用自增,如果1就成功
        brand.setId(null);
        int count = this.brandMapper.insert(brand);
        if (count != 1) {
            //新增失败，抛错误信息
            throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
        }
        //新增分类品牌中间表，多对多关系
        for (Long cid :
                cids) {
            int i = this.brandMapper.saveCategoryBrand(cid, brand.getId());
            if (i != 1) {
                throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        }


    }

    //查询品牌信息

    public Brand queryBrandByBid(Long id) {

        Brand brand = this.brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        List<Category> categoryList = this.brandMapper.selectCategoryByBid(id);
        brand.setCategories(categoryList);
        return brand;
    }

    public void updateBrandById(Brand brand, List<Long> cids) {
        Brand b = new Brand();
        int count = this.brandMapper.updateByPrimaryKey(brand);
        System.out.println(count);
        //修改分类品牌中间表，多对多关系，先删除后增添
        this.brandMapper.deleteCategoryBrand(brand.getId());
        for (Long cid :
                cids) {
            int i = this.brandMapper.saveCategoryBrand(cid, brand.getId());
            if (i != 1) {
                throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        }
    }

    public void deleteBrandByBid(Long bid) {
        Brand brand = new Brand();
        brand.setId(bid);
        int delete = this.brandMapper.delete(brand);
        if (delete!=1){
            throw new LyException(ExceptionEnums.BRAND_DELETE_ERROR);
        }
    }

    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand==null){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return brand;
    }


    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = this.brandMapper.queryByCategoryId(cid);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return list;
    }

    public List<Brand> queryByIds(List<Long> ids) {
        List<Brand> brands= this.brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return  brands;
    }
}
