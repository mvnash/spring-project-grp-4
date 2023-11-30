package be.vinci.ipl.matching.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a transaction to be executed on the VSX platform.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
  /**
   * Alphanumeric code uniquely identifying a financial instrument.
   */
  private String ticker;

  /**
   * Username of the investor who is selling.
   */
  private String seller;

  /**
   * Username of the investor who is buying.
   */
  private String buyer;

  /**
   * GUID of the sell order used to perform this transaction.
   */
  private String sellOrderGuid;

  /**
   * GUID of the buy order used to perform this transaction.
   */
  private String buyOrderGuid;

  /**
   * Quantity of the traded financial instrument.
   */
  private int quantity;

  /**
   * Price at which the transaction is determined.
   */
  private double price;
}
