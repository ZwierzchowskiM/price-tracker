package com.mzwierzchowski.price_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
