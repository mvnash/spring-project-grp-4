package be.vinci.ipl.gateway;

import java.util.List;
import models.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
@FeignClient(name="orders")
public interface OrdersProxy {

  @PostMapping("/orders")
  Order placeOrder(@PathVariable Order order);

  @GetMapping("/orders/by-user/{username}")
  List<Order>getOrdersByUsername(@PathVariable String username);


}
