package com.keuby.ozcowms.product.respository;

import com.keuby.ozcowms.product.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT t.* FROM t_transactions t " +
            "LEFT JOIN t_user_storage us ON us.id = t.user_storage_id " +
            "WHERE us.storage_id = ?1 AND t.parent_id IS NULL AND t.operation_type in (?2) " +
            "ORDER BY t.created_time DESC LIMIT ?3,?4", nativeQuery = true)
    List<Transaction> findTransactions(Long storageId, List<String> types, int start, int size);

    @Query(value = "SELECT t.* FROM t_transactions t " +
            "LEFT JOIN t_user_storage us ON us.id = t.user_storage_id " +
            "WHERE us.storage_id in (?1) AND t.parent_id IS NULL AND t.operation_type in (?2) " +
            "ORDER BY t.created_time DESC LIMIT ?3,?4", nativeQuery = true)
    List<Transaction> findTransactions(List<Long> storageId, List<String> types, int start, int size);
}
