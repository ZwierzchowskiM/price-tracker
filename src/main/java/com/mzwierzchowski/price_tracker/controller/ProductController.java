package com.mzwierzchowski.price_tracker.controller;


import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.service.PriceService;
import com.mzwierzchowski.price_tracker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PriceService priceService;

    @PostMapping("/add")
    public Product addProduct(@RequestBody ProductDTO productRequest) {
        return productService.addProduct(productRequest);
    }


    @GetMapping("/{productId}/check-price")
    public Price checkPrice(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return priceService.checkAndSavePrice(product);
        } else {
            // Obsługa przypadku, gdy produkt nie istnieje
            return null;
        }
    }
}