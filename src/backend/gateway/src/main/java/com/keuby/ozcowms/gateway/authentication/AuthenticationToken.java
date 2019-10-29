package com.keuby.ozcowms.gateway.authentication;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private AuthenticatedPrincipal principal;

    private String credentials;

    public AuthenticationToken(String credentials, AuthenticatedPrincipal principal) {
        super(null);
        this.credentials = credentials;
        this.principal = principal;
        setAuthenticated(true);
    }

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
