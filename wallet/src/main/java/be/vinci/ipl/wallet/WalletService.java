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

    //@GetMapping("/wallet/{username}/net-worth")
    //public ResponseEntity<Double> getNetWorth(){
    public double getNetWorth(String username){
        // Get a set of wallet with all the positions of the user
        Set<Wallet> wallets =  repository.getAllByInvestorUsername(username);
        if(wallets == null)
            return -1;

        double netWorth = 0.0;
        for ( Wallet wallet : wallets ){
            Double price = priceProxy.getPriceForTicker(wallet.getSymbol()).getValue();
            netWorth += wallet.getQuantity() * price;
        }
        return netWorth;
    }

    //@GetMapping("/wallet/{username}")
    //public ResponseEntity<PositionValue> getOpenPositions(){
    public Set<PositionValue> getOpenPositions(String username){
        Set<Wallet> wallets =  repository.getAllByInvestorUsername(username);
        if(wallets.isEmpty())
            return null;
        Set<PositionValue> positions = new HashSet<>();
        for ( Wallet wallet : wallets ){
            PositionValue pv = new PositionValue();
            pv.setSymbol(wallet.getSymbol());
            pv.setQuantity(wallet.getQuantity());
            pv.setSymbol(wallet.getSymbol());
            positions.add(pv);
        }
        return positions;
    }

    public Set<Wallet> addPositions(String username, Set<Position> newPositions){
        if(newPositions.isEmpty())
            return null;
        Set<Wallet> positions =  repository.getAllByInvestorUsername(username);

        for(Position position : newPositions){
            for ( Wallet wallet : positions ){
                // Already possessed
                if(position.getSymbol().equals(wallet.getSymbol())){
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
