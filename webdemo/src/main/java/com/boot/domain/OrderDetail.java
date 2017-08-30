package com.boot.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class OrderDetail implements Serializable {
    private Long id;
    private Long orderId;
    private Long goodsId;
    private Integer goodsCount;
    private BigDecimal goodsPrice;
}
