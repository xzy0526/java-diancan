package com.qcl.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import com.qcl.meiju.FoodStatusEnum;
import lombok.Data;
/**
 * @author xzy
 * *返回给小程序的菜品页
 */
@Data
public class FoodRes {
    @JsonProperty("id")
    private Integer foodId;

    @JsonProperty("name")
    private String foodName;

    @JsonProperty("price")
    private BigDecimal foodPrice;
    @JsonProperty("stock")
    private Integer foodStock;//库存

    @JsonProperty("desc")
    private String foodDesc;

    @JsonProperty("icon")
    private String foodIcon;
}
