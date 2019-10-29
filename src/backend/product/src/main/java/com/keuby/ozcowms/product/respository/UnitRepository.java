package com.keuby.ozcowms.product.respository;

import com.keuby.ozcowms.product.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    String COMMON_UNIT_TAG = "common";

    @Query("SELECT u FROM Unit u WHERE u.parent IS NULL AND u.tag = ?1")
    Optional<Unit> findByTag(String tag);

    @Query("SELECT u FROM Unit u WHERE u.tag = " + COMMON_UNIT_TAG)
    List<Unit> findCommonTags();

    Optional<Unit> findByNameAndTag(String name, String tag);
}
