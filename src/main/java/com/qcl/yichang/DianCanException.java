package com.qcl.yichang;

import com.qcl.meiju.ResultEnum;

/**
 * 编程小石头：2501902696（微信）
 */
public class DianCanException extends RuntimeException{

    private Integer code;

    public DianCanException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public DianCanException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
