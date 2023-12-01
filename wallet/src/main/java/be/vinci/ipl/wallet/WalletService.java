package be.vinci.ipl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class WalletService {
    private WalletRepository repository;
    private PriceProxy priceProxy;
    private InvestorProxy investorProxy;

    //@GetMapping("/wallet/{username}/net-worth")
    //public ResponseEntity<Double> getNetWorth(){
    public Double getNetWorth(String username){
        // Check if the investor exists
        Investor investor = investorProxy.getInvestor(username);
        if(investor == null)
            return null;

        // Get a set of wallet with all the positions of the user
        Set<Wallet> positions =  repository.getAllByInvestorUsername(username);

        double netWorth = 0.0;
        for ( Wallet position : positions ){
            Double price = priceProxy.getPriceForTicker(position.getTicker()).getValueT();
            netWorth += position.getQuantity() * price;
        }
        return netWorth;
    }

    //@GetMapping("/wallet/{username}")
    //public ResponseEntity<PositionValue> getOpenPositions(){
    public Set<PositionValue> getOpenPositions(String username){
        // Check if the investor exists
        Investor investor = investorProxy.getInvestor(username);
        if(investor == null)
            return null;

        Set<Wallet> wallets =  repository.getAllByInvestorUsername(username);
        if(wallets.isEmpty())
            return null;
        Set<PositionValue> positions = new HashSet<>();
        for ( Wallet wallet : wallets ){
            PositionValue pv = new PositionValue();
            pv.setSymbol(wallet.getTicker());
            pv.setQuantity(wallet.getQuantity());
            pv.setSymbol(wallet.getTicker());
            positions.add(pv);
        }
        return positions;
    }

    public Set<Wallet> addPositions(String username, Set<Position> newPositions){
        Investor investor = investorProxy.getInvestor(username);
        if(investor == null)
            return null;

        Set<Wallet> positions =  repository.getAllByInvestorUsername(username);

        for(Position position : newPositions){
            for ( Wallet wallet : positions ){
                // Already possessed
                if(position.getSymbol().equals(wallet.getTicker())){
                    // yes, update its quantity
                    position.setQuantity(position.getQuantity()+ wallet.getQuantity());
                    break;
                }
            }
            // Add this new position.
            Wallet wallet = new Wallet(username, position.getSymbol(), position.getQuantity());
            repository.save(wallet);
        }
        return positions;
    }

}
