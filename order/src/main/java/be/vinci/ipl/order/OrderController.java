package be.vinci.ipl.order;

import be.vinci.ipl.order.models.Order;
import be.vinci.ipl.order.models.OrderUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling HTTP requests related to orders on the platform.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  /**
   * Constructs a new instance of the OrderController.
   *
   * @param orderService The service handling order-related operations.
   */
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Places a new order on the platform.
   *
   * @param order The order to be placed.
   * @return ResponseEntity containing the created order and HTTP status.
   */
  @PostMapping
  public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
    if (order.invalid()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Order createdOrder = orderService.placeOrder(order);

    if (createdOrder == null) {
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return new ResponseEntity<>(createdOrder, HttpStatus.OK);
  }

  /**
   * Retrieves details of a specific order by its unique identifier.
   *
   * @param guid The unique identifier of the order.
   * @return ResponseEntity containing the order details and HTTP status.
   */
  @GetMapping("/{guid}")
  public ResponseEntity<Order> getOrderDetails(@PathVariable String guid) {
    Order order = orderService.getOrderDetails(guid);

    if (order == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  /**
   * Updates the quantity filled for a specific order.
   *
   * @param guid          The unique identifier of the order to be updated.
   * @param updateRequest The request containing the updated quantity filled.
   * @return ResponseEntity with HTTP status indicating the success of the update.
   */
  @PatchMapping("/{guid}")
  public ResponseEntity<Void> updateOrder(@PathVariable String guid,
      @RequestBody OrderUpdateRequest updateRequest) {

    boolean updated = orderService.updateOrder(guid, updateRequest);

    if (!updated) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Retrieves all orders placed by a specific user.
   *
   * @param username The username of the user.
   * @return ResponseEntity containing the list of user's orders and HTTP status.
   */
  @GetMapping("/by-user/{username}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String username) {
    List<Order> userOrders = orderService.getOrdersByUser(username);

    if (userOrders.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(userOrders, HttpStatus.OK);
  }

  /**
   * Retrieves all open orders for a specific ticker and side.
   *
   * @param ticker The ticker symbol of the instrument.
   * @param side   The side of the order (BUY or SELL).
   * @return ResponseEntity containing the list of open orders and HTTP status.
   */
  @GetMapping("/open/by-ticker/{ticker}/{side}")
  public ResponseEntity<List<Order>> getOpenOrdersByTickerAndSide(@PathVariable String ticker,
      @PathVariable String side) {
    List<Order> openOrders = orderService.getOpenOrdersByTickerAndSide(ticker, side);

    return new ResponseEntity<>(openOrders, HttpStatus.OK);
  }
}