package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

  private PriceRepository priceRepository;
  private ProductService productService;

  public PriceService(PriceRepository priceRepository, ProductService productService) {
    this.priceRepository = priceRepository;
    this.productService = productService;
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

  //    @Scheduled(cron = "0 0 6,18 * * *")
  @Scheduled(cron = "0 */10 * * * *")
  public void checkPricesForAllProducts() {
    List<Product> allProducts = productService.getAllProducts();
    for (Product product : allProducts) {
      checkAndSavePrice(product);
    }
  }

  public List<Price> getPriceHistoryForProduct(Product product) {
    return priceRepository.findByProduct(product);
  }
}
