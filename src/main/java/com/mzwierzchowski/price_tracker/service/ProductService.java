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



    public Product addProduct(ProductDTO productRequest) {
        // Pobierz nazwę produktu (raz podczas dodawania)
        String productName = ProductScraper.getProductNameFromUrl(productRequest.getUrl());

        if (productName == null) {
            throw new RuntimeException("Nie udało się pobrać nazwy produktu z podanego URL");
        }


        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setUrl(productRequest.getUrl());

        // Zapisz produkt w bazie danych (nazwa + URL, bez ceny na tym etapie)
        productRepository.save(newProduct);

        return newProduct;
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}