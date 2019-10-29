package com.keuby.ozcow.miniapp.service;

import com.keuby.ozcowms.common.response.JsonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service")
public interface GatewayService {

    @PostMapping(value = "/auth")
    JsonResp<String> auth(@RequestHeader(value = "x-access-token") String accessToken, @RequestParam(value = "userId") Long userId);
}
