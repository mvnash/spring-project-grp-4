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
        private String ticker;

        public WalletId() {
            this.investorUsername = null;
            this.ticker = null;
        }
        public WalletId(final String uname, final String ticker) {
            this.investorUsername = uname;
            this.ticker = ticker;
        }
    }

    @EmbeddedId
    private WalletId id;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer quantity;


    public Wallet(final String user, final String ticker, final int qty) {
        this.id = new WalletId(user, ticker);
        this.quantity = qty;
    }

    public String getUserName() {
        return this.id.investorUsername;
    }
    public String getTicker() {
        return this.id.ticker;
    }
}
