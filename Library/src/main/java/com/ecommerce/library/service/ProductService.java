package com.ecommerce.library.service;

import com.ecommerce.library.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product save(Product product);
    Product update(Product product);
    void deleteById(Long id);
    void enableById(Long id);
}
