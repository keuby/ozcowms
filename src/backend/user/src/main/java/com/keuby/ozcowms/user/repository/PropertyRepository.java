package com.keuby.ozcowms.user.repository;

import com.keuby.ozcowms.user.domain.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {
}
