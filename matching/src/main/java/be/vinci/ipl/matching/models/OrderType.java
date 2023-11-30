package be.vinci.ipl.matching.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enumeration representing the types of orders that are allowed within the VSX platform.
 * Currently, it includes MARKET and LIMIT orders.
 */
@JsonFormat(shape = Shape.STRING)
public enum OrderType {
  /**
   * Represents a MARKET order, indicating an order to be executed immediately at the best available price.
   */
  MARKET,

  /**
   * Represents a LIMIT order, indicating an order to be executed at a specified price (or better).
   */
  LIMIT
}
