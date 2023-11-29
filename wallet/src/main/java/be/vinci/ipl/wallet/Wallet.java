package be.vinci.ipl.wallet;

import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="wallets")
public class Wallet {

    @Embeddable
    public static final class WalletId implements Serializable {
        private String investorUsername;
        private String symbol; // Symbole de l'action ou "CASH" pour le cash

        public WalletId() {
            this.investorUsername = null;
            this.symbol = null;
        }
        public WalletId(final String uname, final String ticker) {
            this.investorUsername = uname;
            this.symbol = ticker;
        }
    }

    @EmbeddedId
    private WalletId id;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer quantity; // Quantité possédée


    public Wallet(final String user, final String ticker, final int qty) {
        this.id = new WalletId(user, ticker);
        this.quantity = qty;
    }

    public String getUserName() {
        return this.id.investorUsername;
    }
    public String getSymbol() {
        return this.id.symbol;
    }
}
