package be.vinci.ipl.matching.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Order {
  /**
   * Unique identifier for the order.
   */
  private String guid;

  /**
   * The username of the investor who placed the order.
   */
  private String owner;

  /**
   * The timestamp indicating when the order was placed.
   */
  private long timestamp;

  /**
   * The ticker representing a unique alphanumeric code for the financial instrument.
   */
  private String ticker;

  /**
   * The quantity of the financial instrument to be traded in the order.
   */
  private int quantity;

  /**
   * The side of the transaction, either BUY or SELL.
   */
  @Enumerated(EnumType.STRING)
  private OrderSide side;

  /**
   * The type of the order, either MARKET or LIMIT.
   */
  @Enumerated(EnumType.STRING)
  private OrderType type;

  /**
   * The limit price for a LIMIT order. Only applicable if the order type is LIMIT.
   */
  private Double limit;

  /**
   * The quantity of the financial instrument that has already been traded.
   */
  private int filled;
}
