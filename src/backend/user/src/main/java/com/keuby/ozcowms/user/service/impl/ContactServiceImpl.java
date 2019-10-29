package com.keuby.ozcowms.user.service.impl;

import com.keuby.ozcowms.common.exception.ServiceException;
import com.keuby.ozcowms.user.domain.Contact;
import com.keuby.ozcowms.user.domain.User;
import com.keuby.ozcowms.user.repository.ContactRepository;
import com.keuby.ozcowms.user.service.ContactService;
import com.keuby.ozcowms.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final UserService userService;

    public ContactServiceImpl(ContactRepository contactRepository, UserService userService) {
        this.contactRepository = contactRepository;
        this.userService = userService;
    }

    @Override
    public Contact create(long userId, String remark, String phone) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在, id=" + userId);
        }
        Contact contact = new Contact();
        contact.setPhone(phone);
        contact.setRemark(remark);
        contact.setUser(user);
        contact.setContact(null);
        return contactRepository.save(contact);
    }

    @Override
    public void update(Long id, String remark, String phone) {
        Optional<Contact> optional = contactRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ServiceException("联系人不存在, id=" + id);
        }
        Contact contact = optional.get();
        contact.setRemark(remark);
        contact.setPhone(phone);
        contactRepository.save(contact);
    }

    @Override
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
