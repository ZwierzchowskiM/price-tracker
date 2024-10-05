package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.repository.PriceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PriceService {


    private PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }


    public boolean checkAndSavePrice(Product product) {
        Double currentPriceValue = ProductScraper.getProductPriceFromUrl(product.getUrl());
        if (currentPriceValue != null) {
            product.setLastPrice(currentPriceValue);
            Price price = new Price();
            price.setProduct(product);
            price.setPriceValue(currentPriceValue);
            price.setDateChecked(LocalDateTime.now());
            priceRepository.save(price);
            return true;
        } else {
            System.err.println("Cena nie została znaleziona dla produktu: " + product.getName());
            return false;
        }
    }
}
