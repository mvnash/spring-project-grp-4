package be.vinci.ipl.wallet;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private String symbol; // Symbole de l'action ou "CASH" pour le cash
    private Integer quantity; // Quantité possédée

}
