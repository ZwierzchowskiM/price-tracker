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
import org.springframework.stereotype.Service;

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
    User user = userService.getUserByUsername(username);

    Product product = productService.findOrCreateProductByUrl(productDTO.getUrl());

    UserProduct userProduct = new UserProduct();
    userProduct.setUser(user);
    userProduct.setProduct(product);
    userProduct.setDateAdded(LocalDateTime.now());
    userProduct.setNotificationType(NotificationType.BELOW_LAST_PRICE);

    return userProductRepository.save(userProduct);
  }

  public boolean removeProductFromUser(Long userId, Long productId) {
    UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
    if (userProduct != null) {
      userProductRepository.delete(userProduct);
      return true;
    }
    return false;
  }

  public List<UserProduct> getUserProductsByUserId(Long userId) {
    return userProductRepository.findByUserId(userId);
  }

  public UserProduct getUserProductByUserIdAndProductId(Long productId, String username) {

    User user = userService.getUserByUsername(username);
    return userProductRepository.findByUserIdAndProductId(user.getId(), productId);
  }

  public void setNotificationForProduct(
      Long productId, String username, NotificationRequestDTO notificationRequest) {
    User user = userService.getUserByUsername(username);
    UserProduct userProduct =
        userProductRepository.findByUserIdAndProductId(user.getId(), productId);
    userProduct.setNotificationType(notificationRequest.getNotificationType());

    if (NotificationType.BELOW_THRESHOLD.equals(notificationRequest.getNotificationType())) {
      userProduct.setNotificationPrice(notificationRequest.getNotificationPrice());
    } else {
      userProduct.setNotificationPrice(null);
    }

    userProductRepository.save(userProduct);
  }
}
