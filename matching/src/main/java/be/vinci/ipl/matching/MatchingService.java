package be.vinci.ipl.matching;

import be.vinci.ipl.matching.models.Order;
import be.vinci.ipl.matching.models.OrderSide;
import be.vinci.ipl.matching.models.OrderType;
import be.vinci.ipl.matching.models.Transaction;
import be.vinci.ipl.matching.proxys.ExecutionProxy;
import be.vinci.ipl.matching.proxys.OrderProxy;
import be.vinci.ipl.matching.proxys.PriceProxy;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

/**
 * Service class for handling matching operations.
 */
@Service
public class MatchingService {
  private final ExecutionProxy executionProxy;
  private final OrderProxy orderProxy;
  private final PriceProxy priceProxy;

  /**
   * Constructs a MatchingService with the specified proxies.
   *
   * @param orderProxy      The proxy for accessing order-related operations.
   * @param executionProxy  The proxy for accessing execution-related operations.
   * @param priceProxy      The proxy for accessing price-related operations.
   */
  public MatchingService(OrderProxy orderProxy, ExecutionProxy executionProxy, PriceProxy priceProxy) {
    this.orderProxy = orderProxy;
    this.executionProxy = executionProxy;
    this.priceProxy = priceProxy;
  }

  /**
   * Attempts to find matches between buy and sell orders for a specified financial instrument.
   * Retrieves orders for matching from the order service.
   * When matches are found, contacts the execution service for each transaction to be executed.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @return true if the matching process was successful, false otherwise.
   */
  public boolean matchOrdersByTicker(String ticker) {
    // Retrieve buy and sell orders for the specified ticker
    List<Order> buyOrders = StreamSupport.stream(orderProxy.readOrdersByTickerAndSide(ticker,
            String.valueOf(OrderSide.BUY)).spliterator(), false)
        .sorted(Comparator.comparingLong(Order::getTimestamp))
        .toList();

    List<Order> sellOrders = StreamSupport.stream(orderProxy.readOrdersByTickerAndSide(ticker,
            String.valueOf(OrderSide.SELL)).spliterator(), false)
        .sorted(Comparator.comparingLong(Order::getTimestamp))
        .toList();

    // Implement the matching algorithm
    for (Order buyOrder : buyOrders) {
      for (Order sellOrder : sellOrders) {
        if (isMatch(buyOrder, sellOrder)){
          int quantityToMatch = calculateQuantityToMatch(buyOrder, sellOrder);

          if (quantityToMatch > 0) {
            String seller = sellOrder.getOwner();
            String buyer = buyOrder.getOwner();
            String sellOrderGuid = sellOrder.getGuid();
            String buyOrderGuid = buyOrder.getGuid();
            double price = determineTransactionPrice(buyOrder, sellOrder);

            // Create a transaction
            Transaction transaction = new Transaction(ticker, seller, buyer, sellOrderGuid,
                buyOrderGuid, quantityToMatch, price);

            // Execute the matching orders
            executionProxy.executeMatchingOrders(ticker, buyer, seller, transaction);
          }
        }
      }
    }

    return true;
  }

  /**
   * Helper method to check if orders can be matched.
   *
   * @param buyOrder  The buy order.
   * @param sellOrder The sell order.
   * @return true if the orders can be matched, false otherwise.
   */
  private boolean isMatch(Order buyOrder, Order sellOrder) {
    if (buyOrder.getType() == OrderType.LIMIT && sellOrder.getType() == OrderType.LIMIT) {
      // Both are LIMIT orders
      return buyOrder.getLimit() >= sellOrder.getLimit();
    } else {
      // At least one is a MARKET order
      return true;
    }
  }

  /**
   * Calculates the quantity to match between two orders.
   *
   * @param buyOrder  The buy order.
   * @param sellOrder The sell order.
   * @return The quantity to match.
   */
  private int calculateQuantityToMatch(Order buyOrder, Order sellOrder) {
    return Math.min(buyOrder.getQuantity() - buyOrder.getFilled(),
        sellOrder.getQuantity() - sellOrder.getFilled());
  }

  /**
   * Determines the transaction price based on the type of sell order.
   *
   * @param buyOrder  The buy order.
   * @param sellOrder The sell order.
   * @return The transaction price.
   */
  private double determineTransactionPrice(Order buyOrder, Order sellOrder) {
    String ticker = buyOrder.getTicker();
    return (sellOrder.getType() == OrderType.LIMIT) ? sellOrder.getLimit()
        : priceProxy.getLastExecutedPrice(ticker);
  }
}
