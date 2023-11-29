package be.vinci.ipl.price;

import org.springframework.stereotype.Service;

import be.vinci.ipl.price.models.Price;

@Service
public class PriceService {

    private final PriceRepository repository;

    public PriceService(PriceRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves the last sale price for a given ticker
     * @param ticker Ticker of the financial instrument
     * @return Last sale price for the ticker
     */
    public Double getLastSalePrice(String ticker) {
        Price price = repository.findById(ticker).orElse(null);
        if (price == null){ // TODO requis ?
            price = new Price(); // TODO un autre new ??
            price.setTicker(ticker);
            price.setValue(1.0);
            repository.save(price);
        }
        
        return price.getValue();
    }

    /**
     * Updates the last sale price for a given ticker
     * @param ticker Ticker of the financial instrument
     * @param newPrice New last sale price
     * @return True if the price was updated, false otherwise
     */
    public boolean updateLastSalePrice(String ticker, Double newPrice) {
        if (!repository.existsById(ticker)) return false;

        Price price = repository.findById(ticker).orElse(null); // TODO fix this
        price.setValue(newPrice);
        repository.save(price);

        return true;
    }
}