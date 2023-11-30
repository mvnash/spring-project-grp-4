package be.vinci.ipl.order.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign client interface for triggering matching operations in the matching service.
 */
@Repository
@FeignClient(name = "matching")
public interface MatchingProxy {

    /**
     * Triggers the matching process for the specified ticker in the matching service.
     *
     * @param ticker The ticker symbol for the financial instrument.
     */
    @PostMapping("/trigger/{ticker}")
    void triggerMatching(@PathVariable String ticker);
}