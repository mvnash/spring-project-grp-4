package be.vinci.ipl.wallet;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PositionValue {

  private String symbol;
  private Integer quantity;
  private double price;

}
