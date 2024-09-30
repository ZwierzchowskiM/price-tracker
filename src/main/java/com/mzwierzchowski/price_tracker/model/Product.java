package com.mzwierzchowski.price_tracker.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String url;
    private String imageUrl;

    @OneToMany(mappedBy = "product")
    private Set<Price> prices;
}