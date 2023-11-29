package be.vinci.ipl.price.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Price {

    @Id
    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Double value;
}