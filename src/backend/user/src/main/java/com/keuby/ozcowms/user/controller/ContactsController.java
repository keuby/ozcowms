package com.keuby.ozcowms.user.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.user.domain.Contact;
import com.keuby.ozcowms.user.domain.User;
import com.keuby.ozcowms.user.service.ContactService;
import com.keuby.ozcowms.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final UserService userService;

    private final ContactService contactService;

    public ContactsController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @GetMapping
    public JsonResp getAll(@RequestHeader(value = "x-user-id") long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return JsonResp.notFound("用户不存在，id=" + userId);
        }
        return JsonResp.ok(user.getContacts());
    }

    @PostMapping
    public JsonResp create(
            @RequestHeader(value = "x-user-id") long userId,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "remark") @NotBlank String remark) {
        Contact contact = contactService.create(userId, remark, phone);
        return JsonResp.ok(contact.toDTO());
    }

    @PutMapping("/{id}")
    public JsonResp update(
            @PathVariable @NotNull Long id,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "remark") @NotBlank String remark) {
        contactService.update(id, remark, phone);
        return JsonResp.ok();
    }

    @DeleteMapping("/{id}")
    public JsonResp delete(
            @PathVariable @NotNull Long id) {
        contactService.delete(id);
        return JsonResp.ok();
    }
}
