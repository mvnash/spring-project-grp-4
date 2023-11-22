package be.vinci.ipl.matching.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an order placed by an investor on the VSX platform.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
  /**
   * Unique identifier for the order.
   */
  @Id
  @Column()
  private String guid;

  /**
   * The username of the investor who placed the order.
   */
  @Column(nullable = false)
  private String owner;

  /**
   * The timestamp indicating when the order was placed.
   */
  @Column(nullable = false)
  private long timestamp;

  /**
   * The ticker representing a unique alphanumeric code for the financial instrument.
   */
  @Column(nullable = false)
  private String ticker;

  /**
   * The quantity of the financial instrument to be traded in the order.
   */
  @Column(nullable = false)
  private int quantity;

  /**
   * The side of the transaction, either BUY or SELL.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderSide side;

  /**
   * The type of the order, either MARKET or LIMIT.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderType type;

  /**
   * The limit price for a LIMIT order. Only applicable if the order type is LIMIT.
   */
  @Column(name = "order_limit")
  private Double limit;

  /**
   * The quantity of the financial instrument that has already been traded.
   */
  @Column(nullable = false)
  private int filled;

  /**
   * Checks if the order is invalid based on certain criteria.
   * @return true if the order is invalid, false otherwise.
   */
  public boolean invalid() {
    return owner == null || owner.isBlank() ||
        timestamp <= 0 ||
        ticker == null || ticker.isBlank() ||
        quantity <= 0 ||
        side == null ||
        type == null || (type == OrderType.LIMIT && limit == null) ||
        filled < 0;
  }
}
