package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.repository.ProductRepository;
import java.util.List;

import com.mzwierzchowski.price_tracker.repository.UserProductRepository;
import com.mzwierzchowski.price_tracker.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProductService {

  private ProductRepository productRepository;
  private UserProductRepository userProductRepository;
  private final UserRepository userRepository;

  public ProductService(
          ProductRepository productRepository,
          UserProductRepository userProductRepository,
          UserRepository userRepository) {
    this.productRepository = productRepository;
    this.userProductRepository = userProductRepository;
    this.userRepository = userRepository;
  }

  public Product findOrCreateProductByUrl(String url) {
    log.info("Finding or creating product with URL: {}", url);

    Product existingProduct = productRepository.findByUrl(url);
    if (existingProduct != null) {
      log.info("Product found with URL: {}", url);
      return existingProduct;
    }

    log.info("Product not found, scraping data for URL: {}", url);
    String productName = ProductScraper.getProductNameFromUrl(url);
    Double productPrice = ProductScraper.getProductPriceFromUrl(url);

    if (productName == null || productPrice == null) {
      log.error("Failed to scrape product data from URL: {}", url);
      throw new RuntimeException("Nie udało się pobrać danych produktu z podanego URL");
    }

    log.info("Scraped product data for URL {}: Name = {}, Price = {}", url, productName, productPrice);

    Product newProduct = new Product();
    newProduct.setName(productName);
    newProduct.setUrl(url);
    newProduct.setLastPrice(productPrice);

    Product savedProduct = productRepository.save(newProduct);
    log.info("New product saved with ID {} and URL {}", savedProduct.getId(), url);

    return savedProduct;
  }

  public List<Product> findProductsForUser(String username) {
    log.info("Finding products for user: {}", username);

    User user = userRepository.findByUsername(username);
    if (user == null) {
      log.error("User not found with username: {}", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    List<Product> products = userProductRepository.findProductsByUser(user);
    log.info("Found {} products for user {}", products.size(), username);

    return products;
  }

  public Product getProductById(Long id) {
    log.info("Fetching product by ID: {}", id);

    Product product = productRepository.findById(id).orElse(null);
    if (product == null) {
      log.warn("Product not found with ID: {}", id);
    } else {
      log.info("Product found with ID: {}", id);
    }

    return product;
  }

  public List<Product> getAllProducts() {
    log.info("Fetching all products from database");

    List<Product> products = productRepository.findAll();
    log.info("Total products found: {}", products.size());

    return products;
  }
}
