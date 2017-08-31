package com.boot.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Order implements Serializable{
    // 订单id
    private Long id;
    // 卖家ID
    private Long sellerId;
    // 买家id
    private Long buyerId;
    private BigDecimal totalAmount;
    private Integer status;
    private Date createTime;




}
