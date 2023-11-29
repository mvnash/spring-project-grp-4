package be.vinci.ipl.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "order") // TODO verifier upper/lowercase
public interface OrderProxy { // TODO

    @PatchMapping("/order/{guid}")
    Video updateOne(@PathVariable String guid, @RequestBody String filled);

}
