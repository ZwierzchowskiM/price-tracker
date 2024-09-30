package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PriceService {


    private PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Sprawdza aktualną cenę produktu i zapisuje ją w bazie danych.
     *
     * @param product produkt, którego cena ma być sprawdzona
     * @return obiekt Price z aktualną ceną lub null w przypadku błędu
     */
    public Price checkAndSavePrice(Product product) {
        Double currentPriceValue = PriceScraper.getPriceFromUrl(product.getUrl());
        if (currentPriceValue != null) {
            Price price = new Price();
            price.setProduct(product);
            price.setPriceValue(currentPriceValue);
            price.setDateChecked(LocalDateTime.now());
            return priceRepository.save(price);
        } else {
            // Obsługa sytuacji, gdy cena nie została znaleziona
            System.err.println("Cena nie została znaleziona dla produktu: " + product.getName());
            return null;
        }
    }
}
