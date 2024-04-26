package com.example.paysonix.network;

import com.example.paysonix.service.SecretCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME = "Token";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid header value";

    private final SecretCipher secretCipher;
    private final String token;

    @Autowired
    public AuthTokenInterceptor(@Value("${token.value}") String token, SecretCipher secretCipher) throws IllegalBlockSizeException, BadPaddingException {
        this.secretCipher = secretCipher;
        this.token = this.secretCipher.encrypt(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerValue = request.getHeader(HEADER_NAME);

        String decryptedToken = secretCipher.decrypt(token);
        if (headerValue == null || !headerValue.equals(decryptedToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, INVALID_TOKEN_MESSAGE);
            return false;
        }

        return true;
    }
}
