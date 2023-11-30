package be.vinci.ipl.order.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enumeration representing the side of an order, indicating whether it is a BUY or SELL order.
 */
@JsonFormat(shape = Shape.STRING)
public enum OrderSide {
  /**
   * Represents a BUY order, indicating an intention to purchase an instrument.
   */
  BUY,

  /**
   * Represents a SELL order, indicating an intention to sell an instrument.
   */
  SELL
}