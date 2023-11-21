package be.vinci.ipl.order.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request to update an existing order, specifically to modify the filled quantity.
 */
@Getter
@Setter
public class OrderUpdateRequest {

  /**
   * The new filled quantity to be updated for the order.
   */
  private int filled;
}
