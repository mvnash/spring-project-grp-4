package be.vinci.ipl.call.repository;

import be.vinci.ipl.call.models.Wallet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Repository
@FeignClient(name = "wallets")
public interface WalletProxy {

    @GetMapping("/wallets")
    ArrayList<Wallet> readAll();

}
