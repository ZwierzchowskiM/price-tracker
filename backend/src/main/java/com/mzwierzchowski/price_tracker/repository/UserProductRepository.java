package com.mzwierzchowski.price_tracker.repository;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
    List<UserProduct> findByUserId(Long userId);
    UserProduct findByUserIdAndProductId(Long userId, Long productId);
    @Query("SELECT up.product FROM UserProduct up WHERE up.user = :user")
    List<Product> findProductsByUser(@Param("user") User user);
}
