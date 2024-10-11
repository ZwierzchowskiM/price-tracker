package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.NotificationType;
import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.repository.PriceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PriceService {

  private PriceRepository priceRepository;
  private ProductService productService;
  private UserProductService userProductService;
  private EmailService emailService;

  public PriceService(
          PriceRepository priceRepository,
          ProductService productService,
          UserProductService userProductService,
          EmailService emailService) {
    this.priceRepository = priceRepository;
    this.productService = productService;
    this.userProductService = userProductService;
    this.emailService = emailService;
  }

  public boolean checkAndSavePrice(UserProduct userProduct) {

    Product product = userProduct.getProduct();
    Double currentPriceValue = ProductScraper.getProductPriceFromUrl(product.getUrl());
    if (currentPriceValue != null) {
      product.setLastPrice(currentPriceValue);
      Price price = new Price();
      price.setProduct(product);
      price.setPriceValue(currentPriceValue);
      price.setDateChecked(LocalDateTime.now());
      priceRepository.save(price);

      if (userProduct.getNotificationType() == NotificationType.BELOW_LAST_PRICE
              && currentPriceValue < product.getLastPrice()) {
        sendPriceDropNotification(userProduct.getUser().getUsername(), userProduct.getProduct(), currentPriceValue);
      } else if (userProduct.getNotificationType() == NotificationType.BELOW_THRESHOLD
              && currentPriceValue < userProduct.getNotificationPrice()) {
        sendPriceDropNotification(userProduct.getUser().getUsername(), userProduct.getProduct(), currentPriceValue);
      }

      return true;
    } else {
      System.err.println("Cena nie została znaleziona dla produktu: " + product.getName());
      return false;
    }
  }

  public void sendPriceDropNotification(String userEmail, Product product, double newPrice) {
    Map<String, Object> model = new HashMap<>();
    model.put("productName", product.getName());
    model.put("newPrice", newPrice);
    model.put("productUrl", product.getUrl());

    String subject = "Powiadomienie o zmianie ceny dla produktu: " + product.getName() + " - " + LocalDate.now();

    try {
      emailService.sendPriceNotification(userEmail, subject, model);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //    @Scheduled(cron = "0 0 6,18 * * *")
  @Scheduled(cron = "0 */10 * * * *")
  public void checkPricesForAllProducts() {
    List<UserProduct> allUserProducts = userProductService.getUserProducts();
    for (UserProduct userProduct : allUserProducts) {
      checkAndSavePrice(userProduct);
    }
  }

  public List<Price> getPriceHistoryForProduct(Product product) {
    return priceRepository.findByProduct(product);
  }
}
