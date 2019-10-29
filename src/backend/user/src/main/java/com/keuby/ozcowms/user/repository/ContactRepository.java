package com.keuby.ozcowms.user.repository;

import com.keuby.ozcowms.user.domain.Contact;
import com.keuby.ozcowms.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByUser(User user);
}
