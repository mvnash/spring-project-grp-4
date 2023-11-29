package be.vinci.ipl.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import be.vinci.ipl.reviews.models.Video;

@Repository
@FeignClient(name = "price") // TODO verifier upper/lowercase
public interface VideosProxy {

    @GetMapping("/price/{ticker}")
    boolean updateLastSalePrice(@PathVariable String ticker, @RequestBody Double newPrice);

}
