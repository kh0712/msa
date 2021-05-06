package com.example.orderservice.controller;


import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getOrders(@PathVariable String userId){
        log.info("Before Get Order Data");
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = orders.stream()
                                        .map(ResponseOrder::of)
                                        .collect(Collectors.toList());
        log.info("After Get Order Data");
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable String userId,
                                         @Valid @RequestBody RequestOrder dto){
        log.info("Before Create Order Data");
        OrderDto orderDto = OrderDto.of(dto);
        orderDto.setUserId(userId);
        
        OrderDto responseOrderDto = orderService.createOrder(orderDto);
        ResponseOrder responseDto = ResponseOrder.of(responseOrderDto);

        //kafkaProducer.send("order-topic", responseOrderDto);
        log.info("After Create Order Data");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/health-check")
    public String status(){
        return "It is working in Order service PORT: " + env.getProperty("local.server.port");
    }

}
