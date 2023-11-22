package be.vinci.ipl.order.models.repositories;

import be.vinci.ipl.order.models.Order;
import be.vinci.ipl.order.models.OrderSide;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing orders in the system.
 */
@EnableFeignClients
@Repository
public interface OrderRepository extends CrudRepository<Order, String> {

  /**
   * Retrieves all orders placed by a specific owner.
   *
   * @param owner The username of the owner.
   * @return List of orders placed by the specified owner.
   */
  List<Order> findByOwner(String owner);

  /**
   * Retrieves all open orders for a specific ticker and side.
   *
   * @param ticker The ticker symbol of the instrument.
   * @param side   The side of the order (BUY or SELL).
   * @return List of open orders for the specified ticker and side.
   */
  List<Order> findOpenOrdersByTickerAndSide(String ticker, OrderSide side);

}