package com.keuby.ozcowms.product.service;


import com.keuby.ozcowms.common.dto.ContactDTO;
import com.keuby.ozcowms.common.response.JsonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-center-service")
public interface ContactService {

    @PostMapping("/contacts")
    JsonResp<ContactDTO> create(@RequestHeader(value = "x-user-id") long userId, @RequestParam(name = "remark") String remark, @RequestParam(name = "phone") String phone);
}
