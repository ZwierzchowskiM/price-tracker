package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.NotificationType;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.model.dtos.NotificationRequestDTO;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.repository.UserProductRepository;
import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserProductService {

  private UserProductRepository userProductRepository;
  private UserService userService;
  private ProductService productService;

  public UserProductService(
          UserProductRepository userProductRepository,
          UserService userService,
          ProductService productService) {
    this.userProductRepository = userProductRepository;
    this.userService = userService;
    this.productService = productService;
  }

  public UserProduct assignProductToUser(String username, ProductDTO productDTO) {
    log.info("Assigning product with URL {} to user {}", productDTO.getUrl(), username);

    User user = userService.getUserByUsername(username);
    Product product = productService.findOrCreateProductByUrl(productDTO.getUrl());

    UserProduct userProduct = new UserProduct();
    userProduct.setUser(user);
    userProduct.setProduct(product);
    userProduct.setDateAdded(LocalDateTime.now());
    userProduct.setNotificationType(NotificationType.BELOW_LAST_PRICE);

    UserProduct savedUserProduct = userProductRepository.save(userProduct);
    log.info("Product assigned to user {} with product ID {} and userProduct ID {}",
            username, product.getId(), savedUserProduct.getId());

    return savedUserProduct;
  }

  public boolean removeProductFromUser(Long userId, Long productId) {
    log.info("Removing product with ID {} from user with ID {}", productId, userId);
    UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
    if (userProduct != null) {
      userProductRepository.delete(userProduct);
      log.info("Product with ID {} successfully removed from user with ID {}", productId, userId);
      return true;
    }
    log.warn("UserProduct not found for user ID {} and product ID {}", userId, productId);
    return false;
  }

  public List<UserProduct> getUserProductsByUserId(Long userId) {
    log.info("Fetching all products assigned to user with ID {}", userId);
    List<UserProduct> userProducts = userProductRepository.findByUserId(userId);
    log.info("Found {} products for user with ID {}", userProducts.size(), userId);
    return userProducts;
  }

  public UserProduct getUserProductByUserIdAndProductId(Long userId, Long productId) {
    log.info("Fetching UserProduct for user ID {} and product ID {}", userId, productId);
    UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
    if (userProduct == null) {
      log.warn("UserProduct not found for user ID {} and product ID {}", userId, productId);
    }
    return userProduct;
  }

  public List<UserProduct> getUserProducts() {
    log.info("Fetching all user products from database");
    List<UserProduct> userProducts = userProductRepository.findAll();
    log.info("Total user products found: {}", userProducts.size());
    return userProducts;
  }

  public void setNotificationForProduct(
          Long productId, String username, NotificationRequestDTO notificationRequest) {
    log.info("Setting notification for product ID {} and user {}", productId, username);

    User user = userService.getUserByUsername(username);
    UserProduct userProduct =
            userProductRepository.findByUserIdAndProductId(user.getId(), productId);

    if (userProduct == null) {
      log.error("UserProduct not found for user {} and product ID {}", username, productId);
      throw new RuntimeException("UserProduct not found for user " + username + " and product ID " + productId);
    }

    userProduct.setNotificationType(notificationRequest.getNotificationType());
    log.info("Notification type set to {} for product ID {}", notificationRequest.getNotificationType(), productId);

    if (NotificationType.BELOW_THRESHOLD.equals(notificationRequest.getNotificationType())) {
      userProduct.setNotificationPrice(notificationRequest.getNotificationPrice());
      log.info("Notification price set to {} for product ID {}", notificationRequest.getNotificationPrice(), productId);
    } else {
      userProduct.setNotificationPrice(null);
      log.info("Notification price reset for product ID {}", productId);
    }

    userProductRepository.save(userProduct);
    log.info("Notification settings saved for user {} and product ID {}", username, productId);
  }
}
