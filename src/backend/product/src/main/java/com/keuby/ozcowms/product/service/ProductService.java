package com.keuby.ozcowms.product.service;

import com.keuby.ozcowms.product.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> findProductsByUserId(Long userId);
    Product getById(Long id);
}
