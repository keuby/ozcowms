package com.keuby.ozcowms.product.service.impl;

import com.keuby.ozcowms.product.domain.Product;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.respository.ProductRepository;
import com.keuby.ozcowms.product.service.ProductService;
import com.keuby.ozcowms.product.service.UserStorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final UserStorageService userStorageService;

    private final ProductRepository productRepository;

    public ProductServiceImpl(UserStorageService userStorageService, ProductRepository productRepository) {
        this.userStorageService = userStorageService;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findProductsByUserId(Long userId) {
        List<UserStorage> storageList = userStorageService.findByUserId(userId);
        return storageList.stream().map(userStorage -> userStorage.getStorage().getProduct()).collect(Collectors.toList());
    }

    @Override
    public Product getById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return productRepository.findById(id).orElse(null);
    }
}
