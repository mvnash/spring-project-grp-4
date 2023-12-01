package be.vinci.ipl.price;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/price")
public class PriceController {

    private final PriceService service;

    public PriceController(PriceService service) {
        this.service = service;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<Double> getLastSalePrice(@PathVariable String ticker) {
        Double price = service.getLastSalePrice(ticker);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    @PatchMapping("/{ticker}")
    public ResponseEntity<Void> updateLastSalePrice(@PathVariable String ticker, @RequestBody Map<String, Double> body) {
        Double value = body.get("value");

        System.out.println(value);
        if (value <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean updated = service.updateLastSalePrice(ticker, value);

        if (!updated) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }
}