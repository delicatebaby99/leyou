package com.leyou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * date:2020-10-25
 * author:zhangxiaoshuai
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  OrderStatusEnum {
    UN_PAY(1,"未付款、未发货"),
    PAYED(2,"已付款、未发货"),
    UN_SURE(3,"已发货，未确认"),
    UN_EVALUATE(4,"已确认，未评价"),
    ORDER_CLOSE(5,"关闭"),
    EVALUATE(6,"已评价");



    private int code;
    private String msg;

}
