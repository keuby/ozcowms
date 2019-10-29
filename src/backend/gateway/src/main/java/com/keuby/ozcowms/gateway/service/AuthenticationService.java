package com.keuby.ozcowms.gateway.service;

import com.keuby.ozcowms.gateway.authentication.UserAuthenticatedPrincipal;

public interface AuthenticationService {
    UserAuthenticatedPrincipal getPrincipalByToken(String token);

    String createPrincipal(Long userId);
}
