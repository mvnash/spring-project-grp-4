package be.vinci.ipl.order;

import be.vinci.ipl.order.models.Order;
import be.vinci.ipl.order.models.OrderSide;
import be.vinci.ipl.order.models.OrderUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing orders in the system.
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;

  /**
   * Constructs an OrderService with the provided OrderRepository.
   *
   * @param orderRepository The repository for orders.
   */
  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  /**
   * Places a new order in the system.
   *
   * @param order The order to be placed.
   * @return The placed order, or null if an order with the same guid already exists.
   */
  public Order placeOrder(Order order) {
    if (orderRepository.existsById(order.getGuid())) {
      return null; // Order with the same guid already exists
    }

    orderRepository.save(order);
    return order;
  }

  /**
   * Retrieves the details of a specific order by its guid.
   *
   * @param guid The guid of the order.
   * @return The order details, or null if the order couldn't be found.
   */
  public Order getOrderDetails(String guid) {
    return orderRepository.findById(guid).orElse(null);
  }

  /**
   * Updates the filled quantity of an existing order.
   *
   * @param guid          The guid of the order to be updated.
   * @param updateRequest The request containing the new filled quantity.
   * @return True if the order was successfully updated, false if the order couldn't be found.
   */
  public boolean updateOrder(String guid, OrderUpdateRequest updateRequest) {
    Order existingOrder = orderRepository.findById(guid).orElse(null);

    if (existingOrder == null) {
      return false; // Order not found
    }

    // Update the filled quantity
    existingOrder.setFilled(updateRequest.getFilled());

    orderRepository.save(existingOrder);
    return true;
  }

  /**
   * Retrieves all orders placed by a specific user.
   *
   * @param username The username of the user.
   * @return List of orders placed by the specified user.
   */
  public List<Order> getOrdersByUser(String username) {
    return orderRepository.findByOwner(username);
  }

  /**
   * Retrieves all open orders for a specific ticker and side.
   *
   * @param ticker The ticker symbol of the instrument.
   * @param side   The side of the order (BUY or SELL).
   * @return List of open orders for the specified ticker and side.
   */
  public List<Order> getOpenOrdersByTickerAndSide(String ticker, String side) {
    return orderRepository.findOpenOrdersByTickerAndSide(ticker, OrderSide.valueOf(side));
  }
}
