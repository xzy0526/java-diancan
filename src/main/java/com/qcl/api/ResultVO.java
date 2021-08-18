package com.qcl.api;

import lombok.Data;
/**
    * @author xzy
    * http请求返回的最外层对象
 * 将数据组装回去
*/
@Data
public class ResultVO<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
