package com.keuby.ozcow.miniapp.service;

import com.keuby.ozcowms.common.dto.UserDTO;
import com.keuby.ozcowms.common.response.JsonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-center-service")
public interface UserService {

    @GetMapping("/user/{id}")
    JsonResp<UserDTO> getById(@PathVariable(value = "id") Long id);

    @GetMapping("/user")
    JsonResp<UserDTO> getByOpenId(@RequestParam(value = "open_id") String openId);

    @PutMapping("/user/{id}/session_key")
    JsonResp setSessionKey(@PathVariable(value = "id") Long id, @RequestParam(value = "session_key") String sessionKey);

    @GetMapping("/user/{id}/session_key")
    JsonResp<String> getSessionKey(@PathVariable(value = "id") Long id);

    @PostMapping("/user")
    JsonResp<UserDTO> create(@RequestBody UserDTO user);

    @PutMapping("/user")
    JsonResp<UserDTO> update(@RequestBody UserDTO user);
}
