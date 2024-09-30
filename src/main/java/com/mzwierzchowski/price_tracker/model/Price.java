package com.mzwierzchowski.price_tracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double priceValue;

    private LocalDateTime dateChecked;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
