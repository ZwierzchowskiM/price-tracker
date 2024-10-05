package com.mzwierzchowski.price_tracker.repository;

import com.mzwierzchowski.price_tracker.model.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
    List<UserProduct> findByUserId(Long userId);
    UserProduct findByUserIdAndProductId(Long userId, Long productId);
}
