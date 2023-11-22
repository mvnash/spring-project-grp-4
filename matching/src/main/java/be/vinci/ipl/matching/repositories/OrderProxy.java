package be.vinci.ipl.matching.repositories;

import be.vinci.ipl.matching.models.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy interface for accessing order-related operations.
 */
@Repository
@FeignClient(name = "order")
public interface OrderProxy {

  /**
   * Retrieves open orders for a specified ticker and side.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @param side   The side of the order (BUY or SELL).
   * @return An iterable of open orders for the specified ticker and side.
   */
  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  Iterable<Order> readOrdersByTickerAndSide(@PathVariable String ticker, @PathVariable String side);

}
