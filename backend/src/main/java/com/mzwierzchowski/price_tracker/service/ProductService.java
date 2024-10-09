package com.mzwierzchowski.price_tracker.service;

import com.mzwierzchowski.price_tracker.model.Product;
import com.mzwierzchowski.price_tracker.model.User;
import com.mzwierzchowski.price_tracker.model.dtos.ProductDTO;
import com.mzwierzchowski.price_tracker.repository.ProductRepository;
import java.util.List;

import com.mzwierzchowski.price_tracker.repository.UserProductRepository;
import com.mzwierzchowski.price_tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    Product existingProduct = productRepository.findByUrl(url);
    if (existingProduct != null) {
      return existingProduct;
    }

    String productName = ProductScraper.getProductNameFromUrl(url);
    Double productPrice = ProductScraper.getProductPriceFromUrl(url);
    if (productName == null || productPrice == null) {
      throw new RuntimeException("Nie udało się pobrać danych produktu z podanego URL");
    }

    Product newProduct = new Product();
    newProduct.setName(productName);
    newProduct.setUrl(url);
    newProduct.setLastPrice(productPrice);
    return productRepository.save(newProduct);
  }

  public List<Product> findProductsForUser(String username) {
    User user = userRepository.findByUsername(username);
    return userProductRepository.findProductsByUser(user);
  }



  public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
}
