package com.boot.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class Order implements Serializable{
    // 订单id
    private String id;
    // 卖家ID
    private Integer sellerId;
    private Integer buyerId;
    private String goodsId;
    private String totalAmount;
    private Integer status;
    private Date createTime;



}
