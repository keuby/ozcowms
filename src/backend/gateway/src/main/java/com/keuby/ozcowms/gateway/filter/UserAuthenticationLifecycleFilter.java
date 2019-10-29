package com.keuby.ozcowms.gateway.filter;

import com.keuby.ozcowms.gateway.authentication.AuthenticationToken;
import com.keuby.ozcowms.gateway.authentication.UserAuthenticatedPrincipal;
import com.keuby.ozcowms.gateway.service.AuthenticationService;
import com.keuby.ozcowms.gateway.utils.RedisClient;
import com.keuby.ozcowms.gateway.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationLifecycleFilter extends OncePerRequestFilter {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String token = TokenUtil.getToken(request);
        UserAuthenticatedPrincipal principal = authenticationService.getPrincipalByToken(token);
        if (principal != null) {
            SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(token, principal));
        }
        chain.doFilter(request, response);
    }
}
