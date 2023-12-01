package be.vinci.ipl.price.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Price {

    @Id
    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false, name = "value_ticker")
    private Double value;
}