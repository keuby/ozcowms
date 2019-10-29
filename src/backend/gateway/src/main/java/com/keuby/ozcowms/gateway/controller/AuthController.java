package com.keuby.ozcowms.gateway.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.gateway.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/auth")
    public JsonResp auth(@RequestParam(value = "userId") Long userId) {
        // TODO: 临时代码，暂时写死
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!"0".equals(authentication.getName())) {
            return JsonResp.error("权限不足");
        }

        String token = authenticationService.createPrincipal(userId);
        if (!StringUtils.isEmpty(token)) {
            return JsonResp.ok(token);
        }
        return JsonResp.error("授权失败");
    }

    @GetMapping
    public JsonResp index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JsonResp.ok(authentication.getName());
    }
}
