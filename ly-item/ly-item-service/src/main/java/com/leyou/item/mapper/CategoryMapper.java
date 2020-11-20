package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * date:2020-05-06
 * author:zhangxiaoshuai
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
}
