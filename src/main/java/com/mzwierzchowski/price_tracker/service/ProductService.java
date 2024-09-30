package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Dodaje nowy produkt do bazy danych.
     * Jeśli produkt o podanym URL już istnieje, zwraca istniejący produkt.
     *
     * @param product obiekt produktu do dodania
     * @return dodany lub istniejący produkt
     */
    public Product addProduct(ProductDTO productRequest) {

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setUrl(productRequest.getUrl());

        Product existingProduct = productRepository.findByUrl(product.getUrl());
        if (existingProduct != null) {
            return existingProduct;
        }
        return productRepository.save(product);
    }

    /**
     * Pobiera produkt na podstawie ID.
     *
     * @param id identyfikator produktu
     * @return znaleziony produkt lub null
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}