package com.leyou.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * date:2020-10-22
 * author:zhangxiaoshuai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusMessage {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    private String username;

    private Long spuId;

    /**
     * 消息类型：1(自动确认收货)  2（自动评论）
     */
    private int type;



}
