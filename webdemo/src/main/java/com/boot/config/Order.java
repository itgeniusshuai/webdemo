package com.boot.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Order implements Serializable{
    // 订单id
    private String id;
    private Integer sellerId;
    private Integer buyerId;
    private String productId;

}
