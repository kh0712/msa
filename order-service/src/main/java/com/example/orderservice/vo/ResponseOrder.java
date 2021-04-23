package com.example.orderservice.vo;


import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {

    private String          productId;
    private Integer         qty;
    private Integer         unitPrice;
    private Integer         totalPrice;
    private LocalDateTime   createdAt;
    private String          orderId;
    private String          userId;

    public static ResponseOrder of(OrderDto dto){
        return ResponseOrder.builder()
                .productId(dto.getProductId())
                .qty(dto.getQty())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(dto.getTotalPrice())
                .createdAt(dto.getCreateAt())
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .build();
    }

    @Builder
    public ResponseOrder(String productId, Integer qty, Integer unitPrice, Integer totalPrice, LocalDateTime createdAt, String orderId, String userId) {
        this.productId = productId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.orderId = orderId;
        this.userId = userId;
    }
}
