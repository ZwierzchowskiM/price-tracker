package com.mzwierzchowski.price_tracker.repository;


import com.mzwierzchowski.price_tracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByUrl(String url);
}