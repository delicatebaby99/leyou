package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * date:2020-05-06
 * author:zhangxiaoshuai
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)) {

            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }

        return list;
    }




    //根据传入的id集合查询所有分类
    public List<String> queryByCategoryNamesIds(List<Long> ids){

        List<Category> list = categoryMapper.selectByIdList(ids);
        List<String> names=new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {

            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        list.forEach(category -> names.add(category.getName()));
        return names;
    }

    //根据三级分类查询全部
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }

    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {

            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        return list;
    }
}
