package com.keuby.ozcowms.gateway.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticatedPrincipal;

@AllArgsConstructor
public class UserAuthenticatedPrincipal implements AuthenticatedPrincipal {

    @Getter
    private long userId;

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
