package com.example.orderservice.domain;

import com.example.orderservice.dto.OrderDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String productId;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false, columnDefinition = "char(128)")
    private String userId;

    @Column(nullable = false, unique = true,columnDefinition = "char(128)")
    private String orderId;

    public static Order of (OrderDto dto){
        return Order.builder()
                .productId(dto.getProductId())
                .qty(dto.getQty())
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(dto.getTotalPrice())
                .build();

    }

    @Builder
    public Order(String productId, Integer qty, Integer unitPrice, Integer totalPrice, String userId, String orderId) {
        this.productId = productId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderId = orderId;
    }
}