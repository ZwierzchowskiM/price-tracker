package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.repository.UserProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProductService {

    @Autowired
    private UserProductRepository userProductRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public UserProduct assignProductToUser(String username, String productUrl) {
        User user = userService.getUserByUsername(username);

        Product product = productService.findOrCreateProductByUrl(productUrl);

        UserProduct userProduct = new UserProduct();
        userProduct.setUser(user);
        userProduct.setProduct(product);
        userProduct.setDateAdded(LocalDateTime.now());

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
}
