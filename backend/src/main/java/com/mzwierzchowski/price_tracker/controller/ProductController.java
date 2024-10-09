package com.mzwierzchowski.price_tracker.controller;

import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.model.dtos.NotificationRequestDTO;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.service.PriceService;
import com.mzwierzchowski.price_tracker.service.ProductService;
import com.mzwierzchowski.price_tracker.service.UserProductService;
import java.util.List;
import java.util.stream.Collectors;
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

  public ProductController(
      ProductService productService,
      PriceService priceService,
      UserProductService userProductService) {
    this.productService = productService;
    this.priceService = priceService;
    this.userProductService = userProductService;
  }

  @PostMapping("/")
  public ResponseEntity<String> addProduct(
      @RequestBody ProductDTO productDTO, Authentication authentication) {
    String username = authentication.getName();
    userProductService.assignProductToUser(username, productDTO);
    return ResponseEntity.ok("Produkt dodany!");
  }

  @GetMapping("/{productId}/check-price")
  public ResponseEntity<?> checkPrice(@PathVariable Long productId) {
    Product product = productService.getProductById(productId);

    boolean updated = priceService.checkAndSavePrice(product);
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
  public ResponseEntity<?> deleteProduct(@PathVariable Long productId, @RequestParam Long userId) {
    boolean deleted = userProductService.removeProductFromUser(userId, productId);
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
  public ResponseEntity<UserProduct> getUserProduct(
      @PathVariable Long productId, Authentication authentication) {
    String username = authentication.getName();
    UserProduct userProduct =
        userProductService.getUserProductByUserIdAndProductId(productId, username);
    return ResponseEntity.ok(userProduct);
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
