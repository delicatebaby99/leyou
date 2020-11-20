package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * date:2020-05-04
 * author:zhangxiaoshuai
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    NAME_CANNOT_BE_NULL(400, "姓名不能为空"),
    CATEGORY_NOT_FOND(404,"商品分类没有查询到"),
    BRAND_NOT_FOUND (404,"品牌没有查询到"),
    UPLOAD_FILE_ERROR (500,"文件上传失败"),
    SPEC_GROUP_NOT_FOUND (500,"产品规格组没有找到"),
    ADD_SPEC_GROUP_ERROR(500,"新增规格组错误"),
    UPDATE_SPEC_GROUP_ERROR(500,"修改规格组错误"),
    DELETE_SPEC_GROUP_ERROR(500,"删除规格组错误"),
    SPEC_PARAM_NOT_FOUND (500,"产品规格参数没有找到"),
    ADD_SPEC_PARAM_ERROR(500,"新增规格参数错误"),
    ADD_GOODS_ERROR(500,"新增商品错误"),
    GOODS_DETAIL_NOT_FOUND(500,"查询商品详情错误"),
    GOODS_SKU_NOT_FOUND(500,"查询商品没找到"),
    GOODS_STOCK_FOUND(500,"查询库存没找到"),
    INVALID_FILE_TYPE (400,"无效的文件类型"),
    BRAND_DELETE_ERROR(500,"删除品牌失败"),
    BRAND_SAVE_ERROR (500,"新增品牌失败"),
    SPU_NOT_FOUND(500,"SPU没有找到"),
    GOODS_UPDATE_ERROR(500,"商品更新失败"),
    GOODS_ID_CANNOT_BE_NULL(500,"商品ID不能为空"),
    UPDATE_SPUDETAIL_ERROR(500,"商品详情更新失败"),
    ORDER_STATUS_ERROR(400,"订单状态错误"),
    WX_PAY_ORDER_FAIL(500,"微信下单通知失败"),
    INVALID_SIGN_ERROR(500,"无效签名错误"),
    INVALID_ORDER_PARAM(500,"订单参数错误"),
    UPDATE_ORDER_STATUS_ERROR(500,"修改订单状态错误"),
    INVALID_USER_TYPE (400,"无效用户类型")
    ;


    /**
     * code 状态码
     */
    private int code;
    /**
     * msg 返回信息
     */
    private String msg;

}
