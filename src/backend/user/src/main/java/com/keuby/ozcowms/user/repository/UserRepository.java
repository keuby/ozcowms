package com.keuby.ozcowms.user.repository;

import com.keuby.ozcowms.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByOpenId(String openId);
}