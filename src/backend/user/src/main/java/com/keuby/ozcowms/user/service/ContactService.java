package com.keuby.ozcowms.user.service;

import com.keuby.ozcowms.user.domain.Contact;

public interface ContactService {

    Contact create(long userId, String remark, String phone);

    void update(Long id, String remark, String phone);

    void delete(Long id);
}
