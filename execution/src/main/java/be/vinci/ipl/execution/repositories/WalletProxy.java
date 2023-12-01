package be.vinci.ipl.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import be.vinci.ipl.execution.models.Position;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {

    @PostMapping("/wallet/{username}") // >0 = add ; <0 = remove
    ResponseEntity<Void> updatePosition(@PathVariable String username, @RequestBody Position addTicker); // TODO [{"ticker": "AAPL", "quantity": 120, "unitvalue": 155.9}]

}