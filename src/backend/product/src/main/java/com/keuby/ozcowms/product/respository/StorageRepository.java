package com.keuby.ozcowms.product.respository;

import com.keuby.ozcowms.product.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StorageRepository extends JpaRepository<Storage, Long> {
}
