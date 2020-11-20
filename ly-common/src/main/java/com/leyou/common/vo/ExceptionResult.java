package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * date:2020-05-04
 * author:zhangxiaoshuai
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;
    private String currentTime;

    public ExceptionResult(ExceptionEnums em){
        this.status=em.getCode();
        this.message=em.getMsg();
        this.timestamp=new Date().getTime();
        this.currentTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

    }
}
