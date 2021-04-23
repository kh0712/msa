package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    @Override
    public OrderDto createOrder(OrderDto dto) {
        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQty()*dto.getUnitPrice());

        Order order = Order.of(dto);
        Order savedOrder = orderRepository.save(order);
        return OrderDto.of(savedOrder);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(OrderDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);

        return OrderDto.of(order);
    }
}
