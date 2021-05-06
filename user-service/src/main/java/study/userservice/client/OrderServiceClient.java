package study.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import study.userservice.controller.UserController;

import java.util.List;

@FeignClient(name = "order-service") // Micro Service 의 이름이다.
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<UserController.ResponseOrder> getOrders(@PathVariable String userId);
}
