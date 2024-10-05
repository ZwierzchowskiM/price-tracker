package com.mzwierzchowski.price_tracker.controller;


import com.mzwierzchowski.price_tracker.model.Price;
import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.UserProduct;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.service.PriceService;
import com.mzwierzchowski.price_tracker.service.ProductService;
import com.mzwierzchowski.price_tracker.service.UserProductService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")

public class ProductController {

    private ProductService productService;
    private PriceService priceService;
    private UserProductService userProductService;

    public ProductController(ProductService productService, PriceService priceService, UserProductService userProductService) {
        this.productService = productService;
        this.priceService = priceService;
        this.userProductService = userProductService;
    }

    @PostMapping
    public UserProduct addProduct(@RequestParam Long userId, @RequestBody ProductDTO productDto) {
        return userProductService.assignProductToUser(userId, productDto.getUrl());
    }


    @GetMapping("/{productId}/check-price")
    public Price checkPrice(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return priceService.checkAndSavePrice(product);
        } else {
            return null;
        }
    }

    @GetMapping("/")
    public List<Product> getProducts() {
    List<Product> products = productService.getAllProducts();
    return products;
    }

    @GetMapping("/user/{userId}")
    public List<Product> getProductsByUser(@PathVariable Long userId) {
        List<UserProduct> userProducts = userProductService.getUserProductsByUserId(userId);
        return userProducts.stream()
                .map(UserProduct::getProduct)
                .collect(Collectors.toList());
    }

}