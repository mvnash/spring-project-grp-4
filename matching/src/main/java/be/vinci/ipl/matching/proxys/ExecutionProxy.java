package be.vinci.ipl.matching.proxys;

import be.vinci.ipl.matching.models.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "execution")
public interface ExecutionProxy {

  @PostMapping("/execute/{ticker}/{seller}/{buyer}")
  void executeMatchingOrders(@PathVariable String ticker, @PathVariable String seller,
      @PathVariable String buyer, @RequestBody Transaction user);

}
