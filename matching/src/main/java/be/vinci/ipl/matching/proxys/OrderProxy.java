package be.vinci.ipl.matching.proxys;

import be.vinci.ipl.matching.models.Order;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "order")
public interface OrderProxy {

  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  Iterable<Order> readOrdersByTickerAndSide(@PathVariable String ticker, @PathVariable String side);

}