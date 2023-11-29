package be.vinci.ipl.execution.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // TODO ou no args contructor ??? Id et tt ????
public class Transaction {

    private String ticker;

    private String seller;
    
    private String buyer;

    private String buy_order_guid;

    private String sell_order_guid;

    private Integer quantity;

    private Double price;
}
