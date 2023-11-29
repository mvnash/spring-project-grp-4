package be.vinci.ipl.execution.models;

import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor // TODO ou no args contructor ??? Id et tt ????
public class Position {

    private String ticker;

    private Integer quantity;

    private Double price;
}
