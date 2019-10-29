package com.keuby.ozcowms.gateway.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;

public class TokenUtil {

    private static final String TOKEN_HEADER = "x-access-token";

    private static final Encoder encoder = Base64.getEncoder();

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public static String genToken() {
        return TokenUtil.genToken(null);
    }

    public static String genToken(String salt) {
        return encoder.encodeToString((salt == null ? "" : salt + UUID.randomUUID().toString().replace("-", "")).getBytes());
    }
}
