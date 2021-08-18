package com.qcl.response;

import lombok.Data;

/**
 * 微信小程序购物车
 */
@Data
public class WxCardResponse {

    /**
     * 商品Id.
     */
    private int productId;

    /**
     * 数量.
     */
    private Integer productQuantity;

    public WxCardResponse(int productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
