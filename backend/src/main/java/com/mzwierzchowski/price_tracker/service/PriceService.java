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
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PriceService {

  private final PriceRepository priceRepository;

  private final UserProductService userProductService;
  private final EmailService emailService;

  public PriceService(
      PriceRepository priceRepository,
      UserProductService userProductService,
      EmailService emailService) {
    this.priceRepository = priceRepository;
    this.userProductService = userProductService;
    this.emailService = emailService;
  }

  public boolean checkAndSavePrice(UserProduct userProduct) {
    Product product = userProduct.getProduct();
    log.info("Checking price for product: {}", product.getName());

    Double currentPriceValue = ProductScraper.getProductPriceFromUrl(product.getUrl());

    if (currentPriceValue != null) {
      log.info("Price found for product {}: {} PLN", product.getName(), currentPriceValue);

      product.setLastPrice(currentPriceValue);
      Price price = new Price();
      price.setProduct(product);
      price.setPriceValue(currentPriceValue);
      price.setDateChecked(LocalDateTime.now());
      priceRepository.save(price);

      if (userProduct.getNotificationType() == NotificationType.BELOW_LAST_PRICE
          && currentPriceValue < product.getLastPrice()) {
        log.info("Price drop detected for product {} below last price", product.getName());
        sendPriceDropNotification(
            userProduct.getUser().getUsername(), userProduct.getProduct(), currentPriceValue);
      } else if (userProduct.getNotificationType() == NotificationType.BELOW_THRESHOLD
          && currentPriceValue < userProduct.getNotificationPrice()) {
        log.info(
            "Price drop detected for product {} below threshold: {} PLN",
            product.getName(),
            userProduct.getNotificationPrice());
        sendPriceDropNotification(
            userProduct.getUser().getUsername(), userProduct.getProduct(), currentPriceValue);
      }

      return true;
    } else {
      log.error("Price not found for product: {}", product.getName());
      return false;
    }
  }

  public void sendPriceDropNotification(String userEmail, Product product, double newPrice) {
    log.info(
        "Sending price drop notification for product: {} to user: {}",
        product.getName(),
        userEmail);

    Map<String, Object> model = new HashMap<>();
    model.put("productName", product.getName());
    model.put("newPrice", newPrice);
    model.put("productUrl", product.getUrl());

    String subject =
        "Powiadomienie o zmianie ceny dla produktu: " + product.getName() + " - " + LocalDate.now();

    try {
      emailService.sendPriceNotification(userEmail, subject, model);
      log.info(
          "Notification sent to {} for product {} with new price {}",
          userEmail,
          product.getName(),
          newPrice);
    } catch (Exception e) {
      log.error(
          "Failed to send price drop notification for product {} to user {}",
          product.getName(),
          userEmail,
          e);
    }
  }

  @Scheduled(cron = "0 0 * * * *") // Co 1 godzinę
  public void checkPricesForAllProducts() {
    log.info("Starting scheduled price check for all products");

    List<UserProduct> allUserProducts = userProductService.getUserProducts();
    for (UserProduct userProduct : allUserProducts) {
      boolean success = checkAndSavePrice(userProduct);
      if (success) {
        log.info("Price check successful for product {}", userProduct.getProduct().getName());
      } else {
        log.warn("Price check failed for product {}", userProduct.getProduct().getName());
      }
    }

    log.info("Scheduled price check completed");
  }

  public List<Price> getPriceHistoryForProduct(Product product) {
    log.info("Fetching price history for product: {}", product.getName());
    List<Price> priceHistory = priceRepository.findByProduct(product);
    log.info("Found {} price records for product {}", priceHistory.size(), product.getName());
    return priceHistory;
  }
}
