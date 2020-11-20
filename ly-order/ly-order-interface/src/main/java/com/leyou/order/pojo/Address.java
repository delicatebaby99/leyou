package com.leyou.order.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * date:2020-10-22
 * author:zhangxiaoshuai
 */
@Data
@Table(name = "tb_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人
     */
    private String name;

    /**
     * 收货电话
     */
    private String phone;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 省
     */
    private String state;

    /**
     * 市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 是否是默认地址
     */
    private Boolean defaultAddress;

    /**
     * 地址标签
     */
    @Transient
    private String label;


}
