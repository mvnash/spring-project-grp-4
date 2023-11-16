package be.vinci.ipl.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  @Column(nullable = true)
  private String guid;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private long timestamp;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int quantity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderSide side;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderType type;

  @Column
  private Double limit;

  @Column(nullable = false)
  private int filled;

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
