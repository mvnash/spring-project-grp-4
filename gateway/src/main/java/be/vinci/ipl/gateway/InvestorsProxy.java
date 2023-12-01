package be.vinci.ipl.gateway;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.List;
import models.Investor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name="investors")
public interface InvestorsProxy {

  @PostMapping("/investors/{username}")
  Investor createInvestor(@PathVariable String username, @RequestBody Investor investor);

  @GetMapping("/investors/{username}")
  Investor readInvestor(@PathVariable String username);

  @PutMapping("/investors/{username}")
  Investor updateInvestor(@PathVariable String username, @RequestBody Investor investor);

  @DeleteMapping("/investors/{username}")
  Investor deleteInvestor(@PathVariable String username);

  @GetMapping("/investors")
  List<Investor> readAllInvestors();


}
