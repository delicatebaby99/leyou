package com.leyou.order.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.order.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * date:2020-10-24
 * author:zhangxiaoshuai
 */
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 分页查询订单
     * @param userId
     * @param status
     * @return
     */
    List<Order> queryOrderList(@Param("userId") Long userId, @Param("status") Integer status);
}
