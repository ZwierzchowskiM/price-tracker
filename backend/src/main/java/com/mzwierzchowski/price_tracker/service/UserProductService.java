package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.repository.UserProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserProductService {

    @Autowired
    private UserProductRepository userProductRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public UserProduct assignProductToUser(Long userId, String productUrl) {
        // Pobierz użytkownika na podstawie userId
        User user = userService.getUserById(userId);

        // Sprawdź, czy produkt już istnieje, a jeśli nie, dodaj go
        Product product = productService.findOrCreateProductByUrl(productUrl);

        // Utwórz nową relację UserProduct i zapisz ją
        UserProduct userProduct = new UserProduct();
        userProduct.setUser(user);
        userProduct.setProduct(product);
        userProduct.setDateAdded(LocalDateTime.now());

        return userProductRepository.save(userProduct);
    }
}
