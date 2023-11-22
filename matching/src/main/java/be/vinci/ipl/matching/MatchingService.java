package be.vinci.ipl.matching;

import be.vinci.ipl.matching.models.Order;
import be.vinci.ipl.matching.models.OrderSide;
import be.vinci.ipl.matching.models.OrderType;
import be.vinci.ipl.matching.models.Transaction;
import be.vinci.ipl.matching.repositories.ExecutionProxy;
import be.vinci.ipl.matching.repositories.OrderProxy;
import be.vinci.ipl.matching.repositories.PriceProxy;

import java.util.ArrayList;
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
   * Retrieves orders for matching from the order service, sorts them by timestamp, and generates a list of transactions.
   * Finally, executes the transactions by invoking the execution service.
   *
   * @param ticker The ticker symbol for the financial instrument.
   * @return true if the matching process was successful, false otherwise.
   */
  public boolean matchOrdersByTicker(String ticker) {
    try {
      // Retrieve buy and sell orders for the specified ticker
      List<Order> buyOrders = StreamSupport.stream(orderProxy.readOrdersByTickerAndSide(ticker,
                      String.valueOf(OrderSide.BUY)).spliterator(), false)
              .sorted(Comparator.comparingLong(Order::getTimestamp))
              .toList();

      List<Order> sellOrders = StreamSupport.stream(orderProxy.readOrdersByTickerAndSide(ticker,
                      String.valueOf(OrderSide.SELL)).spliterator(), false)
              .sorted(Comparator.comparingLong(Order::getTimestamp))
              .toList();

      // Generate a list of transactions based on matching orders
      ArrayList<Transaction> transactions = getTransactions(ticker, buyOrders, sellOrders);

      // Execute the transactions
      executeTransactions(transactions);

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Generates a list of transactions based on matching buy and sell orders for a specific ticker.
   * This method iterates through the provided buy and sell orders, identifies matching pairs,
   * calculates the quantity to match, creates a transaction, and updates the filled quantity in
   * the corresponding buy and sell orders.
   *
   * @param ticker      The ticker symbol for the financial instrument.
   * @param buyOrders   The list of buy orders for the specified ticker.
   * @param sellOrders  The list of sell orders for the specified ticker.
   * @return An ArrayList of Transaction objects representing matched transactions.
   */
  private ArrayList<Transaction> getTransactions(String ticker, List<Order> buyOrders, List<Order> sellOrders) {
    ArrayList<Transaction> transactions = new ArrayList<>();

    for (Order buyOrder : buyOrders) {
      for (Order sellOrder : sellOrders) {
        if (isMatch(buyOrder, sellOrder)) {
          int quantityToMatch = calculateQuantityToMatch(buyOrder, sellOrder);

          if (quantityToMatch > 0) {
            String seller = sellOrder.getOwner();
            String buyer = buyOrder.getOwner();
            String sellOrderGuid = sellOrder.getGuid();
            String buyOrderGuid = buyOrder.getGuid();
            double price = determineTransactionPrice(buyOrder, sellOrder);

            // Create a transaction
            Transaction transaction = new Transaction(ticker, seller, buyer, sellOrderGuid, buyOrderGuid,
                    quantityToMatch, price);
            transactions.add(transaction);

            // Update filled quantity in buy and sell orders
            updateFilledQuantity(buyOrder, quantityToMatch);
            updateFilledQuantity(sellOrder, quantityToMatch);
          }
        }
      }
    }

    return transactions;
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
    // Default price in case of unexpected conditions
    double price = 1.0;

    // Get the order types
    OrderType buyOrderType = buyOrder.getType();
    OrderType sellOrderType = sellOrder.getType();

    // Check the type of buy order
    if (buyOrderType.equals(OrderType.MARKET)) {
      // BUY: MARKET & SELL: MARKET
      if (sellOrderType.equals(OrderType.MARKET)) {
        String ticker = buyOrder.getTicker();
        // Retrieve the last executed price for the ticker
        double lastPrice = priceProxy.getLastExecutedPrice(ticker);
        if (lastPrice > 0.0) {
          price = lastPrice;
        }
      } else {
        // BUY: MARKET & SELL: LIMIT
        price = sellOrder.getLimit();
      }
    } else {
      // BUY: LIMIT & SELL: MARKET
      if (sellOrderType.equals(OrderType.MARKET)) {
        price = buyOrder.getLimit();
      } else {
        // BUY: LIMIT & SELL: LIMIT
        // Calculate the average of buy and sell order limits
        price = (buyOrder.getLimit() + sellOrder.getLimit()) / 2;
      }
    }

    return price;
  }

  /**
   * Updates the filled quantity in the given order based on the quantity matched in a transaction.
   *
   * @param order            The order to be updated.
   * @param quantityMatched The quantity matched in a transaction.
   */
  private void updateFilledQuantity(Order order, int quantityMatched) {
    int newFilledQuantity = order.getFilled() + quantityMatched;
    order.setFilled(newFilledQuantity);
  }

  /**
   * Executes a list of transactions by invoking the execution service for each transaction.
   *
   * @param transactions The list of transactions to be executed.
   */
  private void executeTransactions(List<Transaction> transactions) {
    for (Transaction transaction : transactions) {
      String ticker = transaction.getTicker();
      String buyer = transaction.getBuyer();
      String seller = transaction.getSeller();

      // Execute the transaction using the executionProxy
      executionProxy.executeTransaction(ticker, buyer, seller, transaction);
    }
  }
}