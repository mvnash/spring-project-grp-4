package be.vinci.ipl.matching.proxys;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy interface for accessing price-related operations.
 */
@Repository
@FeignClient(name = "price")
public interface PriceProxy {

  /**
   * Retrieves the last executed price for a specified ticker.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @return The last executed price.
   */
  @GetMapping("/price/{ticker}")
  double getLastExecutedPrice(@PathVariable String ticker);

}
