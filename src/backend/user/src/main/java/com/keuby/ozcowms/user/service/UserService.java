package com.keuby.ozcowms.user.service;

import com.keuby.ozcowms.user.domain.User;

public interface UserService {
    User getByOpenId(String openId);
    User getById(Long id);
    User create(User user);
    User update(User user);
    User createNoLogin(String name);
    void setSessionKey(Long id, String sessionKey);

    User checkUserExist(User user);
}
