package be.vinci.ipl.gateway;

import jakarta.ws.rs.Path;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import be.vinci.ipl.wallet.PositionValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name="wallet")
public interface WalletProxy {

  @GetMapping("/wallet/{username}")
  Set<PositionValue> getOpenPositions(@PathVariable String username);

  @GetMapping("/wallet/{username}/net-worth")
  Double getNetWorth(@PathVariable String username);

  @PostMapping("/wallet/{username}/cash")
  Set<PositionValue> modifyCash(@PathVariable String username);





}
