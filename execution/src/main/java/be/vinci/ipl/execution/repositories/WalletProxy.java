package be.vinci.ipl.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import be.vinci.ipl.execution.models.Position;

@Repository
@FeignClient(name = "wallet") // TODO verifier upper/lowercase
public interface WalletProxy { // TODO

    @PostMapping("/wallet/{username}") // >0 = add ; <0 = remove
    Video updatePosition(@PathVariable String username, @RequestBody Position addTicker); // TODO [{"ticker": "AAPL", "quantity": 120, "unitvalue": 155.9}]

}