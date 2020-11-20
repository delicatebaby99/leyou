package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * date:2020-05-04
 * author:zhangxiaoshuai
 */
@Getter
public class LyException extends RuntimeException {
    private ExceptionEnums exceptionEnums;
    public LyException(ExceptionEnums exceptionEnum) {
        this.exceptionEnums = exceptionEnum;
    }

}
