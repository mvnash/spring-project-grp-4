package be.vinci.ipl.execution;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.vinci.ipl.execution.models.Transaction;

@RestController
@RequestMapping("/execute")
public class ExecutionController {

    private final ExecutionService service;

    public ExecutionController(ExecutionService service) {
        this.service = service;
    }

    @PostMapping("/{ticker}/{seller}/{buyer}")
    public ResponseEntity<Void> executeTransaction(
        @PathVariable String ticker, @PathVariable String seller, @PathVariable String buyer, 
        @RequestBody Transaction transaction
    ) {
        if (   !transaction.getTicker().equals(ticker)
            || !transaction.getSeller().equals(seller)
            || !transaction.getBuyer().equals(buyer))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        service.executeTransaction(transaction);

        //if (!result.block()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        //else // TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }
}