package models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order {
  private String guid;
  private String owner;
  private String timestamp;
  private String ticker;
  private int quantity;
  private double limit;
  private int filled;
  //TODO faut il mettre les 2 autres champs et donc creer des classes ?



}
