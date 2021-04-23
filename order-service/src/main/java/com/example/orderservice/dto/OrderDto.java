package com.example.orderservice.dto;


import com.example.orderservice.domain.Order;
import com.example.orderservice.vo.RequestOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {


    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice; //Service 에서  qty * unitPrice 로 totalPrice 계산

    private String userId;  // PathVariable 로 받는다.
    private String orderId; // Service 에서 UUID 가 세팅된다.

    private LocalDateTime createAt;
    /**
     * 영속객체에서 Dto 변환 시 사용하는 of
     * Order Entity -> OrderDto
     */
    public static OrderDto of(Order order){
        return OrderDto.builder()
                .productId(order.getProductId())
                .qty(order.getQty())
                .userId(order.getUserId())
                .orderId(order.getOrderId())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .createAt(order.getCreatedDate())
                .build();
    }


    /**
     * Request를 Order로 바꾸는데 사용하는 of
     *
     */
    public static OrderDto of(RequestOrder requestOrder){
        return OrderDto.builder()
                .productId(requestOrder.getProductId())
                .qty(requestOrder.getQty())
                .unitPrice(requestOrder.getUnitPrice())
                .build();
    }

    @Builder
    private OrderDto(String productId, Integer qty, Integer unitPrice, Integer totalPrice, String userId, String orderId, LocalDateTime createAt) {
        this.productId = productId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderId = orderId;
        this.createAt = createAt;
    }
}
