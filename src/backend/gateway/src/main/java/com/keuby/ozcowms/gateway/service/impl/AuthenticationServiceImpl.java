package com.keuby.ozcowms.gateway.service.impl;

import com.keuby.ozcowms.gateway.authentication.UserAuthenticatedPrincipal;
import com.keuby.ozcowms.gateway.service.AuthenticationService;
import com.keuby.ozcowms.gateway.utils.RedisClient;
import com.keuby.ozcowms.gateway.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RedisClient redisClient;

    private static final String accessToken = "NDE2OTYyNjk1NzQzNDJlYjk2ZmY3YzNkOGU3ODdhNWQ=";

    private static final String TOKEN_KEY_PREFIX = "user-token:";
    private static final String USER_KEY_PREFIX = "user-id:";

    public AuthenticationServiceImpl(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public UserAuthenticatedPrincipal getPrincipalByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        } else if (accessToken.equals(token)) {
            return new UserAuthenticatedPrincipal(0);
        } else {
            String key = TOKEN_KEY_PREFIX + token;
            return redisClient.get(key, UserAuthenticatedPrincipal.class);
        }
    }

    @Override
    public String createPrincipal(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        String oldTokenKey = redisClient.get(USER_KEY_PREFIX + userId);
        if (!StringUtils.isEmpty(oldTokenKey)) {
            boolean success = redisClient.delete(TOKEN_KEY_PREFIX + oldTokenKey);
            if (!success) {
                return null;
            }
        }
        String token = TokenUtil.genToken(String.valueOf(userId));
        String tokenKey = TOKEN_KEY_PREFIX + token;
        UserAuthenticatedPrincipal principal = new UserAuthenticatedPrincipal(userId);
        boolean tokenCached = redisClient.set(tokenKey, principal);
        if (tokenCached) {
            return redisClient.set(USER_KEY_PREFIX + userId, token) ? token : null;
        }
        return null;
    }
}
