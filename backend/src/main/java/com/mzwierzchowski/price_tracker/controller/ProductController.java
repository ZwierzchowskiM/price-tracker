package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.model.dtos.NotificationRequestDTO;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.service.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "https://your-vercel-app.vercel.app"}, allowCredentials = "true")
public class ProductController {

  private final ProductService productService;
  private final PriceService priceService;
  private final UserProductService userProductService;
  private final UserService userService;

  public ProductController(
          ProductService productService,
          PriceService priceService,
          UserProductService userProductService,
          UserService userService) {
    this.productService = productService;
    this.priceService = priceService;
    this.userProductService = userProductService;
    this.userService = userService;
  }

  @PostMapping("/")
  public ResponseEntity<String> addProduct(
          @RequestBody ProductDTO productDTO, Authentication authentication) {
    String username = authentication.getName();
    log.info("Adding product for user: {}", username);
    userProductService.assignProductToUser(username, productDTO);
    log.info("Product added for user: {}", username);
    return ResponseEntity.ok("Product added!");
  }

  @GetMapping("/{productId}/check-price")
  public ResponseEntity<?> checkPrice(@PathVariable Long productId, Authentication authentication) {
    String username = authentication.getName();
    log.info("Checking price for product ID: {} for user: {}", productId, username);

    User user = userService.getUserByUsername(username);
    UserProduct userProduct = userProductService.getUserProductByUserIdAndProductId(user.getId(), productId);

    boolean updated = priceService.checkAndSavePrice(userProduct);
    if (updated) {
      log.info("Price check successful for product ID: {}", productId);
      return ResponseEntity.ok().build();
    } else {
      log.warn("Price check failed for product ID: {}", productId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/")
  public List<Product> getProducts() {
    log.info("Fetching all products");
    return productService.getAllProducts();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
    log.info("Fetching product with ID: {}", productId);
    Product product = productService.getProductById(productId);
    if (product != null) {
      log.info("Product found with ID: {}", productId);
      return ResponseEntity.ok(product);
    } else {
      log.warn("Product not found with ID: {}", productId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/user/{userId}")
  public List<Product> getProductsByUser(@PathVariable Long userId) {
    log.info("Fetching products for user ID: {}", userId);
    List<UserProduct> userProducts = userProductService.getUserProductsByUserId(userId);
    return userProducts.stream().map(UserProduct::getProduct).collect(Collectors.toList());
  }

  @GetMapping("/user")
  public List<Product> getProductsForCurrentUser(Authentication authentication) {
    String username = authentication.getName();
    log.info("Fetching products for current user: {}", username);
    return productService.findProductsForUser(username);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> deleteProduct(
          @PathVariable Long productId, Authentication authentication) {
    String username = authentication.getName();
    log.info("Attempting to delete product ID: {} for user: {}", productId, username);

    User user = userService.getUserByUsername(username);
    boolean deleted = userProductService.removeProductFromUser(user.getId(), productId);

    if (deleted) {
      log.info("Successfully deleted product ID: {} for user: {}", productId, username);
      return ResponseEntity.ok().build();
    } else {
      log.warn("Failed to delete product ID: {} for user: {}", productId, username);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{productId}/price-history")
  public ResponseEntity<List<Price>> getPriceHistory(@PathVariable Long productId) {
    log.info("Fetching price history for product ID: {}", productId);
    Product product = productService.getProductById(productId);
    if (product != null) {
      List<Price> priceHistory = priceService.getPriceHistoryForProduct(product);
      log.info("Price history found for product ID: {}", productId);
      return ResponseEntity.ok(priceHistory);
    } else {
      log.warn("No price history found for product ID: {}", productId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{productId}/user-product")
  public ResponseEntity<UserProduct> getUserProductDetails(
          @PathVariable Long productId, Authentication authentication) {
    String username = authentication.getName();
    log.info("Fetching user product details for user: {} and product ID: {}", username, productId);

    User user = userService.getUserByUsername(username);
    UserProduct userProductDetails = userProductService.getUserProductByUserIdAndProductId(user.getId(), productId);
    log.info("User product details found for user: {} and product ID: {}", username, productId);

    return ResponseEntity.ok(userProductDetails);
  }

  @PostMapping("/{productId}/set-notification")
  public ResponseEntity<?> setProductNotification(
          @PathVariable Long productId,
          @RequestBody NotificationRequestDTO notificationRequest,
          Authentication authentication) {
    String username = authentication.getName();
    log.info("Setting notification for user: {} and product ID: {}", username, productId);

    try {
      userProductService.setNotificationForProduct(productId, username, notificationRequest);
      log.info("Notification set successfully for user: {} and product ID: {}", username, productId);
      return ResponseEntity.ok("Notification settings saved.");
    } catch (Exception e) {
      log.error("Error setting notification for user: {} and product ID: {}", username, productId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Error saving notification settings.");
    }
  }
}
