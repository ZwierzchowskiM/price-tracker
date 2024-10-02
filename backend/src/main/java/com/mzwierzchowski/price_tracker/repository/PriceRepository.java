package com.mzwierzchowski.price_tracker.repository;


import com.mzwierzchowski.price_tracker.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}