package be.vinci.ipl.matching.proxys;

import be.vinci.ipl.matching.models.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Proxy interface for executing matching orders.
 */
@Repository
@FeignClient(name = "execution")
public interface ExecutionProxy {

  /**
   * Executes matching orders for a specified ticker, seller, and buyer.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @param seller The seller's identifier.
   * @param buyer  The buyer's identifier.
   * @param transaction The transaction details.
   */
  @PostMapping("/execute/{ticker}/{seller}/{buyer}")
  void executeMatchingOrders(@PathVariable String ticker, @PathVariable String seller,
      @PathVariable String buyer, @RequestBody Transaction transaction);

}
