package com.keuby.ozcowms.user.service.impl;

import com.keuby.ozcowms.common.enums.UserStatus;
import com.keuby.ozcowms.user.domain.User;
import com.keuby.ozcowms.user.repository.UserRepository;
import com.keuby.ozcowms.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getById(Long id) {
        Optional<User> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public User getByOpenId(String openId) {
        Optional<User> optional = repository.findByOpenId(openId);
        return optional.orElse(null);
    }

    @Override
    public User create(User user) {
        User userInDB = checkUserExist(user);
        if (userInDB != null) {
            return update(userInDB, true);
        }

        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setStatus(UserStatus.BASE_INFO);
        repository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        return update(user, false);
    }

    @Override
    public User createNoLogin(String name) {
        User user = new User();
        user.setName(name);
        user.setStatus(UserStatus.NO_LOGIN);
        return repository.save(user);
    }

    @Override
    public void setSessionKey(Long id, String sessionKey) {
        Optional<User> optional = repository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setSessionKey(sessionKey);
            repository.save(user);
        }
    }

    private User update(User user, boolean db) {
        User userInDB = db ? user : checkUserExist(user);
        if (userInDB == null) {
            return null;
        }
        if (!StringUtils.isEmpty(user.getName())) {
            userInDB.setName(user.getName());
        }
        if (!StringUtils.isEmpty(user.getNickname())) {
            userInDB.setNickname(user.getNickname());
        }
        if (!StringUtils.isEmpty(user.getSessionKey())) {
            userInDB.setSessionKey(user.getSessionKey());
        }
        if (!StringUtils.isEmpty(user.getAvatar())) {
            userInDB.setAvatar(user.getAvatar());
        }
        return repository.save(userInDB);
    }

    @Override
    public User checkUserExist(User user) {
        if (user.getId() != null) {
            Optional<User> optional = repository.findById(user.getId());
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        if (!StringUtils.isEmpty(user.getOpenId())) {
            Optional<User> optional = repository.findByOpenId(user.getOpenId());
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

}