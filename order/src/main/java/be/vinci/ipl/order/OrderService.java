package be.vinci.ipl.order;

import be.vinci.ipl.order.models.Order;
import be.vinci.ipl.order.models.OrderSide;
import be.vinci.ipl.order.models.OrderUpdateRequest;
import be.vinci.ipl.order.repositories.MatchingProxy;
import be.vinci.ipl.order.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing orders in the system.
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final MatchingProxy matchingProxy;

  /**
   * Constructs an OrderService with the provided OrderRepository.
   *
   * @param orderRepository The repository for orders.
   */
  public OrderService(OrderRepository orderRepository, MatchingProxy matchingProxy) {
    this.orderRepository = orderRepository;
    this.matchingProxy = matchingProxy;
  }

  /**
   * Places an order in the order repository and triggers the matching process.
   *
   * @param order The order to be placed.
   * @return The placed order if successful, null if an order with the same GUID already exists.
   */
  public Order placeOrder(Order order) {
    try {
      // Save the order in the repository
      orderRepository.save(order);

      // Trigger the matching process for the order's ticker
      matchingProxy.triggerMatching(order.getTicker());

      // Return the placed order
      return order;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
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
   * @return True if the order was successfully updated, false if the order couldn't be found or the update request is invalid.
   */
  public boolean updateOrder(String guid, OrderUpdateRequest updateRequest) {
    Order existingOrder = orderRepository.findById(guid).orElse(null);

    if (existingOrder == null || !isValidUpdate(existingOrder, updateRequest)) {
      return false; // Order not found or update request is invalid
    }

    // Update the filled quantity
    existingOrder.setFilled(updateRequest.getFilled());

    orderRepository.save(existingOrder);
    return true; // Successfully updated
  }

  /**
   * Validates the update request for an order.
   * The specifications don't ask to validate it
   * but I chose to do it anyway to avoid any inconsistencies.
   *
   * @param existingOrder The existing order.
   * @param updateRequest The update request.
   * @return True if the update request is valid, false otherwise.
   */
  private boolean isValidUpdate(Order existingOrder, OrderUpdateRequest updateRequest) {
    int oldFilled = existingOrder.getFilled();
    int newFilled = updateRequest.getFilled();
    int quantity = existingOrder.getQuantity();

    return newFilled > oldFilled && newFilled <= quantity && !updateRequest.invalid();
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
