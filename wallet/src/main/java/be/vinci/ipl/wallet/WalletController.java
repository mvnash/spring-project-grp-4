package be.vinci.ipl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@AllArgsConstructor
@RestController
public class WalletController {
    private WalletService service;

    @GetMapping("/wallet/{username}/net-worth")
    public ResponseEntity<Double> getNetWorth(@PathVariable String username){

        Double netWorth = service.getNetWorth(username);
        // Investor not found
        if(netWorth == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(netWorth, HttpStatus.OK);
    }

    @GetMapping("/wallet/{username}")
    public ResponseEntity<Set<PositionValue>> getOpenPositions(@PathVariable String username){
        Set<PositionValue> positions = service.getOpenPositions(username);

        // Investor not found
        if(positions == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PostMapping("/wallet/{username}")
    public ResponseEntity<Set<Wallet>> addPosition(@PathVariable String username, @RequestBody Set<Position> newPositions){
        Set<Wallet> actualWallet = service.addPositions(username, newPositions);
        // Investor not found
        if(actualWallet == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(actualWallet, HttpStatus.OK);
    }
}
