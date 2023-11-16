package be.vinci.ipl.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
  @Id
  @Column()
  private String guid;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private long timestamp;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private String side;

  @Column(nullable = false)
  private String type;

  @Column
  private Double limit;

  @Column(nullable = false)
  private int filled;

  public boolean invalid() {
    return owner == null || owner.isBlank() ||
        timestamp <= 0 ||
        ticker == null || ticker.isBlank() ||
        quantity <= 0 ||
        side == null || (!side.equals("BUY") && !side.equals("SELL")) ||
        type == null || (!type.equals("MARKET") && !type.equals("LIMIT")) ||
        (type.equals("LIMIT") && limit == null) ||
        filled < 0;
  }
}
