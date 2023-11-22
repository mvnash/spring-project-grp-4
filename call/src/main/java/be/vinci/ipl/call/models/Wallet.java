package be.vinci.ipl.call.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Wallet {
    private String symbol;
    private Integer quantity;
    private Integer unitvalue;
}