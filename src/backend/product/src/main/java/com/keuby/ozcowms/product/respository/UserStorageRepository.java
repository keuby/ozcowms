package com.keuby.ozcowms.product.respository;

import com.keuby.ozcowms.product.domain.UserStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserStorageRepository extends JpaRepository<UserStorage, Long> {

    List<UserStorage> findByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT us.* FROM t_user_storage us " +
            "LEFT JOIN t_storage s ON us.storage_id = s.id " +
            "WHERE us.user_id = ?1 AND s.product_id = ?2")
    Optional<UserStorage> findByUserIdAndProductId(Long userId, Long productId);

    @Query(nativeQuery = true, value = "SELECT us.* FROM t_user_storage us " +
            "LEFT JOIN t_storage s ON us.storage_id = s.id " +
            "LEFT JOIN t_products p ON s.product_id = p.id " +
            "WHERE us.user_id = ?1 AND p.category = ?2")
    List<UserStorage> findByUserIdAndCategory(Long userId, String category);
}
