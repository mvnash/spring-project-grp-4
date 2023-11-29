package be.vinci.ipl.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "price")
public class Price {
    @Id
    private String ticker; // nom de l'action
    private Double value;
}
