package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.model.dtos.NotificationRequestDTO;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.service.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

  private ProductService productService;
  private PriceService priceService;
  private UserProductService userProductService;
  private UserService userService;

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
    userProductService.assignProductToUser(username, productDTO);
    return ResponseEntity.ok("Produkt dodany!");
  }

  @GetMapping("/{productId}/check-price")
  public ResponseEntity<?> checkPrice(@PathVariable Long productId, Authentication authentication) {

    User user = userService.getUserByUsername(authentication.getName());
    UserProduct userProduct =
            userProductService.getUserProductByUserIdAndProductId(user.getId(), productId);
    boolean updated = priceService.checkAndSavePrice(userProduct);
    if (updated) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/")
  public List<Product> getProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
    Product product = productService.getProductById(productId);
    if (product != null) {
      return ResponseEntity.ok(product);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/user/{userId}")
  public List<Product> getProductsByUser(@PathVariable Long userId) {
    List<UserProduct> userProducts = userProductService.getUserProductsByUserId(userId);
    return userProducts.stream().map(UserProduct::getProduct).collect(Collectors.toList());
  }

  @GetMapping("/user")
  public List<Product> getProductsForCurrentUser(Authentication authentication) {
    String username = authentication.getName();
    return productService.findProductsForUser(username);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> deleteProduct(
          @PathVariable Long productId, Authentication authentication) {

    User user = userService.getUserByUsername(authentication.getName());
    boolean deleted = userProductService.removeProductFromUser(user.getId(), productId);
    if (deleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{productId}/price-history")
  public ResponseEntity<List<Price>> getPriceHistory(@PathVariable Long productId) {
    Product product = productService.getProductById(productId);
    if (product != null) {
      List<Price> priceHistory = priceService.getPriceHistoryForProduct(product);
      return ResponseEntity.ok(priceHistory);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{productId}/user-product")
  public ResponseEntity<UserProduct> getUserProductDetails(
          @PathVariable Long productId, Authentication authentication) {
    String username = authentication.getName();

    User user = userService.getUserByUsername(username);

    UserProduct userProductDetails =
            userProductService.getUserProductByUserIdAndProductId(user.getId(), productId);
    return ResponseEntity.ok(userProductDetails);
  }

  @PostMapping("/{productId}/set-notification")
  public ResponseEntity<?> setProductNotification(
          @PathVariable Long productId,
          @RequestBody NotificationRequestDTO notificationRequest,
          Authentication authentication) {
    String username = authentication.getName();

    try {
      userProductService.setNotificationForProduct(productId, username, notificationRequest);
      return ResponseEntity.ok("Ustawienia powiadomienia zostały zapisane.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Błąd podczas zapisywania powiadomienia.");
    }
  }
}
