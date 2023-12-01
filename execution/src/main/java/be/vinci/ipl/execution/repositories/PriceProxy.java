package be.vinci.ipl.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "price")
public interface PriceProxy {

    @GetMapping("/price/{ticker}")
    ResponseEntity<Void> updateLastSalePrice(@PathVariable String ticker, @RequestBody Double newPrice);

}
