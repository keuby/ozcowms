package com.keuby.ozcowms.product.service;

import com.keuby.ozcowms.product.domain.UserStorage;

import java.util.List;

public interface UserStorageService {
    UserStorage findByUserIdAndProduct(Long userId, Long productId);
    List<UserStorage> findByUserIdAndCategory(Long userId, String category);
    List<UserStorage> findByUserId(Long userId);

    List<UserStorage> init(long userId, String category, String unitName);
}
